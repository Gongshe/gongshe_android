package com.gongshe.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.gongshe.R;

public class FriendListActivity extends FragmentActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_list);
        HeaderFragment fragment = (HeaderFragment) getSupportFragmentManager().findFragmentById(R.id.common_header);
        fragment.setOnButtonListener(new HeaderFragment.OnButtonListener() {
            @Override
            public void onLeftBtnClicked() {
                onBackPressed();
            }

            @Override
            public void onRightBtnClicked(HeaderFragment.RightBtnId id) {
                Intent intent = new Intent(FriendListActivity.this, ImportContactActivity.class);
                FriendListActivity.this.startActivity(intent);
            }
        });
    }
}