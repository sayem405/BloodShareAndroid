package com.bloodshare.bloodshareandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;


public class LoginActivity extends BaseActivity implements MobileInputFragment.OnFragmentInteractionListener,
        VerifyOldUserFragment.OnFragmentInteractionListener {

    private String mobileNumber;
    private boolean isNew;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        MobileInputFragment mobileInputFragment = MobileInputFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.activity_main, mobileInputFragment).commit();
    }


    @Override
    public void onFragmentInteraction(String mobileNumber, boolean isNew) {
        this.mobileNumber = mobileNumber;
        this.isNew = isNew;
        VerifyOldUserFragment verifyOldUserFragment = VerifyOldUserFragment.newInstance(mobileNumber);
        addFragment(verifyOldUserFragment, VerifyOldUserFragment.TAG);
    }

    @Override
    public void onFragmentInteraction(String token) {
        this.token = token;

        if (isNew) {

        } else {

            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

    }

    public void addFragment(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_main, fragment).addToBackStack(tag).commit();
    }
}
