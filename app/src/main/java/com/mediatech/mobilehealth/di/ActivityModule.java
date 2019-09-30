package com.mediatech.mobilehealth.di;

import com.mediatech.mobilehealth.ui.base.BaseActivity;
import com.mediatech.mobilehealth.ui.base.BaseLocationActivity;
import com.mediatech.mobilehealth.ui.main.MainActivity;
import com.mediatech.mobilehealth.ui.main.MainModule;
import com.mediatech.mobilehealth.ui.main.MainViewModule;
import com.mediatech.mobilehealth.ui.settings.SettingsActivity;
import com.mediatech.mobilehealth.ui.splash.SplashActivity;
import com.mediatech.mobilehealth.ui.user.UserActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract BaseActivity baseActivity();

    @ContributesAndroidInjector
    abstract BaseLocationActivity baseLocationActivity();

    @ContributesAndroidInjector
    abstract SplashActivity splashActivity();

    @ContributesAndroidInjector(modules = {MainViewModule.class, MainModule.class})
    abstract MainActivity mainActivity();

    @ContributesAndroidInjector
    abstract SettingsActivity settingsActivity();

    @ContributesAndroidInjector
    abstract UserActivity userActivity();

}
