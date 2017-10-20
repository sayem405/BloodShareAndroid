package com.bloodshare.bloodshareandroid.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.bloodshare.bloodshareandroid.R;

import static com.bloodshare.bloodshareandroid.utils.ExtraConstants.EXTRA_PROFILE_ID;

public class ProfileActivity extends AppCompatActivity {

    private String profileID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileID = getIntent().getStringExtra(EXTRA_PROFILE_ID);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        startEditFragement();
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabEditProfile);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startEditFragement();
            }
        });

        ProfileActivityFragment fragment = ProfileActivityFragment.newInstance(profileID);
        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).disallowAddToBackStack().commit();*/
    }

    private void startEditFragement() {
        EditProfileFragment fragment = EditProfileFragment.newInstance(profileID);
        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();

    }


    public static void startActivity(Context context, String profileId) {
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra(EXTRA_PROFILE_ID, profileId);
        context.startActivity(intent);
    }

}
