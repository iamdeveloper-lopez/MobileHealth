package com.mediatech.mobilehealth.ui.main;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class MainViewModule {

    @Binds
    abstract MainInteractor.View provideView(MainActivity mainActivity);

}
