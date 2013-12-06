package com.gongshe.controller;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.gongshe.R;
import com.gongshe.model.*;

import java.util.List;

public class ContentFrameFragment extends Fragment {

    private final String TAG = ContentFrameFragment.class.getSimpleName();

    private Activity mActivity;

    private enum TYPE {
        GROUP,
        POST;
    }

    private Group mGroup;
    private Post mPost;
    private TYPE mType;

    private PostAdapter mAdapter;
    private ListView mListView;

    private PostManager.OnPostListUpdateListener mOnNormalUpdateListener = new PostManager.OnPostListUpdateListener() {
        @Override
        public void onPostListUpdate() {
            if (mAdapter != null) mAdapter.notifyDataSetChanged();
        }
    };

    public void setGroupContent(Group group, boolean force) {
        if (group != null && (!group.equals(mGroup) || force)) {
            mType = TYPE.GROUP;
            mGroup = group;
            mPost = null;

            setupCardsList();
            PostManager.getInstance()
                       .setOnPostListUpdateListener(mOnNormalUpdateListener);
            if (group.equals(GongSheConstant.ALL_ACTIVITY_GROUP)) {
                PostManager.getInstance()
                           .getRecentPosts(null);
            } else if (group.equals(GongSheConstant.ALL_INVOLVED_GROUP)) {
                PostManager.getInstance()
                        .getAllInvolved(null);
            } else if (group.equals(GongSheConstant.ALL_AT_ME_GROUP)) {

            } else {
                PostManager.getInstance()
                           .getAllInGroup(group.getId(), null);
            }
        }
    }

    private void setupCardsList() {
        List<ClientPost> postList = null;
        if (mType == TYPE.GROUP) {
            postList = PostManager.getInstance().getGroupPostList(mGroup);
        } else if (mType == TYPE.POST) {
            postList = PostManager.getInstance().getPostList(mPost.getSignature());
        }
        mAdapter = new PostAdapter(mActivity, postList);
        mListView.setAdapter(mAdapter);
    }


    public String getContentName()  {
        if (mType == TYPE.GROUP) {
            return mGroup.getName();
        }
        return mPost.getTitle();
    }

    public Group getCurrentGroup() {
        return mGroup;
    }

    public void setPostContent(Post post) {
        if (post != null) {
            mType = TYPE.POST;
            mPost = post;
            mGroup = null;

            setupCardsList();
            PostManager.getInstance()
                       .setOnPostListUpdateListener(mOnNormalUpdateListener);
            PostManager.getInstance()
                       .getAllOfSameTitle(mPost.getSignature(), null);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_frame, container, false);
        mListView = (ListView) view.findViewById(R.id.post_list);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getCurrentGroup() == null) {
            setGroupContent(GongSheConstant.ALL_ACTIVITY_GROUP, true);
        }
        setupCardsList();
    }

    @Override
    public void onStop() {
        super.onStop();
        PostManager.getInstance()
                   .setOnPostListUpdateListener(null);
    }

    private class PostAdapter extends BaseAdapter {
        List<ClientPost> mList;
        Context mContext;

        public PostAdapter(Context context, List<ClientPost> list) {
            mContext = context;
            mList = list;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public ClientPost getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            ClientPost post = mList.get(position);
            if (post != null) return post.getId();
            return 0;
        }

        private View getViewAt(View convertView, ClientPost post) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.post_simple_at, null);
            }
            TextView textView = (TextView)convertView.findViewById(R.id.txv_post_title);
            textView.setText(post.getTitle());
            textView = (TextView) convertView.findViewById(R.id.txv_owner);
            textView.setText(post.getName());
            textView = (TextView) convertView.findViewById(R.id.txv_time);
            textView.setText(post.getTime().substring(0, 10));
            textView = (TextView) convertView.findViewById(R.id.txv_content);
            textView.setText(post.getContent());
            textView = (TextView) convertView.findViewById(R.id.txv_at_list);
            textView.setText("@连牙饭  @张婧");
            return convertView;
        }

        private View getSimpleView(View convertView, ClientPost post) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.post_simple, null);
            }
            TextView textView = (TextView)convertView.findViewById(R.id.txv_post_title);
            textView.setText(post.getTitle());
            textView = (TextView) convertView.findViewById(R.id.txv_owner);
            textView.setText(post.getName());
            textView = (TextView) convertView.findViewById(R.id.txv_time);
            textView.setText(post.getTime().substring(0, 10));
            textView = (TextView) convertView.findViewById(R.id.txv_content);
            //textView.setText(post.getContent());
            textView.setText("璀璨的剑光平地升起，那刺目的光华背后，伴随着一声惊天动地的巨响，铸铁的巨门被一分为四，冲击力使整扇门向内凹陷，然后分崩离析，飞起来撞向其后的瓮城；剑气的余波融入两侧的城墙之中，黑色的岩石以肉眼可见的速度生长出一道平滑切口，沿着这条切口");
            return convertView;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ClientPost post = getItem(position);
            if (position == 0) {
                return getViewAt(convertView, post);
            }
            return getSimpleView(convertView, post);
        }
    }
}

