package com.mediatech.mobilehealth.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OpenWeatherWrapper implements JsonModel {

    public List<Weather> weather;
    @SerializedName("main")
    public Temperature temperature;
    public Wind wind;
    @SerializedName("sys")
    public Sun sun;

    public OpenWeatherWrapper() {
    }

    @Override
    public String toJson() {
        return new Gson().toJson(this);
    }
}
