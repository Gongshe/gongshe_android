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
        public void onItemSelected();
    }

    OnMenuItemSelectedListener mOnMenuItemSelectedListener;
    Button mBtnGroupManage;
    Button mBtnPersonalInfo;

    public void setOnMenuItemSelectedListener (OnMenuItemSelectedListener listener) {
        mOnMenuItemSelectedListener = listener;
    }

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.list, null);

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
		SampleAdapter adapter = new SampleAdapter(getActivity());
		for (int i = 0; i < 20; i++) {
			adapter.add(new SampleItem("Sample List", android.R.drawable.ic_menu_search));
		}
		setListAdapter(adapter);
	}

	private class SampleItem {
		public String tag;
		public int iconRes;
		public SampleItem(String tag, int iconRes) {
			this.tag = tag; 
			this.iconRes = iconRes;
		}
	}

	public class SampleAdapter extends ArrayAdapter<SampleItem> {

		public SampleAdapter(Context context) {
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
            mOnMenuItemSelectedListener.onItemSelected();
        }
    }
}
