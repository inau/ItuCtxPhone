package com.inau.gstur.gesturepresenter.Gestures;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by Ivan on 24-May-16.
 */
public class ScrollableImageView extends ImageView {

    private static final int INVALID_POINTER_ID = -1;

        final protected float PAN_FACTOR = 100;
        final protected int block_size = 10;

        private float mPosX;
        private float mPosY;

        private float mLastTouchX;
        private float mLastTouchY;
        private float mLastGestureX;
        private float mLastGestureY;
        private int mActivePointerId = INVALID_POINTER_ID;

        private ScaleGestureDetector mScaleDetector;
        private float mScaleFactor = 1.f;

        private Drawable[] mSources;
        private int currDrawable;

        public ScrollableImageView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
            mScaleDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
        }

        public ScrollableImageView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        }

        public void setSources(Drawable[] sources) {
            this.mSources = sources;
            this.currDrawable = 0;
            reloadImage();
        }

        public void nextDrawable() {
            currDrawable = (currDrawable+1) % mSources.length;
            reloadImage();
        }

        public void prevDrawable() {
            currDrawable--;
            if( currDrawable < 0 ) currDrawable = mSources.length-1;
            reloadImage();
        }

        private void reloadImage() {
            this.setImageDrawable( mSources[currDrawable] );

            this.mPosX = this.getWidth() / 4;;
            this.mPosY = this.getHeight() / 4;;
            zoom(1);
            invalidate();
        }

        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            // Let the ScaleGestureDetector inspect all events.
            mScaleDetector.onTouchEvent(ev);

            final int action = ev.getAction();
            switch (action & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN: {
                    Log.i("ges", "finger_down");
                    if (!mScaleDetector.isInProgress()) {
                        final float x = ev.getX();
                        final float y = ev.getY();

                        mLastTouchX = x;
                        mLastTouchY = y;
                        mActivePointerId = ev.getPointerId(0);
                    }
                    break;
                }
                case MotionEvent.ACTION_POINTER_1_DOWN: {
                    Log.i("ges", "finger_down_1");
                    if (mScaleDetector.isInProgress()) {
                        final float gx = mScaleDetector.getFocusX();
                        final float gy = mScaleDetector.getFocusY();
                        mLastGestureX = gx;
                        mLastGestureY = gy;
                    }
                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    Log.i("ges", "finger_moving");

                    // Only move if the ScaleGestureDetector isn't processing a gesture.
                    if (!mScaleDetector.isInProgress()) {
                        final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                        final float x = ev.getX(pointerIndex);
                        final float y = ev.getY(pointerIndex);

                        final float dx = x - mLastTouchX;
                        final float dy = y - mLastTouchY;

                        mPosX += dx;
                        mPosY += dy;

                        invalidate();

                        mLastTouchX = x;
                        mLastTouchY = y;
                    }
                    else{
                        final float gx = mScaleDetector.getFocusX();
                        final float gy = mScaleDetector.getFocusY();

                        final float gdx = gx - mLastGestureX;
                        final float gdy = gy - mLastGestureY;

                        mPosX += gdx;
                        mPosY += gdy;

                        invalidate();

                        mLastGestureX = gx;
                        mLastGestureY = gy;
                    }

                    break;
                }
                case MotionEvent.ACTION_UP: {
                    Log.i("ges", "finger_release");
                    mActivePointerId = INVALID_POINTER_ID;
                    break;
                }
                case MotionEvent.ACTION_CANCEL: {
                    Log.i("ges", "finger_cancel");
                    mActivePointerId = INVALID_POINTER_ID;
                    break;
                }
                case MotionEvent.ACTION_POINTER_UP: {
                    Log.i("ges", "finger_up");

                    final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK)
                            >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                    final int pointerId = ev.getPointerId(pointerIndex);
                    if (pointerId == mActivePointerId) {
                        // This was our active pointer going up. Choose a new
                        // active pointer and adjust accordingly.
                        final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                        mLastTouchX = ev.getX(newPointerIndex);
                        mLastTouchY = ev.getY(newPointerIndex);
                        mActivePointerId = ev.getPointerId(newPointerIndex);
                    }
                    else{
                        final int tempPointerIndex = ev.findPointerIndex(mActivePointerId);
                        mLastTouchX = ev.getX(tempPointerIndex);
                        mLastTouchY = ev.getY(tempPointerIndex);
                    }

                    break;
                }
            }

            return true;
        }

        @Override
        public void onDraw(Canvas canvas) {

            canvas.save();

            canvas.translate(mPosX, mPosY);

            if (mScaleDetector.isInProgress()) {
                canvas.scale(mScaleFactor, mScaleFactor, mScaleDetector.getFocusX(), mScaleDetector.getFocusY());
            }
            else{
                canvas.scale(mScaleFactor, mScaleFactor, mLastGestureX, mLastGestureY);
            }
            super.onDraw(canvas);
            canvas.restore();
        }

    public void pan(GestureListener.Gesture gesture) {
        if(mScaleFactor < 0.5) {
            switch (gesture) {
                case LEFT:
                    prevDrawable();
                    break;
                case RIGHT:
                    nextDrawable();
                    break;
            }
        }
        switch (gesture) {
            case LEFT:
                if(this.mPosX < block_size*PAN_FACTOR) this.mPosX += PAN_FACTOR;
                break;
            case RIGHT:
                if(this.mPosX > -block_size*PAN_FACTOR) this.mPosX -= PAN_FACTOR;
                break;
            case UP:
                if(this.mPosY < block_size*PAN_FACTOR) this.mPosY += PAN_FACTOR;
                break;
            case DOWN:
                if(this.mPosY > -block_size*PAN_FACTOR) this.mPosY -= PAN_FACTOR;
                break;
            case IDLE:
                break;
        }
        Log.i("pan", mPosX+" "+mPosY);
        invalidate();
    }

    public void zoom(int i) {
        if(i > 0 && mScaleFactor < 5) mScaleFactor *= 1.1;
        else if (i<0)   if(mScaleFactor > 0.5) mScaleFactor *= 0.9;
                        else {
                            Toast.makeText(getContext(), "Pan to swap image", Toast.LENGTH_SHORT).show();
                        }
        Log.i("scale", ""+mScaleFactor);
        invalidate();
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                mScaleFactor *= detector.getScaleFactor();

                // Don't let the object get too small or too large.
                mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));

                invalidate();
                return true;
            }
        }

}
