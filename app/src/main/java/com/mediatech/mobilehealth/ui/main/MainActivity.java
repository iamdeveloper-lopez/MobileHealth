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
import com.mediatech.mobilehealth.model.OpenWeatherWrapper;
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

public class MainActivity extends BaseLocationActivity implements MainInteractor.View, SensorEventListener {

    private static final String TAG = "STEP";

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
    @BindView(R.id.activity_main_text_view_pressure)
    TextView pressure;
    @BindView(R.id.activity_main_text_view_humidity)
    TextView humidity;
    @BindView(R.id.activity_main_text_view_wind_speed)
    TextView windSpeed;

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

    private SensorManager sensorManager;
    private Sensor stepSensor;
    private long steps = 0;

    private String lastTimeMillis;
    private int lastStatus = 0;

    private int counter = 0;

    private long startWalkTime = 0;
    private long endWalkTime = 0;

    private long startRunningTime = 0;
    private long endRunningTime = 0;

    private long totalRunning = 0;
    private long totalWalking = 0;

    private int WALKING = 1;
    private int RUNNING = 2;

    private String active_running_time_key;
    private String active_walking_time_key;
    private String steps_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeLayout.setOnRefreshListener(this::fetchOpenWeatherAPI);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        String key = DateHelper.newInstance().getFormattedDate(new Date(), "yyyy-MM-dd");

        active_running_time_key = key + "-active-running-time";
        active_walking_time_key = key + "-active-walking-time";
        steps_key = key + "-steps";

        totalRunning = DatabaseHelper.get().getLong(active_running_time_key, 0);
        totalWalking = DatabaseHelper.get().getLong(active_walking_time_key, 0);

        steps = DatabaseHelper.get().getLong(steps_key, 0);

        showSteps();

        if (database.getSetting() != null) {
            if (database.getSetting().isValid()) {
                //VALID
            } else {
                new IntentHelper.Builder(this)
                        .toClass(SettingsActivity.class)
                        .show();
            }
        } else {
            new IntentHelper.Builder(this)
                    .toClass(SettingsActivity.class)
                    .show();
        }

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
        pressure.setText(String.format("%s hPa",wrapper.temperature.pressure));
        humidity.setText(String.format("%s %%",wrapper.temperature.humidity));
        windSpeed.setText(String.format("%s m/s",wrapper.wind.speed));
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
        String timeMillis = DateHelper.newInstance().getFormattedDate(date, "yyyy-MM-dd HH:mm:ss");
        int status = WALKING;
        if (timeMillis.equalsIgnoreCase(lastTimeMillis)) {
            counter++;
            if (counter >= 3) {
                status = RUNNING;
                Log.d(TAG, "status : running");
            } else if (counter == 1 || counter == 2) {
                status = WALKING;
                Log.d(TAG, "status : walking");
            }
        } else {
            counter = 0;
            counter++;
        }

        lastTimeMillis = timeMillis;

        steps++;
        DatabaseHelper.get().put(steps_key, steps);
        showSteps();

        if (status == RUNNING) {
            Log.d(TAG, "running last status: " + lastStatus);
            if (startRunningTime == 0)
                startRunningTime = System.currentTimeMillis();
            if (lastStatus != 0) {
                if (lastStatus != status) {
                    if (endWalkTime == 0) {
                        endWalkTime = System.currentTimeMillis();
                        //SAVE WALKING
                        totalWalking += (endWalkTime - startWalkTime);
                        DatabaseHelper.get().put(active_walking_time_key, totalWalking);
                        Log.d(TAG, "WALKING TIME : " + totalWalking);
                        startWalkTime = 0;
                        endWalkTime = 0;
                    }
                }
            }
        }

        if (status == WALKING) {
            Log.d(TAG, "walking last status: " + lastStatus);
            if (startWalkTime == 0)
                startWalkTime = System.currentTimeMillis();
            if (lastStatus != 0) {
                if (lastStatus != status) {
                    if (endRunningTime == 0) {
                        endRunningTime = System.currentTimeMillis();
                        //SAVE RUNNING
                        totalRunning += (endRunningTime - startRunningTime);
                        DatabaseHelper.get().put(active_running_time_key, totalRunning);
                        Log.d(TAG, "RUNNING TIME : " + totalRunning);
                        startRunningTime = 0;
                        endRunningTime = 0;
                    }
                }
            }
        }

        lastStatus = status;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        Log.d("Sensor", "name: " + sensor.getName());
        Log.d("Sensor", "type: " + sensor.getStringType());
    }
}
