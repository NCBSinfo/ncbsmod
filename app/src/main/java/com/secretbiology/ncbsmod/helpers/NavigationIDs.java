package com.secretbiology.ncbsmod.helpers;

import android.app.Activity;
import android.content.Intent;

import com.secretbiology.ncbsmod.Home;
import com.secretbiology.ncbsmod.R;
import com.secretbiology.ncbsmod.dashboard.Dashboard;

public class NavigationIDs {
    int resourceID;
    Activity activity;

    public NavigationIDs(int resourceID, Activity activity) {
        this.resourceID = resourceID;
        this.activity = activity;
    }

    public Intent getIntent()
    {
        switch (resourceID) {
            case R.id.nav_home:
                return new Intent(activity, Home.class);
            case R.id.nav_dashboard:
                return new Intent(activity, Dashboard.class);
            default:
                return new Intent(activity, Home.class);
        }
    }
}
