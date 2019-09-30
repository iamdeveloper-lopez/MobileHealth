package com.mediatech.mobilehealth.helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;

import androidx.annotation.AttrRes;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.mediatech.mobilehealth.MobileHealth;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.content.Context.ACTIVITY_SERVICE;

public class AppHelper {

    private static final String TAG = AppHelper.class.getSimpleName();

    private static Activity getActivity(Context context) {
        if (context == null)
            return null;
        else if (context instanceof Activity)
            return (Activity) context;
        else if (context instanceof ContextWrapper)
            return getActivity(((ContextWrapper) context).getBaseContext());
        return null;
    }

    public enum Tint {
        DARK,
        LIGHT
    }

    public static void changeStatusBarIcons(Context context, Tint tint) {
        Activity activity = getActivity(context);
        if (activity != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = activity.getWindow().getDecorView();
            if (tint == Tint.DARK) {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        }
    }

    public static int getAttributeColor(Context context, @AttrRes int attributeColor) {
        Activity activity = getActivity(context);
        TypedValue typedValue = new TypedValue();
        activity.getTheme().resolveAttribute(attributeColor, typedValue, true);
        return typedValue.data;
    }

    public static int getStatusBarHeight(Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowInsets windowInsets = context.getWindow().getDecorView().getRootWindowInsets();
            if (windowInsets != null && windowInsets.getDisplayCutout() != null) {
                Log.d("BAR", "cutouts()");
                int statusBarHeight = 0;
                int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
                }
                Log.d("BAR", "statusBarHeight : " + statusBarHeight);
                return statusBarHeight;
            } else {
                Log.d("BAR", "default()");
                return getStatusBarHeight(context.getBaseContext());
            }
        } else {
            int height = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? 24 : 25;
            float density = context.getResources().getDisplayMetrics().density;
            Log.d("BAR", "height : " + height);
            Log.d("BAR", "density : " + density);
            int statusBarHeight = Math.round(height * density);
            Log.d("BAR", "statusBarHeight : " + statusBarHeight);
            return statusBarHeight;
        }
    }

    public static int getStatusBarHeight(Context context) {
        int height = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? 24 : 25;
        float density = context.getResources().getDisplayMetrics().density;
        Log.d("BAR", "height : " + height);
        Log.d("BAR", "density : " + density);
        int statusBarHeight = Math.round(height * density);
        Log.d("BAR", "statusBarHeight : " + statusBarHeight);
        return statusBarHeight;
    }

    public static int getStatusBarHeight(Fragment context) {
        int height = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? 24 : 25;
        float density = context.getResources().getDisplayMetrics().density;
        Log.d("BAR", "height : " + height);
        Log.d("BAR", "density : " + density);
        int statusBarHeight = Math.round(height * density);
        Log.d("BAR", "statusBarHeight : " + statusBarHeight);
        return statusBarHeight;
    }

    public static boolean isViewOverlapping(View firstView, View secondView) {
        boolean overlapped = false;
        int[] firstPosition = new int[2];
        int[] secondPosition = new int[2];

        firstView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//        secondView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        firstView.getLocationOnScreen(firstPosition);
        secondView.getLocationOnScreen(secondPosition);

        if (!overlapped) {
            int r = firstView.getMeasuredWidth() + firstPosition[0];
            int l = secondPosition[0];
            overlapped = (r >= l) && (r != 0 && l != 0);
        }
//        if (!overlapped) {
//            int l = secondView.getMeasuredWidth() + secondPosition[0];
//            int r = firstPosition[0];
//            overlapped = (l >= r) && (r != 0 && l != 0);
//        }
        return overlapped;
    }

    public static int dp(Context context, int dp) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, dm);
    }

    public static int dp(int dp) {
        Context context = MobileHealth.getContext();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, dm);
    }

    public static boolean isNumber(String str) {
        boolean isNumber = false;
        if (Character.isDigit(str.charAt(0))) {
            isNumber = true;
        }
        return isNumber;
    }

    public static double round(double value, int places) {
        BigDecimal decimal = new BigDecimal(Double.toString(value));
        return decimal.setScale(places, RoundingMode.CEILING).doubleValue();
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) MobileHealth.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo activeNetworkInfo = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                Log.d(TAG, "hasPermissions: " + permission);
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void showDeniedPermissionDialog(final Activity context, String permissionName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle("Permission Required")
                .setMessage(String.format("Allow %s to access your %s by tapping Settings > Permissions > %s.", getApplicationName(), permissionName, permissionName))
                .setPositiveButton("SETTINGS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                        intent.setData(uri);
                        context.startActivity(intent);
                    }
                });
        builder.show();
    }

    public static int getDeviceWidthByPercentage(Activity context, int percent) {
        double percentage = percent / 100f;
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) (metrics.widthPixels * percentage);
    }

    public static int getDeviceWidthByPercentage(int percent) {
        Context context = MobileHealth.getContext();
        double percentage = percent / 100f;
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) (metrics.widthPixels * percentage);
    }

    public static int getDeviceHeightByPercentage(int percent) {
        Context context = MobileHealth.getContext();
        double percentage = percent / 100f;
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) (metrics.heightPixels * percentage);
    }

    public static int getDeviceHeightByPercentage(Activity context, int percent) {
        double percentage = percent / 100f;
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) (metrics.heightPixels * percentage);
    }

    public static int getHeight(View view) {
        try {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            int height = params.height;
            if (height > 0) {
                return height;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (view.getMeasuredHeight() > 0) {
            return view.getMeasuredHeight();
        }
        return getDeviceHeightByPercentage((Activity) view.getContext(), 100);
    }

    public static int getWidth(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        return params.width;
    }

    public static int getHeight(View... views) {
        int totalHeight = 0;
        for (View view : views) {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            totalHeight += params.height;
        }
        return totalHeight;
    }

    public static void toggleStatusBarDecor(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = window.getDecorView();
            boolean isDarkTint = decorView.getSystemUiVisibility() == 0;
            if (isDarkTint) {
                decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                // We want to change tint color to white again.
                // You can also record the flags in advance so that you can turn UI back completely if
                // you have set other flags before, such as translucent or full screen.
                decorView.setSystemUiVisibility(0);
            }
        }
    }

    public static int getDeviceStatusBarHeight(Activity context) {
        Rect rectangle = new Rect();
        Window window = context.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        return rectangle.top;
    }

    public static float getFloatValueInString(String s) {
        return Float.valueOf(s.replaceAll("[^0-9.]", ""));
    }

    public static int getStepsByDistance(float distance) {
        return (int) ((distance * 1000) / 100) * 140;
    }

    public static String generateDeviceId() {
        final String AB = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom rnd = new SecureRandom();
        int len = 10;

        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    public static void openPlayStore(Context context, String applicationId) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + applicationId)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + applicationId)));
        }
    }

    public static void openGoogleMapNavigation(Context context, double latitude, double longitude) {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latitude + "," + longitude);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(mapIntent);
        } else {
            final String googleMapPackage = "com.google.android.apps.maps";
            openPlayStore(context, googleMapPackage);
        }
    }

    public static boolean isServiceRunning(Context context, String serviceName) {
        try {
            ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceName.equals(service.service.getClassName())) {
                    return true;
                }
            }
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private static String getApplicationName() {
        return MobileHealth.getContext().getApplicationInfo().loadLabel(MobileHealth.getContext().getPackageManager()).toString();
    }

    public static List<String> getListOfCountries() {
        List<String> listOfCountries = new ArrayList<>();
        String[] locales = Locale.getISOCountries();
        for (String countryCode : locales) {
            Locale obj = new Locale("", countryCode);
            listOfCountries.add(obj.getDisplayCountry());
        }
        Collections.sort(listOfCountries);
        return listOfCountries;
    }

}