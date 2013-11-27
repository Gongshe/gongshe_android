package com.gongshe;

import android.app.Application;
import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.gongshe.model.network.OkHttpStack;

public class GongSheApp extends Application {
    private static RequestQueue mRequestQueue;
    private static GongSheApp sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        mRequestQueue = Volley.newRequestQueue(this, new OkHttpStack());
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public Context getContext() {
        return getApplicationContext();
    }

    public static GongSheApp getInstance() {
        return sInstance;
    }
}
