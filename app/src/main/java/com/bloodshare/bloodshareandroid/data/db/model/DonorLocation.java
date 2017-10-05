package com.bloodshare.bloodshareandroid.data.db.model;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Ignore;

import com.bloodshare.bloodshareandroid.data.db.DBNamesFields;

public class DonorLocation {

    @ColumnInfo(name = DBNamesFields.COLUMN_LATITUDE)
    public double latitude;

    @ColumnInfo(name = DBNamesFields.COLUMN_LONGITUDE)
    public double longitude;

    @ColumnInfo(name = DBNamesFields.COLUMN_LOCATION)
    public String location;

    public DonorLocation() {
    }

    @Ignore
    public DonorLocation(String location) {
        this.location = location;
    }
}
