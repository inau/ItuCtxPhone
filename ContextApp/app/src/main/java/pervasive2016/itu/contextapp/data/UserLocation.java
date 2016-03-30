package pervasive2016.itu.contextapp.data;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import java.util.List;

import pervasive2016.itu.contextapp.LocationChangeActivity;
import pervasive2016.itu.contextapp.data.entity.BeaconEntity;

/**
 * Created by martinosecchi on 25/03/16.
 */
public class UserLocation {
    protected static BeaconEntity nearestBeacon;

    private static volatile double lat;
    private static volatile double lng;

    public static double getLatitude() {
        return lat;
    }
    public static void setLatitude(double lat) {
        UserLocation.lat = lat;
    }

    public static double getLongitude() {
        return lng;
    }
    public static void setLongitude(double lng) {
        UserLocation.lng = lng;
    }

    public static BeaconEntity getNearestBeacon() {
        return nearestBeacon;
    }
    public static void setNearestBeacon(BeaconEntity nearestBeacon) {
        UserLocation.nearestBeacon = nearestBeacon;
    }

    public static Location getLocation(LocationChangeActivity c) {
        // Getting LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) c.getSystemService(c.LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        if (ActivityCompat.checkSelfPermission(c, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(c, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0.1f, c);

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
        return location;
    }
}
