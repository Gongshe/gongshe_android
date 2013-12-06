package com.gongshe.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.gongshe.model.network.OnNetListener;
import com.gongshe.model.network.PostFetcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gongshe.model.network.RequestUtil.getStatusReturnListener;

public class PostManager {

    public interface OnPostListUpdateListener {
        public void onPostListUpdate();
    }

    private final static String TAG = PostManager.class.getSimpleName();

    private PostManager() {
    } // for singleton

    private static volatile PostManager sInstance;

    private Map<String, List<ClientPost>> mGroupPostMap;
    private Map<String, List<ClientPost>> mPostMap;
    private OnPostListUpdateListener mOnPostListUpdateListener;

    public static PostManager getInstance() {
        if (sInstance == null) {
            synchronized (PostManager.class) {
                if (sInstance == null) {
                    sInstance = new PostManager();
                    sInstance.mGroupPostMap = new HashMap<String, List<ClientPost>>();
                    sInstance.mPostMap = new HashMap<String, List<ClientPost>>();
                }
            }
        }
        return sInstance;
    }

    public List<ClientPost> getGroupPostList(Group group) {
        String key = String.valueOf(group.getId());
        List<ClientPost> postList = mGroupPostMap.get(key);
        if (postList == null) {
            postList = new ArrayList<ClientPost>();
            mGroupPostMap.put(key, postList);
        }
        return postList;
    }

    public List<ClientPost> getPostList(String postSignature) {
        List<ClientPost> postList = mPostMap.get(postSignature);
        if (postList == null) {
            postList = new ArrayList<ClientPost>();
            mPostMap.put(postSignature, postList);
        }
        return postList;
    }

    public void setOnPostListUpdateListener(OnPostListUpdateListener listener) {
        mOnPostListUpdateListener = listener;
    }

    private void notifyPostListChanged() {
        if (mOnPostListUpdateListener != null) mOnPostListUpdateListener.onPostListUpdate();
    }

    private OnNetListener getGroupPostListListener(final OnUpdateListener listener, final int groupId) {
        return new OnNetListener() {
            @Override
            public void OnResponse(String response) {
                ObjectMapper objectMapper = new ObjectMapper();
                CollectionType type = objectMapper.getTypeFactory()
                                                  .constructCollectionType(List.class, ClientPost.class);
                try {
                    List<ClientPost> listPost = objectMapper.readValue(response.getBytes("UTF-8"), type);
                    if (listPost != null) {
                        List<ClientPost> originList = mGroupPostMap.get(String.valueOf(groupId));
                        if (originList == null) {
                            mGroupPostMap.put(String.valueOf(groupId), listPost);
                        } else {
                            originList.clear();
                            originList.addAll(listPost);
                        }
                        if (listener != null) listener.onUpdate();
                        notifyPostListChanged();
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

    private OnNetListener getPostListListener(final OnUpdateListener listener, final String signature) {
        return new OnNetListener() {
            @Override
            public void OnResponse(String response) {
                ObjectMapper objectMapper = new ObjectMapper();
                CollectionType type = objectMapper.getTypeFactory()
                                                  .constructCollectionType(List.class, ClientPost.class);
                try {
                    List<ClientPost> listPost = objectMapper.readValue(response.getBytes("UTF-8"), type);
                    if (listPost != null) {
                        List<ClientPost> originList = mPostMap.get(signature);
                        if (originList == null) {
                            mPostMap.put(signature, listPost);
                        } else {
                            originList.clear();
                            originList.addAll(listPost);
                        }
                        if (listener != null) listener.onUpdate();
                        notifyPostListChanged();
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

    public void createPost(int groupId, String title, String content, OnUpdateListener listener) {
        User user = UserManager.getInstance()
                               .getUser();
        PostFetcher.getsInstance()
                   .createPost(user.getId(), user.getToken(), groupId, title, content,
                           getStatusReturnListener(listener));
    }

    public void replyPost(String content, String signature, OnUpdateListener listener) {
        User user = UserManager.getInstance()
                               .getUser();

        PostFetcher.getsInstance()
                   .replyPost(user.getId(), user.getToken(), content, signature,
                           getStatusReturnListener(listener));
    }

    public void getAllInGroup(int groupId, OnUpdateListener listener) {
        User user = UserManager.getInstance()
                               .getUser();
        PostFetcher.getsInstance()
                   .getAllInGroup(user.getId(), user.getToken(), groupId,
                           getGroupPostListListener(listener, groupId));
    }

    public void getAllOfSameTitle(String signature, OnUpdateListener listener) {
        User user = UserManager.getInstance()
                               .getUser();
        PostFetcher.getsInstance()
                   .getAllOfSameTitle(user.getId(), user.getToken(), signature,
                           getPostListListener(listener, signature));
    }

    public void getRecentPosts(OnUpdateListener listener) {
        User user = UserManager.getInstance()
                               .getUser();
        PostFetcher.getsInstance()
                   .getRecentPosts(user.getId(), user.getToken(), getGroupPostListListener(listener,
                           GongSheConstant.ALL_ACTIVITY_GROUP
                                          .getId()));
    }

    public void getAllInvolved(OnUpdateListener listener) {
        User user = UserManager.getInstance()
                               .getUser();
        PostFetcher.getsInstance()
                   .getAllInvolved(user.getId(), user.getToken(), getGroupPostListListener(listener,
                           GongSheConstant.ALL_INVOLVED_GROUP
                                          .getId()));
    }

}
