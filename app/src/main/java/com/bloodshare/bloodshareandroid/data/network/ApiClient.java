package com.bloodshare.bloodshareandroid.data.network;

import com.bloodshare.bloodshareandroid.utils.ServerConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jokerlab.jokerstool.DateUtil;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sayem on 9/22/2017.
 */

public class ApiClient {

    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit == null) {

            Gson gson = new GsonBuilder()
                    .setDateFormat(DateUtil.DATE_FORMAT_1)
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(ServerConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
