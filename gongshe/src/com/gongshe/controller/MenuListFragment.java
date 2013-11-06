package com.gongshe.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.gongshe.R;

public class MenuListFragment extends ListFragment {

    public interface OnMenuItemSelectedListener {
        public void onItemSelected(int position);
    }

    OnMenuItemSelectedListener mOnMenuItemSelectedListener;
    Button mBtnGroupManage;
    Button mBtnPersonalInfo;

    public void setOnMenuItemSelectedListener (OnMenuItemSelectedListener listener) {
        mOnMenuItemSelectedListener = listener;
    }

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

        mBtnPersonalInfo = (Button) view.findViewById(R.id.btn_personal_info);
        mBtnPersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PersonInfoActivity.class);
                getActivity().startActivity(intent);
            }
        });
        return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		LeftMenuAdapter adapter = new LeftMenuAdapter(getActivity());
        initMenuItem(adapter);
		setListAdapter(adapter);
	}

	private class LeftMenuItem {
		public String tag;
		public int iconRes;
		public LeftMenuItem(String tag, int iconRes) {
			this.tag = tag; 
			this.iconRes = iconRes;
		}
	}

    private void initMenuItem(LeftMenuAdapter adapter) {
        adapter.add(new LeftMenuItem(getString(R.string.menu_all_activities), android.R.drawable.ic_dialog_email));
        adapter.add(new LeftMenuItem(getString(R.string.menu_all_involved_me), android.R.drawable.ic_dialog_info));
        adapter.add(new LeftMenuItem(getString(R.string.menu_all_mention_me), android.R.drawable.ic_menu_view));
        // todo fake group
        adapter.add(new LeftMenuItem("行行摄色", android.R.drawable.ic_menu_camera));
        adapter.add(new LeftMenuItem("荐书", android.R.drawable.ic_menu_agenda));
    }

	public class LeftMenuAdapter extends ArrayAdapter<LeftMenuItem> {

		public LeftMenuAdapter(Context context) {
			super(context, 0);
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

    public void onListItemClick(ListView l, View v, int position, long id) {
        if (mOnMenuItemSelectedListener != null) {
            mOnMenuItemSelectedListener.onItemSelected(position);
        }
    }
}
