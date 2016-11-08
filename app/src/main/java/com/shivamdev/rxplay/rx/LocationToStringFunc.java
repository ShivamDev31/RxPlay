package com.shivamdev.rxplay.rx;

import android.location.Location;

import rx.functions.Func1;


/**
 * Created by shivam on 12/10/16.
 */

public class LocationToStringFunc implements Func1<Location, String> {

    @Override
    public String call(Location location) {
        if (location != null) {
            return location.getLatitude() + " " + location.getLongitude()
                    + " (" + location.getAccuracy() + " )";
        }
        return "Location is null";
    }
}
