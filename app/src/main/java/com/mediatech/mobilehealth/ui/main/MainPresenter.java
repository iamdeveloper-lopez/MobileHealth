package com.mediatech.mobilehealth.ui.main;


import com.jakewharton.rxrelay2.BehaviorRelay;
import com.mediatech.mobilehealth.model.OpenWeatherWrapper;
import com.mediatech.mobilehealth.mvp.BasePresenter;
import com.mediatech.mobilehealth.rx.SchedulersFacade;

import io.reactivex.Observable;
import io.reactivex.Single;

public class MainPresenter extends BasePresenter<MainInteractor.View> implements MainInteractor.Presenter {


    private MainInteractor.UseCase interactorUseCase;

    private SchedulersFacade schedulersFacade;

    private final BehaviorRelay<RequestState> requestStateObserver
            = BehaviorRelay.createDefault(BasePresenter.RequestState.IDLE);

    public MainPresenter(MainInteractor.View view, MainInteractor.UseCase interactorUseCase, SchedulersFacade schedulersFacade) {
        super(view);
        this.interactorUseCase = interactorUseCase;
        this.schedulersFacade = schedulersFacade;

        observeRequestState();
    }

    @Override
    public void loadOpenWeatherAPI(double latitude, double longitude) {
        loadOpenWeatherAPI(interactorUseCase.loadOpenWeatherAPI(latitude, longitude));
    }

    private void loadOpenWeatherAPI(Single<OpenWeatherWrapper> openWeatherWrapperSingle) {
        addDisposable(openWeatherWrapperSingle
                .subscribeOn(schedulersFacade.io())
                .observeOn(schedulersFacade.ui())
                .doOnSubscribe(s -> publishRequestState(RequestState.LOADING))
                .doOnSuccess(s -> publishRequestState(RequestState.COMPLETE))
                .doOnError(t -> publishRequestState(RequestState.ERROR))
                .subscribe(view::showWeather, view::showError));
    }

    private void publishRequestState(RequestState requestState) {
        addDisposable(Observable.just(requestState)
                .observeOn(schedulersFacade.ui())
                .subscribe(requestStateObserver));
    }

    private void observeRequestState() {
        addDisposable(
                requestStateObserver.subscribe(requestState -> {
                    switch (requestState) {
                        case IDLE:
                            break;
                        case LOADING:
                            view.showLoading();
                            break;
                        case COMPLETE:
                            view.hideLoading();
                            break;
                        case ERROR:
                            view.hideLoading();
                            break;
                    }
                }, e -> {
                    e.printStackTrace();
                    view.hideLoading();
                }));
    }

}
