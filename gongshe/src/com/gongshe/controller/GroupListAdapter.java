package com.gongshe.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.gongshe.R;

import java.util.ArrayList;

public class GroupListAdapter extends BaseAdapter {
    static public class GroupInfo {
        String mGroupName;

        public GroupInfo(String name) {
            mGroupName = name;
        }
    }

    Context mContext;
    ArrayList<GroupInfo> mGroupList;

    public GroupListAdapter(Context context) {
        mContext = context;
        mGroupList = new ArrayList<GroupInfo>();
        initFakeData();
    }

    @Override
    public int getCount() {
        return mGroupList.size();
    }

    @Override
    public Object getItem(int position) {
        return mGroupList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.group_list_item, parent, false);
        }
        GroupInfo groupInfo = (GroupInfo) getItem(position);
        TextView textView = (TextView) view.findViewById(R.id.txv_group_name);
        textView.setText(groupInfo.mGroupName);
        return view;
    }

    private void initFakeData() {
        mGroupList.add(new GroupInfo("group one"));
        mGroupList.add(new GroupInfo("group two"));
        mGroupList.add(new GroupInfo("group three"));
    }
}
