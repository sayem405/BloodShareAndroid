package com.bloodshare.bloodshareandroid.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;

import com.bloodshare.bloodshareandroid.data.db.DBNamesFields;

import java.util.Date;

/**
 * Created by sayem on 9/23/2017.
 */

@Entity(tableName = DBNamesFields.TABLE_USER_PROFILE)
public class UserProfile extends Donor {

    public UserProfile(){
        super();
    }

    @Ignore
    public UserProfile(String name, String bloodGroup, Date dob, Location location) {
        super(name, bloodGroup, dob, location);
    }
}
