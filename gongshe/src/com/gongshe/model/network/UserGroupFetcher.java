package com.gongshe.model.network;

import android.util.Log;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gongshe.GongSheApp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static com.gongshe.model.GongSheConstant.*;

public class UserGroupFetcher {
    private static final String TAG = UserGroupFetcher.class.getSimpleName();
    private static volatile UserGroupFetcher sInstance;
    private RequestQueue mQueue;

    private UserGroupFetcher() {
    } // for singleton

    public static UserGroupFetcher getsInstance() {
        if (sInstance == null) {
            synchronized (UserGroupFetcher.class) {
                if (sInstance == null) {
                    sInstance = new UserGroupFetcher();
                    sInstance.mQueue = GongSheApp.getInstance()
                                                 .getRequestQueue();
                }
            }
        }
        return sInstance;
    }

    private Response.Listener<String> getResponseListener(final OnNetListener listener) {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "net response is:" + response);
                if (response != null && response.contains(RESULT_AUTH_ERROR)) {
                    if (listener != null) listener.onError(RESULT_AUTH_ERROR);
                    return;
                }
                if (listener != null) listener.OnResponse(response);
            }
        };
    }

    private Response.ErrorListener getResponseErrorListener(final OnNetListener listener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "get network error." + error.getMessage());
                if (listener != null) listener.onError(error.getMessage());
            }
        };
    }

    public void login(String phoneNum, String password, final OnNetListener listener) {
        try {
            phoneNum = URLEncoder.encode(phoneNum, "UTF-8");
            password = URLEncoder.encode(password, "UTF-8");
        } catch (UnsupportedEncodingException une) {
            une.printStackTrace();
        }
        String url = BASE_URL + PATH_USER + PATH_LOGIN + "?phone=" + phoneNum + "&password=" + password;
        StringRequest request = new StringRequest(url, getResponseListener(listener),
                getResponseErrorListener(listener));
        mQueue.add(request);
    }

    public void registerUser(String name, String phoneNum, String password, final OnNetListener listener) {
        try {
            name = URLEncoder.encode(name, "UTF-8");
            phoneNum = URLEncoder.encode(phoneNum, "UTF-8");
            password = URLEncoder.encode(password, "UTF-8");
        } catch (UnsupportedEncodingException une) {
            une.printStackTrace();
        }
        String url = BASE_URL + PATH_USER + PATH_REGISTER + "?phone=" + phoneNum + "&name=" + name + "&password=" +
                password;
        StringRequest request = new StringRequest(url, getResponseListener(listener),
                getResponseErrorListener(listener));
        mQueue.add(request);
    }

    public void fetchMyGroup(int userId, String token, final OnNetListener listener) {
        try {
            token = URLEncoder.encode(token, "UTF-8");
        } catch (UnsupportedEncodingException une) {
            une.printStackTrace();
        }

        String url = BASE_URL + PATH_GROUP + PATH_MY_GROUP + "?uid=" + userId + "&token=" + token;
        StringRequest request = new StringRequest(url, getResponseListener(listener),
                getResponseErrorListener(listener));
        mQueue.add(request);
    }

    public void fetchBelongGroup(int userId, String token, final OnNetListener listener) {
        try {
            token = URLEncoder.encode(token, "UTF-8");
        } catch (UnsupportedEncodingException une) {
            une.printStackTrace();
        }

        String url = BASE_URL + PATH_GROUP + PATH_BELONG_GROUP + "?uid=" + userId + "&token=" + token;
        StringRequest request = new StringRequest(url, getResponseListener(listener),
                getResponseErrorListener(listener));
        mQueue.add(request);
    }

    public void createGroup(int userId, String token, String groupName, String introduction, final OnNetListener listener) {
        try {
            groupName = URLEncoder.encode(groupName, "UTF-8");
            token = URLEncoder.encode(token, "UTF-8");
        } catch (UnsupportedEncodingException une) {
            une.printStackTrace();
        }
        String url = BASE_URL + PATH_GROUP + PATH_GROUP_CREATE + "?uid=" + userId + "&token=" + token +
                "&gname=" + groupName;
        Map<String, String> body = new HashMap<String, String>(1);
        body.put("introduction", introduction);
        StringPostRequest request = new StringPostRequest(url, body,
                getResponseListener(listener),
                getResponseErrorListener(listener));
        mQueue.add(request);
    }
}
