package com.jokerlab.volleynet.listeners;

/**
 * Created by sayem on 10/21/2016.
 */

public interface NetworkListener {
    void onResponse(int action, @NetworkResponses int networkResponse, int errorCode, Object response);
}
