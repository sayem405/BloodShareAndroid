package com.bloodshare.bloodshareandroid;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bloodshare.bloodshareandroid.databinding.FragmentMobileCheckBinding;
import com.jokerlab.jokerstool.CommonUtil;


public class MobileInputFragment extends BaseFragment implements View.OnClickListener {


    private OnFragmentInteractionListener mListener;
    private FragmentMobileCheckBinding t;

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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        t = DataBindingUtil.inflate(inflater, R.layout.fragment_mobile_check, container, false);
        t.verifyButton.setOnClickListener(this);
        return t.getRoot();
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
        String mobileNumber = t.mobileInputEditText.getText().toString();
        if (CommonUtil.isEmpty(mobileNumber)) return;
        onButtonPressed(mobileNumber);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String mobileNumber, boolean isNew);
    }
}
