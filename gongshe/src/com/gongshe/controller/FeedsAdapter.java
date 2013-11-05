package com.gongshe.controller;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FeedsAdapter extends BaseAdapter {

    List<String> mData = new ArrayList<String>();
    Context mContext;

    public FeedsAdapter(Context context) {
        mContext = context;
        mData.add("feed1");
        mData.add("feed2");
        mData.add("feed3");
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mData.get(position).hashCode(); // todo
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) {
            textView = new TextView(mContext);
        } else {
            textView = (TextView) convertView;
        }
        textView.setText(mData.get(position));
        return textView;
    }
}
