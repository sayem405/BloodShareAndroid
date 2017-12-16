package com.bloodshare.bloodshareandroid.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.bloodshare.bloodshareandroid.data.db.DBNamesFields;
import com.bloodshare.bloodshareandroid.data.network.SerializedNameFields;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.UUID;

/**
 * Created by sayem on 9/23/2017.
 */

public class   Donor {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = DBNamesFields.COLUMN_ID)
    public String id = UUID.randomUUID().toString();
    ;

    @ColumnInfo(name = DBNamesFields.COLUMN_PHONE_NUMBER)
    public String mobile;

    @ColumnInfo(name = DBNamesFields.COLUMN_NAME)
    public String name;

    @ColumnInfo(name = DBNamesFields.COLUMN_BLOOD_GROUP)
    public String bloodGroup;

    @SerializedName(value = SerializedNameFields.LOCATION)
    @Embedded(prefix = DBNamesFields.COLUMN_LOCATION)
    public DonorLocation location;

    @ColumnInfo(name = DBNamesFields.COLUMN_DOB)
    public Date birthDate;

    @ColumnInfo(name = DBNamesFields.COLUMN_GENDER)
    public int gender;


    public Donor() {
    }

    @Ignore
    public Donor(String mobile, String name, String bloodGroup, DonorLocation location) {
        this.mobile = mobile;
        this.name = name;
        this.bloodGroup = bloodGroup;
        this.location = location;
    }

    @Ignore
    public Donor(String name, String bloodGroup, Date dob, DonorLocation location) {
        this.name = name;
        this.bloodGroup = bloodGroup;
        this.location = location;
        this.birthDate = dob;
    }
}
