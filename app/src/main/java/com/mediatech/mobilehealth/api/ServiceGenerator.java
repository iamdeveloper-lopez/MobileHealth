package com.mediatech.mobilehealth.api;

import android.content.Context;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mediatech.mobilehealth.BuildConfig;
import com.mediatech.mobilehealth.helper.AppHelper;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    public static <S> S createService(Context context, Class<S> serviceClass, String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(provideOkHttpClient(context))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(serviceClass);
    }

    private static Gson gson = new GsonBuilder()
            .setLenient()
            .setPrettyPrinting()
            .create();

    private static OkHttpClient provideOkHttpClient(Context context) {
        return new OkHttpClient.Builder()
                .cache(provideCache(context))
                .addInterceptor(provideHttpLoggingInterceptor())
                .addNetworkInterceptor(provideCacheInterceptor())
                .addNetworkInterceptor(new StethoInterceptor())
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    private static Interceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.level(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        return loggingInterceptor;
    }

    private static Cache provideCache(Context context) {
        return new Cache(context.getCacheDir(), 5 * 1024 * 1024);
    }

    private static Interceptor provideCacheInterceptor() {
        return chain -> {
            Request request = chain.request();
            if (AppHelper.isNetworkAvailable()) {
                request.newBuilder().header("Cache-Control", "public, max-age=" + 60 * 10).build();
            } else {
                request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build();
            }
            return chain.proceed(request);
        };
    }

}
