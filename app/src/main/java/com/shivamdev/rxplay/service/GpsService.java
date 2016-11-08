package com.shivamdev.rxplay.service;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.shivamdev.rxplay.MainActivity;
import com.shivamdev.rxplay.R;
import com.shivamdev.rxplay.common.RxApplication;
import com.shivamdev.rxplay.network.GpsApi;
import com.shivamdev.rxplay.network.GpsData;
import com.shivamdev.rxplay.rx.LocationToString;
import com.shivamdev.rxplay.utils.DateTimeUtils;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by shivam on 7/11/16.
 */

public class GpsService extends Service {

    private static final String TAG = GpsService.class.getSimpleName();
    private static final long LOCATION_SERVER_POLLING_INTERVAL = 30;

    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    @Inject
    GpsApi gpsApi;

    @Override
    public void onCreate() {
        super.onCreate();
        RxApplication.getInstance().getComponent().inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "onStartCommand: ");
        final Intent homeIntent = new Intent(GpsService.this, MainActivity.class);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        final PendingIntent pendingIntent = PendingIntent.getActivity(GpsService.this,
                (int) System.currentTimeMillis(), homeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap logoIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        final Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.tracking_your_gps_location))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_HIGH)
                .setLargeIcon(Bitmap.createScaledBitmap(logoIcon, 128, 128, false))
                .build();
        int NOTIFICATION_ID = 1337;
        startForeground(NOTIFICATION_ID, notification);
        /*final PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        final PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, TAG);
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                gpsPingUpdate();
                if (wakeLock != null && !wakeLock.isHeld()) {
                    //wakeLock.acquire();
                }
                handler.postDelayed(this, LOCATION_SERVER_POLLING_INTERVAL);
            }
        };
        handler.post(runnable);*/

        pollGpsPings();

        return START_STICKY;
    }

    private void pollGpsPings() {
        Observable.just(LOCATION_SERVER_POLLING_INTERVAL)
                .delay(LOCATION_SERVER_POLLING_INTERVAL, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                //.retryWhen(new RxPollingFunc1(LOCATION_SERVER_POLLING_INTERVAL, TimeUnit.SECONDS))
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: Polling : ", e);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        gpsPingUpdate();
                        pollGpsPings();
                    }
                });
    }

    private void gpsPingUpdate() {
        Log.d(TAG, "gpsPingUpdate: ");
        ReactiveLocationProvider provider = new ReactiveLocationProvider(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "gpsPingUpdate: Dont have location permissions", null);
            return;
        }
        Subscription subs = provider.getLastKnownLocation()
                .map(new LocationToString())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: Getting location : ", e);
                    }

                    @Override
                    public void onNext(String location) {
                        Log.d(TAG, "onNext: " + location);
                        sendGpsDataToServer(location);
                    }
                });
        compositeSubscription.add(subs);
    }

    private void sendGpsDataToServer(String location) {
        Log.d(TAG, "sendGpsDataToServer: ");
        GpsData gpsData = new GpsData();
        gpsData.location = location;
        gpsData.dateTime = DateTimeUtils.getCurrentFullDateTime();
        gpsData.device = "one";
        Subscription subs = gpsApi.postGpsData(gpsData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: Send to server : ", e);
                    }

                    @Override
                    public void onNext(Void aVoid) {
                        Log.d(TAG, "onNext: " + "GPS Ping at : " + DateTimeUtils.getCurrentDateTime());
                    }
                });
        compositeSubscription.add(subs);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: Gps Service");
        compositeSubscription.clear();
    }
}
