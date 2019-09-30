package com.mediatech.mobilehealth.ui.main;

import com.mediatech.mobilehealth.api.ApiService;
import com.mediatech.mobilehealth.rx.SchedulersFacade;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {

    @Inject
    ApiService apiService;

    @Provides
    MainPresenter providePresenter(MainInteractor.View view, MainInteractor.UseCase interactorUseCase, SchedulersFacade schedulersFacade) {
        return new MainPresenter(view, interactorUseCase, schedulersFacade);
    }

}
