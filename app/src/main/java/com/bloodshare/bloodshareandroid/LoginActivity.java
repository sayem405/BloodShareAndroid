package com.bloodshare.bloodshareandroid;

import android.content.Intent;
import android.os.Bundle;


public class LoginActivity extends BaseActivity implements MobileInputFragment.OnFragmentInteractionListener,
        VerifyOldUserFragment.OnFragmentInteractionListener{

    private String mobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        MobileInputFragment mobileInputFragment = MobileInputFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.activity_main,mobileInputFragment).commit();
    }


    @Override
    public void onFragmentInteraction(String mobileNumber, boolean isNew) {
        this.mobileNumber = mobileNumber;
        if (isNew) {
            VerifyOldUserFragment verifyOldUserFragment = VerifyOldUserFragment.newInstance(mobileNumber);
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_main,verifyOldUserFragment).addToBackStack(null).commit();
        }
    }

    @Override
    public void onFragmentInteraction(boolean authenticated) {
        if (authenticated) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}