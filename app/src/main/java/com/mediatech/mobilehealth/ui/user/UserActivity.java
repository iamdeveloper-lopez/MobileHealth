package com.mediatech.mobilehealth.ui.user;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.mediatech.mobilehealth.R;
import com.mediatech.mobilehealth.helper.AppHelper;
import com.mediatech.mobilehealth.helper.DatabaseHelper;
import com.mediatech.mobilehealth.helper.DateHelper;
import com.mediatech.mobilehealth.model.UserSetting;
import com.mediatech.mobilehealth.ui.base.BaseActivity;

import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import dagger.android.AndroidInjection;

public class UserActivity extends BaseActivity {

    private static final String TAG = "USER";

    @Inject
    DatabaseHelper database;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.activity_user_text_view_calories)
    TextView textViewCalories;
    @BindView(R.id.activity_user_text_view_distance)
    TextView textViewDistance;

    private float walking = 3.8f;
    private float running = 7.5f;

    private UserSetting setting;

    private String active_running_time_key;
    private String active_walking_time_key;
    private String steps_key;

    private long totalRunning = 0;
    private long totalWalking = 0;

    private long steps = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        setting = database.getSetting();

        String key = DateHelper.newInstance().getFormattedDate(new Date(), "yyyy-MM-dd");

        active_running_time_key = key + "-active-running-time";
        active_walking_time_key = key + "-active-walking-time";
        steps_key = key + "-steps";

        totalRunning = DatabaseHelper.get().getLong(active_running_time_key, 0);
        totalWalking = DatabaseHelper.get().getLong(active_walking_time_key, 0);

        steps = DatabaseHelper.get().getLong(steps_key, 0);

        toolbar.setNavigationOnClickListener(view -> {
            finish();
        });

        //CALORIES

        float weight = Float.valueOf(setting.weight);
        float walkingCalories = ((weight * walking) / 3600f) * (totalWalking / 1000.0f);
        Log.d(TAG, "walking : " + walkingCalories);
        float runningCalories = ((weight * running) / 3600f) * (totalRunning / 1000.0f);
        Log.d(TAG, "running : " + runningCalories);

        float totalCalories = (walkingCalories + runningCalories);
        Log.d(TAG, "total : " + totalCalories);

        textViewCalories.setText("" + Math.round(totalCalories) + " kcal");

        //DISTANCE

        float step_length = Float.valueOf(setting.gender) * Float.valueOf(setting.height);
        float distance = (steps * step_length) / (float) 100000;

        textViewDistance.setText("" + AppHelper.round(distance, 2) + " km");

    }
}
