package com.mediatech.mobilehealth.api;

import com.mediatech.mobilehealth.BuildConfig;
import com.mediatech.mobilehealth.model.OpenWeatherWrapper;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    String BASE_URL = BuildConfig.BASE_API_URL;

    @GET("weather")
    Single<OpenWeatherWrapper> loadOpenWeatherAPI(@Query("lat") double latitude, @Query("lon") double longitude, @Query("appid") String appId, @Query("units") String units);

}
