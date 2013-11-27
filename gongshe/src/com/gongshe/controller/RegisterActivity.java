package com.gongshe.controller;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gongshe.GongSheApp;
import com.gongshe.R;
import com.gongshe.model.User;
import com.gongshe.model.UserManager;
import com.gongshe.model.network.OnNetListener;
import com.squareup.okhttp.OkHttpClient;

public class RegisterActivity extends FragmentActivity {

    private EditText mEtxPhone;
    private EditText mEtxPassword;
    private EditText mEtxName;
    private Button mBtnSignUp;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        findViewById(R.id.btn_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mEtxPhone = (EditText) findViewById(R.id.etx_phone_input);
        mEtxPassword = (EditText) findViewById(R.id.etx_password_input);
        mEtxName = (EditText) findViewById(R.id.etx_name_input);
        mBtnSignUp = (Button) findViewById(R.id.btn_sign_up);
        mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSignUp();
            }
        });

        mEtxPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) v.getContext()
                                                                   .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    onSignUp();
                    return true;
                }
                return false;
            }
        });
    }

    private void onSignUp() {
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
        String name = mEtxName.getText().toString().trim();
        if (name == null || name.equals("")) {
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

        UserManager.getInstance().registerUser(name, phone, password, new UserManager.OnUpdateListener() {
            @Override
            public void onUpdate() {
                dialog.dismiss();
                Intent intent = new Intent(RegisterActivity.this, com.gongshe.controller.MainUIActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError() {
                dialog.dismiss();
                Toast toast = Toast.makeText(RegisterActivity.this, R.string.txt_info_error, Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }
}