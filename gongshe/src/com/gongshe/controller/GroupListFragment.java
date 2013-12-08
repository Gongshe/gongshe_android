package com.gongshe.controller;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.gongshe.R;
import com.gongshe.model.Group;
import com.gongshe.model.GroupManager;

import java.util.ArrayList;
import java.util.List;

public class GroupListFragment extends Fragment {

    private enum Mode {
        MENU,
        ACTIVITY;
    }

    public interface OnGroupSelectedListener {
        public void onGroupSelected(Group group);
    }

    private static class TagGroup extends Group {
        public String tag;
    }

    private List<Group> mMyGroupList;
    private List<Group> mBelongGroupList;
    private List<Group> mGroupList = new ArrayList<Group>();

    private Mode mMode;

    private GroupListAdapter mGroupAdapter;
    private ListView mGroupListView;

    private OnGroupSelectedListener mOnGroupSelectedListener;
    private MenuListFragment.OnSpecialMenuListener mSpecialMenuListener;

    private ImageView mIconDown;

    private GroupManager.OnGroupListUpdateListener mDataChangeListener = new GroupManager.OnGroupListUpdateListener() {
        @Override
        public void onGroupListUpdate() {
            setupGroupList();
            mGroupAdapter.notifyDataSetChanged();
            updateIconDown();
        }
    };

    public void setIconDown(ImageView iconDown) {
        mIconDown = iconDown;
        mIconDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGroupListView.smoothScrollByOffset(3);
            }
        });
    }

    private void updateIconDown() {
        if (mMode == Mode.MENU) {
            int start = mGroupListView.getFirstVisiblePosition();
            int end = mGroupListView.getLastVisiblePosition();
            if (start > 0 || end < (mGroupList.size() - 1)) {
                mIconDown.setVisibility(View.VISIBLE);
            } else {
                mIconDown.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);
        TypedArray styles = activity.obtainStyledAttributes(attrs, R.styleable.GroupListFragment);
        mMode = Mode.values()[styles.getInt(R.styleable.GroupListFragment_group_list_mode, 0)];
        styles.recycle();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        if (mMode == Mode.MENU) {
            view = inflater.inflate(R.layout.group_list_menu, container, false);
        } else {
            view = inflater.inflate(R.layout.group_list, container, false);
        }
        mGroupListView = (ListView) view.findViewById(R.id.lsv_group);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mMyGroupList = GroupManager.getInstance()
                                  .getMyGroup();
        mBelongGroupList = GroupManager.getInstance()
                                      .getBelongGroup();
        setupGroupList();
        // setup my group
        mGroupAdapter = new GroupListAdapter(getActivity(), mGroupList);
        mGroupListView.setAdapter(mGroupAdapter);

        mGroupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Group group = mGroupAdapter.getItem(position);
                if (!(group instanceof TagGroup)) {
                    if (mOnGroupSelectedListener != null) {
                        mOnGroupSelectedListener.onGroupSelected(group);
                    }
                } else {
                     if (mMode == Mode.MENU && ((TagGroup) group).tag.equals(getString(R.string.txt_group_manage)))  {
                         if (mSpecialMenuListener != null) {
                             mSpecialMenuListener.onSpecialMenu(MenuListFragment.SpecialMenuType.GROUP_MANAGE);
                         }
                     }
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        GroupManager.getInstance()
                   .setGroupListUpdateListener(mDataChangeListener);
        // update group data
        GroupManager.getInstance()
                   .updateMyGroup(null);
        GroupManager.getInstance()
                   .updateBelongGroup(null);
        updateIconDown();
    }

    @Override
    public void onStop() {
        super.onStop();
        GroupManager.getInstance()
                   .setGroupListUpdateListener(null);
    }

    private void setupGroupList() {
        mGroupList.clear();
        if (!mMyGroupList.isEmpty()) {
            TagGroup tagGroup = new TagGroup();
            tagGroup.tag = getString(R.string.txt_my_group);
            tagGroup.setId(0);
            mGroupList.add(tagGroup);
            mGroupList.addAll(mMyGroupList);
        }
        if (!mBelongGroupList.isEmpty()) {
            TagGroup tagGroup = new TagGroup();
            tagGroup.tag = getString(R.string.txt_belong_group);
            tagGroup.setId(0);
            mGroupList.add(tagGroup);
            mGroupList.addAll(mBelongGroupList);
        }
        if (mMode == Mode.MENU) {
            TagGroup tagGroup = new TagGroup();
            tagGroup.tag = getString(R.string.txt_group_manage);
            tagGroup.setId(0);
            mGroupList.add(tagGroup);
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
                    if (mMode == Mode.MENU) {
                        convertView = LayoutInflater.from(mContext)
                                                    .inflate(R.layout.menu_item_tag, null);
                    } else {
                        convertView = LayoutInflater.from(mContext)
                                                    .inflate(R.layout.group_item_tag, null);
                    }
                }
                if (mMode == Mode.MENU) {
                    TextView textView = (TextView) convertView.findViewById(R.id.txv_menu_name);
                    textView.setText(((TagGroup) group).tag);
                } else {
                    TextView textView = (TextView) convertView;
                    textView.setText(((TagGroup) group).tag);
                }
                convertView.setTag(true);
                return convertView;
            }

            if (convertView == null || (Boolean) convertView.getTag()) {
                if (mMode == Mode.MENU) {
                    convertView = LayoutInflater.from(mContext)
                                                .inflate(R.layout.menu_item, null);
                } else {
                    convertView = LayoutInflater.from(mContext)
                                                .inflate(R.layout.group_list_item, null);
                }
            }
            if (mMode == Mode.MENU) {
                TextView title = (TextView) convertView.findViewById(R.id.txv_menu_name);
                title.setText(group.getName());
            } else {
                //TODO implement upload and download image function
                //            ImageView icon = (ImageView) convertView.findViewById(R.id.row_icon);
//            icon.setImageResource(android.R.drawable.ic_media_play);
                TextView title = (TextView) convertView.findViewById(R.id.row_title);
                title.setText(group.getName());
                TextView txvTimeStamp = (TextView) convertView.findViewById(R.id.txv_time_stamp);
                txvTimeStamp.setText(group.getTime()
                                          .substring(0, 10));
            }
            convertView.setTag(false);
            return convertView;
        }
    }

    public void setOnGroupSelectedListener(OnGroupSelectedListener groupSelectedListener,
                                           MenuListFragment.OnSpecialMenuListener specialMenuListener) {
        mOnGroupSelectedListener = groupSelectedListener;
        mSpecialMenuListener = specialMenuListener;
    }
}
