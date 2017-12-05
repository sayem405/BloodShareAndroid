package com.bloodshare.bloodshareandroid.ui.profile;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.bloodshare.bloodshareandroid.R;
import com.bloodshare.bloodshareandroid.data.model.UserProfile;
import com.bloodshare.bloodshareandroid.databinding.FragmentEditProfileBinding;
import com.bloodshare.bloodshareandroid.ui.common.CustomDatePicker;
import com.bloodshare.bloodshareandroid.viewholder.UserProfileViewModel;
import com.jokerlab.jokerstool.DateUtil;

import static com.bloodshare.bloodshareandroid.utils.ExtraConstants.EXTRA_PROFILE_ID;

public class EditProfileFragment extends Fragment {


    private OnFragmentInteractionListener mListener;
    private String profileID;
    private FragmentEditProfileBinding binding;
    private UserProfileViewModel viewModel;
    private UserProfile userProfile;

    public EditProfileFragment() {

    }

    public static EditProfileFragment newInstance(@NonNull String userID) {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_PROFILE_ID, userID);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            profileID = getArguments().getString(EXTRA_PROFILE_ID);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile, container, false);
        setDatePicker();
        setUpGroupSpinner();
        return binding.getRoot();
    }

    private void setDatePicker() {
        binding.dobPickerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDatePicker picker = CustomDatePicker.newInstance(viewModel.getUser().getValue().birthDate);
                picker.show(getFragmentManager(), null);
            }
        });
    }

    private void setUpGroupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.blood_groups, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(UserProfileViewModel.class);
        viewModel.init(profileID);
        viewModel.getUser().observe(this, new Observer<UserProfile>() {
            @Override
            public void onChanged(@Nullable UserProfile userProfile) {
                EditProfileFragment.this.userProfile = userProfile;
                binding.setUserProfile(userProfile);
                //binding.spinner.setSelection(;
                binding.executePendingBindings();
            }
        });
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public UserProfile getUpdatedUserProfile() {
        userProfile.name = binding.nameEditText.getText().toString();
        userProfile.bloodGroup = getResources().getStringArray(R.array.blood_groups)[binding.spinner.getSelectedItemPosition()];
        userProfile.birthDate = DateUtil.getDateByFormat(binding.dobEditText.getText().toString(), DateUtil.DATE_FORMAT_1);

        return userProfile;
    }
}
