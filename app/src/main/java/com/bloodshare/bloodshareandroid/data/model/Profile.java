package com.bloodshare.bloodshareandroid.data.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by sayem on 10/22/2016.
 */

public class Profile {

    private String profileID;
    private String mobile;
    private String key;


    public String getJson() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

    public String getProfileID() {
        return profileID;
    }

    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
