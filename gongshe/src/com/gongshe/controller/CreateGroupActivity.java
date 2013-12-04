package com.gongshe.controller;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.gongshe.R;
import com.gongshe.model.OnUpdateListener;
import com.gongshe.model.UserManager;

public class CreateGroupActivity extends FragmentActivity {
    private EditText mEtxGroupName;
    private EditText mEtxGroupIntroduction;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_group);

        HeaderFragment fragment = (HeaderFragment) getSupportFragmentManager().findFragmentById(R.id.common_header);
        fragment.setTitle(getString(R.string.txt_create_new_group));
        fragment.setOnButtonListener(new HeaderFragment.OnButtonListener() {
            @Override
            public void onBtnCLicked(HeaderFragment.BtnId id) {
                onBackPressed();
            }
        });

        mEtxGroupName = (EditText) findViewById(R.id.etx_group_name);
        mEtxGroupIntroduction = (EditText) findViewById(R.id.etx_group_introduction);

        Button button = (Button) findViewById(R.id.btn_create_group);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnCreate();
            }
        });

        button = (Button) findViewById(R.id.btn_invite_friend);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:
            }
        });
    }

    private void onBtnCreate() {
        String name = mEtxGroupName.getText()
                                   .toString()
                                   .trim();
        String introduction = mEtxGroupIntroduction.getText()
                                                   .toString()
                                                   .trim();

        boolean isError = false;
        if (name == null || name.equals("")) {
            isError = true;
        }

        if (isError) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.title_error_input)
                    .setMessage(R.string.error_group_name)
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

        UserManager.getInstance().createGroup(name, introduction, new OnUpdateListener() {
            @Override
            public void onUpdate() {
                Intent intent = new Intent(CreateGroupActivity.this, com.gongshe.controller.MainUIActivity.class);
                startActivity(intent);
                dialog.dismiss();
                finish();
            }

            @Override
            public void onError() {
                dialog.dismiss();
                Toast toast = Toast.makeText(CreateGroupActivity.this, R.string.txt_info_error, Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }
}