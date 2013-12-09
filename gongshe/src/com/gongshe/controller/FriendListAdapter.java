package com.gongshe.controller;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.gongshe.R;
import com.gongshe.model.User;
import com.gongshe.view.CirceImageView;

import java.util.List;

public class FriendListAdapter extends BaseAdapter {

    public enum DisplayMode {
        NORMAL,
        SELECTION;
    }

    List<User> mFriendsList;
    Context mContext;

    private DisplayMode mDisplayMode = DisplayMode.NORMAL;

    public void setDisplayMode(DisplayMode displayMode) {
        mDisplayMode = displayMode;
    }

    public FriendListAdapter(Context context, List<User> userList) {
        mContext = context;
        mFriendsList = userList;
    }

    public void setFriendList(List<User> list) {
        mFriendsList = list;
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
        User user = mFriendsList.get(position);
        if (user != null) {
            return user.getId();
        }
        return 0;
    }

    @Override
    public boolean isEnabled(int position) {
        User user = mFriendsList.get(position);
        if (user == null) return false;
        if (user instanceof FriendsFragment.TagUser) {
            return false;
        }
        return true;
    }

    private View getNormalView(int position, View convertView, ViewGroup parent) {
        User user = getItem(position);

        if (user instanceof FriendsFragment.TagUser) {
            if (convertView == null ||  ((Boolean)convertView.getTag()) == false) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.friend_list_item_tag, null);
            }
            convertView.setTag(true);

            ((TextView) convertView).setText(user.getName());
            return convertView;
        }

        if (convertView == null || ((Boolean)convertView.getTag()) == true) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.friend_list_item_normal, null);
        }
        convertView.setTag(false);

        CirceImageView imageView = (CirceImageView) convertView.findViewById(R.id.icon_avatar);
        // TODO set as real avatar.

        TextView textView = (TextView) convertView.findViewById(R.id.txv_name);
        User friendInfo = getItem(position);
        textView.setText(friendInfo.getName());
        return convertView;
    }

    private View getSelectionView(int position, View convertView, ViewGroup parent) {
        User user = getItem(position);

        if (user instanceof FriendsFragment.TagUser) {
            Log.e("yafan", "in tag use");
            if (convertView == null ||  ((Boolean)convertView.getTag()) == false) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.friend_list_item_tag, null);
            }
            convertView.setTag(true);

            ((TextView) convertView).setText(user.getName());
            return convertView;
        }

        if (convertView == null || ((Boolean)convertView.getTag()) == true) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.friend_list_item_normal, null);
        }
        convertView.setTag(false);

        CirceImageView imageView = (CirceImageView) convertView.findViewById(R.id.icon_avatar);
        // TODO set as real avatar.

        TextView textView = (TextView) convertView.findViewById(R.id.txv_name);
        User friendInfo = getItem(position);
        textView.setText(friendInfo.getName());
        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (mDisplayMode == DisplayMode.NORMAL) {
            return getNormalView(position, convertView, parent);
        }
        return getSelectionView(position, convertView, parent);
    }
}
