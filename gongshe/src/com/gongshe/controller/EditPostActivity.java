package com.gongshe.controller;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.gongshe.R;
import com.gongshe.model.OnUpdateListener;
import com.gongshe.model.PostManager;
import com.gongshe.model.User;
import com.gongshe.view.CirceImageView;
import it.sephiroth.android.library.widget.HListView;

import java.util.List;

public class EditPostActivity extends FragmentActivity {
    private static final String TAG = EditPostActivity.class.getSimpleName();
    private static final int PICK_FRIEND_REQUEST_CODE = 1010;

    private HeaderFragment mTitleFragment;
    private EditText mEtxTitle;
    private EditText mEtxContent;
    private TextView mTxvAtFriend;
    private HListView mAtListView;

    private int mGroupId;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_post);

        Intent intent = getIntent();
        mGroupId = intent.getIntExtra("gid", 0);
        if (mGroupId == 0) {
            throw new RuntimeException(TAG + " gid is 0");
        }

        mEtxTitle = (EditText) findViewById(R.id.etx_input_title);
        mEtxContent = (EditText) findViewById(R.id.etx_input_content);
        mTxvAtFriend = (TextView) findViewById(R.id.txv_at_friend);
        mAtListView = (HListView) findViewById(R.id.lsv_at_friend);

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

        mTxvAtFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditPostActivity.this, FriendListActivity.class);
                startActivityForResult(intent, PICK_FRIEND_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("Yafan", "on result" + "code=" + requestCode + "ll=" + resultCode);
        if (requestCode == PICK_FRIEND_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Log.e("Yafan", "on result");
            }
        }
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

    private class AtFriendAdapter extends BaseAdapter {
        private List<User> mList;
        private Context mContext;

        public AtFriendAdapter(Context context, List<User> list) {
            mContext = context;
            mList = list;
        }

        @Override
        public int getCount() {
            if (mList != null) return mList.size();
            return 0;
        }

        @Override
        public User getItem(int position) {
            if (mList != null) return mList.get(position);
            return null;
        }

        @Override
        public long getItemId(int position) {
            if (mList != null) {
                User user = mList.get(position);
                if (user != null) return user.getId();
            }
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.at_friend_item, null);
            }
            CirceImageView view = (CirceImageView) convertView;
            // TODO implement real image download.
            if (position % 2 == 0) {
                view.setImageResource(R.drawable.test_avatar);
            } else {
                view.setImageResource(R.drawable.yafan_icon);
            }
            return null;
        }
    }
}