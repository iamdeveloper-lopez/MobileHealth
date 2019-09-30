package com.mediatech.mobilehealth.helper;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.ref.WeakReference;

public class KeyboardHelper {

    private static final String TAG = KeyboardHelper.class.getSimpleName();

    public static void hideSoftKeyboard(Activity activity) {
        WeakReference<Activity> context = new WeakReference<>(activity);
        View view = context.get().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.get().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public static void hideSoftKeyboard(Context context, View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public static void showSoftKeyboardAndFocus(Activity activity, EditText editText) {
        WeakReference<Activity> context = new WeakReference<>(activity);
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        if (editText.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) context.get().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
            }
        } else {
            Log.d(TAG, "Can't focus on view");
        }
    }

    public static void forceShowSoftKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public static void showSoftKeyboardAndFocus(Context activity, EditText editText) {
        WeakReference<Context> context = new WeakReference<>(activity);
        if (editText.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) context.get().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
            }
        } else {
            Log.d(TAG, "Can't focus on view");
        }
    }

    public static void setUpAutoHideKeyboard(Activity activity, View view) {
        if (!(view instanceof EditText)) {
            view.setOnTouchListener((v, event) -> {
                hideSoftKeyboard(activity);
                return false;
            });
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setUpAutoHideKeyboard(activity, innerView);
            }
        }
    }

    public static void setUpAutoHideKeyboard(View view) {
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        if (!(view instanceof EditText)) {
            view.setOnTouchListener((v, event) -> {
                hideSoftKeyboard(activity);
                return false;
            });
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setUpAutoHideKeyboard(activity, innerView);
            }
        }
    }

}