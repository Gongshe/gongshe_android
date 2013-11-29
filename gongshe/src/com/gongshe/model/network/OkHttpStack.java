package com.gongshe.model.network;

import android.util.Log;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.gongshe.model.UserManager;
import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.gongshe.model.GongSheConstant.RESULT_AUTH_ERROR;
import static com.gongshe.model.GongSheConstant.RESULT_FAIL;

/**
 * An {@link com.android.volley.toolbox.HttpStack HttpStack} implementation which
 * uses OkHttp as its transport.
 */
public class OkHttpStack extends HurlStack {
    private final static String TAG = OkHttpStack.class.getSimpleName();
    private final OkHttpClient client;

    public OkHttpStack() {
        this(new OkHttpClient());
    }

    public OkHttpStack(OkHttpClient client) {
        if (client == null) {
            throw new NullPointerException("Client must not be null.");
        }
        this.client = client;
    }

    @Override protected HttpURLConnection createConnection(URL url) throws IOException {
        return client.open(url);
    }
}