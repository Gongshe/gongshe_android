package com.gongshe.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.gongshe.R;
import com.gongshe.model.Group;

public class GroupManageActivity extends FragmentActivity {

    private HeaderFragment mHeaderFragment;
    private GroupListFragment mGroupListFragment;

    public static final String ACTION_SELECT = "com.songshe.controller.GroupManageActivity.action_select";
    public static final String ACTION_MANAGE = "com.songshe.controller.GroupManageActivity.action_MANAGE";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_manage);

        Intent intent = getIntent();
        String action = intent.getAction();

        mHeaderFragment = (HeaderFragment) getSupportFragmentManager().findFragmentById(R.id.common_header);
        mGroupListFragment = (GroupListFragment) getSupportFragmentManager().findFragmentById(R.id
                .group_list_fragment);

        if (action.equals(ACTION_SELECT)) {
            mHeaderFragment.setTitle(getString(R.string.txt_select_group));
            mHeaderFragment.setRightButtonType(HeaderFragment.ButtonType.INVISIBLE,
                    HeaderFragment.ButtonType.INVISIBLE, HeaderFragment.ButtonType.INVISIBLE);
            mHeaderFragment.setOnButtonListener(new HeaderFragment.OnButtonListener() {
                @Override
                public void onLeftBtnClicked() {
                    onBackPressed();
                }

                @Override
                public void onRightBtnClicked(HeaderFragment.RightBtnId id) {
                    // do nothing here
                }
            });

            mGroupListFragment.setOnGroupSelectedListener(new GroupListFragment.OnGroupSelectedListener() {
                @Override
                public void onGroupSelected(Group group) {
                    Intent intent = new Intent(GroupManageActivity.this, EditPostActivity.class);
                    intent.putExtra("from", getString(R.string.txt_select_group));
                    intent.putExtra("gid", group.getId());
                    startActivity(intent);
                }
            });
        } else if (action.equals(ACTION_MANAGE)) {
            mHeaderFragment.setTitle(getString(R.string.txt_manage_group));

            mHeaderFragment.setRightButtonType(HeaderFragment.ButtonType.ICON,
                    HeaderFragment.ButtonType.INVISIBLE, HeaderFragment.ButtonType.INVISIBLE);
            mHeaderFragment.setOnButtonListener(new HeaderFragment.OnButtonListener() {
                @Override
                public void onLeftBtnClicked() {
                    onBackPressed();
                }

                @Override
                public void onRightBtnClicked(HeaderFragment.RightBtnId id) {
                    Intent intent = new Intent(GroupManageActivity.this, CreateGroupActivity.class);
                    startActivity(intent);
                }
            });

            mGroupListFragment.setOnGroupSelectedListener(new GroupListFragment.OnGroupSelectedListener() {
                @Override
                public void onGroupSelected(Group group) {
//                    Intent intent = new Intent(GroupManageActivity.this, CreateGroupActivity.class);
//                    startActivity(intent);
                }
            });
        }
    }
}