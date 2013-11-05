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

public class ChatContentListAdapter extends BaseAdapter {

    static public class ChatMessage {
        int mOrientation; // to = 0, from =1.
        String mMessage;
        String mSender;
        String mIconUrl;

        public ChatMessage(int orientation, String message, String sender) {
            mOrientation = orientation;
            mMessage = message;
            mSender = sender;
        }
    }

    Context mContext;
    ArrayList<ChatMessage> mMessageList;

    public ChatContentListAdapter(Context context) {
        mContext = context;
        mMessageList = new ArrayList<ChatMessage>();
        initFakeData();
    }

    private void initFakeData() {
        mMessageList.add(new ChatMessage(0, "hello!", "Yafan"));
        mMessageList.add(new ChatMessage(1, "hi", "Xu Hui"));
        mMessageList.add(new ChatMessage(0, "How are u?", "Yafan"));
        mMessageList.add(new ChatMessage(1, "Fine, Thanks.", "Xu hui"));
        mMessageList.add(new ChatMessage(0, "This section discusses how to define custom attributes and specify their values. The next section deals with retrieving and applying the values at runtime.", "yafan"));
    }

    @Override
    public int getCount() {
        return mMessageList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMessageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ChatMessage message = (ChatMessage) getItem(position);
        if (view == null || (Integer) view.getTag() != message.mOrientation) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            if (message.mOrientation == 0) {
                view = inflater.inflate(R.layout.chat_item_to, parent, false);
            } else {
                view = inflater.inflate(R.layout.chat_item_from, parent, false);
            }
            view.setTag(message.mOrientation);
        }
        TextView txv_message = (TextView) view.findViewById(R.id.txv_chat_message);
        txv_message.setText(message.mMessage);
        ImageView iconView = (ImageView) view.findViewById(R.id.imv_chat_icon);
        if (message.mIconUrl == null) {
            iconView.setImageResource(R.drawable.icon);
        } else {
            // todo put real code here.
            iconView.setImageResource(R.drawable.icon);
        }
        return view;
    }
}
