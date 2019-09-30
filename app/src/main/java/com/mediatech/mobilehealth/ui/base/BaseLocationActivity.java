package com.mediatech.mobilehealth.ui.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mediatech.mobilehealth.helper.AppHelper;
import com.mediatech.mobilehealth.interfaces.LocationProviderImpl;
import com.permissionhelper.PermissionHelper;

import dagger.android.AndroidInjection;
import timber.log.Timber;

public abstract class BaseLocationActivity extends BaseActivity implements LocationListener, LocationProviderImpl {

    private static final String TAG = "Location Manager";

    private PermissionHelper permissionHelper;

    private LocationManager locationManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        if (locationManager == null) {
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        fetchLocationUpdates();

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }

    }

    private void fetchLocationUpdates() {
        PermissionHelper.Builder permissionBuilder = new PermissionHelper.Builder(this)
                .addPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .setPermissionListener(new PermissionHelper.RequestPermissionListener() {
                    @Override
                    public void permissionsGranted() {
                        startLocationUpdates();
                    }

                    @Override
                    public void permissionsDenied() {
                        permissionHelper.check();
                    }

                })
                .setPermissionListener((PermissionHelper.RequestPermissionRationaleListener) permissions -> {
                    AppHelper.showDeniedPermissionDialog(this, permissions.get(0));
                });
        permissionHelper = permissionBuilder.build();
        permissionHelper.check();
    }

    private void startLocationUpdates() {
        try {
            locationManager.requestLocationUpdates(provideLocationProvider(), provideLocationInterval(), provideLocationDistanceInterval(), this);
        } catch (java.lang.SecurityException ex) {
            Timber.i(ex, "fail to request location update, ignore");
        } catch (IllegalArgumentException ex) {
            Timber.e("gps provider does not exist " + ex.getMessage());
        }

    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle bundle) {
        if (status == LocationProvider.AVAILABLE) {
            Log.d(TAG, "GPS Available");
        } else if (status == LocationProvider.OUT_OF_SERVICE) {
            Log.d(TAG, "GPS Signal Not Found");
        } else if (status == LocationProvider.TEMPORARILY_UNAVAILABLE) {
            Log.d(TAG, "GPS Signal Lost");
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(TAG, "onProviderEnabled: " + provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d(TAG, "onProviderDisabled: " + provider);
    }
}
