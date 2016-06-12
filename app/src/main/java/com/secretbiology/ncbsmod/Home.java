package com.secretbiology.ncbsmod;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.secretbiology.ncbsmod.activity.Login;
import com.secretbiology.ncbsmod.activity.ModeratorZone;
import com.secretbiology.ncbsmod.activity.UserList;
import com.secretbiology.ncbsmod.constants.Preferences;

public class Home extends AppCompatActivity {

    ImageView icon;
    TextView welcomeMessage, footnote;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        icon = (ImageView)findViewById(R.id.login_status);
        welcomeMessage = (TextView)findViewById(R.id.welcome_message);
        footnote = (TextView)findViewById(R.id.home_footnote);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);

        Button login = (Button)findViewById(R.id.LoginButton);
        if (login != null) {
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Home.this, ModeratorZone.class));
                }
            });
        }

        if(pref.getBoolean(ModeratorZone.IS_MODERATOR, false)){

            icon.setColorFilter(getResources().getColor(R.color.home_green));
            welcomeMessage.setText("Welcome back "+ pref.getString(Login.NAME,"Moderator"));
            footnote.setVisibility(View.INVISIBLE);
            login.setText("Enter");

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finishAffinity();
    }
}
