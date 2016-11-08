package com.shivamdev.rxplay.rx;

import android.location.Location;
import android.util.Log;

import com.shivamdev.rxplay.service.GpsService;

import rx.functions.Func1;

/**
 * Created by shivam on 7/11/16.
 */

public class LocationToString implements Func1<Location, String> {

    private static final String TAG = GpsService.class.getSimpleName();

    @Override
    public String call(Location location) {
        if (location != null) {
            Log.d(TAG, "Converting location to string : ");
            return location.getLatitude() + "," + location.getLongitude();
        }
        return "Location is null";
    }
}
