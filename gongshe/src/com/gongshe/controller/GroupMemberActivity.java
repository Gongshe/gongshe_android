package com.gongshe.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.gongshe.R;
import com.gongshe.model.Group;
import com.gongshe.model.OnUpdateListener;
import com.gongshe.model.User;
import com.gongshe.model.UserManager;

import java.util.List;

public class GroupMemberActivity extends FragmentActivity {

    private enum Mode {
        NORMAL,
        MINUS;
    }

    private Group mGroup;
    private List<User> mListGroupMember;
    private GroupMemberGridAdapter mAdapter;
    private GridView mGridView;

    private ImageView mImvAddIcon;
    private ImageView mImvMinusIcon;

    private HeaderFragment mHeaderFragment;
    private View mOptCotainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_member);

        mHeaderFragment = (HeaderFragment) getSupportFragmentManager().findFragmentById(R.id.common_header);
        mHeaderFragment.setOnButtonListener(new HeaderFragment.OnButtonListener() {
            @Override
            public void onBtnCLicked(HeaderFragment.BtnId id) {
                if (id == HeaderFragment.BtnId.LEFT) {
                    onBackPressed();
                } else {
                    mAdapter.setMode(Mode.NORMAL);
                    mAdapter.notifyDataSetChanged();
                    mHeaderFragment.setRightButtonType(HeaderFragment.ButtonType.INVISIBLE,
                            HeaderFragment.ButtonType.INVISIBLE);
                }

            }
        });

        Intent intent = getIntent();
        retrieveGroupData(intent);

        mOptCotainer = findViewById(R.id.opt_container);
        if (mGroup.getOwner() == UserManager.getInstance().getUser().getId()) {
            mOptCotainer.setVisibility(View.VISIBLE);
            mImvAddIcon = (ImageView) findViewById(R.id.icon_add_member);
            mImvMinusIcon = (ImageView) findViewById(R.id.icon_minus_member);
            mImvMinusIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdapter.setMode(Mode.MINUS);
                    mAdapter.notifyDataSetChanged();
                    mHeaderFragment.setRightButtonType(HeaderFragment.ButtonType.VISIBLE,
                            HeaderFragment.ButtonType.INVISIBLE);
                }
            });
        } else {
            mOptCotainer.setVisibility(View.GONE);
        }

        mGridView = (GridView) findViewById(R.id.grid_member);
        mListGroupMember = UserManager.getInstance().getGroupMember(mGroup.getId());
        mAdapter = new GroupMemberGridAdapter(this, mListGroupMember);
        mGridView.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        UserManager.getInstance().registerOnGroupMemberUpdateListener(new UserManager.OnGroupMemberUpdateListener() {
            @Override
            public void onGroupMemberUpdate() {
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStart();
        UserManager.getInstance().registerOnGroupMemberUpdateListener(null);
    }

    private void retrieveGroupData(Intent intent) {
        mGroup = new Group();
        mGroup.setId(intent.getIntExtra("gid", 0));
        mGroup.setName(intent.getStringExtra("gname"));
        mGroup.setIntroduction(intent.getStringExtra("gintroduction"));
        mGroup.setAvatar(intent.getStringExtra("gavatar"));
        mGroup.setOwner(intent.getIntExtra("gowner", 0));
        mGroup.setTime(intent.getStringExtra("gtime"));
    }

    private void onDeleteMember(final User member) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.txt_loading));
        dialog.setCancelable(false);
        dialog.show();

        UserManager.getInstance().deleteGroupMember(mGroup.getId(), member.getId(), new OnUpdateListener() {
            @Override
            public void onUpdate() {
                dialog.dismiss();
                mListGroupMember.remove(member);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError() {
                dialog.dismiss();
                Toast toast = Toast.makeText(GroupMemberActivity.this, R.string.error_network, Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }


    private class GroupMemberGridAdapter extends BaseAdapter {

        List<User> mMemberList;
        Context mContext;
        private Mode mMode;

        public void setMode(Mode mode) {
            mMode = mode;
        }

        public GroupMemberGridAdapter(Context context, List<User> memberList) {
            mContext = context;
            mMemberList = memberList;
        }

        @Override
        public int getCount() {
            return mMemberList.size();
        }

        @Override
        public User getItem(int position) {
            return mMemberList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mMemberList.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final User user = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.group_membmer_item_grid, null);
            }
            ImageView imageView = (ImageView) convertView.findViewById(R.id.icon_group_member_avatar);
            // TODO: replace by real avatar
            imageView.setImageResource(R.drawable.test_avatar);
            TextView textView = (TextView) convertView.findViewById(R.id.txv_group_member_name);
            textView.setText(user.getName());

            imageView = (ImageView) convertView.findViewById(R.id.icon_delete_member);
            if (mMode == Mode.MINUS && user.getId() != mGroup.getOwner()) {
                imageView.setVisibility(View.VISIBLE);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onDeleteMember(user);
                    }
                });
            } else {
                imageView.setVisibility(View.INVISIBLE);
            }
            return convertView;
        }
    }

}