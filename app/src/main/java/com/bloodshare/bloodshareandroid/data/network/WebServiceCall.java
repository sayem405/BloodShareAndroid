package com.bloodshare.bloodshareandroid.data.network;

import com.bloodshare.bloodshareandroid.data.db.model.Donor;
import com.bloodshare.bloodshareandroid.data.db.model.UserProfile;
import com.bloodshare.bloodshareandroid.utils.ServerConstants;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by sayem on 2/24/2017.
 */

public interface WebServiceCall {

    @POST(ServerConstants.CONTROLLER_USER + ServerConstants.METHOD_POST_AUTHENTICATE)
    Call<ApiAuthentication> authenticate(@Body ApiAuthentication apiAuthentication);

    @GET(ServerConstants.CONTROLLER_USER)
    Call<UserProfile> getUser(@Header("Authorization") String authorization);


    @POST(ServerConstants.CONTROLLER_USER)
    Call<UserProfile> saveUser(@Header("Authorization") String authorization, @Body Donor userProfile);
}
