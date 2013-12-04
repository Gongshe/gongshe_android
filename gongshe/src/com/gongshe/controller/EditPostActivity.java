package com.gongshe.controller;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.gongshe.R;
import com.gongshe.model.OnUpdateListener;
import com.gongshe.model.PostManager;

public class EditPostActivity extends FragmentActivity {

    private HeaderFragment mTitleFragment;
    private EditText mEtxTitle;
    private EditText mEtxContent;
    private Button mBtnAtFriend;
    private int mGroupId;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_post);

        Intent intent = getIntent();
        String from = intent.getStringExtra("from");
        mGroupId = intent.getIntExtra("gid", 0);
        if (mGroupId == 0) finish(); // error.

        mEtxTitle = (EditText) findViewById(R.id.etx_input_title);
        mEtxContent = (EditText) findViewById(R.id.etx_input_content);

        mTitleFragment = (HeaderFragment) getSupportFragmentManager().findFragmentById(R.id.common_header);
        mTitleFragment.setOnButtonListener(new HeaderFragment.OnButtonListener() {
            @Override
            public void onBtnCLicked(HeaderFragment.BtnId id) {
                if (id == HeaderFragment.BtnId.LEFT) {
                    onBackPressed();
                } else {
                    onPost();
                }

            }
        });
    }

    private void onPost() {
        boolean isError = false;
        String title = mEtxTitle.getText().toString().trim();
        if (title == null || title.equals("")) {
            isError = true;
        }
        String content = mEtxContent.getText().toString().trim();
        if (content == null || content.equals("")) {
            isError = true;
        }
        if (isError) {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.error_input_no_complete)
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

        PostManager.getInstance().createPost(mGroupId, title, content, new OnUpdateListener() {
            @Override
            public void onUpdate() {
                dialog.dismiss();
                Intent intent = new Intent(EditPostActivity.this, com.gongshe.controller.MainUIActivity.class);
                intent.putExtra("gid", mGroupId);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError() {
                dialog.dismiss();
                Toast toast = Toast.makeText(EditPostActivity.this, R.string.error_network, Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }
}