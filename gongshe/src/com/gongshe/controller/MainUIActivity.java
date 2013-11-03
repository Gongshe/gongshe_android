package com.gongshe.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gongshe.R;
import com.gongshe.network.OkHttpStack;
import com.gongshe.view.SlidingMenu;

public class MainUIActivity extends FragmentActivity {
    private final static String TAG = MainUIActivity.class.getSimpleName();

    private ImageView mAppIcon;
    private ImageView mTitleBarRightIcon;
    private Button mBtnPost;
    private Button mBtnMessage;
    private Button mBtnFriends;
    private SlidingMenu mSlideMenu;
    private MessageFragment mMessageFrame;
    private ContentFrameFragment mContentFrame;
    private FriendsFragment mFriendsFrame;

    private MenuListFragment.OnMenuItemSelectedListener mOnMenuItemSelectedListener = new MenuListFragment.OnMenuItemSelectedListener() {
        @Override
        public void onItemSelected() {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
            if (mContentFrame != null && fragment != null && fragment != mContentFrame) {
                getSupportFragmentManager().beginTransaction()
                                           .replace(R.id.content_frame, mContentFrame)
                                           .commit();
            }
            mSlideMenu.showContent();
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_ui);
        mContentFrame = new ContentFrameFragment();
        mMessageFrame = new MessageFragment();
        mFriendsFrame = new FriendsFragment();
        setupUILayout();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setupUILayout() {
        setUpSlideMenu();
        getSupportFragmentManager().beginTransaction()
                                   .add(R.id.content_frame, mContentFrame)
                                   .commit();

        mAppIcon = (ImageView) findViewById(R.id.icon_title_bar);
        mTitleBarRightIcon = (ImageView) findViewById(R.id.icon_title_bar_right);

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

        mBtnPost = (Button) findViewById(R.id.btn_post);
        mBtnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPostButton();
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
        mSlideMenu.setMenu(R.layout.menu);
        // setup menu listener
        MenuListFragment menu = (MenuListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_menu_list);
        menu.setOnMenuItemSelectedListener(mOnMenuItemSelectedListener);
    }

    private void onMessageButton() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentById(R.id.content_frame) != mMessageFrame) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, mMessageFrame)
                    .commit();
        }
    }

    private void onPostButton() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentById(R.id.content_frame) != mContentFrame) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, mContentFrame)
                    .commit();
        }
        RequestQueue myQueue = Volley.newRequestQueue(this, new OkHttpStack());
        Request request = new StringRequest("http://www.baidu.com", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);
            }
        }, null);
        myQueue.add(request);
        myQueue.start();
    }

    private void onFriendsButton() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentById(R.id.content_frame) != mFriendsFrame) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, mFriendsFrame)
                    .commit();
        }
    }
}