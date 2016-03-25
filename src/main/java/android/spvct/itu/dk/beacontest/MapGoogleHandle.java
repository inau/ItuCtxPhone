package android.spvct.itu.dk.beacontest;

import android.content.Context;
import android.location.LocationListener;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by DIEM NGUYEN HOANG on 3/18/2016.
 */
public class MapGoogleHandle implements OnMapReadyCallback {
    private static MapGoogleHandle mInstance = null;
    GoogleMap mMap;
    Context c;
    public static MapGoogleHandle getInstance(Context c) {
        if(mInstance == null) {
            mInstance = new MapGoogleHandle( c.getApplicationContext() );
        }
        return mInstance;
    }

    private MapGoogleHandle(Context c) {
        this.c = c;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney = new LatLng(55.6977291, 12.580165);
        mMap.addMarker(new MarkerOptions().position(sydney).title("I dont know where I am"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }
}
