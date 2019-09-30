package com.mediatech.mobilehealth.ui.user;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.mediatech.mobilehealth.R;
import com.mediatech.mobilehealth.ui.base.BaseActivity;

import dagger.android.AndroidInjection;

public class UserActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
    }
}
