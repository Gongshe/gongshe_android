package com.gongshe.controller;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import com.gongshe.R;
import com.gongshe.model.User;

import java.util.List;

public class FriendListAdapter extends BaseAdapter {

    public enum DisplayMode {
        NONE,
        MESSAGE,
        SELECTION;
    }

    List<User> mFriendsList;
    Context mContext;

    private DisplayMode mDisplayMode = DisplayMode.MESSAGE;

    public void setDisplayMode(DisplayMode displayMode) {
        mDisplayMode = displayMode;
    }

    public FriendListAdapter(Context context, List<User> userList) {
        mContext = context;
        mFriendsList = userList;
    }

    @Override
    public int getCount() {
        return mFriendsList.size();
    }

    @Override
    public User getItem(int position) {
        return mFriendsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mFriendsList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.friend_list_item, parent, false);
        }

        TextView textView = (TextView) view.findViewById(R.id.txv_friend_name);
        User friendInfo = getItem(position);
        textView.setText(friendInfo.getName());

        Button button = (Button) view.findViewById(R.id.btn_send_message);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                mContext.startActivity(intent);
            }
        });

        CheckBox checkBox = (CheckBox) view.findViewById(R.id.cb_selection);

        if (mDisplayMode == DisplayMode.SELECTION) {
            checkBox.setVisibility(View.VISIBLE);
            button.setVisibility(View.GONE);
        } else if (mDisplayMode == DisplayMode.MESSAGE) {
            checkBox.setVisibility(View.GONE);
            button.setVisibility(View.VISIBLE);
        } else {
            checkBox.setVisibility(View.GONE);
            button.setVisibility(View.GONE);
        }
        return view;
    }

}
