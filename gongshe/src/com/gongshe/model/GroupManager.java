package com.gongshe.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.gongshe.GongSheApp;
import com.gongshe.model.network.GroupFetcher;
import com.gongshe.model.network.OnNetListener;
import com.gongshe.model.network.UserFetcher;

import java.util.*;

import static com.gongshe.model.GongSheConstant.FILE_KEY_USER_DATA;
import static com.gongshe.model.network.RequestUtil.getStatusReturnListener;

public class GroupManager {

    public interface OnGroupMemberUpdateListener {
        public void onGroupMemberUpdate();
    }

    public interface OnGroupListUpdateListener {
        public void onGroupListUpdate();
    }

    private final static String TAG = GroupManager.class.getSimpleName();
    private final static String MY_GROUP_LIST_KEY = "my_group_list_key";
    private final static String BELONG_GROUP_LIST_KEY = "belong_group_list_key";

    private static volatile GroupManager sInstance;

    private SharedPreferences mPreference;
    private Context mContext;

    private List<Group> mMyGroup;
    private List<Group> mBelongGroup;
    private Map<String, List<User>> mGroupUserMap;

    private OnGroupMemberUpdateListener mOnGroupMemberUpdateListener;
    private OnGroupListUpdateListener mOnGroupListUpdateListener;

    private GroupManager() {
    }

    public static GroupManager getInstance() {
        if (sInstance == null) {
            synchronized (GroupManager.class) {
                if (sInstance == null) {
                    sInstance = new GroupManager();
                    sInstance.mContext = GongSheApp.getInstance()
                                                   .getContext();
                    sInstance.mPreference = sInstance.mContext
                                                     .getSharedPreferences(FILE_KEY_USER_DATA, Context.MODE_PRIVATE);
                    sInstance.mMyGroup = new ArrayList<Group>();
                    sInstance.mBelongGroup = new ArrayList<Group>();
                    sInstance.mGroupUserMap = new HashMap<String, List<User>>();
                }
            }
        }
        return sInstance;
    }

    public List<User> getGroupMember(int groupId) {
        List<User> userList = mGroupUserMap.get(String.valueOf(groupId));
        if (userList == null) {
            userList = new ArrayList<User>();
            mGroupUserMap.put(String.valueOf(groupId), userList);
        }
        return userList;
    }

    public void setGroupListUpdateListener(OnGroupListUpdateListener listener) {
        mOnGroupListUpdateListener = listener;
    }

    private void notifyDataChangeListener() {
        if (mOnGroupListUpdateListener != null) mOnGroupListUpdateListener.onGroupListUpdate();
    }

    public void setOnGroupMemberUpdateListener(OnGroupMemberUpdateListener listener) {
        mOnGroupMemberUpdateListener = listener;
    }

    private void notifyGroupMemberUpdate() {
        if (mOnGroupMemberUpdateListener != null) mOnGroupMemberUpdateListener.onGroupMemberUpdate();
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

    private void clearGroupData(String key) {
        mPreference.edit()
                   .putString(key, "")
                   .commit();
    }

    public void onLogOut() {
        clearGroupData(MY_GROUP_LIST_KEY);
        clearGroupData(BELONG_GROUP_LIST_KEY);
        mMyGroup.clear();
        mBelongGroup.clear();
        mMyGroup.clear();
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

    /**
     * * Following is network operation.
     */

    public void updateMyGroup(final OnUpdateListener listener) {
        User user = UserManager.getInstance()
                               .getUser();
        GroupFetcher.fetchMyGroup(user.getId(), user.getToken(), getUpdateGroupListListener(MY_GROUP_LIST_KEY,
                listener));
    }

    public void updateBelongGroup(final OnUpdateListener listener) {
        User user = UserManager.getInstance()
                               .getUser();
        GroupFetcher.fetchBelongGroup(user.getId(), user.getToken(),
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
        User user = UserManager.getInstance()
                               .getUser();
        GroupFetcher.createGroup(user.getId(), user.getToken(), groupName, introduction,
                getStatusReturnListener(listener));
    }

    public void deleteGroupMember(final int groupId, final int memberId, final OnUpdateListener listener) {
        User user = UserManager.getInstance()
                               .getUser();
        GroupFetcher.deleteGroupMember(user.getId(), user.getToken(), groupId, memberId,
                getStatusReturnListener(listener));
    }

    public void fetchGroupMember(final int groupId, final OnUpdateListener listener) {
        User user = UserManager.getInstance()
                               .getUser();
        GroupFetcher.getGroupAllMember(user.getId(), user.getToken(), groupId, new OnNetListener() {
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

}
