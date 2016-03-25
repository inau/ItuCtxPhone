package itu.msec.sensorserviceapp.wswrapper.data.entity;

import java.util.Date;

/**
 * Created by Ivan on 22-Mar-16.
 */
public class ContextEntity extends BaseEntity {
    public final Long uid;
    public final String type, values;

    public ContextEntity(Long uid, long lat, long lng, String type, String values, Date updated) {
        super(updated, lat, lng);
        this.uid = uid;
        this.type = type;
        this.values = values;
    }

    public ContextEntity(long lat, long lng, String type, String values) {
        this(null, lat, lng, type, values, null);
    }

}
