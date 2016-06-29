package com.secretbiology.ncbsmod.dashboard;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;

import com.secretbiology.ncbsmod.BaseActivity;
import com.secretbiology.ncbsmod.R;
import com.secretbiology.ncbsmod.helpers.DividerDecoration;
import com.secretbiology.ncbsmod.helpers.Utilities;
import com.secretbiology.ncbsmod.interfaces.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Dashboard extends BaseActivity implements User {

    //Activity specific settings
    static {
        menuItem = R.id.nav_send;
    }

    private final String TAG = getClass().getSimpleName();
    SharedPreferences pref;
    RecyclerView recyclerView;
    List<DashboardModel> dashList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewStub viewStub = (ViewStub) findViewById(R.id.baseView);
        viewStub.setLayoutResource(R.layout.dashboard);
        viewStub.inflate();
        mProgressDialog = new ProgressDialog(Dashboard.this);
        pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        dashList = new ArrayList<>();
        String[] topicList = new Utilities().stringToarray(pref.getString(profile.TOPICS,"{\"public\"}"));
        if(topicList.length>0) {
            for (String topic : topicList) {
                Log.i("Tag", topic+" name");
                String[] tempArray = new Utilities().topicConveter(topic);
                DashboardModel dash = new DashboardModel(tempArray[0], tempArray[1]);
                dashList.add(dash);
            }
        }
        recyclerView = (RecyclerView) findViewById(R.id.dashboard_recycleview);
        DashboardAdapter adapter = new DashboardAdapter(dashList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerDecoration(this, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        adapter.setOnItemClickListener(new DashboardAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i("clicked", position+"");
            }
        });
    }

}
