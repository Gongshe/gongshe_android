package com.gongshe.model.network;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.gongshe.GongSheApp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static com.gongshe.model.GongSheConstant.*;
import static com.gongshe.model.network.RequestUtil.getResponseErrorListener;
import static com.gongshe.model.network.RequestUtil.getResponseListener;

public class PostFetcher {
    private static final String TAG = PostFetcher.class.getSimpleName();

    private static volatile PostFetcher sInstance;
    private RequestQueue mQueue;

    private PostFetcher() {
    } // for singleton

    public static PostFetcher getsInstance() {
        if (sInstance == null) {
            synchronized (PostFetcher.class) {
                if (sInstance == null) {
                    sInstance = new PostFetcher();
                    sInstance.mQueue = GongSheApp.getInstance()
                                                 .getRequestQueue();
                }
            }
        }
        return sInstance;
    }

    public void createPost(int userId, String token, int groupId, String title, String content,
                           OnNetListener listener) {
        String url = BASE_URL + PATH_POST + PATH_POST_CREATE;
        Map<String, String> body = new HashMap<String, String>(5);
        body.put("uid", String.valueOf(userId));
        body.put("gid", String.valueOf(groupId));
        body.put("token", token);
        body.put("title", title);
        body.put("content", content);

        StringPostRequest request = new StringPostRequest(url, body,
                getResponseListener(listener),
                getResponseErrorListener(listener));
        mQueue.add(request);
    }

    public void replyPost(int userId, String token, String content, String signature, OnNetListener listener) {
        String url = BASE_URL + PATH_POST + PATH_POST_REPLY;
        Map<String, String> body = new HashMap<String, String>(4);
        body.put("uid", String.valueOf(userId));
        body.put("token", token);
        body.put("content", content);
        body.put("signature", signature);

        StringPostRequest request = new StringPostRequest(url, body, getResponseListener(listener),
                getResponseErrorListener(listener));
        mQueue.add(request);
    }

    public void getAllInGroup(int userId, String token, int groupId, OnNetListener listener) {
        try {
            token = URLEncoder.encode(token, "UTF-8");
        } catch (UnsupportedEncodingException une) {
            une.printStackTrace();
        }
        String url = BASE_URL + PATH_POST + PATH_POST_ALL_IN_GROUP + "?uid=" + userId + "&token=" + token +
                "&gid=" + groupId;

        StringRequest request = new StringRequest(url, getResponseListener(listener),
                getResponseErrorListener(listener));
        mQueue.add(request);
    }

    public void getAllOfSameTitle(int userId, String token, String signature, OnNetListener listener) {
        try {
            token = URLEncoder.encode(token, "UTF-8");
            signature = URLEncoder.encode(signature, "UTF-8");
        } catch (UnsupportedEncodingException une) {
            une.printStackTrace();
        }
        String url = BASE_URL + PATH_POST + PATH_POST_FINDALLOFSAMETITLE + "?uid=" + userId + "&token=" + token +
                "&signature=" + signature;
        StringRequest request = new StringRequest(url, getResponseListener(listener),
                getResponseErrorListener(listener));
        mQueue.add(request);
    }

    public void getRecentPosts(int userId, String token, OnNetListener listener) {
        try {
            token = URLEncoder.encode(token, "UTF-8");
        } catch (UnsupportedEncodingException une) {
            une.printStackTrace();
        }
        String url = BASE_URL + PATH_POST + PATH_POST_FINDRECENTPOSTS + "?uid=" + userId + "&token=" + token;
        StringRequest request = new StringRequest(url, getResponseListener(listener),
                getResponseErrorListener(listener));
        mQueue.add(request);
    }

    public void getAllInvolved(int userId, String token, OnNetListener listener) {
        try {
            token = URLEncoder.encode(token, "UTF-8");
        } catch (UnsupportedEncodingException une) {
            une.printStackTrace();
        }
        String url = BASE_URL + PATH_POST + PATH_POST_FINDALLINVOLVED + "?uid=" + userId + "&token=" + token;
        StringRequest request = new StringRequest(url, getResponseListener(listener),
                getResponseErrorListener(listener));
        mQueue.add(request);
    }

}
