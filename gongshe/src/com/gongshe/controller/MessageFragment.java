package com.gongshe.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.gongshe.R;

public class MessageFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message_frame, container, false);
        ListView listView = (ListView) view.findViewById(R.id.lsv_message_brief_list);
        listView.setAdapter(new MessageListAdapter(getActivity()));
        return view;
    }
}
