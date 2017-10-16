package com.bloodshare.bloodshareandroid.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.bloodshare.bloodshareandroid.data.db.DBNamesFields;

import java.util.Date;
import java.util.UUID;

/**
 * Created by sayem on 9/23/2017.
 */
@Entity(tableName = DBNamesFields.TABLE_DONOR)
public class Donor {
    @PrimaryKey
    @ColumnInfo(name = DBNamesFields.COLUMN_ID)
    public String id;

    @ColumnInfo(name = DBNamesFields.COLUMN_PHONE_NUMBER)
    public String mobile;

    @ColumnInfo(name = DBNamesFields.COLUMN_NAME)
    public String name;

    @ColumnInfo(name = DBNamesFields.COLUMN_BLOOD_GROUP)
    public String bloodGroup;

    @Embedded
    public DonorLocation donorLocation;

    /*@ColumnInfo(name = DBNamesFields.COLUMN_DOB)*/
    @Ignore
    public Date birthDate;


    public Donor() {
        id = UUID.randomUUID().toString();
    }

    @Ignore
    public Donor(String mobile, String name, String bloodGroup, DonorLocation donorLocation) {
        this.mobile = mobile;
        this.name = name;
        this.bloodGroup = bloodGroup;
        this.donorLocation = donorLocation;
    }

    @Ignore
    public Donor(String name, String bloodGroup, Date dob, DonorLocation donorLocation) {
        this.name = name;
        this.bloodGroup = bloodGroup;
        this.donorLocation = donorLocation;
        this.birthDate = dob;
    }
}