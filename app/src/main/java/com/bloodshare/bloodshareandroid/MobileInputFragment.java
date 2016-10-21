package com.bloodshare.bloodshareandroid;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bloodshare.bloodshareandroid.databinding.FragmentMobileCheckBinding;
import com.bloodshare.bloodshareandroid.helper.ServerCalls;
import com.bloodshare.bloodshareandroid.listeners.NetworkListener;
import com.bloodshare.bloodshareandroid.listeners.NetworkResponses;
import com.jokerlab.jokerstool.CommonUtil;


public class MobileInputFragment extends BaseFragment implements View.OnClickListener, NetworkListener {


    private static final String TAG = MobileInputFragment.class.getSimpleName();
    private static final int CHECK_MOBILE_NUMBER = 50;

    private OnFragmentInteractionListener mListener;
    private FragmentMobileCheckBinding t;
    private String mobileNumber;

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
        this.mobileNumber = mobileNumber;
        ServerCalls.checkNewUserAndSendOTP(getActivity(), TAG, CHECK_MOBILE_NUMBER, mobileNumber, this);
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

    @Override
    public void onResponse(int action, @NetworkResponses int networkResponse, int errorCode, Object response) {
        switch (action) {
            case CHECK_MOBILE_NUMBER:
                if (networkResponse == NetworkResponses.RESULT_OK) {
                    boolean isNew = Boolean.valueOf((String) response);
                    mListener.onFragmentInteraction(mobileNumber, isNew);
                } else {
                    Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
                }
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String mobileNumber, boolean isNew);
    }
}
