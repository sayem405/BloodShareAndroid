package com.bloodshare.bloodshareandroid;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bloodshare.bloodshareandroid.databinding.FragmentOldUserVerificationBinding;
import com.bloodshare.bloodshareandroid.helper.ServerCalls;
import com.jokerlab.jokerstool.CommonUtil;
import com.jokerlab.volleynet.listeners.NetworkListener;
import com.jokerlab.volleynet.listeners.NetworkResponses;

import static android.content.ContentValues.TAG;
import static com.jokerlab.volleynet.listeners.NetworkResponses.RESULT_OK;


public class VerifyOldUserFragment extends BaseFragment implements View.OnClickListener, NetworkListener {


    private static final String ARG_PARAM_MOBILE_NUMBER = "mobileNumber";
    private static final int AUTHENTICATE_OTP = 900;
    private String mobileNumber;

    private OnFragmentInteractionListener mListener;


    private FragmentOldUserVerificationBinding binding;
    private String otp;

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
        String otp = binding.otpEditText.getText().toString();
        if (CommonUtil.isEmpty(otp)) return;

        onButtonPressed(otp, mobileNumber);
    }

    public void onButtonPressed(String otp, String number) {
        this.otp = otp;
        binding.progressBar.setVisibility(View.VISIBLE);
        ServerCalls.verifySecurityCode(getActivity(),TAG,AUTHENTICATE_OTP,number,otp,this);
    }

    @Override
    public void onResponse(int action, @NetworkResponses int networkResponse, int errorCode, Object response) {
        binding.progressBar.setVisibility(View.GONE);

        if (networkResponse == RESULT_OK) {
            boolean authenticated = Boolean.valueOf((String) response);
            mListener.onFragmentInteraction(authenticated);
        } else {
            Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(boolean authenticated);
    }
}
