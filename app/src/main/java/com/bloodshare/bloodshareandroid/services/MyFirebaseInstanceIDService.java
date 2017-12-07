package com.bloodshare.bloodshareandroid.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by SAIF on 10/15/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService
{
    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //     sendRegistrationToServer(refreshedToken);
        Log.d("Saif","\n\n\n\n\n"+refreshedToken+"\n\n\n\n");
    }
}

