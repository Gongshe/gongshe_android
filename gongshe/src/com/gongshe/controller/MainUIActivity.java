package com.gongshe.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import com.gongshe.R;
import com.gongshe.model.Group;
import com.gongshe.view.SlidingMenu;

public class MainUIActivity extends FragmentActivity {

    private final static String TAG = MainUIActivity.class.getSimpleName();

    private Button mBtnMessage;
    private Button mBtnFriends;
    private Button mBtnActivities;
    private Button mBtnSettings;

    private HeaderFragment mTitleFrame;
    private MessageFragment mMessageFrame;
    private ContentFrameFragment mActivitiesFrame;
    private FriendsFragment mFriendsFrame;
    private SettingFragment mSettingFrame;

    private SlidingMenu mSlideMenu;

    private GroupListFragment.OnGroupSelectedListener mOnGroupSelectedListener = new GroupListFragment.OnGroupSelectedListener() {
        @Override
        public void onGroupSelected(Group group) {
            onActivities();
            mTitleFrame.setTitle(group.getName());
            mSlideMenu.showContent();
        }
    };

    private MenuListFragment.OnSpecialMenuListener mOnSpecialMenuListener = new MenuListFragment.OnSpecialMenuListener() {
        @Override
        public void onSpecialMenu(MenuListFragment.SpecialMenuType menuType) {
            onActivities();
            if (menuType == MenuListFragment.SpecialMenuType.AT_ME) {
                mActivitiesFrame.setGroupInfo(ContentFrameFragment.GroupInfo.ALL_FEEDS);
                mTitleFrame.setTitle(getString(R.string.menu_all_mention_me));
            } else if (menuType == MenuListFragment.SpecialMenuType.INVOLVED_ME) {
                mActivitiesFrame.setGroupInfo(ContentFrameFragment.GroupInfo.ALL_INVOLVED);
                mTitleFrame.setTitle(getString(R.string.menu_all_involved_me));
            }
            mSlideMenu.showContent();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_ui);

        FragmentManager fragmentManager = getSupportFragmentManager();
        mTitleFrame = (HeaderFragment) fragmentManager.findFragmentById(R.id.common_header);

        mActivitiesFrame = new ContentFrameFragment();
        mActivitiesFrame.setGroupInfo(ContentFrameFragment.GroupInfo.ALL_FEEDS);

        mFriendsFrame = new FriendsFragment();
        mFriendsFrame.setDisplayMode(FriendListAdapter.DisplayMode.MESSAGE);

        mMessageFrame = new MessageFragment();

        mSettingFrame = new SettingFragment();

        setupUILayout();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // setup left_menu listener
        MenuListFragment menu = (MenuListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_menu_list);
        menu.setOnGroupSelectedListener(mOnGroupSelectedListener);
        menu.setOnSpecialMenuListener(mOnSpecialMenuListener);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO:
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
                                   .add(R.id.content_frame, mActivitiesFrame)
                                   .commit();
        // set title frame
        mTitleFrame.setOnButtonListener(new HeaderFragment.OnButtonListener() {
            @Override
            public void onLeftBtnClicked() {
                if (mSlideMenu != null) {
                    mSlideMenu.toggle();
                }
            }

            @Override
            public void onRightBtnClicked(HeaderFragment.RightBtnId id) {
                switch (id) {
                    case ONE:
                        Intent intent = new Intent(MainUIActivity.this, EditPostActivity.class);
                        MainUIActivity.this.startActivity(intent);
                        break;
                    case TWO:
                        break;
                    case TEXT:
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

            mTitleFrame.setRightButtonType(HeaderFragment.ButtonType.INVISIBLE,
                    HeaderFragment.ButtonType.ICON,
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
            mTitleFrame.setRightButtonType(HeaderFragment.ButtonType.INVISIBLE,
                    HeaderFragment.ButtonType.INVISIBLE,
                    HeaderFragment.ButtonType.TEXT);
        }
    }

    private void onActivities() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentById(R.id.content_frame) != mActivitiesFrame) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, mActivitiesFrame)
                    .commit();
            mTitleFrame.setTitle(getString(R.string.btn_activities));
            mTitleFrame.setRightButtonType(HeaderFragment.ButtonType.ICON,
                    HeaderFragment.ButtonType.INVISIBLE,
                    HeaderFragment.ButtonType.INVISIBLE);
        }
    }

    private void onSettings() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentById(R.id.content_frame) != mSettingFrame) {
            fragmentManager.beginTransaction()
                           .replace(R.id.content_frame, mSettingFrame)
                           .commit();
            mTitleFrame.setTitle(getString(R.string.btn_settings));
            mTitleFrame.setRightButtonType(HeaderFragment.ButtonType.INVISIBLE,
                    HeaderFragment.ButtonType.INVISIBLE,
                    HeaderFragment.ButtonType.INVISIBLE);
        }
    }
}