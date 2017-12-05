package com.bloodshare.bloodshareandroid.data.network;

import android.arch.persistence.room.Update;
import android.support.annotation.NonNull;

import com.bloodshare.bloodshareandroid.data.model.ApiAuthentication;
import com.bloodshare.bloodshareandroid.data.model.Donor;
import com.bloodshare.bloodshareandroid.data.model.UserProfile;
import com.bloodshare.bloodshareandroid.utils.ServerConstants;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

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


    @PUT(ServerConstants.CONTROLLER_USER)
    Call<UserProfile> updateUser(@Header("Authorization") String authorization, @Body Donor userProfile);


    @DELETE(ServerConstants.CONTROLLER_USER)
    Call<String> deleteUser(@Header("Authorization") String authorization);


}
