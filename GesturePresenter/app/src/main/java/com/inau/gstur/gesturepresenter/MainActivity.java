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
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.inau.gstur.gesturepresenter.Gestures.BluetoothGestureListener;
import com.inau.gstur.gesturepresenter.Gestures.GestureOverlay;
import com.inau.gstur.gesturepresenter.Gestures.ScrollableImageView;

public class MainActivity extends Activity implements BluetoothGestureListener {
    private ScrollableImageView mGraphView;
    private GestureOverlay go;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGraphView = (ScrollableImageView) findViewById(R.id.image);
        mGraphView.setImageDrawable(getDrawable(R.drawable.one));
        go = new GestureOverlay(this, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
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
            case TILT_LEFT:
                mGraphView.zoom(-1);
                break;
            case TILT_RIGHT:
                mGraphView.zoom(1);

        }
    }
}
