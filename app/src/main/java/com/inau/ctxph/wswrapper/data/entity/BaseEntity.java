package com.inau.ctxph.wswrapper.data.entity;

import java.util.Date;

/**
 * Created by Ivan on 22-Mar-16.
 */
public class BaseEntity {
    public final Date updated;
    public final long lat, lng;

    public BaseEntity(Date updated, long lat, long lng) {
        this.updated = updated;
        this.lat = lat;
        this.lng = lng;
    }
}
