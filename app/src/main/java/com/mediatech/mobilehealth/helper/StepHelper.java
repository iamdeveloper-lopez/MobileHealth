package com.mediatech.mobilehealth.helper;

public class StepHelper {

    public static float getDistanceRun(long steps) {
        float distance = (float) (steps * 78) / (float) 100000;
        return distance;
    }

}
