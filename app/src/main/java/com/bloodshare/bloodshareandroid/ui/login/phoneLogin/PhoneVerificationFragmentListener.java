package com.bloodshare.bloodshareandroid.ui.login.phoneLogin;

import android.support.annotation.StringRes;

/**
 * Created by sayem on 8/24/2017.
 */

public interface PhoneVerificationFragmentListener {
    public void setTitle(@StringRes int title);
    public void verifyPhoneNumber(String phoneNumber, boolean forceResend);
    public void submitConfirmationCode(String confirmationCode);
}
