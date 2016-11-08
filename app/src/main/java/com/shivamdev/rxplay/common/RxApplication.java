package com.shivamdev.rxplay.common;

import android.app.Application;

import com.shivamdev.rxplay.common.di.components.AppComponent;
import com.shivamdev.rxplay.common.di.components.DaggerAppComponent;

/**
 * Created by shivam on 27/9/16.
 */

public class RxApplication extends Application {

    private static RxApplication instance;
    private AppComponent component;

    public static RxApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        setupComponent();
    }

    private void setupComponent() {
        component = DaggerAppComponent
                .builder()
                .build();
    }

    public AppComponent getComponent() {
        return component;
    }
}
