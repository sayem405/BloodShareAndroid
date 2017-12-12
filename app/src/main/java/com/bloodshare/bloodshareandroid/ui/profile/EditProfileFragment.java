package com.bloodshare.bloodshareandroid.ui.profile;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
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
import com.bloodshare.bloodshareandroid.data.model.DonorLocation;
import com.bloodshare.bloodshareandroid.data.model.UserProfile;
import com.bloodshare.bloodshareandroid.databinding.FragmentEditProfileBinding;
import com.bloodshare.bloodshareandroid.ui.common.CustomDatePicker;
import com.bloodshare.bloodshareandroid.viewholder.UserProfileViewModel;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.jokerlab.jokerstool.DateUtil;

import static android.app.Activity.RESULT_OK;
import static com.bloodshare.bloodshareandroid.utils.ExtraConstants.EXTRA_PROFILE_ID;

public class EditProfileFragment extends Fragment {


    private static final int REQUEST_CODE_LOCATION = 256;
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
        setLocationPicker();
        return binding.getRoot();
    }

    private void setLocationPicker() {
        binding.locationEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMap();
            }
        });
        binding.imageViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMap();
            }
        });
    }

    private void openMap() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(getActivity()), REQUEST_CODE_LOCATION);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
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
                viewModel.setLocation(userProfile.donorLocation);
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
        userProfile.donorLocation = getLocation();
        return userProfile;
    }

    private DonorLocation getLocation() {
        return viewModel.getLocation();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_LOCATION) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(getActivity(), data);
                if (place != null) {
                    viewModel.setLocation(new DonorLocation(place));
                    binding.locationEditText.setText(place.getName());
                } else {

                }
            }
        }
    }
}
