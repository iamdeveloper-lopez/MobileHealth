package com.mediatech.mobilehealth.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Sun implements JsonModel {

    @SerializedName("sunrise")
    public long rise;
    @SerializedName("sunset")
    public long set;
    @SerializedName("country")
    public String countryCode;

    public Sun() {
    }

    @Override
    public String toJson() {
        return new Gson().toJson(this);
    }
}
