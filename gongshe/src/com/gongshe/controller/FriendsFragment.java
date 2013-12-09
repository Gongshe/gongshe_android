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

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.fortysevendeg.swipelistview.SwipeListViewListener;
import com.gongshe.R;
import com.gongshe.model.FriendManager;
import com.gongshe.model.User;
import com.gongshe.util.PinyinComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FriendsFragment extends Fragment {
    private final String TAG = FriendsFragment.class.getSimpleName();

    public static class TagUser extends User {
        public boolean isTag = true;
    }

    public static class UserComparator implements Comparator<User> {
        private PinyinComparator mPinyinComparator = new PinyinComparator();
        @Override
        public int compare(User lhs, User rhs) {
            return mPinyinComparator.compare(lhs.getName(), rhs.getName());
        }
    }

    private UserComparator mUserComparator = new UserComparator();

    private FriendListAdapter.DisplayMode mDisplayMode = FriendListAdapter.DisplayMode.NORMAL;
    private FriendListAdapter mAdapter;
    private SwipeListView mListView;
    private List<User> mFriendList = new ArrayList<User>();

    private FriendManager.OnFriendListUpdateListener mUpdateListener = new FriendManager.OnFriendListUpdateListener() {
        @Override
        public void onFriendListUpdate() {
            loadFriendList();
            mAdapter.setFriendList(mFriendList);
            mAdapter.notifyDataSetChanged();
        }
    };

    public void setDisplayMode(FriendListAdapter.DisplayMode displayMode) {
        mDisplayMode = displayMode;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friends_frame, container, false);
        mListView = (SwipeListView) view.findViewById(R.id.lsv_friend_list);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadFriendList();
        mAdapter = new FriendListAdapter(getActivity(), mFriendList);
        mAdapter.setDisplayMode(mDisplayMode);
        mListView.setAdapter(mAdapter);
        updateFriendList();
        mListView.setSwipeListViewListener(mListListener);
    }

    private void loadFriendList() {
        mFriendList.clear();
        genTagList(mFriendList);
        List<User> list = FriendManager.getInstance().getFriendList();
        mFriendList.addAll(list);
        Collections.sort(mFriendList, mUserComparator);

        List<User> removeList = new ArrayList<User>();
        for (int i = 0; i < mFriendList.size(); i++) {
            User user = mFriendList.get(i);
            if (i + 1 < mFriendList.size()) {
                User userNext = mFriendList.get(i + 1);
                if ((user instanceof TagUser) && (userNext instanceof TagUser)) {
                    removeList.add(user);
                }
            } else {
                if (user instanceof TagUser) removeList.add(user);
            }
        }
        mFriendList.removeAll(removeList);
    }

    private void updateFriendList() {
        FriendManager.getInstance().registerOnFriendListUpdateListener(mUpdateListener);
        FriendManager.getInstance().fetchFriendList(null);
    }

    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);
        TypedArray styles = activity.obtainStyledAttributes(attrs, R.styleable.FriendsFragment);
        mDisplayMode = FriendListAdapter.DisplayMode.values()[styles.getInt(R.styleable.FriendsFragment_item_mode, 0)];
        styles.recycle();
    }

    private SwipeListViewListener mListListener = new BaseSwipeListViewListener() {

        @Override
        public void onClickFrontView(int position) {
            //TODO
        }

        @Override
        public void onClickBackView(int position) {
            //TODO
        }
    };

    private void genTagList(List<User> list) {
        final String[] table = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
                "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        int i = -1;
        for (String str : table) {
            TagUser tagUser = new TagUser();
            tagUser.setName(str);
            tagUser.setId(-i);
            i--;
            list.add(tagUser);
        }
    }
}
