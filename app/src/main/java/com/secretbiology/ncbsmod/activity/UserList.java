package com.secretbiology.ncbsmod.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.secretbiology.ncbsmod.R;
import com.secretbiology.ncbsmod.adapters.UserListAdaper;
import com.secretbiology.ncbsmod.database.ExternalData;
import com.secretbiology.ncbsmod.helpers.DividerDecoration;
import com.secretbiology.ncbsmod.models.ExternalDataModel;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class UserList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.user_list_recyclers_view);
        final List<ExternalDataModel> data = new ExternalData(getBaseContext()).getAll();

        Collections.sort(data, new Comparator<ExternalDataModel>(){
            @Override
            public int compare(ExternalDataModel lhs, ExternalDataModel rhs) {
                String entry1 = lhs.getName();
                String entry2 = rhs.getName();
                return entry1.compareTo(entry2);
            }
        });

        UserListAdaper adaper = new UserListAdaper(data);
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
                intent.putExtra(UserWindow.INDENTCODE,String.valueOf(data.get(position).getId()));
                startActivity(intent);
            }
        });
    }
}
