package com.mediatech.mobilehealth.di;

import android.content.Context;

import com.mediatech.mobilehealth.api.ApiService;
import com.mediatech.mobilehealth.api.ServiceGenerator;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApiModule {

    @Singleton
    @Provides
    ApiService apiService(Context context) {
        return ServiceGenerator.createService(context, ApiService.class, ApiService.BASE_URL);
    }
}
