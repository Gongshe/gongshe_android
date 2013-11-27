package com.gongshe.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.gongshe.GongSheApp;

import static com.gongshe.model.GongSheConstant.*;

import com.gongshe.model.network.OnNetListener;
import com.gongshe.model.network.UserGroupFetcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserManager {

    public interface OnUpdateListener {
        public void onUpdate();

        public void onError();
    }

    public interface OnDataChangeListener {
        public void onDataChanged();
    }

    private final static String FILE_KEY_USER_DATA = "file_for_user_data";
    private final static String DATA_KEY_USER = "user_data_key";
    private final static String MY_GROUP_LIST_KEY = "my_group_list_key";
    private final static String BELONG_GROUP_LIST_KEY = "belong_group_list_key";

    private static volatile UserManager sInstance;

    private UserManager() {
    } // do nothing.

    private SharedPreferences mPreference;
    private User mUser;
    private List<Group> mMyGroup;
    private List<Group> mBelongGroup;
    private Context mContext;
    private List<OnDataChangeListener> mUpdateListener;

    private final static String TAG = UserManager.class.getSimpleName();

    public static UserManager getInstance() {
        if (sInstance == null) {
            synchronized (UserManager.class) {
                if (sInstance == null) {
                    sInstance = new UserManager();
                    sInstance.mContext = GongSheApp.getInstance()
                                                   .getContext();
                    sInstance.mPreference = sInstance.mContext
                                                     .getSharedPreferences(FILE_KEY_USER_DATA, Context.MODE_PRIVATE);
                    sInstance.mUpdateListener = new ArrayList<OnDataChangeListener>();
                }
            }
        }
        return sInstance;
    }

    public void registerDataChangeListener(OnDataChangeListener listener) {
        if (listener == null) return;
        if (!mUpdateListener.contains(listener)) {
            mUpdateListener.add(listener);
        }
        return;
    }

    public void unRegisterDataChangeListener(OnDataChangeListener listener) {
        if (listener == null) return;
        mUpdateListener.remove(listener);
    }

    private void notifyDataChangeListener() {
        for (OnDataChangeListener listener : mUpdateListener) {
            listener.onDataChanged();
        }
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
            mUser = objectMapper.readValue(userString.getBytes("UTF-8"), User.class);
            if (mUser != null && mUser.isValidUser()) {
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

    private void clearGroupData(String key) {
        mPreference.edit()
                   .putString(key, "")
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

    public List<Group> getMyGroup() {
        if (mMyGroup != null && !mMyGroup.isEmpty()) return mMyGroup;
        mMyGroup = loadGroupList(MY_GROUP_LIST_KEY);
        return mMyGroup;
    }

    public void saveMyGroup() {
        if (mMyGroup == null || mMyGroup.isEmpty()) return;
        saveGroupList(MY_GROUP_LIST_KEY);
    }

    public List<Group> getBelongGroup() {
        if (mBelongGroup != null && !mBelongGroup.isEmpty()) return mBelongGroup;
        mBelongGroup = loadGroupList(BELONG_GROUP_LIST_KEY);
        return mBelongGroup;
    }

    public void saveBelongGroup() {
        if (mBelongGroup == null || mBelongGroup.isEmpty()) return;
        saveGroupList(BELONG_GROUP_LIST_KEY);
    }

    private List<Group> loadGroupList(String key) {
        String str = mPreference.getString(key, null);
        if (str == null || str.equals("")) return Collections.EMPTY_LIST;

        ObjectMapper objectMapper = new ObjectMapper();
        CollectionType type = objectMapper.getTypeFactory()
                                          .constructCollectionType(List.class, Group.class);
        try {
            List<Group> listGroup = objectMapper.readValue(str.getBytes("UTF-8"), type);
            Log.d(TAG, "read my group:" + str);
            if (listGroup == null || listGroup.isEmpty()) return Collections.EMPTY_LIST;
            return listGroup;
        } catch (Exception e) {
            e.printStackTrace();
            clearGroupData(key);
        }
        return Collections.EMPTY_LIST;
    }

    private void saveGroupList(String key) {
        ObjectMapper objectMapper = new ObjectMapper();
        String str = null;
        try {
            if (key.equals(MY_GROUP_LIST_KEY)) {
                str = objectMapper.writeValueAsString(mMyGroup);
            } else if (key.equals(BELONG_GROUP_LIST_KEY)) {
                str = objectMapper.writeValueAsString(mBelongGroup);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (str != null && str != "") {
            Log.d(TAG, key + "is saved." + str);
            mPreference.edit()
                       .putString(key, str)
                       .commit();
        }
    }

    private OnNetListener getUpdateUserListener(final OnUpdateListener listener) {
        return new OnNetListener() {
            @Override
            public void OnResponse(String response) {
                if (response == null || response.equals("")) {
                    if (listener != null) listener.onError();
                    return;
                }

                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    User user = objectMapper.readValue(response.getBytes("UTF-8"), User.class);
                    if (user != null && user.isValidUser()) {
                        mUser = user;
                        Log.d(TAG, "login user=" + mUser.toString());
                        saveUserData();
                        if (listener != null) listener.onUpdate();
                        notifyDataChangeListener();
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
        UserGroupFetcher.getsInstance()
                        .login(phoneNum, password, getUpdateUserListener(listener));
    }

    public void registerUser(String name, String phoneNum, String password, final OnUpdateListener listener) {
        UserGroupFetcher.getsInstance()
                        .registerUser(name, phoneNum, password, getUpdateUserListener(listener));
    }

    public void updateMyGroup(final OnUpdateListener listener) {
        UserGroupFetcher.getsInstance()
                        .fetchMyGroup(mUser.getId(), mUser.getToken(),
                                getUpdateGroupListListener(MY_GROUP_LIST_KEY, listener));
    }

    public void updateBelongGroup(final OnUpdateListener listener) {
        UserGroupFetcher.getsInstance()
                        .fetchBelongGroup(mUser.getId(), mUser.getToken(),
                                getUpdateGroupListListener(BELONG_GROUP_LIST_KEY, listener));
    }

    private OnNetListener getUpdateGroupListListener(final String key, final OnUpdateListener listener) {
        return new OnNetListener() {
            @Override
            public void OnResponse(String response) {
                if (response == null || response.equals("")) {
                    if (listener != null) listener.onError();
                    return;
                }

                ObjectMapper objectMapper = new ObjectMapper();
                CollectionType type = objectMapper.getTypeFactory()
                                                  .constructCollectionType(List.class, Group.class);
                try {
                    List<Group> listGroup = objectMapper.readValue(response.getBytes("UTF-8"), type);
                    if (listGroup != null) {
                        if (key.equals(MY_GROUP_LIST_KEY)) {
                            mMyGroup.clear();
                            mMyGroup.addAll(listGroup);
                        } else if (key.equals(BELONG_GROUP_LIST_KEY)) {
                            mBelongGroup.clear();
                            mBelongGroup.addAll(listGroup);
                        }
                        saveGroupList(key);
                        if (listener != null) listener.onUpdate();
                        notifyDataChangeListener();
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (listener != null) listener.onError();
            }

            @Override
            public void onError(String errorMessage) {
                if (errorMessage.equals(RESULT_AUTH_ERROR)) {
                    mUser = null;
                    // TODO: clear data here, need the user to log in again.
                    clearUserData();
                }
                if (listener != null) listener.onError();
            }
        };
    }

    public void createGroup(String groupName, String introduction, final OnUpdateListener listener) {
        int userId = mUser.getId();
        String token = mUser.getToken();
        UserGroupFetcher.getsInstance()
                        .createGroup(userId, token, groupName, introduction, getStatusReturnListener(listener));
    }

    private OnNetListener getStatusReturnListener(final OnUpdateListener listener) {
        return new OnNetListener() {
            @Override
            public void OnResponse(String response) {
                if (response == null || response.equals("") || response.equals(GongSheConstant.RESULT_FAIL)) {
                    if (listener != null) listener.onError();
                    return;
                }
                if (listener != null) listener.onUpdate();
                UserGroupFetcher.getsInstance().fetchMyGroup(mUser.getId(), mUser.getToken(),
                        getUpdateGroupListListener(MY_GROUP_LIST_KEY, listener));
            }

            @Override
            public void onError(String errorMessage) {
                if (listener != null) listener.onError();
            }
        };
    }

}
