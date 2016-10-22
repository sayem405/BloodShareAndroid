package com.bloodshare.bloodshareandroid;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bloodshare.bloodshareandroid.databinding.FragmentOldUserVerificationBinding;

import static android.content.ContentValues.TAG;


public class VerifyOldUserFragment extends BaseFragment implements View.OnClickListener {


    private static final String ARG_PARAM_MOBILE_NUMBER = "mobileNumber";
    private static final String ARG_PARAM2 = "param2";


    private String mobileNumber;

    private OnFragmentInteractionListener mListener;


    private FragmentOldUserVerificationBinding binding;

    public VerifyOldUserFragment() {

    }


    public static VerifyOldUserFragment newInstance(String mobileNumber) {
        VerifyOldUserFragment fragment = new VerifyOldUserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_MOBILE_NUMBER, mobileNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mobileNumber = getArguments().getString(ARG_PARAM_MOBILE_NUMBER);
            Log.d(TAG, "onCreate: " + mobileNumber);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_old_user_verification, container, false);
        binding.button2.setOnClickListener(this);
        return binding.getRoot();
    }

    public void onButtonPressed(String mobileNumber) {
        if (mListener != null) {
            mListener.onFragmentInteraction(mobileNumber, true);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String mobileNumber, boolean isNew);
    }
}
