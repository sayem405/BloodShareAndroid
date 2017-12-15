package com.bloodshare.bloodshareandroid.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bloodshare.bloodshareandroid.BloodShareApp;
import com.bloodshare.bloodshareandroid.R;
import com.bloodshare.bloodshareandroid.data.model.UserProfile;
import com.bloodshare.bloodshareandroid.data.network.ApiClient;
import com.bloodshare.bloodshareandroid.data.network.WebServiceCall;
import com.bloodshare.bloodshareandroid.utils.SPKeys;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bloodshare.bloodshareandroid.utils.ExtraConstants.EXTRA_PROFILE_ID;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = ProfileActivity.class.getSimpleName();

    private String profileID;
    private EditProfileFragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileID = getIntent().getStringExtra(EXTRA_PROFILE_ID);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
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
        fragment = EditProfileFragment.newInstance(profileID);
        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();

    }


    public static void startActivity(Context context, String profileId) {
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra(EXTRA_PROFILE_ID, profileId);
        context.startActivity(intent);
    }

    public void updateUserClicked(View view) {
        Toast.makeText(this, "update user", Toast.LENGTH_SHORT).show();
        UserProfile userProfile = fragment.getUpdatedUserProfile();
        WebServiceCall serviceCall = ApiClient.getClient().create(WebServiceCall.class);
        final SharedPreferences sharedPref = getSharedPreferences(BloodShareApp.TAG, MODE_PRIVATE);

        String userAccessToken = sharedPref.getString(SPKeys.SP_KEY_ACCESS_TOKEN, null);
        serviceCall.saveUser(ApiClient.getAuthorization(userAccessToken), userProfile).enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "profile updated");
                    ((BloodShareApp) getApplication()).getDb().getAppDao().insert(userProfile);
                    finish();
                } else {
                    Log.e(TAG, " not successful");
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }
}
