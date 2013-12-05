package com.gongshe.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import com.gongshe.R;
import com.gongshe.model.GongSheConstant;
import com.gongshe.model.Group;
import com.gongshe.model.GroupManager;
import com.gongshe.view.SlidingMenu;

public class MainUIActivity extends FragmentActivity {

    private final static String TAG = MainUIActivity.class.getSimpleName();

    private Button mBtnMessage;
    private Button mBtnFriends;
    private Button mBtnActivities;
    private Button mBtnSettings;

    private HeaderFragment mTitleFrame;
    private MessageFragment mMessageFrame;
    private ContentFrameFragment mContentFrame;
    private FriendsFragment mFriendsFrame;
    private SettingFragment mSettingFrame;

    private SlidingMenu mSlideMenu;

    private GroupListFragment.OnGroupSelectedListener mOnGroupSelectedListener = new GroupListFragment.OnGroupSelectedListener() {
        @Override
        public void onGroupSelected(Group group) {
            mContentFrame.setGroupContent(group, false);
            updateContentFrame();
            mSlideMenu.showContent();
        }
    };

    private MenuListFragment.OnSpecialMenuListener mOnSpecialMenuListener = new MenuListFragment.OnSpecialMenuListener() {
        @Override
        public void onSpecialMenu(MenuListFragment.SpecialMenuType menuType) {
            Group group = null;
            if (menuType == MenuListFragment.SpecialMenuType.AT_ME) {
                group = GongSheConstant.ALL_AT_ME_GROUP;
            } else if (menuType == MenuListFragment.SpecialMenuType.INVOLVED_ME) {
                group = GongSheConstant.ALL_INVOLVED_GROUP;
            } else if (menuType == MenuListFragment.SpecialMenuType.GROUP_MANAGE) {
                Intent intent = new Intent(MainUIActivity.this, GroupManageActivity.class);
                intent.setAction(GroupManageActivity.ACTION_MANAGE);
                intent.putExtra("from", getString(R.string.txt_home_page));
                startActivity(intent);
                return;
            }
            mContentFrame.setGroupContent(group, false);
            updateContentFrame();
            mSlideMenu.showContent();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_ui);

        FragmentManager fragmentManager = getSupportFragmentManager();
        mTitleFrame = (HeaderFragment) fragmentManager.findFragmentById(R.id.common_header);

        mContentFrame = new ContentFrameFragment();

        mFriendsFrame = new FriendsFragment();
        mFriendsFrame.setDisplayMode(FriendListAdapter.DisplayMode.MESSAGE);

        mMessageFrame = new MessageFragment();

        mSettingFrame = new SettingFragment();

        setupUILayout();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // setup contentFrame
        if (mContentFrame.getCurrentGroup() == null) {
            mContentFrame.setGroupContent(GongSheConstant.ALL_ACTIVITY_GROUP, true);
        }

        // setup left_menu listener
        MenuListFragment menu = (MenuListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_menu_list);
        menu.setOnMenuListener(mOnSpecialMenuListener, mOnGroupSelectedListener);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        int gid = intent.getIntExtra("gid", -1);
        Group group = GroupManager.getInstance()
                                 .findGroupById(gid);
        mContentFrame.setGroupContent(group, true);
        updateContentFrame();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO:
    }

    private void setupUILayout() {
        // setup slide menu
        setUpSlideMenu();

        // set up activities frame
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content_frame, mContentFrame)
                .commit();

        // set title frame
        mTitleFrame.setOnButtonListener(new HeaderFragment.OnButtonListener() {
            @Override
            public void onBtnCLicked(HeaderFragment.BtnId id) {
                switch (id) {
                    case LEFT:
                        if (mSlideMenu != null) {
                            mSlideMenu.toggle();
                        }
                        break;
                    case RIGHT_ONE: {
                        Intent intent = null;
                        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
                        if (fragment == mContentFrame) {
                            Group group = mContentFrame.getCurrentGroup();
                            if (group.equals(GongSheConstant.ALL_AT_ME_GROUP) ||
                                    group.equals(GongSheConstant.ALL_INVOLVED_GROUP) ||
                                    group.equals(GongSheConstant.ALL_ACTIVITY_GROUP)) {
                                intent = new Intent(MainUIActivity.this, GroupManageActivity.class);
                                intent.setAction(GroupManageActivity.ACTION_SELECT);
                            } else {
                                intent = new Intent(MainUIActivity.this, EditPostActivity.class);
                            }
                            intent.putExtra("from", getString(R.string.txt_home_page));
                            intent.putExtra("gid", group.getId());
                            MainUIActivity.this.startActivity(intent);
                        } else if (fragment == mFriendsFrame) {
                            intent = new Intent(MainUIActivity.this, ImportFriendActivity.class);
                        }
                        if (intent != null) {
                            MainUIActivity.this.startActivity(intent);
                        }
                    }
                    break;
                    case RIGHT_TWO:
                        break;
                }
            }
        });

        mBtnFriends = (Button) findViewById(R.id.btn_friends);
        mBtnFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFriendsButton();
            }
        });

        mBtnMessage = (Button) findViewById(R.id.btn_message);
        mBtnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMessageButton();
            }
        });

        mBtnActivities = (Button) findViewById(R.id.btn_activities);
        mBtnActivities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onActivities();
            }
        });

        mBtnSettings = (Button) findViewById(R.id.btn_setting);
        mBtnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSettings();
            }
        });
    }

    private void setUpSlideMenu() {
        mSlideMenu = new SlidingMenu(this);
        mSlideMenu.setMode(SlidingMenu.LEFT);
        mSlideMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        mSlideMenu.setShadowWidthRes(R.dimen.shadow_width);
        mSlideMenu.setShadowDrawable(R.drawable.shadow);
        mSlideMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        mSlideMenu.setFadeDegree(0.35f);
        mSlideMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        mSlideMenu.setMenu(R.layout.left_menu);
    }

    private void onMessageButton() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentById(R.id.content_frame) != mMessageFrame) {
            fragmentManager.beginTransaction()
                           .replace(R.id.content_frame, mMessageFrame)
                           .commit();
            mTitleFrame.setTitle(getString(R.string.btn_message));

            mTitleFrame.setRightButtonType(HeaderFragment.ButtonType.VISIBLE,
                    HeaderFragment.ButtonType.INVISIBLE);
        }
    }

    private void onFriendsButton() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentById(R.id.content_frame) != mFriendsFrame) {
            fragmentManager.beginTransaction()
                           .replace(R.id.content_frame, mFriendsFrame)
                           .commit();
            mTitleFrame.setTitle(getString(R.string.btn_friends));
            mTitleFrame.setRightButtonType(HeaderFragment.ButtonType.VISIBLE,
                    HeaderFragment.ButtonType.INVISIBLE);
        }
    }

    private void onActivities() {
        mContentFrame.setGroupContent(GongSheConstant.ALL_ACTIVITY_GROUP, true);
        updateContentFrame();
    }

    private void updateContentFrame() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentById(R.id.content_frame) != mContentFrame) {
            fragmentManager.beginTransaction()
                           .replace(R.id.content_frame, mContentFrame)
                           .commit();
            mTitleFrame.setRightButtonType(HeaderFragment.ButtonType.VISIBLE,
                    HeaderFragment.ButtonType.INVISIBLE);
        }
        mTitleFrame.setTitle(mContentFrame.getContentName());
    }

    private void onSettings() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentById(R.id.content_frame) != mSettingFrame) {
            fragmentManager.beginTransaction()
                           .replace(R.id.content_frame, mSettingFrame)
                           .commit();
            mTitleFrame.setTitle(getString(R.string.btn_settings));
            mTitleFrame.setRightButtonType(HeaderFragment.ButtonType.INVISIBLE,
                    HeaderFragment.ButtonType.INVISIBLE);
        }
    }
}