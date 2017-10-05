package com.bloodshare.bloodshareandroid.helper;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jokerlab.volleynet.RequestBuilder;
import com.jokerlab.volleynet.listeners.NetworkListener;
import com.bloodshare.bloodshareandroid.utils.ServerConstants;
import com.jokerlab.volleynet.VolleyRequestManager;

import static com.jokerlab.volleynet.listeners.NetworkResponses.NETWORK_ERROR;
import static com.jokerlab.volleynet.listeners.NetworkResponses.RESULT_OK;

/**
 * Created by sayem on 10/6/2016.
 */

public class ServerCalls {
    private static final String TAG = ServerCalls.class.getSimpleName();

    public static void checkNewUserAndSendOTP(Context context, final String REQUEST_TAG, final int action,
                                              final String phoneNumber, final NetworkListener listener) {

        String url = ServerConstants.CONTROLLER_USER + ServerConstants.METHOD_GET_IS_NEW_SEND_OTP;
        VolleyRequestManager.getInstance(context).getRequestBuilder(url, null, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(REQUEST_TAG, response);
                listener.onResponse(action, RESULT_OK, 0, response);
            }
        }).setErrorListener(new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(REQUEST_TAG, error.toString());
                listener.onResponse(action, NETWORK_ERROR, ServerCallHelper.getErrorCode(error), null);
            }
        })
                .setBaseUrl(ServerConstants.BASE_URL)
                .addParam(ServerCallHelper.PARAM_MOBILE, phoneNumber)
                .buildJsonRequestAndAddToQueue(REQUEST_TAG);
    }


    public static void verifySecurityCode(Context context, final String REQUEST_TAG, final int action,
                                          String phoneNumber, String otp, final NetworkListener listener) {

        String jsonString = ServerCallHelper.getJsonForVerify(phoneNumber,otp);
        String url = ServerConstants.CONTROLLER_USER + ServerConstants.METHOD_POST_AUTHENTICATE;

        Log.d(TAG, "verifySecurityCode: json @" + jsonString);

        VolleyRequestManager.getInstance(context).getRequestBuilder(url, jsonString, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(REQUEST_TAG, response.toString());
                listener.onResponse(action, RESULT_OK, 0, response);
            }
        }).setErrorListener(new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(REQUEST_TAG, error.toString());
                listener.onResponse(action, NETWORK_ERROR, ServerCallHelper.getErrorCode(error), null);
            }
        })
                .setMethod(Request.Method.POST)
                .setRequestType(RequestBuilder.JSON_STRING_REQUEST)
                .setBaseUrl(ServerConstants.BASE_URL)
                .buildJsonRequestAndAddToQueue(REQUEST_TAG);

    }
}
