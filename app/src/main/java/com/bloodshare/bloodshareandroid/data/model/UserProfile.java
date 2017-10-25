package com.bloodshare.bloodshareandroid.data.model;

import android.arch.persistence.room.Entity;

import com.bloodshare.bloodshareandroid.data.db.DBNamesFields;

/**
 * Created by sayem on 9/23/2017.
 */

@Entity(tableName = DBNamesFields.TABLE_USER_PROFILE)
public class UserProfile extends Donor {


}
