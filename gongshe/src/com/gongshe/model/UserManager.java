package com.gongshe.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.gongshe.GongSheApp;
import com.gongshe.model.network.OnNetListener;
import com.gongshe.model.network.UserGroupFetcher;

import java.util.*;

public class UserManager {

    public interface OnDataChangeListener {
        public void onDataChanged();
    }

    private final static String FILE_KEY_USER_DATA = "file_for_user_data";
    private final static String DATA_KEY_USER = "user_data_key";
    private final static String MY_GROUP_LIST_KEY = "my_group_list_key";
    private final static String BELONG_GROUP_LIST_KEY = "belong_group_list_key";
    private final static String FRIEND_LIST_KEY = "friend_list_key";

    private static volatile UserManager sInstance;

    private UserManager() {
    } // do nothing.

    private SharedPreferences mPreference;
    private User mUser;
    private List<Group> mMyGroup;
    private List<Group> mBelongGroup;
    private Context mContext;
    private List<OnDataChangeListener> mUpdateListener;
    private List<User> mFriendList;
    private Map<String, List<User>> mGroupUserMap;

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
                    sInstance.mMyGroup = new ArrayList<Group>();
                    sInstance.mBelongGroup = new ArrayList<Group>();
                    sInstance.mFriendList = new ArrayList<User>();
                    sInstance.mGroupUserMap = new HashMap<String, List<User>>();
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
        List<Group> list = loadGroupList(MY_GROUP_LIST_KEY);
        if (list != null && !list.isEmpty()) {
            mMyGroup.clear();
            mMyGroup.addAll(list);
        }
        return mMyGroup;
    }

    public void saveMyGroup() {
        if (mMyGroup == null || mMyGroup.isEmpty()) return;
        saveGroupList(MY_GROUP_LIST_KEY);
    }

    public List<Group> getBelongGroup() {
        if (mBelongGroup != null && !mBelongGroup.isEmpty()) return mBelongGroup;
        List<Group> list = loadGroupList(BELONG_GROUP_LIST_KEY);
        if (list != null && !list.isEmpty()) {
            mBelongGroup.clear();
            mBelongGroup.addAll(list);
        }
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
                if (listener != null) listener.onUpdate();
            }

            @Override
            public void onError(String errorMessage) {
                if (listener != null) listener.onError();
            }
        };
    }

    public void logOut() {
        clearUserData();
        clearGroupData(MY_GROUP_LIST_KEY);
        clearGroupData(BELONG_GROUP_LIST_KEY);
        clearFriendData();
        mUser = null;
        mMyGroup.clear();
        mBelongGroup.clear();
        mFriendList.clear();
    }

    public void onAuthError() {
        logOut();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public Group findGroupById(int gid) {
        if (gid == -1) return GongSheConstant.ALL_ACTIVITY_GROUP;
        if (gid == -2) return GongSheConstant.ALL_AT_ME_GROUP;
        if (gid == -3) return GongSheConstant.ALL_INVOLVED_GROUP;

        for (Group group : mMyGroup) {
            if (group.getId() == gid) return group;
        }

        for (Group group : mBelongGroup) {
            if (group.getId() == gid) return group;
        }

        return null;
    }

    public List<User> getFriendList() {
        if (mFriendList != null && !mFriendList.isEmpty()) return mFriendList;
        String str = mPreference.getString(FRIEND_LIST_KEY, null);
        if (str == null || str.equals("")) return mFriendList;

        ObjectMapper objectMapper = new ObjectMapper();
        CollectionType type = objectMapper.getTypeFactory().constructCollectionType(List.class, User.class);
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
                mPreference.edit().putString(FRIEND_LIST_KEY, str).commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearFriendData() {
        mPreference.edit().putString(FRIEND_LIST_KEY, "").commit();
    }

    public interface OnFriendListUpdateListener {
        public void onFriendListUpdate();
    }

    private OnFriendListUpdateListener mOnFriendListUpdateListener;

    public void registerOnFriendListUpdateListener(OnFriendListUpdateListener listUpdateListener) {
        mOnFriendListUpdateListener = listUpdateListener;
    }

    private void notifyFriendListUpdate() {
        if (mOnFriendListUpdateListener != null) mOnFriendListUpdateListener.onFriendListUpdate();
    }

    public void fetchFriendList(final OnUpdateListener listener) {
        UserGroupFetcher.getsInstance().fetchFriendList(mUser.getId(), mUser.getToken(), new OnNetListener() {
            @Override
            public void OnResponse(String response) {
                ObjectMapper objectMapper = new ObjectMapper();
                CollectionType type = objectMapper.getTypeFactory().constructCollectionType(List.class, User.class);
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

    public void fetchUserListByPhone(List<String> phoneList,
                                     final List<User> resultList,
                                     final OnUpdateListener listener) {
        UserGroupFetcher.getsInstance()
                        .findUserListByPhone(mUser.getId(), mUser.getToken(), phoneList, new OnNetListener() {
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

    public void addFriendByPhone(String phone, OnUpdateListener listener) {
        UserGroupFetcher.getsInstance().addFriendByPhone(mUser.getId(), mUser.getToken(), phone,
                getStatusReturnListener(listener));
    }

    public void addFriendById(int friendId, OnUpdateListener listener) {
        UserGroupFetcher.getsInstance().addFriendById(mUser.getId(), mUser.getToken(), friendId,
                getStatusReturnListener(listener));
    }

    public List<User> getGroupMember(int groupId) {
        List<User> userList = mGroupUserMap.get(String.valueOf(groupId));
        if (userList == null) {
            userList = new ArrayList<User>();
            mGroupUserMap.put(String.valueOf(groupId), userList);
        }
        return userList;
    }

    public interface OnGroupMemberUpdateListener {
        public void onGroupMemberUpdate();
    }

    private OnGroupMemberUpdateListener mOnGroupMemberUpdateListener;

    public void registerOnGroupMemberUpdateListener(OnGroupMemberUpdateListener listener) {
        mOnGroupMemberUpdateListener = listener;
    }

    private void notifyGroupMemberUpdate() {
        if (mOnGroupMemberUpdateListener != null) mOnGroupMemberUpdateListener.onGroupMemberUpdate();
    }

    public void fetchGroupMember(final int groupId, final OnUpdateListener listener) {
        UserGroupFetcher.getsInstance().getGroupAllMember(mUser.getId(), mUser.getToken(), groupId, new OnNetListener() {
            @Override
            public void OnResponse(String response) {
                ObjectMapper objectMapper = new ObjectMapper();
                CollectionType type = objectMapper.getTypeFactory()
                                                  .constructCollectionType(List.class, User.class);
                try {
                    List<User> listMember = objectMapper.readValue(response.getBytes("UTF-8"), type);
                    if (listMember != null) {
                        List<User> originList = mGroupUserMap.get(String.valueOf(groupId));
                        if (originList != null) {
                            originList.clear();
                            originList.addAll(listMember);
                        } else {
                            mGroupUserMap.put(String.valueOf(groupId), listMember);
                        }
                        if (listener != null) listener.onUpdate();
                        notifyGroupMemberUpdate();
                        return;
                    }
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

    public void deleteGroupMember(final int groupId, final int memberId, final OnUpdateListener listener) {
        UserGroupFetcher.getsInstance().deleteGroupMember(mUser.getId(), mUser.getToken(), groupId, memberId,
                getStatusReturnListener(listener));
    }
}
