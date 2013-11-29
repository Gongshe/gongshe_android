package com.gongshe.controller;

import android.content.Context;
import android.content.Intent;
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
        INVOLVED_ME;
    }

    public interface OnSpecialMenuListener {
        public void onSpecialMenu(SpecialMenuType menuType);
    }

    private Button mBtnGroupManage;
    private ListView mSpecialListView;
    private SpecialMenuAdapter mSpecialMenuAdapter;
    private OnSpecialMenuListener mOnSpecialMenuListener;
    private GroupListFragment mGroupListFragment;

    public void setOnSpecialMenuListener(OnSpecialMenuListener listener) {
        mOnSpecialMenuListener = listener;
    }

    public void setOnGroupSelectedListener(GroupListFragment.OnGroupSelectedListener listener) {
        mGroupListFragment.setOnGroupSelectedListener(listener);
    }

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.left_menu_list, null);

        mBtnGroupManage = (Button) view.findViewById(R.id.btn_group_manage);
        mBtnGroupManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GroupManageActivity.class);
                intent.setAction(GroupManageActivity.ACTION_MANAGE);
                intent.putExtra("from", getActivity().getString(R.string.txt_home_page));
                getActivity().startActivity(intent);
            }
        });

        mSpecialListView = (ListView) view.findViewById(R.id.list_menu_special);
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
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.group_list_item, null);
			}
			ImageView icon = (ImageView) convertView.findViewById(R.id.row_icon);
			icon.setImageResource(android.R.drawable.ic_media_play);
			TextView title = (TextView) convertView.findViewById(R.id.row_title);
			title.setText(getItem(position).tag);
			return convertView;
		}
	}
}
