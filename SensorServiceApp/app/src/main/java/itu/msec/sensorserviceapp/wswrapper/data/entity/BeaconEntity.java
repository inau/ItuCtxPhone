package itu.msec.sensorserviceapp.wswrapper.data.entity;

import java.util.Date;

/**
 * Created by Ivan on 22-Mar-16.
 */
public class BeaconEntity extends BaseEntity {
    public final String key, uid, major, minor;

    public BeaconEntity( String key, String uid, String major, String minor, long lat, long lng, Date updated ) {
        super(updated, lat, lng);
        this.key = key;
        this.uid = uid;
        this.major = major;
        this.minor = minor;
    }

    public BeaconEntity( String uid, String major, String minor, long lat, long lng) {
        this(null, uid, major, minor, lat, lng, null);
    }

}
