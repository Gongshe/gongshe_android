package com.gongshe.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.gongshe.R;
import com.gongshe.model.UserManager;

public class CreateGroupActivity extends Activity {
    private Button mBtnCreateGroup;
    private EditText mEtxGroupName;
    private EditText mEtxGroupIntroduction;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_group);

        ListView listView = (ListView) findViewById(R.id.lsv_group_members);
        FriendListAdapter adapter = new FriendListAdapter(this);
        adapter.setDisplayMode(FriendListAdapter.DisplayMode.NONE);
        listView.setAdapter(adapter);

        Button button = (Button) findViewById(R.id.btn_back);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mBtnCreateGroup = (Button) findViewById(R.id.btn_create_group);
        mBtnCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnCreate();
            }
        });

        mEtxGroupName = (EditText) findViewById(R.id.etx_group_name);
        mEtxGroupIntroduction = (EditText) findViewById(R.id.etx_group_introduction);
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

        UserManager.getInstance().createGroup(name, introduction, new UserManager.OnUpdateListener() {
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