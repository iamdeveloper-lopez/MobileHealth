package com.mediatech.mobilehealth.model;

import android.text.TextUtils;

import com.google.gson.Gson;

import org.parceler.Parcel;

@Parcel
public class UserSetting implements JsonModel {

    public String gender;//   male = 0.415 | female = 0.413
    public String height;//CM male = 172 | female = 162
    public String weight;//KG

    public UserSetting() {
    }

    public UserSetting(String gender, String height) {
        this.gender = gender;
        this.height = height;
    }

    public UserSetting(String gender, String height, String weight) {
        this.gender = gender;
        this.height = height;
        this.weight = weight;
    }

    public boolean isValid() {
        if (!TextUtils.isEmpty(gender) && !TextUtils.isEmpty(height) && !TextUtils.isEmpty(weight)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toJson() {
        return new Gson().toJson(this);
    }
}
