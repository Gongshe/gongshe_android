package com.gongshe.model;

import android.content.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.gongshe.GongSheApp;
import com.gongshe.model.network.OnNetListener;
import com.gongshe.model.network.PostFetcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostManager {
    private final static String TAG = PostManager.class.getSimpleName();

    public interface OnPostListUpdateListener {
        public void onPostListUpdate();
    }

    private PostManager() {
    } // for singleton

    private static volatile PostManager sInstance;
    private Context mContext;

    private Map<String, List<Post>> mGroupPostMap;
    private Map<String, List<Post>> mPostMap;
    private OnPostListUpdateListener mOnPostListUpdateListener;

    public static PostManager getInstance() {
        if (sInstance == null) {
            synchronized (PostManager.class) {
                if (sInstance == null) {
                    sInstance = new PostManager();
                    sInstance.mContext = GongSheApp.getInstance()
                                                   .getContext();
                    sInstance.mGroupPostMap = new HashMap<String, List<Post>>();
                    sInstance.mPostMap = new HashMap<String, List<Post>>();
                }
            }
        }
        return sInstance;
    }

    public List<Post> getGroupList(Group group) {
        String key = String.valueOf(group.getId());
        List<Post> postList = mGroupPostMap.get(key);
        if (postList == null) {
            postList = new ArrayList<Post>();
            mGroupPostMap.put(key, postList);
        }
        return postList;
    }

    public List<Post> getPostList(String postSignature) {
        List<Post> postList = mPostMap.get(postSignature);
        if (postList == null) {
            postList = new ArrayList<Post>();
            mPostMap.put(postSignature, postList);
        }
        return postList;
    }

    public void registerOnPostListUpdateListener(OnPostListUpdateListener listener) {
            mOnPostListUpdateListener = listener;
    }

    private void notifyPostListChanged() {
        if (mOnPostListUpdateListener != null) mOnPostListUpdateListener.onPostListUpdate();
    }

    private OnNetListener getPostStatusListener(final OnUpdateListener listener) {
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

    private OnNetListener getGroupPostListListener(final OnUpdateListener listener, final int groupId) {
        return new OnNetListener() {
            @Override
            public void OnResponse(String response) {
                ObjectMapper objectMapper = new ObjectMapper();
                CollectionType type = objectMapper.getTypeFactory()
                                                  .constructCollectionType(List.class, Post.class);
                try {
                    List<Post> listPost = objectMapper.readValue(response.getBytes("UTF-8"), type);
                    if (listPost != null) {
                        List<Post> originList = mGroupPostMap.get(String.valueOf(groupId));
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
                                                  .constructCollectionType(List.class, Post.class);
                try {
                    List<Post> listPost = objectMapper.readValue(response.getBytes("UTF-8"), type);
                    if (listPost != null) {
                        List<Post> originList = mPostMap.get(signature);
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
                           getPostStatusListener(listener));
    }

    public void replyPost(String content, String signature, OnUpdateListener listener) {
        User user = UserManager.getInstance()
                               .getUser();

        PostFetcher.getsInstance()
                   .replyPost(user.getId(), user.getToken(), content, signature,
                           getPostStatusListener(listener));
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
