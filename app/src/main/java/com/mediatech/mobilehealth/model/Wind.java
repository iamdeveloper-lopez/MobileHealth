package com.mediatech.mobilehealth.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Wind implements JsonModel {

    public String speed;
    @SerializedName("deg")
    public String degrees;

    public Wind() {
    }

    @Override
    public String toJson() {
        return new Gson().toJson(this);
    }
}
