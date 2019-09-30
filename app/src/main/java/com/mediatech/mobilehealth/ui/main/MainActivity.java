package com.mediatech.mobilehealth.ui.main;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mediatech.mobilehealth.R;
import com.mediatech.mobilehealth.helper.DatabaseHelper;
import com.mediatech.mobilehealth.helper.DateHelper;
import com.mediatech.mobilehealth.helper.IntentHelper;
import com.mediatech.mobilehealth.interfaces.StepListener;
import com.mediatech.mobilehealth.model.OpenWeatherWrapper;
import com.mediatech.mobilehealth.sensor.StepDetector;
import com.mediatech.mobilehealth.ui.base.BaseLocationActivity;
import com.mediatech.mobilehealth.ui.settings.SettingsActivity;
import com.mediatech.mobilehealth.ui.user.UserActivity;
import com.squareup.picasso.Picasso;

import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import timber.log.Timber;

public class MainActivity extends BaseLocationActivity implements MainInteractor.View, SensorEventListener, StepListener {

    @BindView(R.id.activity_main_swipe_layout)
    SwipeRefreshLayout swipeLayout;
    @BindView(R.id.activity_main_image_view_weather_cloud_icon_thumbnail)
    ImageView cloud_icon_thumbnail;
    @BindView(R.id.activity_main_text_view_weather_cloud_description)
    TextView description;
    @BindView(R.id.activity_main_text_view_temperature)
    TextView temperature;
    @BindView(R.id.activity_main_text_view_country)
    TextView country;
    @BindView(R.id.activity_main_text_view_step_counter)
    TextView stepCounterView;
    @BindView(R.id.activity_main_text_view_step_counter_label)
    TextView stepCounterLabel;

    @OnClick(R.id.activity_main_image_button_settings)
    void onSettingsClicked() {
        new IntentHelper.Builder(this)
                .toClass(SettingsActivity.class)
                .show();
    }

    @OnClick(R.id.activity_main_image_button_user)
    void onUserClicked() {
        new IntentHelper.Builder(this)
                .toClass(UserActivity.class)
                .show();
    }

    @Inject
    MainPresenter presenter;
    @Inject
    DatabaseHelper database;

    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor stepSensor;
    private long steps = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeLayout.setOnRefreshListener(this::fetchOpenWeatherAPI);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchOpenWeatherAPI();
        sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this, stepSensor);
    }

    private void fetchOpenWeatherAPI() {
        Location location = database.getLocation();
        if (location != null) {
            presenter.loadOpenWeatherAPI(location.getLatitude(), location.getLongitude());
        } else {
            swipeLayout.setRefreshing(false);
        }
    }

    public void showSteps() {
        stepCounterView.setText(String.valueOf(steps));
        stepCounterLabel.setText(String.format("step%s", steps > 0 ? "s" : ""));
    }

    @Override
    public void showWeather(OpenWeatherWrapper wrapper) {
        swipeLayout.setRefreshing(false);
        Picasso.get().load(wrapper.weather.get(0).getIcon()).into(cloud_icon_thumbnail);
        description.setText(wrapper.weather.get(0).title);
        temperature.setText(String.format("%sºC", wrapper.temperature.value));
        country.setText(database.getCountryByCode(wrapper.sun.countryCode));
    }

    @Override
    public void showLoading() {
        swipeLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        swipeLayout.setRefreshing(false);
    }

    @Override
    public void showError(Throwable throwable) {
        Timber.e(throwable);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            String time = DatabaseHelper.get().getString("timestamp", null);
            if (time == null) {
                database.save(location);
                return;
            }
            long timestamp = Long.valueOf(time);
            if (DateHelper.isOlderThan(10, timestamp)) {
                database.save(location);
                presenter.loadOpenWeatherAPI(location.getLatitude(), location.getLongitude());
            } else {
                Log.d("LOCATION", "Location is still valid");
            }
        }
    }

    @Override
    public long provideLocationInterval() {
        return 5;
    }

    @Override
    public float provideLocationDistanceInterval() {
        return 1;
    }

    @Override
    public String provideLocationProvider() {
        return LocationManager.GPS_PROVIDER;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Date date = new Date();
        date.setTime(System.currentTimeMillis());
        Log.d("step", "time: " + DateHelper.newInstance().getFormattedDate(date, "yyyy-MM-dd HH:mm:ss"));
        Sensor sensor = event.sensor;
        float[] values = event.values;
        int value = -1;
        if (values.length > 0) {
            value = (int) values[0];
            Log.d("step", "value1: " + value);
            steps = value;
            showSteps();
        }
//        if (sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
//            Log.d("step", "value2: " + steps++);
//            showSteps();
////            simpleStepDetector.updateAccel(event.timestamp, event.values[0], event.values[1], event.values[2]);
//        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        Log.d("Sensor", "name: " + sensor.getName());
        Log.d("Sensor", "type: " + sensor.getStringType());
    }

    @Override
    public void step(long timeNs) {
//        numSteps++;
//        stepCounterView.setText(String.valueOf(numSteps));
//        stepCounterLabel.setText(String.format("step%s", numSteps > 0 ? "s" : ""));
    }
}
