package com.gongshe.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.gongshe.R;
import com.gongshe.model.Group;
import com.gongshe.model.User;
import com.gongshe.model.UserManager;
import it.sephiroth.android.library.widget.HListView;

import java.util.ArrayList;
import java.util.List;

public class GroupInfoActivity extends FragmentActivity {
    private Group mGroup;
    private List<User> mListGroupMember;
    private GroupMemberAdapter mGroupMemberAdapter;
    private TextView mTxvMemberNum;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_info);

        Intent intent = getIntent();
        retrieveGroupData(intent);

        HeaderFragment fragment = (HeaderFragment) getSupportFragmentManager().findFragmentById(R.id.common_header);
        fragment.setTitle(getString(R.string.txt_group_info));
        fragment.setOnButtonListener(new HeaderFragment.OnButtonListener() {
            @Override
            public void onBtnCLicked(HeaderFragment.BtnId id) {
                onBackPressed();
            }
        });

        TextView textView = (TextView) findViewById(R.id.txv_group_name);
        textView.setText(mGroup.getName());

        textView = (TextView) findViewById(R.id.txv_introduction);
        textView.setText(mGroup.getIntroduction());
        textView.setMovementMethod(new ScrollingMovementMethod());

        mListGroupMember = UserManager.getInstance().getGroupMember(mGroup.getId());
        mGroupMemberAdapter = new GroupMemberAdapter(this, mListGroupMember);
        HListView listView = (HListView) findViewById(R.id.lsv_group_member);
        listView.setAdapter(mGroupMemberAdapter);

        mTxvMemberNum = (TextView)findViewById(R.id.txv_member_num);
        mTxvMemberNum.setText(mListGroupMember.size() + getString(R.string.txt_human_being));

        UserManager.getInstance().registerOnGroupMemberUpdateListener(new UserManager.OnGroupMemberUpdateListener() {
            @Override
            public void onGroupMemberUpdate() {
                mGroupMemberAdapter.notifyDataSetChanged();
                mTxvMemberNum.setText(mListGroupMember.size() + getString(R.string.txt_human_being));
            }
        });
        UserManager.getInstance().fetchGroupMember(mGroup.getId(), null);
    }

    @Override
    protected void onStop() {
        super.onStop();
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

    private class GroupMemberAdapter extends BaseAdapter {
        List<User> mMemberList;
        Context mContext;

        public GroupMemberAdapter(Context context, List<User> memberList) {
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
            User user = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.group_member_item, null);
            }
            ImageView imageView = (ImageView) convertView.findViewById(R.id.icon_group_member_avatar);
            // TODO: replace by real avatar
            imageView.setImageResource(R.drawable.test_avatar);
            TextView textView = (TextView) convertView.findViewById(R.id.txv_group_member_name);
            textView.setText(user.getName());
            return convertView;
        }
    }
}