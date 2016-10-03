package com.jokerlab.volleynet;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * Created by Sayem on 4/24/2015.
 */
public class JsonRequestBuilder {

    //JsonObjectRequest request;
    StringRequest request;
    VolleyRequestManager volleyRequestManager;
    private String baseUrl;// = Constants.ACTIVITY_PROFILE_SERVER;
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

    public JsonRequestBuilder(Context context, String methodUrl, String json, Response.Listener listener) {
        this.context = context;
        this.methodUrl = methodUrl;
        this.json = json;
        this.listener = listener;
        this.timeOutHandler = new Handler(Looper.getMainLooper());
    }


    public JsonRequestBuilder setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public JsonRequestBuilder setMethod(int method) {
        this.method = method;
        return this;
    }

    public JsonRequestBuilder setErrorListener(Response.ErrorListener errorListener) {
        this.errorListener = errorListener;
        return this;
    }

    public JsonRequestBuilder setMethodUrl(String methodUrl) {
        this.methodUrl = methodUrl;
        return this;
    }

    public JsonRequestBuilder setTAG(String TAG) {
        this.TAG = TAG;
        return this;
    }


    public StringRequest buildJsonRequest() {


        fullUrl = baseUrl + methodUrl;// + "?token=" + token;
        /*request = new JsonObjectRequest(method, fullUrl, json, listener, errorListener){*/

        request = new StringRequest(method, fullUrl, listener, errorListener) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                //return super.getBody();

                return json.getBytes(Charset.forName("UTF-8"));
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=" + getParamsEncoding();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Accept-Encoding", "gzip");
                return map;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                if (response.headers.containsKey("Content-Encoding")) {
                    String output = ""; // note: better to use StringBuilder
                    try {
                        final GZIPInputStream gStream = new GZIPInputStream(new ByteArrayInputStream(response.data));
                        final InputStreamReader reader = new InputStreamReader(gStream);
                        final BufferedReader in = new BufferedReader(reader);
                        String read;
                        while ((read = in.readLine()) != null) {
                            output += read;
                        }
                        reader.close();
                        in.close();
                        gStream.close();
                    } catch (IOException e) {
                        return Response.error(new ParseError());
                    }

                    if (timeOutThrown) {
                        return Response.error(new VolleyError("Forced timeout"));
                    } else {
                        responseListener = null;
                        return Response.success(output, HttpHeaderParser.parseCacheHeaders(response));
                    }
                }

                return super.parseNetworkResponse(response);
            }

        };
        // explanation : http://stackoverflow.com/a/22169775/1263362
        int socketTimeout = 45000;//45 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        if (TAG != null) {
            request.setTag(TAG);
        }

        return request;
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

    private JsonRequestBuilder setTAG(String tag, long timeOutinSec) {
        this.TAG = tag;

        timeOutHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                timeOutThrown = true;

                request.cancel();

                if (responseListener != null) {
                    responseListener.onCancelListener();
                }
            }
        }, timeOutinSec * 1000);

        return this;
    }

    public interface ResponseListener {
        void onCancelListener();
    }
}
