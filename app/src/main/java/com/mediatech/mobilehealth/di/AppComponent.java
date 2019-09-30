package com.mediatech.mobilehealth.di;


import com.mediatech.mobilehealth.MobileHealth;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        AppModule.class,
        ApiModule.class,
        ActivityModule.class
})
public interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(MobileHealth app);

        AppComponent build();
    }

    void inject(MobileHealth app);

}
