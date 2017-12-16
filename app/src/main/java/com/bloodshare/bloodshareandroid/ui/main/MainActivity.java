package com.bloodshare.bloodshareandroid.ui.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bloodshare.bloodshareandroid.BloodShareApp;
import com.bloodshare.bloodshareandroid.R;
import com.bloodshare.bloodshareandroid.data.model.UserProfile;
import com.bloodshare.bloodshareandroid.data.network.ApiClient;
import com.bloodshare.bloodshareandroid.data.network.WebServiceCall;
import com.bloodshare.bloodshareandroid.ui.login.LoginActivity;
import com.bloodshare.bloodshareandroid.ui.profile.ProfileActivity;
import com.bloodshare.bloodshareandroid.utils.SPKeys;
import com.bloodshare.bloodshareandroid.viewmodel.UserProfileViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    private static final String EXTRA_USER_ID = "user_id";

    private UserProfile userProfile;
    private MeFragment meFragment;
    private ExploreFragment exploreFragment;
    private InterestedFragment interestedFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final String userID = getIntent().getStringExtra(EXTRA_USER_ID);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.getHeaderView(0);
        headerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileActivity.startActivity(v.getContext(), userID);
            }
        });

        ImageView profileView = (ImageView) headerLayout.findViewById(R.id.profileView);
        profileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.deleteUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SharedPreferences sharedPref = getSharedPreferences(BloodShareApp.TAG, MODE_PRIVATE);
                WebServiceCall serviceCall = ApiClient.getClient().create(WebServiceCall.class);
                String userAccessToken = sharedPref.getString(SPKeys.SP_KEY_ACCESS_TOKEN, null);
                if (TextUtils.isEmpty(userAccessToken)) {
                    Log.w(TAG, "user access token is empty");
                } else {
                    Log.d(TAG, "user access token @" + userAccessToken);
                }
                serviceCall.deleteUser(ApiClient.getAuthorization(userAccessToken)).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            sharedPref.edit().remove(SPKeys.SP_KEY_ACCESS_TOKEN).apply();
                            sharedPref.edit().remove(SPKeys.SP_KEY_USER_ID).apply();
                            Log.d(TAG, "deleted");
                            finish();
                            LoginActivity.startLoginActivity(MainActivity.this);
                        } else {
                            Toast.makeText(MainActivity.this, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e(TAG, t.toString());
                    }
                });

            }
        });

        final TextView nameTextView = headerLayout.findViewById(R.id.nameTextView);
        final TextView phoneTextView = headerLayout.findViewById(R.id.phoneTextView);

        final BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnNavigationItemSelectedListener(this);

        setExploreFragementToContainer();

        UserProfileViewModel viewModel = ViewModelProviders.of(this).get(UserProfileViewModel.class);
        viewModel.init(userID);
        viewModel.getUser().observe(this, new Observer<UserProfile>() {
            @Override
            public void onChanged(@Nullable UserProfile userProfile) {
                MainActivity.this.userProfile = userProfile;
                nameTextView.setText(userProfile.name);
                phoneTextView.setText(userProfile.mobile);
            }
        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Toast.makeText(this, "show", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.action_me) {
            Fragment meFragment = getMeFragment();
            setFragment(meFragment, null);

        } else if (id == R.id.action_interested) {
            Fragment interestedFragment = getInterestedFragment();
            setFragment(interestedFragment, null);
        } else if (id == R.id.action_discover) {
            setExploreFragementToContainer();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setExploreFragementToContainer() {
        Fragment exploreFragment = getExploreFragment();
        setFragment(exploreFragment, null);
    }

    public static void startActivity(Context context, String userID) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(EXTRA_USER_ID, userID);
        context.startActivity(intent);
    }

    public MeFragment getMeFragment() {
        if (meFragment == null) {
            meFragment = MeFragment.newInstance(null, null);
        }
        return meFragment;
    }

    public InterestedFragment getInterestedFragment() {
        if (interestedFragment == null) {
            interestedFragment = InterestedFragment.newInstance(null, null);
        }
        return interestedFragment;
    }

    public ExploreFragment getExploreFragment() {
        if (exploreFragment == null) {
            exploreFragment = ExploreFragment.newInstance(null, null);
        }
        return exploreFragment;
    }

    public void setFragment(Fragment fragment, String tag) {
        FragmentManager ft = getSupportFragmentManager();
        ft.beginTransaction().replace(R.id.fragmentContainer, fragment, tag).disallowAddToBackStack().commit();
    }
}
