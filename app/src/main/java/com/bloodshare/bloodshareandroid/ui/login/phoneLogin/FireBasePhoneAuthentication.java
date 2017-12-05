package com.bloodshare.bloodshareandroid.ui.login.phoneLogin;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.bloodshare.bloodshareandroid.BloodShareApp;
import com.bloodshare.bloodshareandroid.R;
import com.bloodshare.bloodshareandroid.data.model.ApiAuthentication;
import com.bloodshare.bloodshareandroid.data.model.Donor;
import com.bloodshare.bloodshareandroid.data.model.DonorLocation;
import com.bloodshare.bloodshareandroid.data.model.UserProfile;
import com.bloodshare.bloodshareandroid.data.network.ApiClient;
import com.bloodshare.bloodshareandroid.data.network.WebServiceCall;
import com.bloodshare.bloodshareandroid.databinding.ActivityFirebasePhoneAuthenticationBinding;
import com.bloodshare.bloodshareandroid.ui.base.BaseActivity;
import com.bloodshare.bloodshareandroid.ui.login.PersonalInfoFragment;
import com.bloodshare.bloodshareandroid.ui.main.MainActivity;
import com.bloodshare.bloodshareandroid.utils.ExtraConstants;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.jokerlab.jokerstool.DateUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bloodshare.bloodshareandroid.utils.SPKeys.SP_KEY_ACCESS_TOKEN;
import static com.bloodshare.bloodshareandroid.utils.SPKeys.SP_KEY_USER_ID;

/**
 * Created by sayem on 8/18/2017.
 */

public class FireBasePhoneAuthentication extends BaseActivity implements PhoneVerificationFragmentListener, PersonalInfoFragment.PersonalInfoFragmentListener {


    private static final String TAG = FireBasePhoneAuthentication.class.getSimpleName();

    private static final int REQUEST_CODE_LOCATION = 235;

    private static final String KEY_VERIFICATION_PHONE = "KEY_VERIFICATION_PHONE";
    private static final String KEY_STATE = "KEY_STATE";
    private boolean ommitDismissingDialog;
    private WebServiceCall serviceCall;
    private String userAccessToken;


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({VERIFICATION_NOT_STARTED, VERIFICATION_STARTED, VERIFIED})
    public static @interface VerificationState {


    }

    public static final int VERIFICATION_NOT_STARTED = 1;

    public static final int VERIFICATION_STARTED = 2;
    public static final int VERIFIED = 3;

    private static final long AUTO_RETRIEVAL_TIMEOUT_MILLIS = 120000;
    private static final long SHORT_DELAY_MILLIS = 750;
    private ActivityFirebasePhoneAuthenticationBinding binding;


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private int mVerificationState;
    private CompletableProgressDialog mProgressDialog;
    private String mPhoneNumber;
    private Boolean mIsDestroyed = false;
    private Handler mHandler;
    private PhoneAuthProvider.ForceResendingToken mForceResendingToken;
    private String mVerificationId;
    private AlertDialog mAlertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_firebase_phone_authentication);
        mHandler = new Handler();

        mVerificationState = VERIFICATION_NOT_STARTED;

        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            mPhoneNumber = savedInstanceState.getString(KEY_VERIFICATION_PHONE);
            if (savedInstanceState.getSerializable(KEY_STATE) != null) {
                mVerificationState = savedInstanceState.getInt(KEY_STATE, VERIFICATION_NOT_STARTED);
            }
            return;
        }
        String phone = getIntent().getStringExtra(ExtraConstants.EXTRA_PHONE);

        VerifyPhoneNumberFragment fragment = VerifyPhoneNumberFragment.newInstance(phone);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_verify_phone, fragment, VerifyPhoneNumberFragment.TAG)
                .disallowAddToBackStack()
                .commit();
        //showPersonalInfoFragment();
        serviceCall = ApiClient.getClient().create(WebServiceCall.class);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mVerificationState == VERIFICATION_STARTED) {
            sendCode(mPhoneNumber, false);
        } else if (mVerificationState == VERIFIED) {
            getFireBaseUserToken(FirebaseAuth.getInstance().getCurrentUser());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ommitDismissingDialog = false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(KEY_STATE, mVerificationState);
        outState.putString(KEY_VERIFICATION_PHONE, mPhoneNumber);
        ommitDismissingDialog = true;
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            mVerificationState = VERIFICATION_NOT_STARTED;
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    public static Intent getStartIntent(Context context, String phone) {
        Intent intent = new Intent(context, FireBasePhoneAuthentication.class);
        intent.putExtra(ExtraConstants.EXTRA_PHONE, phone);
        return intent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_LOCATION) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                if (place != null) {
                    getPersonalInfoFragment().setLocation(place);
                } else {

                }
            }
        }
    }

    @Override
    public void verifyPhoneNumber(String phoneNumber, boolean forceResend) {
        sendCode(phoneNumber, forceResend);
        if (forceResend) {
            showLoadingDialog(getString(R.string.fui_resending));
        } else {
            showLoadingDialog(getString(R.string.fui_verifying));
        }
    }

    private void sendCode(String phoneNumber, boolean forceResend) {
        mPhoneNumber = phoneNumber;
        mVerificationState = VERIFICATION_STARTED;

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                AUTO_RETRIEVAL_TIMEOUT_MILLIS,
                TimeUnit.MILLISECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        if (!mIsDestroyed) {
                            FireBasePhoneAuthentication.this.onVerificationSuccess(phoneAuthCredential);
                        }
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException ex) {
                        if (!mIsDestroyed) {
                            FireBasePhoneAuthentication.this.onVerificationFailed(ex);
                        }
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId,
                                           @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        mVerificationId = verificationId;
                        mForceResendingToken = forceResendingToken;
                        if (!mIsDestroyed) {
                            FireBasePhoneAuthentication.this.onCodeSent();
                        }
                    }
                },
                forceResend ? mForceResendingToken : null);
    }

    private void onCodeSent() {
        completeLoadingDialog(getString(R.string.fui_code_sent));
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismissLoadingDialog();
                showSubmitCodeFragment();
            }
        }, SHORT_DELAY_MILLIS);
        if (getSubmitConfirmationCodeFragment() != null) {
            getSubmitConfirmationCodeFragment()
                    .setConfirmationCode("");
        }
    }

    private void onVerificationSuccess(@NonNull final PhoneAuthCredential phoneAuthCredential) {
        if (TextUtils.isEmpty(phoneAuthCredential.getSmsCode())) {
            signingWithCreds(phoneAuthCredential);
        } else {
            //Show Fragment if it is not already visible
            showSubmitCodeFragment();
            SubmitConfirmationCodeFragment submitConfirmationCodeFragment =
                    getSubmitConfirmationCodeFragment();


            showLoadingDialog(getString(R.string.fui_retrieving_sms));
            if (submitConfirmationCodeFragment != null) {
                submitConfirmationCodeFragment.setConfirmationCode(String.valueOf
                        (phoneAuthCredential.getSmsCode()));
            }
            signingWithCreds(phoneAuthCredential);
        }
    }

    private void signingWithCreds(@NonNull PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth.getInstance()
                .signInWithCredential(phoneAuthCredential)
                .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(final AuthResult authResult) {
                        mVerificationState = VERIFIED;
                        getFireBaseUserToken(authResult.getUser());

                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dismissLoadingDialog();
                        //incorrect confirmation code
                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            FirebaseAuthError error = FirebaseAuthError.fromException(
                                    (FirebaseAuthInvalidCredentialsException) e);

                            switch (error) {
                                case ERROR_INVALID_VERIFICATION_CODE:
                                    showAlertDialog(
                                            getString(R.string.fui_incorrect_code_dialog_body),
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    getSubmitConfirmationCodeFragment()
                                                            .setConfirmationCode("");
                                                }
                                            });
                                    break;
                                case ERROR_SESSION_EXPIRED:
                                    showAlertDialog(
                                            getString(R.string.fui_error_session_expired),
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    getSubmitConfirmationCodeFragment()
                                                            .setConfirmationCode("");
                                                }
                                            });
                                    break;
                                default:
                                    Log.w(TAG, error.getDescription() + " " + e);
                                    showAlertDialog(error.getDescription(), null);
                            }
                        } else {
                            showAlertDialog(e.getLocalizedMessage(), null);
                        }
                    }
                });
    }

    private void getFireBaseUserToken(FirebaseUser user) {
        user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<GetTokenResult> task) {

                if (task.isSuccessful()) {
                    String token = task.getResult().getToken();
                    Log.d(TAG, token);
                    if (!mIsDestroyed)
                        authenticate(token);
                }
            }
        });
    }

    private void saveContact(final UserProfile token) {
        completeLoadingDialog(getString(R.string.fui_verified));

        // Activity can be recreated before this message is handled
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!mIsDestroyed) {
                    if (!ommitDismissingDialog)
                        dismissLoadingDialog();
                        saveUserAndStartMain(token);
                }
            }
        }, SHORT_DELAY_MILLIS);
    }

    private void onVerificationFailed(@NonNull FirebaseException ex) {
        VerifyPhoneNumberFragment verifyPhoneNumberFragment = (VerifyPhoneNumberFragment)
                getSupportFragmentManager().findFragmentByTag(VerifyPhoneNumberFragment.TAG);

        if (verifyPhoneNumberFragment == null) {
            return;
        }
        if (ex instanceof FirebaseAuthException) {
            FirebaseAuthError error = FirebaseAuthError.fromException((FirebaseAuthException) ex);

            switch (error) {
                case ERROR_INVALID_PHONE_NUMBER:
                    verifyPhoneNumberFragment.showError(getString(R.string.fui_invalid_phone_number));
                    dismissLoadingDialog();
                    break;
                case ERROR_TOO_MANY_REQUESTS:
                    showAlertDialog(getString(R.string.fui_error_too_many_attempts), null);
                    dismissLoadingDialog();
                    break;
                case ERROR_QUOTA_EXCEEDED:
                    showAlertDialog(getString(R.string.fui_error_quota_exceeded), null);
                    dismissLoadingDialog();
                    break;
                default:
                    Log.w(TAG, error.getDescription() + " '\n " + ex);
                    dismissLoadingDialog();
                    showAlertDialog(error.getDescription(), null);
            }
        } else {
            Log.w(TAG, ex.getLocalizedMessage());
            dismissLoadingDialog();
            showAlertDialog(ex.getLocalizedMessage(), null);
        }
    }

    private void showLoadingDialog(String message) {
        dismissLoadingDialog();

        if (mProgressDialog == null) {
            mProgressDialog = CompletableProgressDialog.show(getFragmentManager());
        }

        mProgressDialog.setMessage(message);
    }

    private void dismissLoadingDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismissAllowingStateLoss();
            mProgressDialog = null;
        }
    }

    private void authenticate(String token) {
        if (serviceCall == null) {
            serviceCall = ApiClient.getClient().create(WebServiceCall.class);
        }

        serviceCall.authenticate(new ApiAuthentication(token)).enqueue(new Callback<ApiAuthentication>() {
            @Override
            public void onResponse(Call<ApiAuthentication> call, Response<ApiAuthentication> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "access token @" + response.body().userAccessToken);

                    userAccessToken = response.body().userAccessToken;
                    if (response.body().isUserNew) {
                        //Toast.makeText(FireBasePhoneAuthentication.this, "new user .. need implementation", Toast.LENGTH_SHORT).show();
                        showPersonalInfoFragment();
                        dismissLoadingDialog();
                        //saveContact(null);
                    } else {
                        getUser(response.body().userAccessToken);
                    }
                }

            }

            @Override
            public void onFailure(Call<ApiAuthentication> call, Throwable t) {

            }
        });
    }


    public void getUser(String userAccessToken) {
        Log.d(TAG, "user access token @" + userAccessToken);
        serviceCall.getUser(getAuthorization(userAccessToken)).enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                Log.d(TAG, "user " + response.body().mobile + response.body().toString());
                saveContact(response.body());
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    @NonNull
    private String getAuthorization(String userAccessToken) {
        return ApiClient.getAuthorization(userAccessToken);
    }

    @Override
    protected void onDestroy() {
        mIsDestroyed = true;
        mHandler.removeCallbacksAndMessages(null);
        dismissLoadingDialog();
        super.onDestroy();
    }

    private void completeLoadingDialog(String content) {
        if (mProgressDialog != null) {
            mProgressDialog.onComplete(content);
        }
    }

    private void showPersonalInfoFragment() {
        if (getPersonalInfoFragment() == null) {
            PersonalInfoFragment fragment = new PersonalInfoFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction().replace(R.id.fragment_verify_phone, fragment, PersonalInfoFragment.TAG).addToBackStack(null);
            if (!isFinishing() && !mIsDestroyed) {
                ft.commitAllowingStateLoss();
            }
        }
    }

    public PersonalInfoFragment getPersonalInfoFragment() {
        return (PersonalInfoFragment) getSupportFragmentManager().findFragmentByTag(PersonalInfoFragment.TAG);
    }

    private void showSubmitCodeFragment() {
        if (getSubmitConfirmationCodeFragment() == null) {
            SubmitConfirmationCodeFragment f = SubmitConfirmationCodeFragment.newInstance(mPhoneNumber);
            FragmentTransaction t = getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_verify_phone, f, SubmitConfirmationCodeFragment.TAG)
                    .addToBackStack(null);

            if (!isFinishing() && !mIsDestroyed) {
                t.commitAllowingStateLoss();
            }
        }
    }

    private SubmitConfirmationCodeFragment getSubmitConfirmationCodeFragment() {
        return (SubmitConfirmationCodeFragment) getSupportFragmentManager().findFragmentByTag
                (SubmitConfirmationCodeFragment.TAG);
    }

    private void showAlertDialog(@NonNull String s, DialogInterface.OnClickListener onClickListener) {
        mAlertDialog = new AlertDialog.Builder(this)
                .setMessage(s)
                .setPositiveButton(R.string.fui_incorrect_code_dialog_positive_button_text, onClickListener)
                .show();
    }

    public void submitConfirmationCode(String confirmationCode) {
        showLoadingDialog(getString(R.string.fui_verifying));
        signingWithCreds(PhoneAuthProvider.getCredential(mVerificationId, confirmationCode));
    }

    @Override
    public void submitPersonalInfo(String name, String DOB, String bloodGroup, Place place) {
        Donor donor = new Donor(name, bloodGroup, DateUtil.getDateByFormat(DOB, DateUtil.DATE_FORMAT_1), new DonorLocation(place));
        updateUser(donor);
    }

    @Override
    public void locationClicked(View view) {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), REQUEST_CODE_LOCATION);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    public void updateUser(final Donor userProfile) {

        serviceCall.saveUser(getAuthorization(userAccessToken), userProfile).enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {

                if (response.isSuccessful()) {
                    saveUserAndStartMain(response.body());
                } else {
                    Log.w(TAG, "updateUser @" + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {

            }
        });
    }

    private void saveUserAndStartMain(UserProfile response) {
        SharedPreferences sharedPref = getSharedPreferences(BloodShareApp.TAG, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(SP_KEY_USER_ID, response.id);
        editor.apply();
        editor.putString(SP_KEY_ACCESS_TOKEN, userAccessToken);
        editor.apply();
        ((BloodShareApp) getApplication()).getDb().getAppDao().insert(response);
        MainActivity.startActivity(FireBasePhoneAuthentication.this, response.id);
        finish();
    }
}