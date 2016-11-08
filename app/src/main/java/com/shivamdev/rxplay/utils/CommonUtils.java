package com.shivamdev.rxplay.utils;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.shivamdev.rxplay.MainActivity;
import com.shivamdev.rxplay.views.DatePickerFragment;
import com.shivamdev.rxplay.views.TimePickerFragment;

import rx.Observable;
import rx.Subscriber;
import rx.subjects.PublishSubject;

/**
 * Created by shivam on 12/8/16.
 */

public class CommonUtils {

    private static final String TAG = CommonUtils.class.getSimpleName();

    public static void hideKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Service.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }

    public static boolean isServiceRunning(Context context, Class clazz) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service :
                manager.getRunningServices(Integer.MAX_VALUE)) {
            if (clazz.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static Observable<String> openDatePicker(Context context) {
        PublishSubject<String> dateSubject = PublishSubject.create();
        DatePickerFragment datePickerFragment = DatePickerFragment
                .newInstance(System.currentTimeMillis(), 0);
        datePickerFragment.show(((MainActivity) context).getSupportFragmentManager(), TAG);
        datePickerFragment.getSelectedDate()
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Logger.log(TAG, "onCompleted: ");
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: ", e);
                    }
                    @Override
                    public void onNext(String selectedDate) {
                        dateSubject.onNext(selectedDate);
                        dateSubject.onCompleted();
                    }
                });
        return dateSubject;
    }

    public static Observable<String> openTimePicker(Context context) {
        PublishSubject<String> dateSubject = PublishSubject.create();
        TimePickerFragment timePickerFragment = TimePickerFragment.newInstance();
        timePickerFragment.show(((MainActivity) context).getSupportFragmentManager(), TAG);
        timePickerFragment.getSelectedTime()
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Logger.log(TAG, "onCompleted: ");
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: ", e);
                    }
                    @Override
                    public void onNext(String selectedDate) {
                        dateSubject.onNext(selectedDate);
                        dateSubject.onCompleted();
                    }
                });
        return dateSubject;
    }
}