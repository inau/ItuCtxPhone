package pervasive2016.itu.contextapp;

import android.Manifest;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.IndoorBuilding;
import com.google.android.gms.maps.model.IndoorLevel;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import pervasive2016.itu.contextapp.data.UserLocation;
import pervasive2016.itu.contextapp.data.entity.BeaconEntity;
import pervasive2016.itu.contextapp.data.entity.ContextEntity;
import pervasive2016.itu.contextapp.presentation.StatsFragment;
import pervasive2016.itu.contextapp.web.ApiAdapter;

/**
 * Created by DIEM NGUYEN HOANG on 3/18/2016.
 * Heavily modified by Ivan
 */
public class LocationChangeActivity extends FragmentActivity implements GoogleMap.OnIndoorStateChangeListener, Observer, LocationListener, OnMapReadyCallback {
    final static String TAG = "LOCATIONLISTENER";
    GoogleMap googleMap;

    String mClosestKey;
    ComponentName caller;

    volatile String lastClick;
    Map<String, BeaconEntity> markerData = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_map_view);
        // Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        Intent intent = getIntent();
        caller = getCallingActivity();
        Log.i("CALL", "?"+caller);

        mClosestKey = intent.getStringExtra("key");

        Log.i(TAG, "Beacon key = " + mClosestKey);

        // Showing status
        if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
            Log.i(TAG, "Google Play Services are not available");

        } else { // Google Play Services are available

            // Getting reference to the SupportMapFragment of activity_main.xml
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

            // Getting GoogleMap object from the fragment
            fm.getMapAsync(this);
            //googleMap = fm.getMapAsync(this);

        }

        if( caller != null ) return;
        startService( new Intent(this, ContextService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if( caller != null ) return;
        stopService(new Intent(this, ContextService.class));
    }

    @Override
    public void onLocationChanged(Location location) {
        if( caller != null ) return;
        // Getting latitude of the current location
        double latitude = location.getLatitude();

        // Getting longitude of the current location
        double longitude = location.getLongitude();

        updateUserLocation(latitude, longitude);

        // Creating a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        // Enabling MyLocation Layer of Google Map
        googleMap.setMyLocationEnabled(true);

        // Getting LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0.1f, this);

        List<String> providers = locationManager.getProviders(true);
        Location location = null;
        for(String provider: providers){
            Location l = locationManager.getLastKnownLocation(provider);
            if(l == null){
                continue;
            }
            if(location == null || l.getAccuracy() > location.getAccuracy()){
                location = l;
            }
        }

        updateUserLocation( location.getLatitude(), location.getLongitude() );

        centerOnLocation(new LatLng(location.getLatitude(), location.getLongitude()));
        googleMap.setOnIndoorStateChangeListener(this);

        if( caller != null ) {
            final Intent i = LocationChangeActivity.this.getIntent();

            Toast.makeText(LocationChangeActivity.this,
                    "Do a long click on the map to set coordinates for the new beacon",
                    Toast.LENGTH_SHORT)
                    .show();

            googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    i.putExtra(MainActivity.keys[3], latLng.latitude);
                    i.putExtra(MainActivity.keys[4], latLng.longitude);
                    setResult(RESULT_OK, i);
                    finish();
                }
            });
            return; //skip adding beacons
        }

        requestAllBeacons();

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                BeaconEntity be = markerData.get(marker.getId());
                lastClick = marker.getId();
                try {
                    Log.i("Marker", "Request " + be.getLatitude().longValue() + " " + be.getLongtitude().longValue());
                    ApiAdapter
                            .getApihandlerCTX(LocationChangeActivity.this, null, ApiAdapter.WebMethod.GET)
                            .execute(ApiAdapter
                                            .urlBuilderFilter(ApiAdapter.APIS.CONTEXTS,
                                                    be.getLatitude().longValue(),
                                                    be.getLongtitude().longValue(),
                                                    DayAgo()
                                            )
                            );
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });


        Log.i(TAG, "Google Play Services ARe available");
    }

    private void requestAllBeacons() {
        try {
            ApiAdapter
                    .getApihandlerBCS(LocationChangeActivity.this, null, ApiAdapter.WebMethod.GET)
                    .execute(ApiAdapter.urlBuilder(ApiAdapter.APIS.BEACONS, ""));
            Log.i("MarkFetch", "ALL beacons");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void requestNearbyBeacons() {
        try {
            URL url = ApiAdapter.urlBuilderFilter(ApiAdapter.APIS.BEACONS,
                    (long) UserLocation.getLatitude(), (long) UserLocation.getLongitude(), null);
            Log.i("MarkFetch", url.getPath() + url.getQuery());
            ApiAdapter
                    .getApihandlerBCS(LocationChangeActivity.this, null, ApiAdapter.WebMethod.GET)
                    .execute(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void centerOnLocation(LatLng coords) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coords, 18));
    }

    public void clearMapMarkers() {
        if(googleMap != null)
            markerData.clear();
            googleMap.clear();
    }

    private void addClosestBeacon(BeaconEntity be) {
        Marker m = googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(be.getLatitude(), be.getLongtitude()))
                        .title(be.getMajor() + minorToRoom(be.getMinor()))
                        .icon(BitmapDescriptorFactory.fromResource(android.R.drawable.presence_online))
        );
        markerData.put(m.getId(), be);
    }

    public void addBeaconMarker(BeaconEntity beacon) {
        Log.i("MAP", "add marker triggered");
        if(googleMap != null){
            Log.i("MAP", "not null");
            if( googleMap.isIndoorEnabled() ) {
                Log.i("MAPL", "Indoor");
                IndoorBuilding building = googleMap.getFocusedBuilding();
                if(building != null) {
                    Log.i("MAPL", "Building");
                    List<IndoorLevel> levels = building.getLevels();
                    int i = building.getActiveLevelIndex();

                    if(!((5 - Integer.parseInt( beacon.getMajor() )) == i)) return;
                    final Marker m = googleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(beacon.getLatitude(), beacon.getLongtitude()))
                                    .title("" + beacon.getMajor() + minorToRoom(beacon.getMinor()))
                                .icon(BitmapDescriptorFactory.fromResource(android.R.drawable.presence_invisible))
                    );
                    markerData.put(m.getId(), beacon);
                }

            else {
                final Marker m = googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(beacon.getLatitude(), beacon.getLongtitude()))
                                .title("" + beacon.getMajor() + minorToRoom(beacon.getMinor()))
                                .icon(BitmapDescriptorFactory.fromResource(android.R.drawable.presence_invisible))
                );
                markerData.put(m.getId(), beacon);
            }
            }


        }
    }

    private String minorToRoom(String minor) {
        if(minor != null && minor.length() > 3)
            switch (minor.substring(0, 1)) {
                case "1":
                    return "A" + minor.substring(1, 3);
                case "2":
                    return "B" + minor.substring(1, 3);
                case "3":
                    return "C" + minor.substring(1, 3);
                case "4":
                    return "D" + minor.substring(1, 3);
                case "5":
                    return "E" + minor.substring(1, 3);
                default:
                    return "bbbb";
            }
        else return "aaaa";
    }

    private Date DayAgo() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, -1);
        return c.getTime();
    }

    private void updateUserLocation(double lat, double lng) {
        UserLocation.setLatitude( lat );
        UserLocation.setLongitude( lng );
        Log.i("LOC", "UserLocation Set to (" + UserLocation.getLatitude() + ", " + UserLocation.getLongitude() + ")");
    }

    @Override
    public void update(Observable observable, Object data) {
        if(data instanceof ContextEntity[] ) {
            ContextEntity[] res = (ContextEntity[]) data;
            BeaconEntity be = markerData.get(lastClick);
            StatsFragment.newInstance(getApplicationContext(), be.getMajor()+minorToRoom(be.getMinor()), res).show(getSupportFragmentManager(), "");
        }
        else if(data instanceof BeaconEntity[] ) {
            BeaconEntity[] res = (BeaconEntity[]) data;
            Log.i("BCR", "#" + res.length );
            for (BeaconEntity be : res)
                if( be.getKey().equals(mClosestKey) ) addClosestBeacon(be);
                else addBeaconMarker(be);
        }
    }

    @Override
    public void onIndoorBuildingFocused() {

    }

    @Override
    public void onIndoorLevelActivated(IndoorBuilding indoorBuilding) {
        clearMapMarkers();
        requestAllBeacons();
    }
}
