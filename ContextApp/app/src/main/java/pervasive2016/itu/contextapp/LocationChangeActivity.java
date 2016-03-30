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
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import pervasive2016.itu.contextapp.presentation.ActionFragment;
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

    List<BeaconEntity> lastData;

    volatile Marker lastClick;
    Map<Marker, BeaconEntity> markerData = new HashMap<>();
    Marker relocateMarker = null;

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
        startService(new Intent(this, ContextService.class));
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

        Location location = UserLocation.getLocation(this);

        updateUserLocation(location.getLatitude(), location.getLongitude());

        centerOnLocation(new LatLng(location.getLatitude(), location.getLongitude()));
        googleMap.setOnIndoorStateChangeListener(this);

        if( caller != null ) {
            Toast.makeText(LocationChangeActivity.this,
                    "Do a long click on the map to set coordinates for the new beacon",
                    Toast.LENGTH_SHORT)
                    .show();
        }
            googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    if (caller != null) {
                        Intent i = LocationChangeActivity.this.getIntent();
                        i.putExtra(MainActivity.keys[3], latLng.latitude);
                        i.putExtra(MainActivity.keys[4], latLng.longitude);
                        setResult(RESULT_OK, i);
                        finish();

                    } else {
                        if (relocateMarker == null) {
                            relocateMarker = googleMap.addMarker(new MarkerOptions()
                                           // .icon(BitmapDescriptorFactory.fromResource(android.R.drawable.presence_invisible))
                                            .title("RelocateMarker")
                                            .position(latLng)
                            );
                        } else relocateMarker.setPosition(latLng);
                    }
                    Log.i("CLICK", " "+latLng.latitude + ", " + latLng.longitude );
                }
            });

        if( caller != null ) return;
        requestAllBeacons();

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                lastClick = marker;
                if (relocateMarker != null && marker.getId().equals(relocateMarker.getId()))
                    return false;

                BeaconEntity be = markerData.get(marker);

                //POPUP MENU
                Double l, ll;
                if (relocateMarker == null) {
                    l = null;
                    ll = null;
                } else {
                    l = relocateMarker.getPosition().latitude;
                    ll = relocateMarker.getPosition().longitude;
                    Log.i("CLICKV", "" + l + " " + ll);
                }
                ActionFragment.newInstance(LocationChangeActivity.this,
                        l, ll,
                        be,
                        DayAgo().getTime()
                ).show(getSupportFragmentManager(), "POP");

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
            if(lastData != null) return;
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
        Log.i("CLR", "triggered");
        if(googleMap != null){
            Log.i("CLR", "triggered");
          //  for (Marker m : markerData.keySet()) {
          //      m.remove();
          //  }
            markerData.clear();
            googleMap.clear();
        }
    }

    private void addClosestBeacon(BeaconEntity be) {
        Marker m = googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(be.getLatitude(), be.getLongtitude()))
                        .title(be.getName())
                        .icon(BitmapDescriptorFactory.fromResource(android.R.drawable.presence_online))
        );
        markerData.put(m, be);
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
                                    .title("" + beacon.getName())
                                    .icon(BitmapDescriptorFactory.fromResource(android.R.drawable.presence_invisible))
                    );
                    markerData.put(m, beacon);
                }

            else {
                final Marker m = googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(beacon.getLatitude(), beacon.getLongtitude()))
                                .title(beacon.getName())
                                .icon(BitmapDescriptorFactory.fromResource(android.R.drawable.presence_invisible))
                );
                markerData.put(m, beacon);
            }
            }


        }
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
            if(be == null) return;
            StatsFragment.newInstance(getApplicationContext(), be.getName(), res).show(getSupportFragmentManager(), "");
        }
        else if(data instanceof BeaconEntity[] ) {
            BeaconEntity[] res = (BeaconEntity[]) data;
            if( res != null && res.length == 1 ) {
                BeaconEntity old = markerData.get( lastClick );
                if( old != null && old.equals( res[0] ) ) {
                    if(lastData == null) {
                        requestAllBeacons();
                        return;
                    }
                    lastData.remove(old);
                    lastData.add(res[0]);
                    return;
                }
            }
            clearMapMarkers();
            for (BeaconEntity be : res) {
                Log.i("BeaconsReceived", be.getLatitude() + " "+ be.getLongtitude() );
                if( be.getKey().equals(mClosestKey) ) addClosestBeacon(be);
                else addBeaconMarker(be);
            }
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
