package com.secretbiology.ncbsmod.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rohitsuratekar.retro.google.gcm.Commands;
import com.rohitsuratekar.retro.google.gcm.MakeQuery;
import com.rohitsuratekar.retro.google.gcm.Service;
import com.rohitsuratekar.retro.google.gcm.topic.model.ConferenceData;
import com.rohitsuratekar.retro.google.gcm.topic.reponse.TopicResponse;
import com.secretbiology.ncbsmod.R;
import com.secretbiology.ncbsmod.adapters.TopicWindowAdapter;
import com.secretbiology.ncbsmod.database.TopicData;
import com.secretbiology.ncbsmod.helpers.Converters;
import com.secretbiology.ncbsmod.helpers.GeneralFunctions;
import com.secretbiology.ncbsmod.models.TopicDataModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TopicWindow extends AppCompatActivity {

    public static final String INDENT_TOPICNAME = "topicName";
    public static final String INDENT_EXTERNALCODE = "ExternalCode";
    public static final String INFO = "INFO";
    public static final String IMP = "IMP";

    public static final String TOPIC_TITLE = "topicTitle";

    String topicName, externalCode;
    TextView topicLevel, warning;
    Button sendBtn;
    EditText message;
    SharedPreferences pref;
    List<TopicDataModel> refinedList;
    TopicWindowAdapter adaper;
    private ProgressDialog mProgressDialog;
    CheckBox impBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topic_window);
        pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        Intent intent = getIntent();
        topicName = intent.getStringExtra(INDENT_TOPICNAME);
        externalCode = intent.getStringExtra(INDENT_EXTERNALCODE);

        impBox = (CheckBox)findViewById(R.id.imp_checkbox);
        message = (EditText)findViewById(R.id.topicwindow_message);
        sendBtn = (Button)findViewById(R.id.topic_sendButton);
        topicLevel = (TextView)findViewById(R.id.topicwindow_topic);
        warning = (TextView) findViewById(R.id.topicwindow_warning);

        topicLevel.setText(new Converters().topicConverter(externalCode));
        warning.setText("Message sent here will be delivered to everyone");


        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        View view = getSupportActionBar().getCustomView();
        TextView name = (TextView) view.findViewById(R.id.actionbar_title);
        name.setText(new Converters().topicConverter(topicName));

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.topicwindow_recyclerView);
        final List<TopicDataModel> data = new TopicData(getBaseContext()).getAll();
        refinedList = new ArrayList<>();
        for(TopicDataModel item: data){
            if(item.getTopicName().equals(topicName)){
                refinedList.add(item);
            }
        }

        adaper = new TopicWindowAdapter(refinedList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(TopicWindow.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adaper);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(message.getText().length()!=0){


                    ConferenceData data = new ConferenceData();
                    data.setMessage(message.getText().toString());
                    data.setExternalCode(externalCode);
                    if(impBox.isChecked()){
                        data.setGcm_extra(IMP);
                    }
                    else {
                        data.setGcm_extra(INFO);
                    }
                    data.setTitle(pref.getString(TOPIC_TITLE,"Announcement"));

                    MakeQuery query = new MakeQuery();
                    query.setData(data);
                    query.setTo("/topics/"+topicName);
                    showProgressDialog();
                    sendTopicMessage(pref.getString(Login.KEY,"error"),query);
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
                    TopicDataModel a = new TopicDataModel(0, new GeneralFunctions().timeStamp(),
                            topicName, message.getText().toString(),data.getData().getGcm_extra());
                    new TopicData(getBaseContext()).add(a);
                    refinedList.add(a);
                    adaper.notifyDataSetChanged();
                    impBox.setChecked(false);
                    message.setText("");
                } else {
                    Toast.makeText(getBaseContext(),"Error",Toast.LENGTH_LONG).show();
                }
                hideProgressDialog();
            }

            @Override
            public void onFailure(Call<TopicResponse> call, Throwable t) {
                hideProgressDialog();

            }
        });
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

}
