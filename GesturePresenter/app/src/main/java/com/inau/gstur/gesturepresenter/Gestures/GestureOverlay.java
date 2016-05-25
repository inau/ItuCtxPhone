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

    public GestureOverlay(Activity host, BluetoothGestureListener listener) {
        UP = (Button) host.findViewById(R.id.g_up);
        setupListener(UP, (BluetoothGestureListener) host, BluetoothGestureListener.Gesture.UP);
        DOWN = (Button) host.findViewById(R.id.g_down);
        setupListener(DOWN, (BluetoothGestureListener) host, BluetoothGestureListener.Gesture.DOWN);
        LEFT = (Button) host.findViewById(R.id.g_left);
        setupListener(LEFT, (BluetoothGestureListener) host, BluetoothGestureListener.Gesture.LEFT);
        RIGHT = (Button) host.findViewById(R.id.g_right);
        setupListener(RIGHT, (BluetoothGestureListener) host, BluetoothGestureListener.Gesture.RIGHT);
        TILTL = (Button) host.findViewById(R.id.g_tiltL);
        setupListener(TILTL, (BluetoothGestureListener) host, BluetoothGestureListener.Gesture.TILT_LEFT);
        TILTR = (Button) host.findViewById(R.id.g_tiltR);
        setupListener(TILTR, (BluetoothGestureListener) host, BluetoothGestureListener.Gesture.TILT_RIGHT);
         }

    void setupListener(Button fab, final BluetoothGestureListener target, final BluetoothGestureListener.Gesture type) {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                target.onGestureRecognition(type);
                Log.i(TAG, type.name());
            }
        });
    }

}
