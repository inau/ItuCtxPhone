/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.inau.gstur.gesturepresenter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.inau.gstur.gesturepresenter.Comms.GestureService;
import com.inau.gstur.gesturepresenter.Gestures.GestureListener;
import com.inau.gstur.gesturepresenter.Gestures.GestureOverlay;
import com.inau.gstur.gesturepresenter.Gestures.ScrollableImageView;

public class MainActivity extends Activity implements GestureListener {
    private ScrollableImageView mGraphView;
    private GestureOverlay go;
    protected static GestureListener reference;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGraphView = (ScrollableImageView) findViewById(R.id.image);
        mGraphView.setSources(new Drawable[]{
                getDrawable(R.drawable.bone1),
                getDrawable(R.drawable.bone2),
                getDrawable(R.drawable.bone3)
        });

        reference = this;

        startService(new Intent(this, GestureService.class));
        go = new GestureOverlay(this, this);
    }

    public static GestureListener getBTRef() {
        return reference;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, GestureService.class));
        reference = null;
    }

    @Override
    public void onGestureRecognition(Gesture gesture) {
        switch (gesture) {
            case UP:
                mGraphView.pan(gesture);
                break;
            case DOWN:
                mGraphView.pan(gesture);
                break;
            case LEFT:
                mGraphView.pan(gesture);
                break;
            case RIGHT:
                mGraphView.pan(gesture);
                break;
            case TILTL:
                mGraphView.zoom(-1);
                break;
            case TILTR:
                mGraphView.zoom(1);

        }
    }
}
