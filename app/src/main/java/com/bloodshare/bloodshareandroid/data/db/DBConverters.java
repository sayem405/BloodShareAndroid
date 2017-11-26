package com.bloodshare.bloodshareandroid.data.db;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by sayem on 11/26/2017.
 */

public class DBConverters {

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null || value == 0 ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
