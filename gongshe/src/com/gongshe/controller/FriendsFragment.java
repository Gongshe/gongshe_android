package com.gongshe.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.gongshe.R;

public class FriendsFragment extends Fragment {
    private final String TAG = FriendsFragment.class.getSimpleName();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friends_frame, container, false);
        ListView listView = (ListView) view.findViewById(R.id.lsv_friend_list);
        listView.setAdapter(new FriendListAdapter(getActivity()));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }
}
