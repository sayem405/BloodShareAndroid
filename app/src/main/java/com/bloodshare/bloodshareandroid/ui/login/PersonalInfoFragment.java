package com.bloodshare.bloodshareandroid.ui.login;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.bloodshare.bloodshareandroid.R;
import com.bloodshare.bloodshareandroid.databinding.FragmentPersonalInfoBinding;
import com.bloodshare.bloodshareandroid.ui.base.BaseFragment;
import com.google.android.gms.location.places.Place;
import com.jokerlab.jokerstool.DateUtil;

/**
 * Created by sayem on 9/23/2017.
 */

public class PersonalInfoFragment extends BaseFragment implements View.OnClickListener {

    public static final String TAG = PersonalInfoFragment.class.getSimpleName();

    private FragmentPersonalInfoBinding binding;
    private PersonalInfoFragmentListener listener;
    private Place place;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_personal_info, container, false);
        setUpGroupSpinner();
        binding.saveButton.setOnClickListener(this);
        binding.locationEditText.setOnClickListener(this);
        return binding.getRoot();
    }

    private void setUpGroupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.blood_groups, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.bloodSpinner.setAdapter(adapter);
        binding.bloodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position > 0) {
                    binding.locationEditText.requestFocus();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == binding.saveButton.getId()) {
            boolean allInputGiven = checkFields();
            if (allInputGiven && listener != null) {
                String name = binding.nameEditText.getText().toString();
                String dob = binding.DOBEditText.getText().toString();
                String bloodGroup = getResources().getStringArray(R.array.blood_groups)[binding.bloodSpinner.getSelectedItemPosition()];
                listener.submitPersonalInfo(name, dob, bloodGroup, place);
            }
        } else if (view.getId() == binding.locationEditText.getId()) {
            if (listener != null) listener.locationClicked(view);
        }
    }

    private boolean checkFields() {
        boolean allInputGiven = true;
        if (TextUtils.isEmpty(binding.nameEditText.getText().toString())) {
            binding.nameEditText.setError(getString(R.string.error_give_name));
            allInputGiven = false;
        } else {
            binding.nameEditText.setError(null);
        }
        String dob = binding.DOBEditText.getText().toString();
        if (TextUtils.isEmpty(dob)) {
            binding.DOBEditText.setError(getString(R.string.error_give_dob));
            allInputGiven = false;
        } else if (DateUtil.getDateByFormat(dob, DateUtil.DATE_FORMAT_1) == null) {
            binding.DOBEditText.setError(getString(R.string.error_give_dob_format));
            allInputGiven = false;
        } else {
            binding.DOBEditText.setError(null);
        }

        if (binding.bloodSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(getActivity(), R.string.select_blood_group, Toast.LENGTH_SHORT).show();
            allInputGiven = false;
        }

        if (place == null) {
            binding.locationEditText.setError(getString(R.string.error_give_location));
            allInputGiven = false;
        } else {
            binding.locationEditText.setError(null);
        }


        return allInputGiven;
    }

    public interface PersonalInfoFragmentListener {
        void submitPersonalInfo(String name, String DOB, String bloodGroup, Place place);

        void locationClicked(View view);
    }

    public void setLocation(Place place) {
        this.place = place;
        if (binding != null) {
            binding.locationEditText.setText(place.getName());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (!(getActivity() instanceof PersonalInfoFragmentListener)) {
            throw new IllegalStateException("Activity must implement PhoneVerificationHandler");
        }
        listener = (PersonalInfoFragmentListener) getActivity();
    }
}
