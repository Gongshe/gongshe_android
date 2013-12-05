package com.gongshe.controller;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.gongshe.R;

public class MenuListFragment extends Fragment {

    public enum SpecialMenuType {
        AT_ME,
        INVOLVED_ME,
        GROUP_MANAGE;
    }

    public interface OnSpecialMenuListener {
        public void onSpecialMenu(SpecialMenuType menuType);
    }

    private ListView mSpecialListView;
    private SpecialMenuAdapter mSpecialMenuAdapter;
    private OnSpecialMenuListener mOnSpecialMenuListener;
    private GroupListFragment mGroupListFragment;

    private ImageView mIconDown;

    public void setOnMenuListener(OnSpecialMenuListener specialMenuListener,
                                  GroupListFragment.OnGroupSelectedListener groupSelectedListener) {
        mOnSpecialMenuListener = specialMenuListener;
        mGroupListFragment.setOnGroupSelectedListener(groupSelectedListener, specialMenuListener);
    }

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.left_menu_list, null);
        mSpecialListView = (ListView) view.findViewById(R.id.list_menu_special);
        mIconDown = (ImageView) view.findViewById(R.id.icon_down);
        return view;
	}

    @Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
        // setup special
		mSpecialMenuAdapter = new SpecialMenuAdapter(getActivity());
        mSpecialListView.setAdapter(mSpecialMenuAdapter);
        mSpecialListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    mOnSpecialMenuListener.onSpecialMenu(SpecialMenuType.AT_ME);
                } else if (position == 1) {
                    mOnSpecialMenuListener.onSpecialMenu(SpecialMenuType.INVOLVED_ME);
                }
            }
        });
        mGroupListFragment = (GroupListFragment) getFragmentManager().findFragmentById(R.id.group_list_fragment);
        mGroupListFragment.setIconDown(mIconDown);
	}

    private class SpecialMenuItem {
        public String tag;
        public SpecialMenuItem(String tag) {
            this.tag = tag;
        }
    }

    private class SpecialMenuAdapter extends ArrayAdapter<SpecialMenuItem> {

		public SpecialMenuAdapter(Context context) {
			super(context, 0);
            add(new SpecialMenuItem(getString(R.string.menu_all_mention_me)));
            add(new SpecialMenuItem(getString(R.string.menu_all_involved_me)));
        }

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.menu_item_tag, null);
			}
            View view = convertView.findViewById(R.id.view_divider);
            if (position == 0) {
                view.setVisibility(View.GONE);
            } else {
                view.setVisibility(View.VISIBLE);
            }
			TextView title = (TextView) convertView.findViewById(R.id.txv_menu_name);
			title.setText(getItem(position).tag);
			return convertView;
		}
	}
}
