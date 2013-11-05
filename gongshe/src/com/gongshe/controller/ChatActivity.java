package com.gongshe.controller;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ListView;
import com.gongshe.R;

public class ChatActivity extends FragmentActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        ListView listView = (ListView) findViewById(R.id.lsv_chat_content);
        listView.setAdapter(new ChatContentListAdapter(this));
    }
}