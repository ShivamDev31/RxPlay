package com.shivamdev.rxplay.utils;

import android.util.Log;

/**
 * Created by shivam on 12/8/16.
 */

public class Logger {

    private static final String TAG = Logger.class.getSimpleName();

    public static void log(String tag, String msg) {
        Log.d(tag, msg);
    }

    public static void log(String msg) {
        log(TAG, msg);
    }
}
