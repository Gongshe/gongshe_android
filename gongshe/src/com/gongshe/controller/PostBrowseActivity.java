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
import com.gongshe.model.Post;
import com.gongshe.model.PostManager;

public class PostBrowseActivity extends FragmentActivity {

    private ContentFrameFragment mContentFrame;
    private HeaderFragment mHeaderFrame;
    private EditText mEtxReply;
    private Post mPost;
    private String mFrom;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_browse);
        mEtxReply = (EditText) findViewById(R.id.etx_post_reply);
        Button button = (Button) findViewById(R.id.btn_reply);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onReply();
            }
        });

        Intent intent = getIntent();
        mFrom = intent.getStringExtra("from");
        String title = intent.getStringExtra("title");
        String signature = intent.getStringExtra("signature");
        int id = intent.getIntExtra("pid", 0);
        mPost = new Post();
        mPost.setId(id);
        mPost.setTitle(title);
        mPost.setSignature(signature);

        mContentFrame = (ContentFrameFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        mContentFrame.setPostContent(mPost);

        mHeaderFrame = (HeaderFragment) getSupportFragmentManager().findFragmentById(R.id.common_header);
        mHeaderFrame.setTitle(mContentFrame.getContentName());
        mHeaderFrame.setLetBtnText(mFrom);
        mHeaderFrame.setOnButtonListener(new HeaderFragment.OnButtonListener() {
            @Override
            public void onLeftBtnClicked() {
                onBackPressed();
            }

            @Override
            public void onRightBtnClicked(HeaderFragment.RightBtnId id) {
                // do nothing.
            }
        });
    }

    private void onReply() {
        String content = mEtxReply.getText().toString().trim();
        if (content == null || content.equals("")) {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.error_input_content)
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

        PostManager.getInstance().replyPost(content, mPost.getSignature(), new OnUpdateListener() {
            @Override
            public void onUpdate() {
                dialog.dismiss();
                Intent intent = new Intent(PostBrowseActivity.this, PostBrowseActivity.class);
                intent.putExtra("from", mFrom);
                intent.putExtra("title", mPost.getTitle());
                intent.putExtra("pid", mPost.getId());
                intent.putExtra("signature", mPost.getSignature());
                startActivity(intent);
                finish();
            }

            @Override
            public void onError() {
                dialog.dismiss();
                Toast toast = Toast.makeText(PostBrowseActivity.this, R.string.error_network, Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }
}