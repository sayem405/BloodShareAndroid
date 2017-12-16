package com.bloodshare.bloodshareandroid.data.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.bloodshare.bloodshareandroid.AppConstants;
import com.bloodshare.bloodshareandroid.data.model.Donor;
import com.bloodshare.bloodshareandroid.data.model.UserProfile;

/**
 * Created by sayem on 9/23/2017.
 */

@Database(version = AppConstants.DATABASE_VERSION, exportSchema = false,
        entities = {UserProfile.class})
@TypeConverters({DBConverters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract AppDao getAppDao();
}
