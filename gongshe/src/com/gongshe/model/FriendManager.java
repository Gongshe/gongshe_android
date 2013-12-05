package com.gongshe.model;

import android.content.Context;
import android.content.SharedPreferences;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.gongshe.GongSheApp;
import com.gongshe.model.network.FriendFetcher;
import com.gongshe.model.network.OnNetListener;
import com.gongshe.model.network.UserFetcher;

import java.util.ArrayList;
import java.util.List;

import static com.gongshe.model.GongSheConstant.FILE_KEY_USER_DATA;
import static com.gongshe.model.network.RequestUtil.getStatusReturnListener;

public class FriendManager {

    public interface OnFriendListUpdateListener {
        public void onFriendListUpdate();
    }

    private final static String TAG = FriendManager.class.getSimpleName();
    private final static String FRIEND_LIST_KEY = "friend_list_key";

    private static volatile FriendManager sInstance;

    private SharedPreferences mPreference;
    private Context mContext;
    private List<User> mFriendList;
    private OnFriendListUpdateListener mOnFriendListUpdateListener;

    private FriendManager() {
    }

    public static FriendManager getInstance() {
        if (sInstance == null) {
            synchronized (FriendManager.class) {
                if (sInstance == null) {
                    sInstance = new FriendManager();
                    sInstance.mContext = GongSheApp.getInstance()
                                                   .getContext();
                    sInstance.mPreference = sInstance.mContext
                                                     .getSharedPreferences(FILE_KEY_USER_DATA, Context.MODE_PRIVATE);
                    sInstance.mFriendList = new ArrayList<User>();
                }
            }
        }
        return sInstance;
    }

    public void registerOnFriendListUpdateListener(OnFriendListUpdateListener listUpdateListener) {
        mOnFriendListUpdateListener = listUpdateListener;
    }

    private void notifyFriendListUpdate() {
        if (mOnFriendListUpdateListener != null) mOnFriendListUpdateListener.onFriendListUpdate();
    }

    public List<User> getFriendList() {
        if (mFriendList != null && !mFriendList.isEmpty()) return mFriendList;

        String str = mPreference.getString(FRIEND_LIST_KEY, null);
        if (str == null || str.equals("")) return mFriendList;

        ObjectMapper objectMapper = new ObjectMapper();
        CollectionType type = objectMapper.getTypeFactory()
                                          .constructCollectionType(List.class, User.class);
        try {
            List<User> list = objectMapper.readValue(str.getBytes("UTF-8"), type);
            if (list != null && !list.isEmpty()) {
                mFriendList.clear();
                mFriendList.addAll(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mFriendList;
    }

    public void saveFriendList() {
        if (mFriendList == null || mFriendList.isEmpty()) return;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String str = objectMapper.writeValueAsString(mFriendList);
            if (str != null && !str.equals("")) {
                mPreference.edit()
                           .putString(FRIEND_LIST_KEY, str)
                           .commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearFriendData() {
        mPreference.edit()
                   .putString(FRIEND_LIST_KEY, "")
                   .commit();
    }

    public void onLogOut() {
        clearFriendData();
        mFriendList.clear();
    }

    /**
     * ** Following is network operations ***
     */

    public void addFriendByPhone(String phone, OnUpdateListener listener) {
        User user = UserManager.getInstance()
                               .getUser();
        FriendFetcher.addFriendByPhone(user.getId(), user.getToken(), phone, getStatusReturnListener(listener));
    }

    public void addFriendById(int friendId, OnUpdateListener listener) {
        User user = UserManager.getInstance()
                               .getUser();
        FriendFetcher.addFriendById(user.getId(), user.getToken(), friendId, getStatusReturnListener(listener));
    }

    public void fetchFriendList(final OnUpdateListener listener) {
        User user = UserManager.getInstance()
                               .getUser();
        FriendFetcher.fetchFriendList(user.getId(), user.getToken(), new OnNetListener() {
            @Override
            public void OnResponse(String response) {
                ObjectMapper objectMapper = new ObjectMapper();
                CollectionType type = objectMapper.getTypeFactory()
                                                  .constructCollectionType(List.class, User.class);
                try {
                    List<User> list = objectMapper.readValue(response.getBytes("UTF-8"), type);
                    if (list != null && !list.isEmpty()) {
                        mFriendList.clear();
                        mFriendList.addAll(list);
                        saveFriendList();
                        if (listener != null) listener.onUpdate();
                        notifyFriendListUpdate();
                    }
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
