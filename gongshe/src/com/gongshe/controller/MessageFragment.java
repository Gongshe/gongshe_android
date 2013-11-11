package com.gongshe.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.gongshe.R;

public class MessageFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message_frame, container, false);
        ListView listView = (ListView) view.findViewById(R.id.lsv_message_brief_list);
        listView.setAdapter(new MessageListAdapter(getActivity()));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (getActivity() != null) {
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    getActivity().startActivity(intent);
                }
            }
        });
        return view;
    }
}
