package com.mediatech.mobilehealth.mvp;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BasePresenter<V> {

    public enum RequestState {
        IDLE,
        LOADING,
        COMPLETE,
        ERROR
    }

    protected final V view;

    private CompositeDisposable disposables = new CompositeDisposable();

    public BasePresenter(V view) {
        this.view = view;
    }

    public void attach() {

    }

    public void detach() {
        disposables.clear();
    }

    protected void addDisposable(Disposable disposable) {
        disposables.add(disposable);
    }

}
