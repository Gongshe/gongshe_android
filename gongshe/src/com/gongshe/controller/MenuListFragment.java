package com.gongshe.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.gongshe.R;
import com.gongshe.model.Group;
import com.gongshe.model.UserManager;

import java.util.Collections;
import java.util.List;

public class MenuListFragment extends Fragment {

    private Button mBtnGroupManage;
    private ListView mSpecialListView;
    private ListView mMygroupListView;
    private ListView mBelongGroupListView;
    private TextView mTxvMyGroup;
    private TextView mTxvBelongGroup;
    private SpecialMenuAdapter mSpecialMenuAdapter;
    private GroupListAdapter mMyGroupAdapter;
    private GroupListAdapter mBelongGroupAdapter;
    private UserManager.OnDataChangeListener mDataChangeListener = new UserManager.OnDataChangeListener() {
        @Override
        public void onDataChanged() {
            mMyGroupAdapter.notifyDataSetChanged();
            mBelongGroupAdapter.notifyDataSetChanged();
        }
    };

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.left_menu_list, null);

        mBtnGroupManage = (Button) view.findViewById(R.id.btn_group_manage);
        mBtnGroupManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GroupManageActivity.class);
                getActivity().startActivity(intent);
            }
        });

        mSpecialListView = (ListView) view.findViewById(R.id.list_menu_special);
        mMygroupListView = (ListView) view.findViewById(R.id.list_menu_my_group);
        mBelongGroupListView = (ListView) view.findViewById(R.id.list_menu_belong_group);
        mTxvMyGroup = (TextView) view.findViewById(R.id.txv_my_group);
        mTxvBelongGroup = (TextView) view.findViewById(R.id.txv_belong_group);
        return view;
	}

    @Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
        // setup special
		mSpecialMenuAdapter = new SpecialMenuAdapter(getActivity());
        mSpecialListView.setAdapter(mSpecialMenuAdapter);
        // setup my group
        mMyGroupAdapter = new GroupListAdapter(getActivity(), UserManager.getInstance().getMyGroup());
        mMygroupListView.setAdapter(mMyGroupAdapter);
        // setup belong group
        mBelongGroupAdapter = new GroupListAdapter(getActivity(), UserManager.getInstance().getBelongGroup());
        mBelongGroupListView.setAdapter(mBelongGroupAdapter);

        // setup data change listener
        UserManager.getInstance().registerDataChangeListener(mDataChangeListener);

        // update group data
        UserManager.getInstance().updateMyGroup(null);
        UserManager.getInstance().updateBelongGroup(null);
	}

	private class LeftMenuItem {
		public String tag;
		public int iconRes;
		public LeftMenuItem(String tag, int iconRes) {
			this.tag = tag; 
			this.iconRes = iconRes;
		}
	}

	private class SpecialMenuAdapter extends ArrayAdapter<LeftMenuItem> {

		public SpecialMenuAdapter(Context context) {
			super(context, 0);
            add(new LeftMenuItem(getString(R.string.menu_all_involved_me), android.R.drawable.ic_dialog_info));
            add(new LeftMenuItem(getString(R.string.menu_all_mention_me), android.R.drawable.ic_menu_view));
        }

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.row, null);
			}
			ImageView icon = (ImageView) convertView.findViewById(R.id.row_icon);
			icon.setImageResource(getItem(position).iconRes);
			TextView title = (TextView) convertView.findViewById(R.id.row_title);
			title.setText(getItem(position).tag);
			return convertView;
		}
	}

    private class GroupListAdapter extends BaseAdapter {
        private Context mContext;
        private List<Group> mGroupList;

        public GroupListAdapter(Context context, List<Group> groupList) {
            mContext = context;
            mGroupList = groupList;
        }

        @Override
        public int getCount() {
            if (mGroupList != null && !mGroupList.isEmpty()) {
                return mGroupList.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (mGroupList != null && !mGroupList.isEmpty()) {
                return mGroupList.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            if (mGroupList != null && !mGroupList.isEmpty()) {
                return mGroupList.get(position).getId();
            }
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.row, null);
            }
            Group group = (Group) getItem(position);
            ImageView icon = (ImageView) convertView.findViewById(R.id.row_icon);
            icon.setImageResource(android.R.drawable.ic_media_play);
            TextView title = (TextView) convertView.findViewById(R.id.row_title);
            title.setText(group.getName());
            return convertView;
        }
    }
}
