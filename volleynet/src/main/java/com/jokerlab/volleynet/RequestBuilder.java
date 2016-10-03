package com.jokerlab.volleynet;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IntDef;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.charset.Charset;

/**
 * Created by Sayem on 4/24/2015.
 */
public class RequestBuilder {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STRING_REQUEST, JSON_OBJECT_REQUEST})
    public @interface RequestType {
    }

    private static final int STRING_REQUEST = 1;
    private static final int JSON_OBJECT_REQUEST = 2;

    private Request request;
    private VolleyRequestManager volleyRequestManager;
    private String baseUrl;
    private String methodUrl;
    private String fullUrl;
    private Response.ErrorListener errorListener;
    private Response.Listener listener;
    private int method = Request.Method.POST;
    private String json;
    private Context context;
    private boolean timeOutThrown;
    private Handler timeOutHandler;
    private String TAG;
    private ResponseListener responseListener;
    private int requestType = STRING_REQUEST;
    private JSONObject jsonObject;

    public RequestBuilder(Context context, String methodUrl, String json, Response.Listener listener) {
        this.context = context;
        this.methodUrl = methodUrl;
        this.json = json;
        this.listener = listener;
        this.timeOutHandler = new Handler(Looper.getMainLooper());
    }


    public RequestBuilder setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public RequestBuilder setRequestType(@RequestType int requestType) {
        this.requestType = requestType;
        return this;
    }

    public RequestBuilder setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }

    public RequestBuilder setMethod(int method) {
        this.method = method;
        return this;
    }

    public RequestBuilder setErrorListener(Response.ErrorListener errorListener) {
        this.errorListener = errorListener;
        return this;
    }

    public RequestBuilder setMethodUrl(String methodUrl) {
        this.methodUrl = methodUrl;
        return this;
    }

    public RequestBuilder setTAG(String TAG) {
        this.TAG = TAG;
        return this;
    }


    public Request buildJsonRequest() {
        fullUrl = baseUrl + methodUrl;
        request = getRequest();

        if (TAG != null) {
            request.setTag(TAG);
        }
        return request;
    }

    private Request getRequest() {
        switch (requestType) {

            case JSON_OBJECT_REQUEST:
                return new JsonObjectRequest(method, fullUrl, jsonObject, listener, errorListener);

            default:
                return new StringRequest(method, fullUrl, listener, errorListener) {
                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        return json.getBytes(Charset.forName("UTF-8"));
                    }

                };
        }
    }


    public void setVolleyRequestManager(VolleyRequestManager volleyRequestManager) {
        this.volleyRequestManager = volleyRequestManager;
    }

    public void buildJsonRequestAndAddToQueue() {
        buildJsonRequest();
        volleyRequestManager.addToRequestQueue(request);
    }

    public void buildJsonRequestAndAddToQueue(String TAG) {
        setTAG(TAG);
        buildJsonRequest();
        volleyRequestManager.addToRequestQueue(request);
    }

    public void buildJsonRequestAndAddToQueue(String TAG, long timeOut, ResponseListener listener) {
        this.responseListener = listener;
        setTAG(TAG, timeOut);
        buildJsonRequest();
        volleyRequestManager.addToRequestQueue(request);
    }

    private RequestBuilder setTAG(String tag, long timeOutinSec) {
        this.TAG = tag;
        return this;
    }

    public interface ResponseListener {
        void onCancelListener();
    }
}
