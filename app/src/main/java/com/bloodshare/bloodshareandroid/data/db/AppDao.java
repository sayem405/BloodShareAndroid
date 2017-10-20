package com.bloodshare.bloodshareandroid.data.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.bloodshare.bloodshareandroid.data.model.UserProfile;

/**
 * Created by sayem on 9/23/2017.
 */
@Dao
public interface AppDao {

    @Query("Select * from " + DBNamesFields.TABLE_USER_PROFILE + " where " + DBNamesFields.COLUMN_ID + " = :userID limit 1")
    public LiveData<UserProfile> loadUserByID(String userID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(UserProfile... userProfiles);
}
