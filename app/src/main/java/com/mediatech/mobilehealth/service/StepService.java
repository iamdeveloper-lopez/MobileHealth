package com.mediatech.mobilehealth.service;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.mediatech.mobilehealth.helper.DateHelper;

import java.util.Date;

public class StepService extends Service implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor stepSensor;
    private long steps = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_FASTEST);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this, stepSensor);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Date date = new Date();
        date.setTime(System.currentTimeMillis());
        float[] values = event.values;
        int value = -1;
        if (values.length > 0) {
            value = (int) values[0];
            Log.d("step", "value: " + value);
            steps = value;
            Log.d("step", "time: " + DateHelper.newInstance().getFormattedDate(date, "yyyy-MM-dd"));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
