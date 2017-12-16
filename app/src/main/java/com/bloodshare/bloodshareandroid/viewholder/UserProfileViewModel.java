package com.bloodshare.bloodshareandroid.viewholder;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.bloodshare.bloodshareandroid.AppRepository;
import com.bloodshare.bloodshareandroid.BloodShareApp;
import com.bloodshare.bloodshareandroid.data.model.DonorLocation;
import com.bloodshare.bloodshareandroid.data.model.UserProfile;

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
        if (this.location != null && this.location.name.equals(location.name)) return;
        this.location = location;
    }

    public DonorLocation getLocation() {
        return location;
    }
}
