package com.mediatech.mobilehealth.ui.splash;

import android.content.Intent;
import android.os.Bundle;

import com.mediatech.mobilehealth.R;
import com.mediatech.mobilehealth.ui.base.BaseActivity;
import com.mediatech.mobilehealth.ui.main.MainActivity;

import java.util.concurrent.TimeUnit;

import dagger.android.AndroidInjection;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Single.fromCallable(() -> "Starting activity")
                .subscribeOn(Schedulers.io())
                .delay(5, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    Timber.d("Starting delay");
                })
                .doOnSuccess(s -> {
                    Timber.d(s);
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                })
                .subscribe();

    }
}
