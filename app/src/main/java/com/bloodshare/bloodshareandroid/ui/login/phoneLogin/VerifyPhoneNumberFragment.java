package com.bloodshare.bloodshareandroid.ui.login.phoneLogin;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.bloodshare.bloodshareandroid.R;
import com.bloodshare.bloodshareandroid.databinding.FragmentVerifyPhoneBinding;
import com.bloodshare.bloodshareandroid.ui.base.BaseFragment;
import com.bloodshare.bloodshareandroid.utils.ExtraConstants;

import java.util.Locale;

import static com.bloodshare.bloodshareandroid.utils.ExtraConstants.EXTRA_COUNTRY_CODE;
import static com.bloodshare.bloodshareandroid.utils.ExtraConstants.EXTRA_LOCALE;

/**
 * Created by sayem on 8/18/2017.
 */

public class VerifyPhoneNumberFragment extends BaseFragment implements View.OnClickListener {

    public static final String TAG = VerifyPhoneNumberFragment.class.getSimpleName();

    private FragmentVerifyPhoneBinding binding;
    private Context mAppContext;
    private PhoneVerificationFragmentListener listener;

    private static final int RC_PHONE_HINT = 22;
    private String phoneNumber;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_verify_phone, container, false);

        setUpCountrySpinner();
        setupSendCodeButton();
        setupTerms();


        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (!(getActivity() instanceof PhoneVerificationFragmentListener)) {
            throw new IllegalStateException("Activity must implement PhoneVerificationHandler");
        }
        listener = (PhoneVerificationFragmentListener) getActivity();

        if (savedInstanceState != null) {
            Locale locale = (Locale) savedInstanceState.getSerializable(EXTRA_LOCALE);
            int countryCode = savedInstanceState.getInt(EXTRA_COUNTRY_CODE);
            binding.countryList.setSpinnerText(countryCode, locale);
            return;
        }
        listener.setTitle(R.string.fui_verify_phone_number_title);
        String phone;
        if (TextUtils.isEmpty(phoneNumber)) {
            phone = getArguments().getString(ExtraConstants.EXTRA_PHONE);
        } else {
            phone = phoneNumber;
        }
        if (!TextUtils.isEmpty(phone)) {
            PhoneNumber phoneNumber = PhoneNumberUtils.getPhoneNumber(phone);
            setPhoneNumber(phoneNumber);
            setCountryCode(phoneNumber);
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        CountryInfo countryInfo = (CountryInfo) binding.countryList.getTag();
        outState.putSerializable(EXTRA_LOCALE, countryInfo.locale);
        outState.putInt(EXTRA_COUNTRY_CODE, countryInfo.countryCode);
    }

    private void setPhoneNumber(PhoneNumber phoneNumber) {
        if (PhoneNumber.isValid(phoneNumber)) {
            binding.phoneNumber.setText(phoneNumber.getPhoneNumber());
            binding.phoneNumber.setSelection(phoneNumber.getPhoneNumber().length());
        }
    }

    private void setCountryCode(PhoneNumber phoneNumber) {
        if (PhoneNumber.isCountryValid(phoneNumber)) {
            binding.countryList.setSelectedForCountry(new Locale("", phoneNumber.getCountryIso()),
                    phoneNumber.getCountryCode());
        }
    }

    private void setupSendCodeButton() {
        binding.sendCode.setOnClickListener(this);
    }

    private void setUpCountrySpinner() {
        binding.countryList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.phoneNumberError.setText("");
            }
        });
    }

    private void setupTerms() {
        final String verifyPhoneButtonText = getString(R.string.fui_verify_phone_number);
        final String terms = getString(R.string.fui_sms_terms_of_service, verifyPhoneButtonText);
        binding.sendSmsTos.setText(terms);
    }

    public static VerifyPhoneNumberFragment newInstance(String phone) {
        VerifyPhoneNumberFragment fragment = new VerifyPhoneNumberFragment();

        Bundle args = new Bundle();
        args.putString(ExtraConstants.EXTRA_PHONE, phone);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == binding.sendCode.getId()) {
            String phone = getPseudoValidPhoneNumber();
            if (TextUtils.isEmpty(phone)) {
                binding.phoneNumberError.setText(R.string.fui_invalid_phone_number);
            } else {
                binding.phoneNumberError.setText("");
                phoneNumber = phone;
                listener.verifyPhoneNumber(phone, false);
            }
        }
    }

    @Nullable
    private String getPseudoValidPhoneNumber() {
        final CountryInfo countryInfo = (CountryInfo) binding.countryList.getTag();
        final String everythingElse = binding.phoneNumber.getText().toString();

        if (TextUtils.isEmpty(everythingElse)) {
            return null;
        }

        return PhoneNumberUtils.formatPhoneNumber(everythingElse, countryInfo);
    }

    void showError(String e) {
        binding.phoneNumberError.setText(e);
    }
}
