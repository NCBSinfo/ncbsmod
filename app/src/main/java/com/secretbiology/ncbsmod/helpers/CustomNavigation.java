package com.secretbiology.ncbsmod.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.secretbiology.ncbsmod.R;
import com.secretbiology.ncbsmod.interfaces.User;

public class CustomNavigation implements User{

    NavigationView navigationView;
    Context context;
    SharedPreferences pref;

    public CustomNavigation(NavigationView navigationView, Context context) {
        this.navigationView = navigationView;
        this.context = context;
        this.pref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setUp() {
        View header = navigationView.getHeaderView(0);
        TextView name = (TextView) header.findViewById(R.id.Navigation_Name);
        TextView email = (TextView) header.findViewById(R.id.Navigation_Email);
        ImageView icon = (ImageView) header.findViewById(R.id.header_icon);
        if(pref.getBoolean(AUTHORIZED,false)) {
            if (name != null) {
                icon.setImageResource(R.drawable.icon_profile);
                name.setText(pref.getString(profile.NAME, "User Name"));
                email.setText(pref.getString(profile.EMAIL, "email@domain.com"));
                navigationView.inflateMenu(R.menu.base_drawer);
            }
        }
        else {
            if (name != null) {
                icon.setImageResource(R.drawable.icon_warning);
                name.setText("Unauthorized account");
                email.setText("please sign in");
                navigationView.inflateMenu(R.menu.unauthorized_menu);
            }
        }
    }
}
