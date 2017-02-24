package com.bloodshare.bloodshareandroid.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.bloodshare.bloodshareandroid.R;
import com.bloodshare.bloodshareandroid.ui.base.BaseActivity;
import com.bloodshare.bloodshareandroid.ui.main.MainActivity;


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
        addFragment(mobileInputFragment, MobileInputFragment.TAG);
        //startActivity(new Intent(this, MainActivity.class));

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
        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).addToBackStack(tag).commit();
    }
}
