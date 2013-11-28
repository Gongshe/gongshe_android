package com.gongshe.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gongshe.R;
import com.gongshe.model.UserManager;

public class SettingFragment extends Fragment {
    private static final String TAG = SettingFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting, container, false);
        view.findViewById(R.id.btn_log_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogOut();
            }
        });
        return view;
    }

    private void onLogOut() {
        UserManager.getInstance().logOut();
        getActivity().finish();
    }
}
