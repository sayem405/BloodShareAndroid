package com.bloodshare.bloodshareandroid;

import android.os.Bundle;

import com.bloodshare.bloodshareandroid.helper.ServerCalls;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ServerCalls.checkNewUserAndSendOTP(this,"tag", "01914820010");
    }
}
