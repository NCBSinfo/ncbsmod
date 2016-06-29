package com.secretbiology.ncbsmod;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.secretbiology.ncbsmod.dashboard.Dashboard;
import com.secretbiology.ncbsmod.helpers.Utilities;
import com.secretbiology.ncbsmod.interfaces.Network;
import com.secretbiology.ncbsmod.interfaces.User;
import com.secretbiology.ncbsmod.login.Login;

import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.util.List;

public class Home extends AppCompatActivity implements User, Network {
    List<Proxy> proxyList;
    SharedPreferences pref;
    ImageView icon;
    TextView welcomeMessage, footnote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);

        ProxySelector defaultProxySelector = ProxySelector.getDefault();
        proxyList = defaultProxySelector.select(URI.create("http://www.google.com"));
        icon = (ImageView) findViewById(R.id.login_status);
        welcomeMessage = (TextView) findViewById(R.id.welcome_message);
        footnote = (TextView) findViewById(R.id.home_footnote);
        Button loginBtn = (Button) findViewById(R.id.LoginButton);

        if (pref.getBoolean(AUTHORIZED, false)) {
            icon.setColorFilter(getResources().getColor(R.color.home_green));
            icon.setImageResource(R.drawable.icon_verified);
            welcomeMessage.setText("Welcome back " + pref.getString(profile.NAME, "Moderator"));
            footnote.setVisibility(View.INVISIBLE);
            loginBtn.setText("Enter");

        }


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pref.getBoolean(AUTHORIZED, false)) {
                    startActivity(new Intent(Home.this, Dashboard.class));
                } else {
                    if (new Utilities().isNetworkAvailable(getBaseContext())) {
                        if (proxyList.size() > 0) {
                            if (proxyList.get(0).address() != null) {
                                new AlertDialog.Builder(Home.this)
                                        .setTitle("Proxy network warning!")
                                        .setMessage("It looks like you are using proxy network : \"" + proxyList.get(0).address().toString() +
                                                "\" . Unfortunately, our database currently don't support proxy. Please use non proxy network for login. " +
                                                "After login you can use any network :)")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                        .show();
                            } else {
                                startActivity(new Intent(Home.this, Login.class));
                            }
                        }
                    } else {
                        Toast.makeText(getBaseContext(), "No network", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
    }


}
