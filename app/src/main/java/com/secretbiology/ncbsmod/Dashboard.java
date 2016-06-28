package com.secretbiology.ncbsmod;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.ViewStub;

public class Dashboard extends BaseActivity {

    //Activity specific settings
    static {
        menuItem = R.id.nav_send;
    }

    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewStub viewStub = (ViewStub) findViewById(R.id.baseView);
        viewStub.setLayoutResource(R.layout.dashboard);
        viewStub.inflate();
        mProgressDialog = new ProgressDialog(Dashboard.this);
    }

}
