package com.inau.gstur.gesturepresenter.Gestures;

/**
 * Created by Ivan on 24-May-16.
 */
public interface GestureListener {

    public enum Gesture {
        UP, DOWN, LEFT, RIGHT, TILTL, TILTR, IDLE
    }

    void onGestureRecognition(Gesture gesture);

}
