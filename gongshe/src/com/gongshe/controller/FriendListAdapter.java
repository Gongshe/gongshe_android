package com.gongshe.controller;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.gongshe.R;

import java.util.ArrayList;

public class FriendListAdapter extends BaseAdapter {
    public static class FriendInfo {
        public String mName;

        public FriendInfo(String name) {
            mName = name;
        }
    }

    ArrayList<FriendInfo> mFriendsList;
    Context mContext;

    public FriendListAdapter(Context context) {
        mFriendsList = new ArrayList<FriendInfo>();
        mContext = context;
        initFakeData();
    }

    @Override
    public int getCount() {
        return mFriendsList.size();
    }

    @Override
    public Object getItem(int position) {
        return mFriendsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.friend_list_item, parent, false);
        }
        TextView textView = (TextView) view.findViewById(R.id.txv_friend_name);
        FriendInfo friendInfo = (FriendInfo) getItem(position);
        textView.setText(friendInfo.mName);

        Button button = (Button) view.findViewById(R.id.btn_send_message);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                mContext.startActivity(intent);
            }
        });

        return view;
    }

    private void initFakeData() {
        mFriendsList.add(new FriendInfo("Xu kai"));
        mFriendsList.add(new FriendInfo("Hugh ji"));
        mFriendsList.add(new FriendInfo("Hu Xu"));
        mFriendsList.add(new FriendInfo("Yafan Lian"));
    }
}
