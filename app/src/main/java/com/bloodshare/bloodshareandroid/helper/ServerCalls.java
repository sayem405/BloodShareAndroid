package com.bloodshare.bloodshareandroid.helper;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bloodshare.bloodshareandroid.listeners.NetworkListener;
import com.bloodshare.bloodshareandroid.model.ServerConstants;
import com.jokerlab.volleynet.VolleyRequestManager;

import static com.bloodshare.bloodshareandroid.listeners.NetworkResponses.NETWORK_ERROR;
import static com.bloodshare.bloodshareandroid.listeners.NetworkResponses.RESULT_OK;

/**
 * Created by sayem on 10/6/2016.
 */

public class ServerCalls {
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
                listener.onResponse(action, NETWORK_ERROR, getErrorCode(error), null);
            }
        })
                .setBaseUrl(ServerConstants.BASE_URL)
                .addParam(ServerCallHelper.PARAM_MOBILE, phoneNumber)
                .buildJsonRequestAndAddToQueue(REQUEST_TAG);
    }

    private static int getErrorCode(VolleyError error) {
        return error.networkResponse!= null ? error.networkResponse.statusCode: 0;
    }

}
