package com.gongshe.model.network;

import com.android.volley.toolbox.StringRequest;
import com.gongshe.GongSheApp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static com.gongshe.model.GongSheConstant.*;
import static com.gongshe.model.network.RequestUtil.getResponseErrorListener;
import static com.gongshe.model.network.RequestUtil.getResponseListener;

public class FriendFetcher {
    private static final String TAG = FriendFetcher.class.getSimpleName();

    private FriendFetcher() {
    }

    public static void addFriendByPhone(int userId, String token, String friendPhone, OnNetListener listener) {
        try {
            token = URLEncoder.encode(token, "UTF-8");
        } catch (UnsupportedEncodingException une) {
            une.printStackTrace();
        }
        String url = BASE_URL + PATH_USER + PATH_ADD_FRIEND_BY_PHONE + "?uid=" + userId + "&token=" + token +
                "&friendPhone=" + friendPhone;
        StringRequest request = new StringRequest(url, getResponseListener(listener),
                getResponseErrorListener(listener));
        GongSheApp.getInstance()
                  .getRequestQueue()
                  .add(request);
    }

    public static void addFriendById(int userId, String token, int friendId, OnNetListener listener) {
        try {
            token = URLEncoder.encode(token, "UTF-8");
        } catch (UnsupportedEncodingException une) {
            une.printStackTrace();
        }
        String url = BASE_URL + PATH_USER + PATH_ADD_FRIEND_BY_ID + "?uid=" + userId + "&token=" + token +
                "&fid=" + friendId;
        StringRequest request = new StringRequest(url, getResponseListener(listener),
                getResponseErrorListener(listener));
        GongSheApp.getInstance()
                  .getRequestQueue()
                  .add(request);
    }

    public static void fetchFriendList(int userId, String token, OnNetListener listener) {
        try {
            token = URLEncoder.encode(token, "UTF-8");
        } catch (UnsupportedEncodingException une) {
            une.printStackTrace();
        }
        String url = BASE_URL + PATH_USER + PATH_FIND_FRIENDS + "?uid=" + userId + "&token=" + token;
        StringRequest request = new StringRequest(url, getResponseListener(listener),
                getResponseErrorListener(listener));
        GongSheApp.getInstance()
                  .getRequestQueue()
                  .add(request);
    }

}
