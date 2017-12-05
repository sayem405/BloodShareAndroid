package com.jokerlab.jokerstool;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by sayem on 9/30/2016.
 */

public class DateUtil {

    private static final String TAG = DateUtil.class.getSimpleName();

    public static final String DATE_TIME_FORMAT_1 = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DATE_FORMAT_1 = "dd-MM-yyyy";

    public static Calendar getCurrentCalender() {
        return Calendar.getInstance();
    }

    public static Date getDateByFormat(String dateString, String dateFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.US);
        try {
            return simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            Log.w(TAG, e.toString());
            return null;
        }
    }

    public static String getDateByFormat(Date date, String dateFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.US);
        return simpleDateFormat.format(date);
    }
}
