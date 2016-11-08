package com.shivamdev.rxplay.common.di.components;

import com.shivamdev.rxplay.common.di.modules.NetModule;
import com.shivamdev.rxplay.fragments.RetrofitFragment;
import com.shivamdev.rxplay.service.GpsService;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by shivam on 27/9/16.
 */

@Singleton
@Component(modules = {NetModule.class})
public interface AppComponent {
    void inject(RetrofitFragment fragment);

    void inject(GpsService service);
}
