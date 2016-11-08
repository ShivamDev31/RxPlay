package com.shivamdev.rxplay.common.di.modules;

import com.shivamdev.rxplay.network.GitApi;
import com.shivamdev.rxplay.network.GpsApi;
import com.shivamdev.rxplay.network.RetrofitAdapter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by shivam on 27/9/16.
 */

@Module
public class NetModule {

    @Provides
    @Singleton
    public GitApi provideGitApi() {
        return RetrofitAdapter.get().getRetrofit().create(GitApi.class);
    }

    @Provides
    @Singleton
    public GpsApi provideGpsApi() {
        return RetrofitAdapter.get().getRetrofit().create(GpsApi.class);
    }

}
