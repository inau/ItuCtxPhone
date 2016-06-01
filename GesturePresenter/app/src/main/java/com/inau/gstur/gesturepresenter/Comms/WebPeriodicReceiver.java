package com.inau.gstur.gesturepresenter.Comms;

import android.util.Log;

import com.inau.gstur.gesturepresenter.Gestures.GestureListener;

/**
 * Created by Ivan on 29-May-16.
 */
public class WebPeriodicReceiver implements GestureHandler, Runnable {
    protected GestureListener target;
    protected volatile boolean requesting = false;

    public WebPeriodicReceiver(GestureListener target) {
        this.target = target;
    }

    @Override
    public void onGesturesReceived(GestureInfo[] gi) {
        requesting = false;
        for (GestureInfo g : gi) {
            Log.i("AA", g.gesture + " " + g.count);
            for (int i = 0; i < g.count; i++) target.onGestureRecognition(g.gesture);
        }
    }

    @Override
    public void run() {
        if(target == null) return;
        while (true) {
            requesting = true;
            new WebHelper(this).execute();
            while(requesting) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
