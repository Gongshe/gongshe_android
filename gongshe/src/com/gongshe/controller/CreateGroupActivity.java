package com.gongshe.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import com.gongshe.R;

public class CreateGroupActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_group);

        ListView listView = (ListView) findViewById(R.id.lsv_group_members);
        listView.setAdapter(new FriendListAdapter(this));

        Button button = (Button) findViewById(R.id.btn_back);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        button = (Button) findViewById(R.id.btn_add_member);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateGroupActivity.this, FriendListActivity.class);
                startActivity(intent);
            }
        });
    }
}