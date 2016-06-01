package com.inau.gstur.gesturepresenter.Comms;

/**
 * Created by Ivan on 29-May-16.
 */
public interface GestureHandler {

    void onGesturesReceived(GestureInfo[] gi);
}