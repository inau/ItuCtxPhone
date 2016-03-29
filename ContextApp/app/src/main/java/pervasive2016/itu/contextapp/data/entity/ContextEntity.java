package pervasive2016.itu.contextapp.data.entity;

import java.util.Date;

/**
 * Created by Ivan on 22-Mar-16.
 */
public class ContextEntity {
    public final Long uid;
    public final String type, values;
    public final Date updated;
    public final double lat, lng;

    public ContextEntity(Long uid, long lat, long lng, String type, String values, Date updated) {
        this.updated = updated;
        this.lat = lat;
        this.lng = lng;
        this.uid = uid;
        this.type = type;
        this.values = values;
    }

    public ContextEntity(long lat, long lng, String type, String values) {
        this(null, lat, lng, type, values, null);
    }

    public ContextEntity(Double lat, Double lng, String type, String values) {
        this(null, lat.longValue(), lng.longValue(), type, values, null);
    }

}
