package com.bloodshare.bloodshareandroid.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.TextView;

import com.bloodshare.bloodshareandroid.BloodShareApp;
import com.bloodshare.bloodshareandroid.R;
import com.bloodshare.bloodshareandroid.ui.base.BaseActivity;
import com.bloodshare.bloodshareandroid.ui.login.phoneLogin.FireBasePhoneAuthentication;
import com.bloodshare.bloodshareandroid.ui.main.MainActivity;
import com.bloodshare.bloodshareandroid.utils.SPKeys;


public class LoginActivity extends BaseActivity {

    private String mobileNumber;
    private boolean isNew;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref = getSharedPreferences(BloodShareApp.TAG, MODE_PRIVATE);
        String userID = sharedPref.getString(SPKeys.SP_KEY_USER_ID, null);
        String userAccessToken = sharedPref.getString(SPKeys.SP_KEY_ACCESS_TOKEN, null);
        if (TextUtils.isEmpty(userID)) {
            startActivity(new Intent(this, FireBasePhoneAuthentication.class));
        } else {
            MainActivity.startActivity(this, userID);
        }
        finish();
    }

}
