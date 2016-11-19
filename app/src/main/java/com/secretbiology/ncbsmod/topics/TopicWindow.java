package com.secretbiology.ncbsmod.topics;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.secretbiology.ncbsmod.BaseActivity;
import com.secretbiology.ncbsmod.R;
import com.secretbiology.ncbsmod.dashboard.Dashboard;
import com.secretbiology.ncbsmod.database.NotificationData;
import com.secretbiology.ncbsmod.database.models.NotificationModel;
import com.secretbiology.ncbsmod.helpers.Utilities;
import com.secretbiology.ncbsmod.interfaces.User;
import com.secretbiology.retro.google.fcm.Commands;
import com.secretbiology.retro.google.fcm.MakeQuery;
import com.secretbiology.retro.google.fcm.Service;
import com.secretbiology.retro.google.fcm.topic.model.TopicData;
import com.secretbiology.retro.google.fcm.topic.reponse.TopicResponse;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopicWindow extends BaseActivity implements User {

    //Public constants
    public static final String INTENT = "topicWindow";

    private final String TAG = getClass().getSimpleName();
    SharedPreferences pref;
    RecyclerView recyclerView;
    String curentTopic, title;
    Button sendBtn;
    EditText message;
    TextView defaultTitle;
    TopicWindowAdapter adaper;
    List<NotificationModel> refinedList;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewStub viewStub = (ViewStub) findViewById(R.id.baseView);
        viewStub.setLayoutResource(R.layout.topic_window);
        viewStub.inflate();
        mProgressDialog = new ProgressDialog(TopicWindow.this);
        pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        Intent intent = getIntent();
        curentTopic = intent.getStringExtra(INTENT);
        if (curentTopic == null) {
            Toast.makeText(getBaseContext(), "Something is wrong", Toast.LENGTH_LONG).show();
            startActivity(new Intent(TopicWindow.this, Dashboard.class));
        }

        message = (EditText) findViewById(R.id.topicwindow_message);
        sendBtn = (Button) findViewById(R.id.topic_sendButton);
        defaultTitle = (TextView) findViewById(R.id.topic_default_title);
        title = "Message from admin";
        defaultTitle.setText("Default notification title will be \" " + title + " \"(click this to change)");

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        View view = getSupportActionBar().getCustomView();
        TextView name = (TextView) view.findViewById(R.id.actionbar_title);
        name.setText(new Utilities().topicConveter(curentTopic)[0]);

        recyclerView = (RecyclerView) findViewById(R.id.topicwindow_recyclerView);
        final List<NotificationModel> data = new NotificationData(getBaseContext()).getAll();

        refinedList = new ArrayList<>();
        for (NotificationModel item : data) {
            if (item.getFrom().contains(curentTopic)) {
                refinedList.add(item);
            }
        }
        adaper = new TopicWindowAdapter(refinedList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(TopicWindow.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adaper);
        recyclerView.scrollToPosition(refinedList.size() - 1);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (message.getText().length() != 0) {

                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(TopicWindow.this);
                    alertDialog.setTitle("Confirm message to " + new Utilities().topicConveter(curentTopic)[0]);

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        alertDialog.setMessage(Html.fromHtml("<b>Title</b>: " + title + "<br><br><b>Message</b>:<br>" + message.getText().toString(), Html.FROM_HTML_MODE_LEGACY));
                    } else {
                        alertDialog.setMessage(Html.fromHtml("<b>Title</b>: " + title + "<br><br><b>Message</b>:<br>" + message.getText().toString()));
                    }
                    alertDialog.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            TopicData data = new TopicData();
                            data.setMessage(message.getText().toString());
                            data.setTitle(title);
                            MakeQuery query = new MakeQuery();
                            query.setData(data);
                            query.setTo("/topics/" + curentTopic);
                            showProgressDialog();
                            mProgressDialog.setMessage("Sending...");
                            sendTopicMessage(pref.getString(profile.KEY, "error"), query);

                        }
                    });
                    alertDialog.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    alertDialog.show();

                }
            }
        });

        defaultTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(TopicWindow.this);
                alertDialog.setCancelable(false);
                alertDialog.setTitle("Change title");
                alertDialog.setMessage("Enter custom notification title below.");
                final EditText input = new EditText(TopicWindow.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input); // uncomment this line
                alertDialog.setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (input.getText().toString().length() != 0) {
                            title = input.getText().toString();
                            defaultTitle.setText("Notification title will be \" " + title + " \"");
                        }
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alertDialog.show();
            }
        });
    }

    public void sendTopicMessage(String token, final MakeQuery data) {
        Commands ThisService = Service.createService(Commands.class, token);
        Call<TopicResponse> call = ThisService.sendTopicMessage(data);
        call.enqueue(new Callback<TopicResponse>() {
            @Override
            public void onResponse(Call<TopicResponse> call, Response<TopicResponse> response) {
                if (response.isSuccess()) {
                    NotificationModel notification = new NotificationModel();
                    notification.setTitle(title);
                    notification.setMessage(message.getText().toString());
                    notification.setTimestamp(new Utilities().timeStamp());
                    notification.setRcode("None");
                    notification.setRcodeValue("None");
                    notification.setExtraParameters("None");
                    notification.setFrom(curentTopic);
                    new NotificationData(getBaseContext()).add(notification);
                    refinedList.add(notification);
                    adaper.notifyDataSetChanged();
                    recyclerView.scrollToPosition(refinedList.size() - 1);
                    message.setText("");
                    sendLog(notification);
                    hideProgressDialog();

                } else {
                    Log.i("Error ", response.message());
                    Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<TopicResponse> call, Throwable t) {

                Log.i("Failed ", t.getLocalizedMessage());

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

    private void sendLog(NotificationModel notification) {
        com.secretbiology.retro.google.form.Commands formservice = com.secretbiology.retro.google.form.Service.createService(com.secretbiology.retro.google.form.Commands.class);
        Call<ResponseBody> call = formservice
                .submitLog(
                        notification.getTitle(),
                        notification.getMessage(),
                        notification.getRcode(),
                        notification.getRcodeValue(),
                        notification.getExtraParameters(),
                        notification.getFrom(),
                        pref.getString(profile.EMAIL, "email@domain.com"),
                        "Submit");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, t.getLocalizedMessage());
            }
        });
    }


}
