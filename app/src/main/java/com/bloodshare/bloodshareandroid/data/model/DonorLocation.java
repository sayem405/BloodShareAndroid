package com.bloodshare.bloodshareandroid.data.model;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Ignore;

import com.bloodshare.bloodshareandroid.data.db.DBNamesFields;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

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
    public DonorLocation(Place place) {
        this.location = place.getName().toString();
        this.latitude = place.getLatLng().latitude;
        this.longitude = place.getLatLng().longitude;
    }
}
