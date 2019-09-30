package com.mediatech.mobilehealth.ui.main;


import com.mediatech.mobilehealth.BuildConfig;
import com.mediatech.mobilehealth.api.ApiService;
import com.mediatech.mobilehealth.model.OpenWeatherWrapper;

import javax.inject.Inject;

import io.reactivex.Single;

interface MainInteractor {

    interface View {

        void showWeather(OpenWeatherWrapper wrapper);

        void showLoading();

        void hideLoading();

        void showError(Throwable throwable);

    }

    interface Presenter {

        void loadOpenWeatherAPI(double latitude, double longitude);

    }

    class UseCase {

        private final ApiService apiService;

        @Inject
        public UseCase(ApiService apiService) {
            this.apiService = apiService;
        }

        Single<OpenWeatherWrapper> loadOpenWeatherAPI(double latitude, double longitude) {
            return apiService.loadOpenWeatherAPI(latitude, longitude, BuildConfig.API_KEY, "metric");
        }

    }

}
