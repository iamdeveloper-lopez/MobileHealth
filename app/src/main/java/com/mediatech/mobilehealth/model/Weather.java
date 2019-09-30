package com.mediatech.mobilehealth.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mediatech.mobilehealth.BuildConfig;

import org.parceler.Parcel;

@Parcel
public class Weather implements JsonModel {

    public long id;
    @SerializedName("main")
    public String title;
    public String description;
    public String icon;

    public Weather() {
    }

    public String getIcon() {
        return String.format(BuildConfig.BASE_ICON_URL, icon);
    }

    @Override
    public String toJson() {
        return new Gson().toJson(this);
    }
}
