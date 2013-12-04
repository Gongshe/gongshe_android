package com.gongshe.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import com.gongshe.R;

public class PeopleGroupActivity extends FragmentActivity {

    private PeopleGroupAdapter mPeopleGroupAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.people_group);
        mPeopleGroupAdapter = new PeopleGroupAdapter(this);
        setUpList();

        HeaderFragment fragment = (HeaderFragment) getSupportFragmentManager().findFragmentById(R.id.common_header);
        fragment.setOnButtonListener(new HeaderFragment.OnButtonListener() {
            @Override
            public void onBtnCLicked(HeaderFragment.BtnId id) {
                onBackPressed();
            }
        });
    }

    private void setUpList() {
        GridView gridView = (GridView) findViewById(R.id.gdv_people_grid);
        gridView.setAdapter(mPeopleGroupAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PeopleGroupAdapter.PeopleInfo peopleInfo = (PeopleGroupAdapter.PeopleInfo) mPeopleGroupAdapter.getItem(position);
                if (peopleInfo instanceof PeopleGroupAdapter.PeopleInfoAddIcon) {
                    Intent intent = new Intent(PeopleGroupActivity.this, FriendListActivity.class);
                    PeopleGroupActivity.this.startActivity(intent);
                } else if (peopleInfo instanceof PeopleGroupAdapter.PeopleInfo) {
                    Intent intent = new Intent(PeopleGroupActivity.this, PersonInfoActivity.class);
                    PeopleGroupActivity.this.startActivity(intent);
                }
            }
        });
    }
}