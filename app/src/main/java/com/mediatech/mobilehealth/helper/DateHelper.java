package com.mediatech.mobilehealth.helper;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {

    public static final String INPUT_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZ";
    public static final String SIMPLE_TIME_PATTERN = "hh:mma";
    public static final String SIMPLE_DATE_PATTERN = "dd ";
    public static final String DATE_PATTERN = "yyyy-MM-dd";

    private String inputPattern;
    private String outputPattern;

    public DateHelper(Builder builder) {
        this.inputPattern = builder.inputPattern;
        this.outputPattern = builder.outputPattern;
    }

    public DateHelper() {
    }

    public static DateHelper newInstance() {
        return new DateHelper();
    }

    private static final int MINUTES = 60 * 1000;

    public static boolean isOlderThan(int minute, long time) {
        long minuteAgo = System.currentTimeMillis() - (minute * MINUTES);
        return time < minuteAgo;
    }

    public String getFormattedDate(Date date, String outputPattern) {
        return new SimpleDateFormat(outputPattern).format(date);
    }

    @SuppressLint("SimpleDateFormat")
    public static String getFormattedDateString(String dateString, String inputPattern, String pattern) throws ParseException {
        return new SimpleDateFormat(pattern).format(new SimpleDateFormat(inputPattern).parse(dateString));
    }

    public String getFormattedDateString(String dateString) throws ParseException {
        return new SimpleDateFormat(outputPattern).format(new SimpleDateFormat(inputPattern).parse(dateString));
    }

    /**
     * @param milliseconds
     * @return
     */
    public static boolean isExpired(long milliseconds) {
        Log.d("DATE", "isExpired: " + new Date(milliseconds * 1000));
        return new Date(milliseconds * 1000).before(new Date());
    }

    public String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(outputPattern);
        return sdf.format(c.getTime());
    }

    public static final class Builder {

        private String inputPattern;
        private String outputPattern;

        public Builder setInputPattern(String inputPattern) {
            this.inputPattern = inputPattern;
            return this;
        }

        public Builder setOutputPattern(String outputPattern) {
            this.outputPattern = outputPattern;
            return this;
        }

        public DateHelper build() {
            return new DateHelper(this);
        }

    }

}