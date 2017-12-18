package com.bloodshare.bloodshareandroid.ui.profile;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bloodshare.bloodshareandroid.R;
import com.bloodshare.bloodshareandroid.data.model.UserProfile;
import com.bloodshare.bloodshareandroid.databinding.FragmentProfileBinding;
import com.bloodshare.bloodshareandroid.viewholder.UserProfileViewModel;

import static com.bloodshare.bloodshareandroid.utils.ExtraConstants.EXTRA_USER_ID;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProfileActivityFragment extends Fragment {

    private static final String TAG = ProfileActivityFragment.class.getSimpleName();
    private FragmentProfileBinding binding;
    private String profileID;

    public static ProfileActivityFragment newInstance(@NonNull String userID) {
        ProfileActivityFragment fragment = new ProfileActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_USER_ID, userID);
        fragment.setArguments(bundle);
        return fragment;
    }

    public ProfileActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileID = getArguments().getString(EXTRA_USER_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile,container,false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        UserProfileViewModel viewModel = ViewModelProviders.of(getActivity()).get(UserProfileViewModel.class);
        viewModel.init(profileID);
        viewModel.getUser().observe(this, new Observer<UserProfile>() {
            @Override
            public void onChanged(@Nullable UserProfile userProfile) {
                binding.setUserProfile(userProfile);
                binding.executePendingBindings();
            }
        });
    }
}
