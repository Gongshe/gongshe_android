package com.gongshe.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.gongshe.R;
import com.gongshe.model.User;
import com.gongshe.model.UserManager;

public class LoginActivity extends Activity {

    private EditText mEtxPhone;
    private EditText mEtxPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        User user = UserManager.getInstance().getUser();
        if (user != null && user.isValidUser()) {
            Intent intent = new Intent(this, com.gongshe.controller.MainUIActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.login);
        // set up login button
        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginTouched();
            }
        });
        findViewById(R.id.btn_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSignUpTouched();
            }
        });
        mEtxPhone = (EditText) findViewById(R.id.user_phone_input);
        mEtxPassword = (EditText) findViewById(R.id.user_passwd_input);
        mEtxPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) v.getContext()
                                                                   .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    onLoginTouched();
                    return true;
                }
                return false;
            }
        });
    }

    private void onLoginTouched() {
        // validate the input
        boolean isErrorInput = false;
        String phone = mEtxPhone.getText().toString().trim();
        //TODO: validate the string is a valid phone number. (by SMS in the future,
        // but at least check if it's a number).
        if (phone == null || phone.equals("")) {
            isErrorInput = true;
        }
        try {
            Long.parseLong(phone);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            isErrorInput = true;
        }

        String password = mEtxPassword.getText().toString().trim();
        if (password == null || password.equals("")) {
            isErrorInput = true;
        }

        if (isErrorInput) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.title_error_input)
                    .setMessage(R.string.txt_error_input)
                    .setNegativeButton(R.string.txt_confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create()
                    .show();
            return;
        }

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.txt_loading));
        dialog.setCancelable(false);
        dialog.show();

        UserManager.getInstance().login(phone, password, new UserManager.OnUpdateListener() {
            @Override
            public void onUpdate() {
                dialog.dismiss();
                Intent intent = new Intent(LoginActivity.this, com.gongshe.controller.MainUIActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError() {
                dialog.dismiss();
                Toast toast = Toast.makeText(LoginActivity.this, R.string.txt_info_error, Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    private void onSignUpTouched() {
        Intent intent = new Intent(this, com.gongshe.controller.RegisterActivity.class);
        startActivity(intent);
    }
}
