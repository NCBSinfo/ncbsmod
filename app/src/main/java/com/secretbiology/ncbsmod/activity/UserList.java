package com.secretbiology.ncbsmod.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.secretbiology.ncbsmod.Home;
import com.secretbiology.ncbsmod.R;
import com.secretbiology.ncbsmod.adapters.UserListAdaper;
import com.secretbiology.ncbsmod.constants.Preferences;
import com.secretbiology.ncbsmod.database.ExternalData;
import com.secretbiology.ncbsmod.helpers.DividerDecoration;
import com.secretbiology.ncbsmod.models.ExternalDataModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UserList extends AppCompatActivity {

    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.user_list_recyclers_view);
        final List<ExternalDataModel> data = new ExternalData(getBaseContext()).getAll();

        TextView lastData = (TextView)findViewById(R.id.last_data_fetch);
        pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        if (lastData != null) {
            lastData.setText("Last updated: "+ pref.getString(Preferences.LAST_FETCHED, "Never"));
        }

        Collections.sort(data, new Comparator<ExternalDataModel>(){
            @Override
            public int compare(ExternalDataModel lhs, ExternalDataModel rhs) {
                String entry1 = lhs.getName();
                String entry2 = rhs.getName();
                return entry1.compareTo(entry2);
            }
        });

        final List<ExternalDataModel> refinedList = new ArrayList<>();

        for(ExternalDataModel a : data){
            if(a.getExternalCode().equals(PreferenceManager.
                getDefaultSharedPreferences(getBaseContext()).getString(Login.EXTRA_CODE,"NONE"))){
                refinedList.add(a);
            }
        }
        if(pref.getString(Login.ACCESS_LEVEL,"1").equals("2")){
            refinedList.clear();
            for(ExternalDataModel a : data){
                refinedList.add(a);
            }
        }

        UserListAdaper adaper = new UserListAdaper(refinedList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(UserList.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adaper);
        RecyclerView.ItemDecoration itemDecoration = new DividerDecoration(UserList.this, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        adaper.setOnItemClickListener(new UserListAdaper.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent intent = new Intent(UserList.this, UserWindow.class);
                intent.putExtra(UserWindow.INDENTCODE,String.valueOf(refinedList.get(position).getId()));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Home.class));
    }
}
