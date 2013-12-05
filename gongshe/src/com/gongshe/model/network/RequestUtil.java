package com.gongshe.model.network;

import android.util.Log;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gongshe.model.OnUpdateListener;
import com.gongshe.model.UserManager;

import static com.gongshe.model.GongSheConstant.RESULT_AUTH_ERROR;
import static com.gongshe.model.GongSheConstant.RESULT_FAIL;

public class RequestUtil {
    private static final String TAG = RequestUtil.class.getSimpleName();

    public static Response.Listener<String> getResponseListener(final OnNetListener listener) {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "net response is:" + response);
                if (response != null && response.contains(RESULT_AUTH_ERROR)) {
                    Log.e(TAG, "on auth error.");
                    UserManager.getInstance().onAuthError();
                    if (listener != null) listener.onError(RESULT_AUTH_ERROR);
                    return;
                }
                if (response != null && response.contains(RESULT_FAIL)) {
                    if (listener != null) listener.onError(RESULT_FAIL);
                    return;
                }
                if (response == null || response.equals("")) {
                    if (listener != null) listener.onError(RESULT_FAIL);
                    return;
                }
                if (listener != null) listener.OnResponse(response);
            }
        };
    }

    public static Response.ErrorListener getResponseErrorListener(final OnNetListener listener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "get network error." + error.getMessage());
                if (listener != null) listener.onError(error.getMessage());
            }
        };
    }

    public static OnNetListener getStatusReturnListener(final OnUpdateListener listener) {
        return new OnNetListener() {
            @Override
            public void OnResponse(String response) {
                if (listener != null) listener.onUpdate();
            }

            @Override
            public void onError(String errorMessage) {
                if (listener != null) listener.onError();
            }
        };
    }

}
