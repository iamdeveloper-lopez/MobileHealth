package com.mediatech.mobilehealth.di;

import android.content.Context;

import com.mediatech.mobilehealth.MobileHealth;
import com.mediatech.mobilehealth.helper.DatabaseHelper;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @Provides
    Context provideContext(MobileHealth app) {
        return app.getApplicationContext();
    }

    @Provides
    DatabaseHelper provideDatabaseHelper(MobileHealth app) {
        return DatabaseHelper.with(app);
    }

}
