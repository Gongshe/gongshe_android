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
            public void onBtnCLicked(HeaderFragment.BtnId id) {
                if (id == HeaderFragment.BtnId.LEFT) {
                    setResult(RESULT_OK);
                    onBackPressed();
                } else {
                    Intent intent = new Intent(FriendListActivity.this, ImportFriendActivity.class);
                    FriendListActivity.this.startActivity(intent);
                }
            }
        });
    }

}