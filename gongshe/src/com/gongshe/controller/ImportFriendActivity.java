package com.gongshe.controller;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.gongshe.R;
import com.gongshe.model.FriendManager;
import com.gongshe.model.OnUpdateListener;
import com.gongshe.model.User;
import com.gongshe.model.UserManager;

import java.util.ArrayList;
import java.util.List;

public class ImportFriendActivity extends FragmentActivity {

    private HeaderFragment mHeaderFragment;
    private ListView mContactListView;
    private List<FetchContactWorker.ContactInfo> mContactInfoList;
    private FriendListAdapter mAdapter;
    private List<User> mPotentialFriendList;

    static final int MESSAGE_DATA_READY = 100;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MESSAGE_DATA_READY) {
                checkFriendsByPhone();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_invitation);

        mHeaderFragment = (HeaderFragment) getSupportFragmentManager().findFragmentById(R.id.common_header);
        mHeaderFragment.setOnButtonListener(new HeaderFragment.OnButtonListener() {
            @Override
            public void onBtnCLicked(HeaderFragment.BtnId id) {
                onBackPressed();
            }
        });

        mPotentialFriendList = new ArrayList<User>();
        mContactInfoList = new ArrayList<FetchContactWorker.ContactInfo>();
        mAdapter = new FriendListAdapter(getApplicationContext(), mPotentialFriendList);
        mAdapter.setDisplayMode(FriendListAdapter.DisplayMode.NONE);
        mContactListView = (ListView) findViewById(R.id.contact_list);
        mContactListView.setAdapter(mAdapter);

        mContactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User friend = mAdapter.getItem(position);
                FriendManager.getInstance().addFriendById(friend.getId(), new OnUpdateListener() {
                    @Override
                    public void onUpdate() {
                        onBackPressed();
                    }

                    @Override
                    public void onError() {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }
                });
            }
        });
        getContactData();
    }

    private void getContactData() {
        mContactInfoList.clear();
        new FetchContactWorker(getApplicationContext(), mHandler, mContactInfoList).start();
    }


    private void checkFriendsByPhone() {
        List<String> phoneList = new ArrayList<String>();
        for (FetchContactWorker.ContactInfo contactInfo : mContactInfoList) {
            phoneList.add(contactInfo.getPhoneNumber());
        }

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.txt_loading));
        dialog.setCancelable(false);
        dialog.show();

        UserManager.getInstance().fetchUserListByPhone(phoneList, mPotentialFriendList, new OnUpdateListener() {
            @Override
            public void onUpdate() {
                dialog.dismiss();
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError() {
                dialog.dismiss();
            }
        });
    }
}