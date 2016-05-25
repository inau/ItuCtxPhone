package com.inau.gstur.gesturepresenter.Gestures;

/**
 * Created by Ivan on 24-May-16.
 */
public interface BluetoothGestureListener {

    public enum Gesture {
        UP, DOWN, LEFT, RIGHT, TILT_LEFT, TILT_RIGHT, IDLE
    }

    void onGestureRecognition(Gesture gesture);

}
