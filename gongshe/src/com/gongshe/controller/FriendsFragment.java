package com.gongshe.controller;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.gongshe.R;
import com.gongshe.model.UserManager;

public class FriendsFragment extends Fragment {
    private final String TAG = FriendsFragment.class.getSimpleName();

    private FriendListAdapter.DisplayMode mDisplayMode;
    private FriendListAdapter mAdapter;

    public void setDisplayMode(FriendListAdapter.DisplayMode displayMode) {
        mDisplayMode = displayMode;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friends_frame, container, false);
        ListView listView = (ListView) view.findViewById(R.id.lsv_friend_list);
        mAdapter = new FriendListAdapter(getActivity(), UserManager.getInstance().getFriendList());
        mAdapter.setDisplayMode(mDisplayMode);
        listView.setAdapter(mAdapter);
        updateFriendList();
        return view;
    }

    private void updateFriendList() {
        UserManager.getInstance().fetchFriendList(null);
        UserManager.getInstance().registerOnFriendListUpdateListener(new UserManager.OnFriendListUpdateListener() {
            @Override
            public void onFriendListUpdate() {
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);
        TypedArray styles = activity.obtainStyledAttributes(attrs, R.styleable.FriendsFragment);
        mDisplayMode = FriendListAdapter.DisplayMode.values()[styles.getInt(R.styleable.FriendsFragment_item_mode, 0)];
        styles.recycle();
    }
}
