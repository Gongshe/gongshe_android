package com.gongshe.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import com.gongshe.R;

public class GroupManageActivity extends FragmentActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_manage);

        HeaderFragment fragment = (HeaderFragment) getSupportFragmentManager().findFragmentById(R.id.common_header);
        fragment.setOnButtonListener(new HeaderFragment.OnButtonListener() {
            @Override
            public void onLeftBtnClicked() {
                onBackPressed();
            }

            @Override
            public void onRightBtnClicked(HeaderFragment.RightBtnId id) {
                // do nothing here
            }
        });


        Button button = (Button) findViewById(R.id.btn_create_group);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupManageActivity.this, CreateGroupActivity.class);
                startActivity(intent);
            }
        });
    }
}