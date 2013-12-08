package com.gongshe.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.gongshe.R;
import com.gongshe.model.*;

import java.util.List;

public class GroupPostFragment extends Fragment {

    private final String TAG = GroupPostFragment.class.getSimpleName();

    private Activity mActivity;
    private Group mGroup;
    private PostAdapter mAdapter;
    private ListView mListView;

    private PostManager.OnPostListUpdateListener mOnNormalUpdateListener = new PostManager.OnPostListUpdateListener() {
        @Override
        public void onPostListUpdate() {
            mAdapter.notifyDataSetChanged();
        }
    };

    public void setGroupContent(Group group, boolean force) {
        if (group != null && (!group.equals(mGroup) || force)) {
            mGroup = group;

            setupCardsList();

            PostManager.getInstance()
                       .addOnPostListUpdateListener(mOnNormalUpdateListener);
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
        List<ClientPost> postList = PostManager.getInstance().getGroupPostList(mGroup);
        mAdapter = new PostAdapter(mActivity, postList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ClientPost post = mAdapter.getItem(position);
                Intent intent = new Intent(mActivity, PostBrowseActivity.class);
                intent.putExtra("title", post.getTitle());
                intent.putExtra("pid", post.getId());
                intent.putExtra("signature", post.getSignature());
                startActivity(intent);
            }
        });
    }

    public String getContentName() {
        return mGroup.getName();
    }

    public Group getCurrentGroup() {
        return mGroup;
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
                   .removeOnPostListUpdateListener(mOnNormalUpdateListener);
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

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ClientPost post = getItem(position);
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
            textView.setText(post.getContent());
            textView = (TextView) convertView.findViewById(R.id.txv_at_list);
            // TODO implement @ function
            if (position == 0) {
                textView.setText("@连牙饭  @张婧");
                textView.setVisibility(View.VISIBLE);
            } else {
                textView.setText(null);
                textView.setVisibility(View.GONE);
            }
            return convertView;
        }
    }
}
