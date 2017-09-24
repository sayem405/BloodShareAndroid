package com.bloodshare.bloodshareandroid;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.bloodshare.bloodshareandroid.data.db.AppDatabase;
import com.bloodshare.bloodshareandroid.data.db.DBNamesFields;

/**
 * Created by sayem on 2/24/2017.
 */

public class BloodShareApp extends Application {

    public static final String TAG = BloodShareApp.class.getSimpleName();
    private AppDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, DBNamesFields.DATABASE_NAME).allowMainThreadQueries().build();
    }

    public AppDatabase getDb() {
        return db;
    }

}
