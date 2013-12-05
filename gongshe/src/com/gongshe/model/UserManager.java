package com.gongshe.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.gongshe.GongSheApp;
import com.gongshe.model.network.OnNetListener;
import com.gongshe.model.network.UserFetcher;

import java.util.List;

import static com.gongshe.model.GongSheConstant.FILE_KEY_USER_DATA;

public class UserManager {

    public interface OnUserInfoUpdateListener {
        public void OnUserInfoUpdate();
    }

    private final static String TAG = UserManager.class.getSimpleName();
    private final static String DATA_KEY_USER = "user_data_key";

    private static volatile UserManager sInstance;

    private SharedPreferences mPreference;
    private Context mContext;
    private User mUser;

    private OnUserInfoUpdateListener mOnUserInfoUpdateListener;

    private UserManager() {
    } // do nothing.

    public static UserManager getInstance() {
        if (sInstance == null) {
            synchronized (UserManager.class) {
                if (sInstance == null) {
                    sInstance = new UserManager();
                    sInstance.mContext = GongSheApp.getInstance()
                                                   .getContext();
                    sInstance.mPreference = sInstance.mContext
                                                     .getSharedPreferences(FILE_KEY_USER_DATA, Context.MODE_PRIVATE);


                }
            }
        }
        return sInstance;
    }

    public void setOnUserInfoUpdateListener(OnUserInfoUpdateListener listener) {
        mOnUserInfoUpdateListener = listener;
    }

    private void notifyUserInfoUpdate() {
        if (mOnUserInfoUpdateListener != null) mOnUserInfoUpdateListener.OnUserInfoUpdate();
    }

    public User getUser() {
        if (mUser != null && mUser.isValidUser()) {
            return mUser;
        }
        loadUserData();
        return mUser;
    }

    private void loadUserData() {
        String userString = mPreference.getString(DATA_KEY_USER, null);
        if (userString == null) return;

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            User user = objectMapper.readValue(userString.getBytes("UTF-8"), User.class);
            if (user != null && user.isValidUser()) {
                mUser = user;
                Log.d(TAG, "load user=" + mUser.toString());
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            clearUserData();
        }
    }

    private void clearUserData() {
        mPreference.edit()
                   .putString(DATA_KEY_USER, "")
                   .commit();
    }

    private void saveUserData() {
        if (mUser == null || !mUser.isValidUser()) return;

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String str = objectMapper.writeValueAsString(mUser);
            Log.d(TAG, "save user :" + str);
            mPreference.edit()
                       .putString(DATA_KEY_USER, str)
                       .commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    private OnNetListener getUpdateUserListener(final OnUpdateListener listener) {
        return new OnNetListener() {
            @Override
            public void OnResponse(String response) {
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    User user = objectMapper.readValue(response.getBytes("UTF-8"), User.class);
                    if (user != null && user.isValidUser()) {
                        mUser = user;
                        Log.d(TAG, "login user=" + mUser.toString());
                        saveUserData();
                        if (listener != null) listener.onUpdate();
                        notifyUserInfoUpdate();
                        return;
                    }
                } catch (Exception e) {
                    Log.e(TAG, "login user return data error. got:" + response);
                    e.printStackTrace();
                }
                if (listener != null) listener.onError();
            }

            @Override
            public void onError(String errorMessage) {
                if (listener != null) listener.onError();
            }
        };
    }

    public void login(String phoneNum, String password, final OnUpdateListener listener) {
        UserFetcher.login(phoneNum, password, getUpdateUserListener(listener));
    }

    public void registerUser(String name, String phoneNum, String password, final OnUpdateListener listener) {
        UserFetcher.registerUser(name, phoneNum, password, getUpdateUserListener(listener));
    }

    public void logOut() {
        mUser = null;
        clearUserData();
        GroupManager.getInstance()
                    .onLogOut();
        FriendManager.getInstance()
                     .onLogOut();
    }

    public void onAuthError() {
        logOut();
        android.os
                .Process
                .killProcess(android.os
                        .Process
                        .myPid());
    }

    public void fetchUserListByPhone(List<String> phoneList,
                                     final List<User> resultList,
                                     final OnUpdateListener listener) {
        UserFetcher.findUserListByPhone(mUser.getId(), mUser.getToken(), phoneList, new OnNetListener() {
            @Override
            public void OnResponse(String response) {
                ObjectMapper objectMapper = new ObjectMapper();
                CollectionType type = objectMapper.getTypeFactory()
                                                  .constructCollectionType(List.class, User.class);
                try {
                    List<User> list = objectMapper.readValue(response.getBytes("UTF-8"), type);
                    if (list != null && !list.isEmpty()) {
                        resultList.clear();
                        resultList.addAll(list);
                    }
                    if (listener != null) listener.onUpdate();
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (listener != null) listener.onError();
            }

            @Override
            public void onError(String errorMessage) {
                if (listener != null) listener.onError();
            }
        });
    }


}
