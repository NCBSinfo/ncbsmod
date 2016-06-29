package com.secretbiology.ncbsmod.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Switch;

import com.secretbiology.ncbsmod.interfaces.Network;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Rohit Suratekar on 28-06-16.
 */
public class Utilities implements Network{

    //Check network condition
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public String timeStamp(){
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss a d MMM yy", Locale.getDefault());
        return formatter.format(new Date());
    }

    public String[] stringToarray(String input) {
        List<String> output = new ArrayList<>();
        input = input.replace("{", "");
        input = input.replace("}", "");
        String[] split = input.split(",");
        for (String s : split) {
            s = s.replace("\"", "");
            output.add(s.replaceAll("\\s+", ""));
        }
        return output.toArray(new String[output.size()]);
    }

    public String[] topicConveter(String topic){
        String[] tempArray = new String[2];
        switch (topic){
            case topics.PUBLIC: tempArray[0] = "Public Group"; tempArray[1] = "General notifications"; break;
            case topics.EMERGENCY: tempArray[0] = "Emergency"; tempArray[1] = "Important notifications"; break;
            case topics.CAMP16: tempArray[0] = "CAMP 2016"; tempArray[1] = "Notifications for CAMP 16 course"; break;
            default: tempArray[0] = "Unknown group"; tempArray[1] = "Group name is not listed"; break;
        }
        return tempArray;
    }

}
