package com.bloodshare.bloodshareandroid;

import android.app.Fragment;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bloodshare.bloodshareandroid.databinding.FragmentMobileCheckBinding;
import com.bloodshare.bloodshareandroid.databinding.FragmentOldUserVerificationBinding;
import com.jokerlab.jokerstool.CommonUtil;


public class VerifyOldUserFragment extends Fragment implements View.OnClickListener {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private FragmentOldUserVerificationBinding binding;

    public VerifyOldUserFragment() {

    }


    public static VerifyOldUserFragment newInstance() {
        VerifyOldUserFragment fragment = new VerifyOldUserFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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
            mListener.onFragmentInteraction(mobileNumber,true);
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
