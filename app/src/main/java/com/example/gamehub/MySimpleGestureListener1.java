package com.example.gamehub;

import android.view.GestureDetector;
import android.view.MotionEvent;

public class MySimpleGestureListener1 extends GestureDetector.SimpleOnGestureListener {

    private CoingrabActivity parent;
    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;

    MySimpleGestureListener1(CoingrabActivity parent) {
        this.parent = parent;
    }

    @Override
    public boolean onDown(MotionEvent event) {
        return true;
    }

    // implement the swipe function here:
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2,
                           float velocityX, float velocityY) {
        boolean result = false;

        // find amount of change in both X and Y axis
        float diffX = e2.getX() - e1.getX();
        float diffY = e2.getY() - e1.getY();

        if (Math.abs(diffX) > Math.abs(diffY)) {
            if (Math.abs(diffX) > SWIPE_THRESHOLD
                    && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffX > 0) {
                    parent.doRight(null);
                } else {
                    parent.doLeft(null);
                }
            }
            result = true;
        } else if (Math.abs(diffY) > SWIPE_THRESHOLD
                && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
            if (diffY > 0) {
                parent.doDown(null);
            } else {
                parent.doUp(null);
            }
            result = true;
        }
        return result;
    }
}
