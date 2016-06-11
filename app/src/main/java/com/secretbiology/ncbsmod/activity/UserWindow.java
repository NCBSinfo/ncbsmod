package com.secretbiology.ncbsmod.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rohitsuratekar.retro.google.gcm.Commands;
import com.rohitsuratekar.retro.google.gcm.Service;
import com.rohitsuratekar.retro.google.gcm.single.SingleQuery;
import com.rohitsuratekar.retro.google.gcm.single.model.SingleData;
import com.rohitsuratekar.retro.google.gcm.single.reponse.SingleResponse;
import com.secretbiology.ncbsmod.R;
import com.secretbiology.ncbsmod.adapters.UserWindowAdapter;
import com.secretbiology.ncbsmod.database.ExternalData;
import com.secretbiology.ncbsmod.database.UserData;
import com.secretbiology.ncbsmod.helpers.GeneralFunctions;
import com.secretbiology.ncbsmod.models.ExternalDataModel;
import com.secretbiology.ncbsmod.models.UserDataModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserWindow extends AppCompatActivity {

    //Constants
    public static final String INDENTCODE = "userIndentCode";
    public static final String TITLE = "personalTitle";
    public static final String PERSONAL = "PERSONAL";


    ExternalDataModel currentUser;
    TextView email, timestamp;
    Button sendBtn;
    EditText message;
    UserWindowAdapter adaper;
    List<UserDataModel> refinedList;
    private ProgressDialog mProgressDialog;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_window);

        Intent intent = getIntent();
        String userCode = intent.getStringExtra(INDENTCODE);
        currentUser = new ExternalData(getBaseContext()).get(Integer.valueOf(userCode));
        email = (TextView) findViewById(R.id.userwindow_email);
        timestamp = (TextView) findViewById(R.id.userwindow_timestamp);
        sendBtn = (Button)findViewById(R.id.sendButton);
        message = (EditText)findViewById(R.id.userwindow_message);
        email.setText("Email : "+currentUser.getEmail());
        timestamp.setText("Last token update : "+currentUser.getTimestamp());

        pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        View view = getSupportActionBar().getCustomView();
        TextView name = (TextView) view.findViewById(R.id.actionbar_title);
        name.setText(currentUser.getName());

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.userwindow_recyclerView);
        final List<UserDataModel> data = new UserData(getBaseContext()).getAll();
        refinedList = new ArrayList<>();
        for(UserDataModel item: data){
            if(item.getUserID()==currentUser.getId()){
                refinedList.add(item);
            }
        }
        adaper = new UserWindowAdapter(refinedList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(UserWindow.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adaper);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(message.getText().length()!=0){

                    SingleData data = new SingleData();
                    data.setTitle(pref.getString(TITLE,"Message from "+pref.getString(Login.NAME,"Moderator")));
                    data.setMessage(message.getText().toString());
                    data.setGcm_extra(PERSONAL);
                    data.setExternalCode(pref.getString(Login.EXTRA_CODE,"NORMAL"));
                    SingleQuery query = new SingleQuery();
                    query.setData(data);
                    query.setTo(currentUser.getToken());
                    showProgressDialog();
                    sendMessage(pref.getString(Login.KEY,"error"),query);

                }

            }
        });

    }

    public void sendMessage (String token, SingleQuery data){
        Commands ThisService = Service.createService(Commands.class, token);
        Call<SingleResponse> call = ThisService.sendMessage(data);
        call.enqueue(new Callback<SingleResponse>() {
            @Override
            public void onResponse(Call<SingleResponse> call, Response<SingleResponse> response) {

                if (response.isSuccess()) {

                    UserDataModel a = new UserDataModel();
                    a.setId(0);
                    a.setTimestamp(new GeneralFunctions().timeStamp());
                    a.setExtraVariables("none");
                    a.setUserID(currentUser.getId());
                    a.setMessage(message.getText().toString());
                    new UserData(getBaseContext()).add(a);
                    refinedList.add(a);
                    adaper.notifyDataSetChanged();
                    message.setText("");
                } else {
                    Toast.makeText(getBaseContext(),"Error",Toast.LENGTH_LONG).show();
                }
                hideProgressDialog();

            }

            @Override
            public void onFailure(Call<SingleResponse> call, Throwable t) {
                Log.i("Error",t.toString());
                hideProgressDialog();

            }
        });
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Sending...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.userwindow_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id==R.id.action_delete){
            new AlertDialog.Builder(UserWindow.this)
                    .setTitle("Are you sure ?")
                    .setMessage("You are abount to delete entry for "+  currentUser.getEmail())
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            //Delete all messages related to this user
                            List<UserDataModel> allMessages = new UserData(getBaseContext()).getAll();
                            for (UserDataModel d : allMessages){
                                if(d.getUserID()==currentUser.getId()){
                                    new UserData(getBaseContext()).delete(d);
                                }
                            }
                            //Delete user from database
                            new ExternalData(getBaseContext()).delete(currentUser);
                            startActivity(new Intent(UserWindow.this,UserList.class));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }

        return super.onOptionsItemSelected(item);
    }


}
