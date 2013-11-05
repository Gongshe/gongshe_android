package com.gongshe.controller;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.gongshe.R;

public class EditPostActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_post);

        Button button = (Button) findViewById(R.id.btn_back);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}