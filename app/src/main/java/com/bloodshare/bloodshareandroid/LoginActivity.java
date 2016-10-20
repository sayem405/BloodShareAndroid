package com.bloodshare.bloodshareandroid;

import android.net.Uri;
import android.os.Bundle;


public class LoginActivity extends BaseActivity implements MobileInputFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        MobileInputFragment mobileInputFragment = MobileInputFragment.newInstance();

        getFragmentManager().beginTransaction().add(R.id.activity_main,mobileInputFragment).commit();
    }


    @Override
    public void onFragmentInteraction(String mobileNumber, boolean isNew) {
        if (isNew) {
            VerifyOldUserFragment verifyOldUserFragment = VerifyOldUserFragment.newInstance();
            getFragmentManager().beginTransaction().replace(R.id.activity_main,verifyOldUserFragment).addToBackStack(null).commit();
        }
    }
}
