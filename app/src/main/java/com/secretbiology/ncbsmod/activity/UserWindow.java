package com.secretbiology.ncbsmod.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.secretbiology.ncbsmod.R;
import com.secretbiology.ncbsmod.adapters.UserListAdaper;
import com.secretbiology.ncbsmod.adapters.UserWindowAdapter;
import com.secretbiology.ncbsmod.database.ExternalData;
import com.secretbiology.ncbsmod.database.UserData;
import com.secretbiology.ncbsmod.helpers.DividerDecoration;
import com.secretbiology.ncbsmod.helpers.GeneralFunctions;
import com.secretbiology.ncbsmod.models.ExternalDataModel;
import com.secretbiology.ncbsmod.models.UserDataModel;

import java.util.ArrayList;
import java.util.List;

public class UserWindow extends AppCompatActivity {

    //Constants
    public static final String INDENTCODE = "userIndentCode";

    ExternalDataModel currentUser;
    TextView email, timestamp;
    Button sendBtn;
    EditText message;
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

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        View view = getSupportActionBar().getCustomView();
        TextView name = (TextView) view.findViewById(R.id.actionbar_title);
        name.setText(currentUser.getName());

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.userwindow_recyclerView);
        final List<UserDataModel> data = new UserData(getBaseContext()).getAll();
        final List<UserDataModel> refinedList = new ArrayList<>();
        for(UserDataModel item: data){
            if(item.getUserID()==currentUser.getId()){
                refinedList.add(item);
            }
        }


        final UserWindowAdapter adaper = new UserWindowAdapter(refinedList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(UserWindow.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adaper);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(message.getText().length()!=0){
                    UserDataModel a = new UserDataModel(0, new GeneralFunctions().timeStamp(),
                            currentUser.getId(), message.getText().toString(),"none");
                    new UserData(getBaseContext()).add(a);
                    refinedList.add(a);
                    adaper.notifyDataSetChanged();
                    message.setText("");
                }

            }
        });

    }

}
