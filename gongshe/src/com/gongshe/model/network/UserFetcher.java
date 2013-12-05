package com.gongshe.model.network;

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

public class UserFetcher {

    private static final String TAG = UserFetcher.class.getSimpleName();

    private UserFetcher() {
    }

    public static void login(String phoneNum, String password, OnNetListener listener) {
        try {
            phoneNum = URLEncoder.encode(phoneNum, "UTF-8");
            password = URLEncoder.encode(password, "UTF-8");
        } catch (UnsupportedEncodingException une) {
            une.printStackTrace();
        }
        String url = BASE_URL + PATH_USER + PATH_LOGIN + "?phone=" + phoneNum + "&password=" + password;
        StringRequest request = new StringRequest(url, getResponseListener(listener),
                getResponseErrorListener(listener));
        GongSheApp.getInstance()
                  .getRequestQueue()
                  .add(request);
    }

    public static void registerUser(String name, String phoneNum, String password, OnNetListener listener) {
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
        GongSheApp.getInstance()
                  .getRequestQueue()
                  .add(request);
    }

    public static void findUserListByPhone(int userId, String token, List<String> phoneList, OnNetListener listener) {
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
        GongSheApp.getInstance()
                  .getRequestQueue()
                  .add(request);
    }

}
