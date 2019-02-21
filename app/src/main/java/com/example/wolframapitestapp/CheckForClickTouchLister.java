package com.example.wolframapitestapp;

import android.view.MotionEvent;
import android.view.View;

public class CheckForClickTouchLister implements View.OnTouchListener {

    private final static long MAX_TOUCH_DURATION = 100;
    private static long DOWN_TIME = 0;

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                DOWN_TIME = event.getEventTime(); //init time
                return true;
            case MotionEvent.ACTION_UP:
                if (event.getEventTime() - DOWN_TIME <= MAX_TOUCH_DURATION) return true;
                break;
        }
        return false;
    }
}