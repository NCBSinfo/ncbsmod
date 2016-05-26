package com.secretbiology.ncbsmod;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.secretbiology.ncbsmod.activity.Login;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        Button debugBtn = (Button)findViewById(R.id.debug_button);
        if (debugBtn != null) {
            debugBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Home.this, Login.class));
                }
            });
        }
    }
}
