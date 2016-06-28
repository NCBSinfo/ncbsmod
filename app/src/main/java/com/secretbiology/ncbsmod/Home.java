package com.secretbiology.ncbsmod;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.secretbiology.ncbsmod.helpers.Utilities;
import com.secretbiology.ncbsmod.login.Login;

import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.util.List;

public class Home extends AppCompatActivity {
    List<Proxy> proxyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        ProxySelector defaultProxySelector = ProxySelector.getDefault();
        proxyList = defaultProxySelector.select(URI.create("http://www.google.com"));


        startActivity(new Intent(Home.this, Login.class));

        Button btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                }
                else {
                    Toast.makeText(getBaseContext(),"No network",Toast.LENGTH_LONG).show();
                }

            }
        });
    }


}
