package com.gongshe.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.gongshe.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class MessageListAdapter extends BaseAdapter {
    static public class MessageBrief {
        String mFrom;
        String mMessage;
        public MessageBrief(String from, String message) {
            mFrom = from;
            mMessage = message;
        }
    }

    private Context mContext;
    private ArrayList<MessageBrief> mMessageBriefList;

    public MessageListAdapter(Context context) {
        mContext = context;
        mMessageBriefList = new ArrayList<MessageBrief>();
        initFakeData();
    }

    @Override
    public int getCount() {
        return mMessageBriefList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMessageBriefList.get(position);
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
            view = inflater.inflate(R.layout.message_brief_item, parent, false);
        }
        MessageBrief messageBrief = (MessageBrief) getItem(position);
        TextView textView = (TextView) view.findViewById(R.id.txv_message_brief_from);
        textView.setText(messageBrief.mFrom);
        textView = (TextView) view.findViewById(R.id.txv_message_brief);
        textView.setText(messageBrief.mMessage);
        return view;
    }

    private void initFakeData() {
        mMessageBriefList.add(new MessageBrief("Yafan", "Hello world."));
        mMessageBriefList.add(new MessageBrief("xukai", "Thanks CCTV."));
        mMessageBriefList.add(new MessageBrief("Xu Hui", "hi hi hi"));
        mMessageBriefList.add(new MessageBrief("Yafan", "Hello world"));
    }
}
