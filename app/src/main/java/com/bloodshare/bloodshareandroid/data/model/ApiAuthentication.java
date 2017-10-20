package com.bloodshare.bloodshareandroid.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sayem on 9/22/2017.
 */

public class ApiAuthentication {
    @SerializedName("firebase_token")
    public String fireBaseToken;

    @SerializedName("user_access_token")
    public String userAccessToken;

    @SerializedName("is_user_new")
    public boolean isUserNew;


    public ApiAuthentication(String fireBaseToken) {
        this.fireBaseToken = fireBaseToken;
    }
}
