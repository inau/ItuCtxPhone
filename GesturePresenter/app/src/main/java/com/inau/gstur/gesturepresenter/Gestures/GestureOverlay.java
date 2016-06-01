package com.inau.gstur.gesturepresenter.Gestures;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.inau.gstur.gesturepresenter.R;

/**
 * Created by Ivan on 24-May-16.
 */
public class GestureOverlay {
    final String TAG = "overlay";
    public Button UP, DOWN, LEFT, RIGHT, TILTL, TILTR;

    public GestureOverlay(Activity host, GestureListener listener) {
        UP = (Button) host.findViewById(R.id.g_up);
        setupListener(UP, (GestureListener) host, GestureListener.Gesture.UP);
        DOWN = (Button) host.findViewById(R.id.g_down);
        setupListener(DOWN, (GestureListener) host, GestureListener.Gesture.DOWN);
        LEFT = (Button) host.findViewById(R.id.g_left);
        setupListener(LEFT, (GestureListener) host, GestureListener.Gesture.LEFT);
        RIGHT = (Button) host.findViewById(R.id.g_right);
        setupListener(RIGHT, (GestureListener) host, GestureListener.Gesture.RIGHT);
        TILTL = (Button) host.findViewById(R.id.g_tiltL);
        setupListener(TILTL, (GestureListener) host, GestureListener.Gesture.TILTL);
        TILTR = (Button) host.findViewById(R.id.g_tiltR);
        setupListener(TILTR, (GestureListener) host, GestureListener.Gesture.TILTR);
         }

    void setupListener(Button fab, final GestureListener target, final GestureListener.Gesture type) {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                target.onGestureRecognition(type);
                Log.i(TAG, type.name());
            }
        });
    }

}
