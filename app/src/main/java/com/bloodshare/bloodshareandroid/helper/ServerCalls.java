package com.bloodshare.bloodshareandroid.helper;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bloodshare.bloodshareandroid.model.ServerConstants;
import com.jokerlab.volleynet.VolleyRequestManager;

/**
 * Created by sayem on 10/6/2016.
 */

public class ServerCalls {
    public static void checkNewUserAndSendOTP(Context context, final String REQUEST_TAG, final String phoneNumber) {

        String url = ServerConstants.CONTROLLER_USER + ServerConstants.METHOD_GET_IS_NEW_SEND_OTP;
        VolleyRequestManager.getInstance(context).getRequestBuilder(url, null, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(REQUEST_TAG, response);
            }
        }).setErrorListener(new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(REQUEST_TAG,error.toString());
            }
        })
                .setBaseUrl(ServerConstants.BASE_URL)
                .addParam(ServerCallHelper.PARAM_MOBILE, phoneNumber)
                .buildJsonRequestAndAddToQueue(REQUEST_TAG);
    }

}
