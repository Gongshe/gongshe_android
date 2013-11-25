package com.gongshe.controller;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ListView;
import com.gongshe.R;

public class ChatActivity extends FragmentActivity {
    public @interface Test {
        String id() default "d";
    }

    @Test
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        ListView listView = (ListView) findViewById(R.id.lsv_chat_content);
        listView.setAdapter(new ChatContentListAdapter(this));
        HeaderFragment fragment = (HeaderFragment) getSupportFragmentManager().findFragmentById(R.id.common_header);
        fragment.setOnButtonListener(new HeaderFragment.OnButtonListener() {
            @Override
            public void onLeftButtonClicked() {
                onBackPressed();
            }

            @Override
            public void onRightButtonClicked() {
                // do nothing here
            }
        });
    }
}