package com.jokerlab.volleynet;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v4.util.ArrayMap;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sayem on 4/24/2015.
 */
public class RequestBuilder {

    public RequestBuilder(Context context, String methodUrl, JSONObject jsonObject, Response.Listener<JSONObject> listener) {
        this.context = context;
        this.methodUrl = methodUrl;
        this.jsonObject = jsonObject;
        this.listener = listener;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STRING_REQUEST, JSON_OBJECT_REQUEST, JSON_STRING_REQUEST})
    public @interface RequestType {
    }

    public static final int STRING_REQUEST = 1;
    public static final int JSON_OBJECT_REQUEST = 2;
    public static final int JSON_STRING_REQUEST = 3;

    private Request request;
    private VolleyRequestManager volleyRequestManager;
    private String baseUrl;
    private String methodUrl;
    private String fullUrl;
    private Response.ErrorListener errorListener;
    private Response.Listener listener;
    private int method = Request.Method.GET;
    private String json;
    private Context context;
    private String TAG;
    private int requestType = STRING_REQUEST;
    private JSONObject jsonObject;
    private Map<String, String> params;

    public RequestBuilder(Context context, String methodUrl, String json, Response.Listener listener) {
        this.context = context;
        this.methodUrl = methodUrl;
        this.json = json;
        this.listener = listener;
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

    public RequestBuilder setParams(ArrayMap<String, String> params) {
        this.params = params;
        return this;
    }

    public RequestBuilder addParam(String key, String value) {
        if (params == null) {
            params = new HashMap<>();
        }
        params.put(key, value);
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

            case JSON_STRING_REQUEST:
                return new JsonStringRequest(method, fullUrl, json, listener, errorListener);

            default:
                fullUrl = getUrlForGet(fullUrl);
                return new StringRequest(method, fullUrl, listener, errorListener);
        }
    }

    private String getUrlForGet(String fullUrl) {
        if (params != null && params.size() > 0) {
            StringBuilder builder = new StringBuilder();
            builder.append(fullUrl + "?");
            for (String key : params.keySet()) {
                Object value = params.get(key);
                if (value != null) {
                    try {
                        value = URLEncoder.encode(String.valueOf(value), "utf-8");

                        if (builder.length() > 0)
                            builder.append("&");
                        builder.append(key).append("=").append(value);
                    } catch (UnsupportedEncodingException e) {
                    }
                }
            }
            return builder.toString();
        }


        return fullUrl;
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
