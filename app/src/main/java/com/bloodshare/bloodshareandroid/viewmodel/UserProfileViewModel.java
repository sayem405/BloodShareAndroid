package com.bloodshare.bloodshareandroid.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;

import com.bloodshare.bloodshareandroid.data.AppRepository;
import com.bloodshare.bloodshareandroid.BloodShareApp;
import com.bloodshare.bloodshareandroid.data.model.DonorLocation;
import com.bloodshare.bloodshareandroid.data.model.UserProfile;

import java.util.Locale;

/**
 * Created by sayem on 9/23/2017.
 */

public class UserProfileViewModel extends AndroidViewModel {
    private LiveData<UserProfile> user;
    private AppRepository appRepository;
    private DonorLocation location;

    public UserProfileViewModel(Application application) {
        super(application);
    }

    public void init(String userProfileID) {
        if (user != null) return;
        user = ((BloodShareApp) getApplication()).getDb().getAppDao().loadUserByID(userProfileID);
    }

    public LiveData<UserProfile> getUser() {
        return user;
    }

    public void saveUserProfile(UserProfile userProfile) {
        appRepository.saveUserProfile(userProfile);
    }


    public void setLocation(DonorLocation location) {
        if (this.location != null && this.location.location.equals(location.location)) return;
        this.location = location;
    }

    public DonorLocation getLocation() {
        return location;
    }
}
