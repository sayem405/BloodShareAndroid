package com.bloodshare.bloodshareandroid;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bloodshare.bloodshareandroid.databinding.FragmentMobileCheckBinding;
import com.jokerlab.jokerstool.CommonUtil;


public class MobileInputFragment extends Fragment implements View.OnClickListener {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private FragmentMobileCheckBinding binding;

    public MobileInputFragment() {

    }


    public static MobileInputFragment newInstance() {
        MobileInputFragment fragment = new MobileInputFragment();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mobile_check, container, false);
        binding.verifyButton.setOnClickListener(this);
        return binding.getRoot();
    }

    public void onButtonPressed(String mobileNumber) {
        if (mListener != null) {
            mListener.onFragmentInteraction(mobileNumber,true);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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
        String mobileNumber = binding.mobileInputEditText.getText().toString();
        if (CommonUtil.isEmpty(mobileNumber)) return;
        onButtonPressed(mobileNumber);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String mobileNumber, boolean isNew);
    }
}
