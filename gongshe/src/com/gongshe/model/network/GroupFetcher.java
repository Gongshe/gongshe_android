package com.gongshe.model.network;

import com.android.volley.toolbox.StringRequest;
import com.gongshe.GongSheApp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static com.gongshe.model.GongSheConstant.*;
import static com.gongshe.model.network.RequestUtil.getResponseErrorListener;
import static com.gongshe.model.network.RequestUtil.getResponseListener;

public class GroupFetcher {
    private static final String TAG = GroupFetcher.class.getSimpleName();

    private GroupFetcher() {
    }

    public static void fetchMyGroup(int userId, String token, OnNetListener listener) {
        try {
            token = URLEncoder.encode(token, "UTF-8");
        } catch (UnsupportedEncodingException une) {
            une.printStackTrace();
        }
        String url = BASE_URL + PATH_GROUP + PATH_MY_GROUP + "?uid=" + userId + "&token=" + token;
        StringRequest request = new StringRequest(url, getResponseListener(listener),
                getResponseErrorListener(listener));
        GongSheApp.getInstance()
                  .getRequestQueue()
                  .add(request);
    }

    public static void fetchBelongGroup(int userId, String token, OnNetListener listener) {
        try {
            token = URLEncoder.encode(token, "UTF-8");
        } catch (UnsupportedEncodingException une) {
            une.printStackTrace();
        }
        String url = BASE_URL + PATH_GROUP + PATH_BELONG_GROUP + "?uid=" + userId + "&token=" + token;
        StringRequest request = new StringRequest(url, getResponseListener(listener),
                getResponseErrorListener(listener));
        GongSheApp.getInstance()
                  .getRequestQueue()
                  .add(request);
    }

    public static void createGroup(int userId, String token, String groupName, String introduction,
                                   OnNetListener listener) {
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
        GongSheApp.getInstance()
                  .getRequestQueue()
                  .add(request);
    }

    public static void getGroupAllMember(int userId, String token, int groupId, OnNetListener listener) {
        try {
            token = URLEncoder.encode(token, "UTF-8");
        } catch (UnsupportedEncodingException une) {
            une.printStackTrace();
        }
        String url = BASE_URL + PATH_GROUP + PATH_GROUP_GET_ALL_MEMBER + "?uid=" + userId + "&token=" + token +
                "&gid=" + groupId;
        StringRequest request = new StringRequest(url, getResponseListener(listener),
                getResponseErrorListener(listener));
        GongSheApp.getInstance()
                  .getRequestQueue()
                  .add(request);
    }

    public static void deleteGroupMember(int userId, String token, int groupId, int memberId, OnNetListener listener) {
        try {
            token = URLEncoder.encode(token, "UTF-8");
        } catch (UnsupportedEncodingException une) {
            une.printStackTrace();
        }
        String url = BASE_URL + PATH_GROUP + PATH_GROUP_DELETE_MEMBER + "?uid=" + userId + "&token=" + token +
                "&mid=" + memberId + "&gid=" + groupId;
        StringRequest request = new StringRequest(url, getResponseListener(listener),
                getResponseErrorListener(listener));
        GongSheApp.getInstance()
                  .getRequestQueue()
                  .add(request);
    }

}
