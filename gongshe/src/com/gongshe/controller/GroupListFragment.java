package com.gongshe.controller;

import android.content.Context;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.gongshe.R;
import com.gongshe.model.Group;
import com.gongshe.model.UserManager;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class GroupListFragment extends Fragment {

    public interface OnGroupSelectedListener {
        public void onGroupSelected(Group group);
    }

    private static class TagGroup extends Group {
        public String tag;
    }

    private List<Group> mMyGroupList;
    private List<Group> mBelongGroupList;
    private List<Group> mGroupList = new ArrayList<Group>();

    private GroupListAdapter mGroupAdapter;
    private ListView mGroupListView;

    private OnGroupSelectedListener mOnGroupSelectedListener;

    private UserManager.OnDataChangeListener mDataChangeListener = new UserManager.OnDataChangeListener() {
        @Override
        public void onDataChanged() {
            setupGroupList();
            mGroupAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.group_list, container, false);
        mGroupListView = (ListView) view.findViewById(R.id.lsv_group);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mMyGroupList = UserManager.getInstance().getMyGroup();
        mBelongGroupList = UserManager.getInstance().getBelongGroup();
        setupGroupList();
        // setup my group
        mGroupAdapter = new GroupListAdapter(getActivity(), mGroupList);
        mGroupListView.setAdapter(mGroupAdapter);
        mGroupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Group group = mGroupAdapter.getItem(position);
                if (mOnGroupSelectedListener != null) {
                    mOnGroupSelectedListener.onGroupSelected(group);
                }
            }
        });

        // setup data change listener
        UserManager.getInstance().registerDataChangeListener(mDataChangeListener);

        // update group data
        UserManager.getInstance().updateMyGroup(null);
        UserManager.getInstance().updateBelongGroup(null);
    }

    private void setupGroupList() {
        mGroupList.clear();
        if (!mMyGroupList.isEmpty()) {
            TagGroup tagGroup = new TagGroup();
            tagGroup.tag = getString(R.string.txt_my_group);
            tagGroup.setId(-1);
            mGroupList.add(tagGroup);
            mGroupList.addAll(mMyGroupList);
        }
        if (!mBelongGroupList.isEmpty()) {
            TagGroup tagGroup = new TagGroup();
            tagGroup.tag = getString(R.string.txt_belong_group);
            tagGroup.setId(-1);
            mGroupList.add(tagGroup);
            mGroupList.addAll(mBelongGroupList);
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
        public Group getItem(int position) {
            if (mGroupList != null && !mGroupList.isEmpty()) {
                return mGroupList.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            if (mGroupList != null && !mGroupList.isEmpty()) {
                return mGroupList.get(position)
                                 .getId();
            }
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Group group = getItem(position);
            boolean isTag = group instanceof TagGroup;

            if (isTag) {
                if (convertView == null || !(Boolean) convertView.getTag()) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.group_item_tag, null);
                }
                TextView textView = (TextView) convertView;
                textView.setText(((TagGroup) group).tag);
                convertView.setTag(true);
                return convertView;
            }

            if (convertView == null || (Boolean) convertView.getTag()) {
                convertView = LayoutInflater.from(mContext)
                                            .inflate(R.layout.group_list_item, null);
            }
//            ImageView icon = (ImageView) convertView.findViewById(R.id.row_icon);
//            icon.setImageResource(android.R.drawable.ic_media_play);
            TextView title = (TextView) convertView.findViewById(R.id.row_title);
            title.setText(group.getName());
            TextView txvTimeStamp = (TextView) convertView.findViewById(R.id.txv_time_stamp);
            txvTimeStamp.setText(group.getTime().substring(0, 10));
            return convertView;
        }
    }

    public void setOnGroupSelectedListener(OnGroupSelectedListener listener) {
        mOnGroupSelectedListener = listener;
    }
}
