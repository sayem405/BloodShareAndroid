package com.jokerlab.volleynet.listeners;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.jokerlab.volleynet.listeners.NetworkResponses.NETWORK_ERROR;
import static com.jokerlab.volleynet.listeners.NetworkResponses.RESULT_OK;

/**
 * Created by sayem on 10/21/2016.
 */

@Retention(RetentionPolicy.SOURCE)
@IntDef({RESULT_OK, NETWORK_ERROR})
public @interface NetworkResponses {
    int RESULT_OK = 1;
    int NETWORK_ERROR = 2;

}
