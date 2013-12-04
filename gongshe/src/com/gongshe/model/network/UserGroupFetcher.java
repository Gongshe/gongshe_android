package com.gongshe.model.network;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gongshe.GongSheApp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gongshe.model.GongSheConstant.*;
import static com.gongshe.model.network.RequestUtil.getResponseErrorListener;
import static com.gongshe.model.network.RequestUtil.getResponseListener;

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

    public void login(String phoneNum, String password, OnNetListener listener) {
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

    public void registerUser(String name, String phoneNum, String password, OnNetListener listener) {
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

    public void fetchMyGroup(int userId, String token, OnNetListener listener) {
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

    public void fetchBelongGroup(int userId, String token, OnNetListener listener) {
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

    public void createGroup(int userId, String token, String groupName, String introduction, OnNetListener listener) {
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

    public void fetchFriendList(int userId, String token, OnNetListener listener) {
        try {
            token = URLEncoder.encode(token, "UTF-8");
        } catch (UnsupportedEncodingException une) {
            une.printStackTrace();
        }
        String url = BASE_URL + PATH_USER + PATH_FIND_FRIENDS + "?uid=" + userId + "&token=" + token;
        StringRequest request = new StringRequest(url, getResponseListener(listener),
                getResponseErrorListener(listener));
        mQueue.add(request);
    }

    public void findUserListByPhone(int userId, String token, List<String> phoneList, OnNetListener listener) {
        String url = BASE_URL + PATH_USER + PATH_FIND_USERLIST_BY_PHONE;
        Map<String, String> body = new HashMap<String, String>(3);
        body.put("uid", String.valueOf(userId));
        body.put("token", token);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String strPhoneList = objectMapper.writeValueAsString(phoneList);
            body.put("strPhoneList", strPhoneList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringPostRequest request = new StringPostRequest(url, body, getResponseListener(listener),
                getResponseErrorListener(listener));
        mQueue.add(request);
    }

    public void addFriendByPhone(int userId, String token, String friendPhone, OnNetListener listener) {
        try {
            token = URLEncoder.encode(token, "UTF-8");
        } catch (UnsupportedEncodingException une) {
            une.printStackTrace();
        }
        String url = BASE_URL + PATH_USER + PATH_ADD_FRIEND_BY_PHONE + "?uid=" + userId + "&token=" + token +
                "&friendPhone=" + friendPhone;
        StringRequest request = new StringRequest(url, getResponseListener(listener),
                getResponseErrorListener(listener));
        mQueue.add(request);
    }

    public void addFriendById(int userId, String token, int friendId, OnNetListener listener) {
        try {
            token = URLEncoder.encode(token, "UTF-8");
        } catch (UnsupportedEncodingException une) {
            une.printStackTrace();
        }
        String url = BASE_URL + PATH_USER + PATH_ADD_FRIEND_BY_ID + "?uid=" + userId + "&token=" + token +
                "&fid=" + friendId;
        StringRequest request = new StringRequest(url, getResponseListener(listener),
                getResponseErrorListener(listener));
        mQueue.add(request);
    }

    public void getGroupAllMember(int userId, String token, int groupId, OnNetListener listener) {
        try {
            token = URLEncoder.encode(token, "UTF-8");
        } catch (UnsupportedEncodingException une) {
            une.printStackTrace();
        }
        String url = BASE_URL + PATH_GROUP + PATH_GROUP_GET_ALL_MEMBER + "?uid=" + userId + "&token=" + token +
                "&gid=" + groupId;
        StringRequest request = new StringRequest(url, getResponseListener(listener),
                getResponseErrorListener(listener));
        mQueue.add(request);
    }


}
