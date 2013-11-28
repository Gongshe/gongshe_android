package com.gongshe.controller;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ListView;
import com.gongshe.R;

public class ImportContactActivity extends FragmentActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.import_contact);
        HeaderFragment fragment = (HeaderFragment) getSupportFragmentManager().findFragmentById(R.id.common_header);
        fragment.setOnButtonListener(new HeaderFragment.OnButtonListener() {
            @Override
            public void onLeftBtnClicked() {
                onBackPressed();
            }

            @Override
            public void onRightBtnClicked(HeaderFragment.RightBtnId id) {
                // do nothing here.
            }
        });
        ListView listView = (ListView) findViewById(R.id.lsv_contact_friend);
        FriendListAdapter adapter = new FriendListAdapter(this);
        adapter.setDisplayMode(FriendListAdapter.DisplayMode.SELECTION);
        listView.setAdapter(adapter);

        listView = (ListView) findViewById((R.id.lsv_weibo_friend));
        adapter = new FriendListAdapter(this);
        adapter.setDisplayMode(FriendListAdapter.DisplayMode.SELECTION);
        listView.setAdapter(adapter);
    }
}