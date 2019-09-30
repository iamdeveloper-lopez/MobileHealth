package com.mediatech.mobilehealth.helper;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.fragment.app.Fragment;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class IntentHelper {

    private IntentHelper(Builder builder) {

        switch (builder.intentType) {
            case ACTIVITY: {
                Intent intent = new Intent(builder.activity.get(), builder.cls);
                if (builder.extras != null) {
                    intent.putExtras(builder.extras);
                }
                if (builder.data != null) {
                    intent.setData(builder.data);
                }
                if (!TextUtils.isEmpty(builder.action)) {
                    intent.setAction(builder.action);
                }
                if (builder.mainPage) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                }
                if (builder.requestCode != Integer.MIN_VALUE) {
                    builder.activity.get().startActivityForResult(intent, builder.requestCode);
                } else {
                    builder.activity.get().startActivity(intent);
                }
                if (builder.exitParentPage) {
                    builder.activity.get().finish();
                }
                break;
            }
            case FRAGMENT: {
                Intent intent = new Intent(builder.fragment.get().getActivity(), builder.cls);
                if (builder.extras != null) {
                    intent.putExtras(builder.extras);
                }
                if (builder.data != null) {
                    intent.setData(builder.data);
                }
                if (!TextUtils.isEmpty(builder.action)) {
                    intent.setAction(builder.action);
                }
                if (builder.mainPage) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                }
                if (builder.requestCode != Integer.MIN_VALUE) {
                    builder.fragment.get().startActivityForResult(intent, builder.requestCode);
                } else {
                    builder.fragment.get().startActivity(intent);
                }
                if (builder.exitParentPage) {
                    Activity activity = builder.fragment.get().getActivity();
                    if (activity != null) {
                        activity.finish();
                    }
                }
                break;
            }
        }
    }

    private enum IntentType {
        ACTIVITY,
        FRAGMENT
    }

    public static class Builder {
        private WeakReference<Activity> activity;
        private WeakReference<Fragment> fragment;
        private Class<?> cls;
        private Bundle extras;
        private Uri data;
        private String action;
        private boolean mainPage;
        private boolean withAnimation;
        private int requestCode = Integer.MIN_VALUE;
        private IntentType intentType;
        private boolean exitParentPage;

        public Builder(Activity activity) {
            intentType = IntentType.ACTIVITY;
            this.activity = new WeakReference<>(activity);
        }

        public Builder(Fragment fragment) {
            intentType = IntentType.FRAGMENT;
            this.fragment = new WeakReference<>(fragment);
        }

        public Builder toClass(Class<?> cls) {
            this.cls = cls;
            return this;
        }

        public Builder setTitle(String title) {
            checkIfNotNull();
            this.extras.putString("title", title);
            return this;
        }

        public Builder setData(Uri data) {
            this.data = data;
            return this;
        }

        public Builder setAction(String action) {
            this.action = action;
            return this;
        }

        public Builder putExtras(Bundle extras) {
            if (this.extras != null)
                this.extras.putAll(extras);
            else
                this.extras = extras;
            return this;
        }

        public Builder putExtra(String key, Serializable object) {
            checkIfNotNull();
            this.extras.putSerializable(key, object);
            return this;
        }

        public Builder putExtra(String key, List<String> object) {
            checkIfNotNull();
            this.extras.putStringArrayList(key, new ArrayList<>(object));
            return this;
        }

        public Builder putExtra(String key, ArrayList<String> object) {
            checkIfNotNull();
            this.extras.putStringArrayList(key, object);
            return this;
        }

        public Builder putExtra(String key, Parcelable object) {
            checkIfNotNull();
            this.extras.putParcelable(key, object);
            return this;
        }

        private void checkIfNotNull() {
            if (this.extras == null)
                this.extras = new Bundle();
        }

        public Builder putExtra(String key, String object) {
            checkIfNotNull();
            this.extras.putString(key, object);
            return this;
        }

        public Builder putExtra(String key, boolean object) {
            checkIfNotNull();
            this.extras.putBoolean(key, object);
            return this;
        }

        public Builder putExtra(String key, int object) {
            checkIfNotNull();
            this.extras.putInt(key, object);
            return this;
        }

        public Builder putSerializableExtra(String key, Serializable object) {
            checkIfNotNull();
            this.extras.putSerializable(key, object);
            return this;
        }

        public Builder isMainPage(boolean mainPage) {
            this.mainPage = mainPage;
            return this;
        }

        public Builder setAsMainPage(boolean mainPage) {
            this.mainPage = mainPage;
            return this;
        }

        public Builder isMainPage() {
            this.mainPage = true;
            return this;
        }

        public Builder withAnimation(boolean withAnimation) {
            this.withAnimation = withAnimation;
            return this;
        }

        public Builder withAnimation() {
            this.withAnimation = true;
            return this;
        }

        public Builder exitCurrentPage() {
            exitParentPage = true;
            return this;
        }

        public Builder requestCode(int requestCode) {
            this.requestCode = requestCode;
            return this;
        }

        public IntentHelper show() {
            return new IntentHelper(this);
        }

    }

}
