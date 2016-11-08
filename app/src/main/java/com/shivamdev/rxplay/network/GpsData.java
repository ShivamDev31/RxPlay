package com.shivamdev.rxplay.network;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by shivam on 7/11/16.
 */

@Parcel
public class GpsData {

    /**
     * {"Location":
     * [{
     * <p>
     * GEOLocation: "12.932355,77.602337",
     * CapturedDateTime: "11-05-2016 14:17:22",
     * BatteryPercentage:"100"
     * },
     * <p>
     * {
     * GEOLocation: "12.932355,77.602337",
     * CapturedDateTime: "11-05-2016 14:17:22",
     * BatteryPercentage:"78"
     * }]
     * }
     */

    @SerializedName("GEOLocation")
    public String location;

    @SerializedName("CapturedDateTime")
    public String dateTime;

    @SerializedName("Device")
    public String device;
}
