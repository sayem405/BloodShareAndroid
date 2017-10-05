package com.bloodshare.bloodshareandroid;

import android.arch.lifecycle.LiveData;

import com.bloodshare.bloodshareandroid.data.db.AppDao;
import com.bloodshare.bloodshareandroid.data.db.model.UserProfile;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by sayem on 9/23/2017.
 */
@Singleton
public class AppRepository {

    private final AppDao appDao;

    @Inject
    public AppRepository(AppDao appDao) {
        this.appDao = appDao;
    }

    public LiveData<UserProfile> getUserProfile(String userProfileID) {
        return appDao.loadUserByID(userProfileID);
    }

    public void saveUserProfile(UserProfile userProfile) {
        appDao.insert(userProfile);
    }
}
