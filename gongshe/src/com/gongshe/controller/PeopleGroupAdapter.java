package com.gongshe.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.gongshe.R;

import java.util.ArrayList;

public class PeopleGroupAdapter extends BaseAdapter {

    static public class PeopleInfo {
        public interface OnClickListener {
            public void onClick();
        }
        private String mName;
        private int mIconRes;

        public PeopleInfo(String name, int iconRes) {
            mName = name;
            mIconRes = iconRes;
            if (mIconRes == 0) {
                mIconRes = R.drawable.icon;
            }
        }
    }

    static public class PeopleInfoAddIcon extends PeopleInfo  {
        public PeopleInfoAddIcon(String name, int iconRes) {
            super(name, iconRes);
        }
    }

    Context mContext;
    ArrayList<PeopleInfo> mPeopleGroupList;

    public PeopleGroupAdapter(Context context) {
        mContext = context;
        mPeopleGroupList = new ArrayList<PeopleInfo>();
        initFakeData();
    }

    @Override
    public int getCount() {
        return mPeopleGroupList.size();
    }

    @Override
    public Object getItem(int position) {
        return mPeopleGroupList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final PeopleInfo peopleInfo = (PeopleInfo) getItem(position);
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.people_group_item, parent, false);
        }
        ImageView imageView = (ImageView) view.findViewById(R.id.imv_people_icon);
        imageView.setImageResource(peopleInfo.mIconRes);
        TextView textView = (TextView) view.findViewById(R.id.txv_people_name);
        textView.setText(peopleInfo.mName);
        return view;
    }

    private void initFakeData() {
        mPeopleGroupList.add(new PeopleInfoAddIcon("", android.R.drawable.ic_menu_add));
        mPeopleGroupList.add(new PeopleInfo("hugh", R.drawable.icon));
        mPeopleGroupList.add(new PeopleInfo("xu hui", R.drawable.icon));
        mPeopleGroupList.add(new PeopleInfo("Yafan", R.drawable.icon));
        mPeopleGroupList.add(new PeopleInfo("Xu Kai", R.drawable.icon));
        mPeopleGroupList.add(new PeopleInfo("Wang Long", R.drawable.icon));
        mPeopleGroupList.add(new PeopleInfo("Jin Meng", R.drawable.icon));
        mPeopleGroupList.add(new PeopleInfo("Wang Nan", R.drawable.icon));
    }
}
