package com.inau.gstur.gesturepresenter.Comms;

import com.inau.gstur.gesturepresenter.Gestures.GestureListener;

/**
 * Created by Ivan on 30-May-16.
 */
public class GestureInfo {
    final int count;
        final GestureListener.Gesture gesture;

        public GestureInfo(GestureListener.Gesture gesture, int count) {
            this.count = count;
            this.gesture = gesture;
        }
}
