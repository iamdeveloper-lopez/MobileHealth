package com.mediatech.mobilehealth.model;

import com.google.gson.Gson;

import org.parceler.Parcel;

@Parcel
public class UserSetting implements JsonModel {

    public String gender;//male = 0.415 | female = 0.413
    public String height;//male = 172 | female = 162

    public UserSetting() {
    }

    public UserSetting(String gender, String height) {
        this.gender = gender;
        this.height = height;
    }

    @Override
    public String toJson() {
        return new Gson().toJson(this);
    }
}
