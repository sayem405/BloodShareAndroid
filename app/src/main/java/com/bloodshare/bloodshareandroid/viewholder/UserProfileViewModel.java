package com.bloodshare.bloodshareandroid.viewholder;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.bloodshare.bloodshareandroid.AppRepository;
import com.bloodshare.bloodshareandroid.data.db.model.UserProfile;

/**
 * Created by sayem on 9/23/2017.
 */

public class UserProfileViewModel extends AndroidViewModel {
    private LiveData<UserProfile> user;
    private AppRepository appRepository;

    public UserProfileViewModel(Application application) {
        super(application);
    }

    public void init(String userProfileID) {
        if (user != null) return;
        user = appRepository.getUserProfile(userProfileID);
    }

    public LiveData<UserProfile> getUser() {
        return user;
    }

    public void saveUserProfile(UserProfile userProfile) {
        appRepository.saveUserProfile(userProfile);
    }
}
