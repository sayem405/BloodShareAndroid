package com.bloodshare.bloodshareandroid.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bloodshare.bloodshareandroid.R;

import static com.bloodshare.bloodshareandroid.utils.ExtraConstants.EXTRA_PROFILE_ID;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String profileID = getIntent().getStringExtra(EXTRA_PROFILE_ID);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabEditProfile);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ProfileActivityFragment fragment = ProfileActivityFragment.newInstance(profileID);
        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
    }


    public static void startActivity(Context context, String profileId) {
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra(EXTRA_PROFILE_ID, profileId);
        context.startActivity(intent);
    }

}
