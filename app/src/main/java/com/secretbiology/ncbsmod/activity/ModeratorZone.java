package com.secretbiology.ncbsmod.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.secretbiology.ncbsmod.Home;
import com.secretbiology.ncbsmod.R;
import com.secretbiology.ncbsmod.adapters.ModeratorListAdapter;
import com.secretbiology.ncbsmod.constants.Network;
import com.secretbiology.ncbsmod.database.Database;
import com.secretbiology.ncbsmod.helpers.DividerDecoration;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ModeratorZone extends AppCompatActivity {

    public static final String IS_MODERATOR = "isMod";

    SharedPreferences pref;
    TextView modName, modEmail;
    Button listButton, changeUser;
    ImageView profilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moderator_zone);

        pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        if(!pref.getBoolean(IS_MODERATOR,false)){
            finish();
            startActivity(new Intent(this, Login.class));
        }
        profilePic = (ImageView)findViewById(R.id.mod_profile_ic);
        listButton = (Button)findViewById(R.id.mod_userlist);
        changeUser = (Button)findViewById(R.id.changeUser);

        String profilePicurl = pref.getString(Login.IMAGE_URL,"none");
        if(!profilePicurl.equals("none")){
            Picasso.with(getBaseContext()).load(profilePicurl).into(profilePic);
        }

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

        changeUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog alertDialog = new AlertDialog.Builder(
                        ModeratorZone.this).create();

                alertDialog.setTitle("Are you sure?");
                alertDialog.setMessage("If you change user, all previous user data will be erased .");
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        pref.edit().clear().apply();
                        new Database(getBaseContext()).recreate();
                        startActivity(new Intent(ModeratorZone.this, Home.class));
                    }
                });
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setCanceledOnTouchOutside(false);

                // Showing Alert Message
                alertDialog.show();
            }
        });

    }
}
