package com.mediatech.mobilehealth.interfaces;

public interface LocationProviderImpl {

    long provideLocationInterval();
    float provideLocationDistanceInterval();
    String provideLocationProvider();

}
