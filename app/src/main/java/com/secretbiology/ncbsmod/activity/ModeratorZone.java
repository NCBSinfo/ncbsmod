package com.secretbiology.ncbsmod.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.secretbiology.ncbsmod.R;
import com.secretbiology.ncbsmod.adapters.ModeratorListAdapter;
import com.secretbiology.ncbsmod.constants.Network;
import com.secretbiology.ncbsmod.helpers.DividerDecoration;

import java.util.ArrayList;
import java.util.List;

public class ModeratorZone extends AppCompatActivity {

    public static final String IS_MODERATOR = "isMod";

    SharedPreferences pref;
    TextView modName, modEmail;
    Button listButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moderator_zone);

        pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        if(!pref.getBoolean(IS_MODERATOR,false)){
            finish();
            startActivity(new Intent(this, Login.class));
        }

        listButton = (Button)findViewById(R.id.mod_userlist);

        modName = (TextView)findViewById(R.id.mod_name);
        modEmail = (TextView)findViewById(R.id.mod_email);
        modName.setText(pref.getString(Login.NAME,"User"));
        modEmail.setText(pref.getString(Login.EMAIL,"email@domain.com"));
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.moderator_recyclerView);
        final List<String[]> data = new ArrayList<>();
        String[] item = {pref.getString(Login.EXTRA_CODE,"None"),"Details of this activity", Network.CONFERENCE};
        data.add(item);

        if(pref.getString(Login.ACCESS_LEVEL,"1").equals("2")){
            String[] item2 = {"PUBLIC","All users", Network.CONFERENCE};
            data.add(item2);
        }

        final ModeratorListAdapter adaper = new ModeratorListAdapter(data);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ModeratorZone.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adaper);
        RecyclerView.ItemDecoration itemDecoration = new DividerDecoration(ModeratorZone.this, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        adaper.setOnItemClickListener(new ModeratorListAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent intent = new Intent(ModeratorZone.this, TopicWindow.class);
                intent.putExtra(TopicWindow.INDENT_TOPICNAME,data.get(position)[2]);
                intent.putExtra(TopicWindow.INDENT_EXTERNALCODE,data.get(position)[0]);
                startActivity(intent);
            }
        });

        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ModeratorZone.this, UserList.class));
            }
        });

    }
}
