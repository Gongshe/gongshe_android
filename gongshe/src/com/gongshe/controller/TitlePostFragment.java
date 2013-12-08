package com.gongshe.controller;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.gongshe.R;
import com.gongshe.model.ClientPost;
import com.gongshe.model.OnUpdateListener;
import com.gongshe.model.Post;
import com.gongshe.model.PostManager;

import java.util.List;

public class TitlePostFragment extends Fragment {

    private final String TAG = TitlePostFragment.class.getSimpleName();

    private Post mPost;

    private PostAdapter mAdapter;
    private ListView mListView;

    private PostManager.OnPostListUpdateListener mOnNormalUpdateListener = new PostManager.OnPostListUpdateListener() {
        @Override
        public void onPostListUpdate() {
            mAdapter.notifyDataSetChanged();
        }
    };

    private void setupCardsList() {
        List<ClientPost> postList = PostManager.getInstance()
                                               .getPostList(mPost.getSignature());
        mAdapter = new PostAdapter(getActivity(), postList);
        mListView.setAdapter(mAdapter);
    }

    public String getContentName() {
        return mPost.getTitle();
    }

    public void setPostContent(Post post) {
        mPost = post;
        setupCardsList();
        PostManager.getInstance()
                   .addOnPostListUpdateListener(mOnNormalUpdateListener);
        PostManager.getInstance()
                   .getAllOfSameTitle(mPost.getSignature(), null);
    }

    public void updateContent(OnUpdateListener listener) {
        PostManager.getInstance()
                .getAllOfSameTitle(mPost.getSignature(), listener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_frame, container, false);
        mListView = (ListView) view.findViewById(R.id.post_list);
        return view;
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
                convertView = LayoutInflater.from(mContext)
                                            .inflate(R.layout.title_post_item, null);
            }

            ImageView imv = (ImageView) convertView.findViewById(R.id.imv_avatar);
            // TODO set imv as real image.

            TextView textView = (TextView) convertView.findViewById(R.id.txv_owner_name);
            textView.setText(post.getName());

            textView = (TextView) convertView.findViewById(R.id.txv_time);
            textView.setText(post.getTime().substring(11, 16));

            textView = (TextView) convertView.findViewById(R.id.txv_day);
            textView.setText(post.getTime().substring(0, 10));

            textView = (TextView) convertView.findViewById(R.id.txv_content);
            if (position == 1) {
                post.setContent("　　克劳斯悠闲地笑道：“伊文斯，你已经是大奥术师了，不必这么客气，称呼我克劳斯就行了，我是一个喜欢早到的人，每次等待别人的时候，我就像拥有了多余的时间，可以自由地思考，可以安静地发呆，享受着这突然冒出来的奢侈时光，这是一种美妙的感受，你不觉得吗？你也来的很早？”");
            }
            textView.setText(post.getContent());
            return convertView;
        }
    }
}

