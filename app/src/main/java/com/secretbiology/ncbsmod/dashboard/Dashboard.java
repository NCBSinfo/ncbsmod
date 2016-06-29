package com.secretbiology.ncbsmod.dashboard;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.secretbiology.ncbsmod.BaseActivity;
import com.secretbiology.ncbsmod.Home;
import com.secretbiology.ncbsmod.R;
import com.secretbiology.ncbsmod.database.Database;
import com.secretbiology.ncbsmod.helpers.DividerDecoration;
import com.secretbiology.ncbsmod.helpers.Utilities;
import com.secretbiology.ncbsmod.interfaces.User;
import com.secretbiology.ncbsmod.topics.TopicWindow;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends BaseActivity implements User {

    private final String TAG = getClass().getSimpleName();
    SharedPreferences pref;
    RecyclerView recyclerView;
    List<DashboardModel> dashList;
    TextView name, email;
    Button logOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewStub viewStub = (ViewStub) findViewById(R.id.baseView);
        viewStub.setLayoutResource(R.layout.dashboard);
        viewStub.inflate();
        pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        name = (TextView) findViewById(R.id.dashboard_name);
        email = (TextView) findViewById(R.id.dashboard_email);
        name.setText(pref.getString(profile.NAME, "User"));
        email.setText(pref.getString(profile.EMAIL, "email@domain.com"));

        dashList = new ArrayList<>();
        final String[] topicList = new Utilities().stringToarray(pref.getString(profile.TOPICS, "{\"public\"}"));
        if (topicList.length > 0) {
            for (String topic : topicList) {
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
                Intent intent = new Intent(Dashboard.this, TopicWindow.class);
                intent.putExtra(TopicWindow.INTENT, topicList[position]);
                startActivity(intent);
            }
        });

        logOut = (Button) findViewById(R.id.dashboard_logout);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(Dashboard.this)
                        .setTitle("Are you sure?")
                        .setMessage("This will erase all database and preferences.")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseAuth.getInstance().signOut();
                                pref.edit().clear().apply();
                                new Database(getBaseContext()).restartDatabase();
                                Intent intent = new Intent(Dashboard.this, Home.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setIcon(R.drawable.icon_warning)
                        .show();

            }
        });
    }

}
