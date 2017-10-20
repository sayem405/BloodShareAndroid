package com.bloodshare.bloodshareandroid.helper;

        import com.android.volley.VolleyError;
        import com.bloodshare.bloodshareandroid.data.model.Profile;


/**
 * Created by sayem on 10/6/2016.
 */

public class ServerCallHelper {

    private static final String TAG = "ServerCallHelper";
    public static final String PARAM_MOBILE = "mobile";


    public static int getErrorCode(VolleyError error) {
        return error.networkResponse!= null ? error.networkResponse.statusCode: 0;
    }

    public static String getJsonForVerify(String phoneNumber, String otp) {
        Profile profile = new Profile();
        profile.setKey(otp);
        profile.setMobile(phoneNumber);

        return profile.getJson();
    }
}
