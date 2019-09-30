package com.mediatech.mobilehealth.helper;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.mediatech.mobilehealth.MobileHealth;

import net.grandcentrix.tray.TrayPreferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

public class DatabaseHelper extends TrayPreferences {

    private static final String TAG = DatabaseHelper.class.getSimpleName();

    private static DatabaseHelper INSTANCE;

    private DatabaseHelper(final Context context) {
        super(context, context.getPackageName(), 1);
    }

    @Override
    protected void onCreate(int initialVersion) {
        super.onCreate(initialVersion);
    }

    @Override
    protected void onUpgrade(int oldVersion, int newVersion) {
        super.onUpgrade(oldVersion, newVersion);
    }

    @Override
    protected void onDowngrade(int oldVersion, int newVersion) {
        super.onDowngrade(oldVersion, newVersion);
    }

    public static DatabaseHelper with(Context context) {
        if (INSTANCE == null) {
            Timber.i("Initialized()");
            INSTANCE = new DatabaseHelper(context);
            return INSTANCE;
        } else
            return INSTANCE;
    }

    public static DatabaseHelper get() {
        if (INSTANCE == null) {
            Context context = MobileHealth.getContext();
            Timber.i("Initialized()");
            INSTANCE = new DatabaseHelper(context);
            return INSTANCE;
        } else
            return INSTANCE;
    }

    public List<String> getListOfCountries() {
        List<String> listOfCountries = new ArrayList<>();
        String[] locales = Locale.getISOCountries();
        for (String countryCode : locales) {
            Locale obj = new Locale("", countryCode);
            listOfCountries.add(obj.getDisplayCountry());
        }
        Collections.sort(listOfCountries);
        return listOfCountries;
    }

    public String getCountryByCode(String code) {
        return new Locale("", code).getDisplayCountry();
    }

    public void save(Location location) {
        Log.d("LOCATION", "Location is saved : " + location);
        DatabaseHelper.get().put("timestamp", String.valueOf(System.currentTimeMillis()));
        DatabaseHelper.get().put("latitude", String.valueOf(location.getLatitude()));
        DatabaseHelper.get().put("longitude", String.valueOf(location.getLongitude()));
    }

    public Location getLocation() {
        String lat = getString("latitude", null);
        String lon = getString("longitude", null);
        if (lat != null && lon != null) {
            Location location = new Location(LocationManager.GPS_PROVIDER);
            location.setLatitude(Double.valueOf(lat));
            location.setLongitude(Double.valueOf(lon));
            return location;
        } else {
            return null;
        }
    }

}