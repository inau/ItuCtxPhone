package pervasive2016.itu.contextapp.data;

/**
 * Created by martinosecchi on 25/03/16.
 */
public class UserLocation {
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
}
