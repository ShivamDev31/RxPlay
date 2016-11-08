package com.shivamdev.rxplay.network;

import com.shivamdev.rxplay.utils.Constants;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by shivam on 7/11/16.
 */

public interface GpsApi {

    @POST(Constants.Urls.GEO_URL)
    Observable<Void> postGpsData(@Body GpsData gpsData);
}
