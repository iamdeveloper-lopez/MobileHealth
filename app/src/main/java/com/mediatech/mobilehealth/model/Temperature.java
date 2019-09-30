package com.mediatech.mobilehealth.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Temperature implements JsonModel {

    @SerializedName("temp")
    public String value;
    public String pressure;
    public String humidity;
    @SerializedName("temp_max")
    public String max;
    @SerializedName("temp_min")
    public String min;

    public Temperature() {
    }

    @Override
    public String toJson() {
        return new Gson().toJson(this);
    }
}
