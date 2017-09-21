package com.bloodshare.bloodshareandroid.ui.login.phoneLogin;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.bloodshare.bloodshareandroid.R;
import com.bloodshare.bloodshareandroid.databinding.FragmentConfirmationCodeLayoutBinding;
import com.bloodshare.bloodshareandroid.ui.base.BaseFragment;


/**
 * Created by sayem on 8/21/2017.
 */

public class SubmitConfirmationCodeFragment extends BaseFragment {
    public static final String TAG = SubmitConfirmationCodeFragment.class.getSimpleName();

    private static final String EXTRA_MILLIS_UNTIL_FINISHED = "EXTRA_MILLIS_UNTIL_FINISHED";
    private static final long RESEND_WAIT_MILLIS = 15000;


    private FragmentConfirmationCodeLayoutBinding binding;
    private CustomCountDownTimer mCountdownTimer;
    private long mMillisUntilFinished;

    private PhoneVerificationFragmentListener mVerifier;


    public static SubmitConfirmationCodeFragment newInstance(String phoneNumber) {
        SubmitConfirmationCodeFragment fragment = new SubmitConfirmationCodeFragment();

        Bundle args = new Bundle();
        args.putString(ExtraConstants.EXTRA_PHONE, phoneNumber);

        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_confirmation_code_layout, container, false);

        final String phoneNumber = getArguments().getString(ExtraConstants.EXTRA_PHONE);

        setupConfirmationCodeEditText();
        setupEditPhoneNumberTextView(phoneNumber);
        setupCountDown(RESEND_WAIT_MILLIS);
        setupSubmitConfirmationCodeButton();
        setupResendConfirmationCodeTextView(phoneNumber);

        return binding.getRoot();
    }

    private void setupConfirmationCodeEditText() {
        resetConfirmationCode();
        SpacedEditText.BucketedTextChangeListener listener = createBucketedTextChangeListener();
        binding.confirmationCode.addTextChangedListener(listener);
    }

    private void resetConfirmationCode() {
        binding.confirmationCode.setText("------");
    }

    private SpacedEditText.BucketedTextChangeListener createBucketedTextChangeListener() {
        return new SpacedEditText.BucketedTextChangeListener(binding.confirmationCode, 6, "-",
                createBucketOnEditCallback(binding.submitConfirmationCode));
    }

    private void setupEditPhoneNumberTextView(@Nullable String phoneNumber) {
        binding.editPhoneNumber.setText(TextUtils.isEmpty(phoneNumber) ? "" : phoneNumber);
        binding.editPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                }
            }
        });
    }

    private void setupCountDown(long startTimeMillis) {
        //set the timer view
        setTimer(startTimeMillis / 1000);

        //create a countdown
        mCountdownTimer = createCountDownTimer(binding.ticker, binding.resendCode, this,
                startTimeMillis);

        //start the countdown
        startTimer();
    }


    private void setTimer(long millisUntilFinished) {
        binding.ticker.setText(String.format(getString(R.string.fui_resend_code_in),
                timeRoundedToSeconds(millisUntilFinished)));
    }

    private CustomCountDownTimer createCountDownTimer(final TextView timerText, final TextView
            resendCode, final SubmitConfirmationCodeFragment fragment, final long startTimeMillis) {
        return new CustomCountDownTimer(startTimeMillis, 500) {
            SubmitConfirmationCodeFragment mSubmitConfirmationCodeFragment = fragment;

            public void onTick(long millisUntilFinished) {
                mMillisUntilFinished = millisUntilFinished;
                mSubmitConfirmationCodeFragment.setTimer(millisUntilFinished);
            }

            public void onFinish() {
                timerText.setText("");
                timerText.setVisibility(View.GONE);
                resendCode.setVisibility(View.VISIBLE);
            }
        };
    }

    private void setupSubmitConfirmationCodeButton() {
        binding.submitConfirmationCode.setEnabled(false);

        binding.submitConfirmationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String confirmationCode = binding.confirmationCode.getUnspacedText()
                        .toString();
                if (TextUtils.isEmpty(confirmationCode)) return;
                mVerifier.submitConfirmationCode(confirmationCode);
            }
        });
    }

    private void setupResendConfirmationCodeTextView(final String phoneNumber) {
        binding.resendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVerifier.verifyPhoneNumber(phoneNumber, true);
                binding.resendCode.setVisibility(View.GONE);
                binding.ticker.setVisibility(View.VISIBLE);
                binding.ticker.setText(String.format(getString(R.string.fui_resend_code_in),
                        RESEND_WAIT_MILLIS / 1000));
                mCountdownTimer.renew();
            }
        });
    }

    void setConfirmationCode(String code) {
        binding.confirmationCode.setText(code);
    }

    private void startTimer() {
        if (mCountdownTimer != null) {
            mCountdownTimer.start();
        }
    }

    private void cancelTimer() {
        if (mCountdownTimer != null) {
            mCountdownTimer.cancel();
        }
    }

    private SpacedEditText.BucketedTextChangeListener.ContentChangeCallback createBucketOnEditCallback(final Button button) {
        return new SpacedEditText.BucketedTextChangeListener.ContentChangeCallback() {
            @Override
            public void whileComplete() {
                button.setEnabled(true);
            }

            @Override
            public void whileIncomplete() {
                button.setEnabled(false);
            }
        };
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mCountdownTimer.update(savedInstanceState.getLong(EXTRA_MILLIS_UNTIL_FINISHED));
        }

        if (!(getActivity() instanceof PhoneVerificationFragmentListener)) {
            throw new IllegalStateException("Activity must implement PhoneVerificationHandler");
        }
        mVerifier = (PhoneVerificationFragmentListener) getActivity();

        //mVerifier.setTitle(R.string.fui_verify_phone_number_title);

    }

    private int timeRoundedToSeconds(double millis) {
        return (int) Math.ceil(millis / 1000);
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.confirmationCode.requestFocus();
        InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imgr.showSoftInput(binding.confirmationCode, 0);
    }

    @Override
    public void onDestroyView() {
        cancelTimer();
        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(EXTRA_MILLIS_UNTIL_FINISHED, mMillisUntilFinished);
    }
}
