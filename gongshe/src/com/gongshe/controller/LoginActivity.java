package com.gongshe.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.gongshe.R;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // set up login button
        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginTouched();
            }
        });
    }

    private void onLoginTouched() {
        Intent intent = new Intent(this, com.gongshe.controller.MainUIActivity.class);
        startActivity(intent);
        finish();
    }
}
