package com.gongshe.controller;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.gongshe.R;
import com.gongshe.model.Group;
import com.gongshe.model.UserManager;

import java.util.List;

public class GroupListFragment extends Fragment {

    public interface OnGroupSelectedListener {
        public void onGroupSelected(Group group);
    }

    private ListView mMyGroupListView;
    private ListView mBelongGroupListView;

    private GroupListAdapter mMyGroupAdapter;
    private GroupListAdapter mBelongGroupAdapter;

    private OnGroupSelectedListener mOnGroupSelectedListener;

    private UserManager.OnDataChangeListener mDataChangeListener = new UserManager.OnDataChangeListener() {
        @Override
        public void onDataChanged() {
            if (mMyGroupAdapter.getCount() > 0) {
                View view = mMyGroupListView.getEmptyView();
                if (view != null) {
                    view.setVisibility(View.GONE);
                    mMyGroupListView.setEmptyView(null);
                }
            }
            if (mBelongGroupAdapter.getCount() > 0) {
                View view = mBelongGroupListView.getEmptyView();
                if (view != null) {
                    view.setVisibility(View.GONE);
                    mBelongGroupListView.setEmptyView(null);
                }
            }
            mMyGroupAdapter.notifyDataSetChanged();
            mBelongGroupAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.group_list, container, false);

        mMyGroupListView = (ListView) view.findViewById(R.id.list_menu_my_group);
        TextView textView = (TextView) view.findViewById(R.id.txv_empty_my_group);
        mMyGroupListView.setEmptyView(textView);

        mBelongGroupListView = (ListView) view.findViewById(R.id.list_menu_belong_group);
        textView = (TextView) view.findViewById(R.id.txv_empty_belong_group);
        mBelongGroupListView.setEmptyView(textView);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // setup my group
        mMyGroupAdapter = new GroupListAdapter(getActivity(), UserManager.getInstance().getMyGroup());
        mMyGroupListView.setAdapter(mMyGroupAdapter);
        mMyGroupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mOnGroupSelectedListener.onGroupSelected(mMyGroupAdapter.getItem(position));
            }
        });

        // setup belong group
        mBelongGroupAdapter = new GroupListAdapter(getActivity(), UserManager.getInstance().getBelongGroup());
        mBelongGroupListView.setAdapter(mBelongGroupAdapter);
        mBelongGroupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mOnGroupSelectedListener.onGroupSelected(mBelongGroupAdapter.getItem(position));
            }
        });

        // setup data change listener
        UserManager.getInstance().registerDataChangeListener(mDataChangeListener);

        // update group data
        UserManager.getInstance().updateMyGroup(null);
        UserManager.getInstance().updateBelongGroup(null);
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
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext)
                                            .inflate(R.layout.group_list_item, null);
            }
            Group group = getItem(position);
            ImageView icon = (ImageView) convertView.findViewById(R.id.row_icon);
            icon.setImageResource(android.R.drawable.ic_media_play);
            TextView title = (TextView) convertView.findViewById(R.id.row_title);
            title.setText(group.getName());
            return convertView;
        }
    }

    public void setOnGroupSelectedListener(OnGroupSelectedListener listener) {
        mOnGroupSelectedListener = listener;
    }
}
