package com.jokerlab.volleynet;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by Sayem on 4/24/2015.
 */
public class VolleyRequestManager {

    private static VolleyRequestManager instance;
    RequestBuilder requestBuilder;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private Context context;

    private VolleyRequestManager(final Context context) {
        this.context = context;
        requestQueue = getRequestQueue();
        imageLoader = getImageLoader();
    }

    public synchronized static VolleyRequestManager getInstance(Context context) {
        if (instance == null) {
            instance = new VolleyRequestManager(context);
        }
        return instance;
    }

    public ImageLoader getImageLoader() {
        if (imageLoader == null) {
            imageLoader = new ImageLoader(getRequestQueue(), new ImageLoader.ImageCache() {
                private final LruBitmapCache mCache = new LruBitmapCache(context.getApplicationContext());

                public void putBitmap(String url, Bitmap bitmap) {
                    mCache.put(url, bitmap);
                }

                public Bitmap getBitmap(String url) {
                    return mCache.get(url);
                }
            });
        }
        return imageLoader;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }

    public void cancelRequest(String tag) {
        if (tag != null) {
            getRequestQueue().cancelAll(tag);
        }
    }

    public RequestBuilder getRequestBuilder(String methodUrl, String json, Response.Listener<String> listener) {
        requestBuilder = new RequestBuilder(context, methodUrl, json, listener);
        requestBuilder.setVolleyRequestManager(this);
        return requestBuilder;
    }
}
