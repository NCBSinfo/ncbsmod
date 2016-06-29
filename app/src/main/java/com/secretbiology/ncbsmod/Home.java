package com.secretbiology.ncbsmod;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.secretbiology.ncbsmod.dashboard.Dashboard;
import com.secretbiology.ncbsmod.helpers.Utilities;
import com.secretbiology.ncbsmod.interfaces.Network;
import com.secretbiology.ncbsmod.interfaces.User;
import com.secretbiology.ncbsmod.login.Login;
import com.secretbiology.retro.google.fcm.Commands;
import com.secretbiology.retro.google.fcm.MakeQuery;
import com.secretbiology.retro.google.fcm.Service;
import com.secretbiology.retro.google.fcm.topic.model.TopicData;
import com.secretbiology.retro.google.fcm.topic.reponse.TopicResponse;

import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity implements User, Network{
    List<Proxy> proxyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        ProxySelector defaultProxySelector = ProxySelector.getDefault();
        proxyList = defaultProxySelector.select(URI.create("http://www.google.com"));

        startActivity(new Intent(Home.this, Dashboard.class));

        TopicData data = new TopicData();
        data.setTitle("Testing notification");
        data.setMessage("debug");

        final MakeQuery query = new MakeQuery();
        query.setData(data);
        query.setTo("/topics/"+"test");

        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());


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

    public void sendTopicMessage (String token, final MakeQuery data){
        Commands ThisService = Service.createService(Commands.class, token);
        Call<TopicResponse> call = ThisService.sendTopicMessage(data);
        call.enqueue(new Callback<TopicResponse>() {
            @Override
            public void onResponse(Call<TopicResponse> call, Response<TopicResponse> response) {
                if (response.isSuccess()) {
                    Log.i("Success", response.message());

                } else {
                    Log.i("Error ", response.message());
                    Toast.makeText(getBaseContext(),"Error",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<TopicResponse> call, Throwable t) {

                Log.i("Failed ", t.getLocalizedMessage());

            }
        });
    }



}
