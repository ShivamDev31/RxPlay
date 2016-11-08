package com.shivamdev.rxplay.common.di.modules;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by shivam on 27/9/16.
 */

@Module
public class AppModule {

    private final Application application;

    public AppModule(Application app) {
        application = app;
    }

    @Singleton
    @Provides
    public Application providesApplication() {
        return application;
    }

}
