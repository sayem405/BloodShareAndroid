package com.bloodshare.bloodshareandroid.ui.post;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.bloodshare.bloodshareandroid.R;
import com.bloodshare.bloodshareandroid.ui.base.BaseActivity;
import com.bloodshare.bloodshareandroid.utils.ExtraConstants;

public class BloodSeekActivity extends BaseActivity {

    private static final String TAG = BloodSeekActivity.class.getSimpleName();
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_seek);
        userId = getIntent().getStringExtra(ExtraConstants.EXTRA_USER_ID);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, new BloodSeekFragment(), null);
        ft.disallowAddToBackStack();
        ft.commit();
    }


    public static void startActivityForResult(Activity activity, String userId, int requestCode) {
        Intent intent = new Intent(activity, BloodSeekActivity.class);
        intent.putExtra(ExtraConstants.EXTRA_USER_ID, userId);
        activity.startActivityForResult(intent, requestCode);
    }
}
