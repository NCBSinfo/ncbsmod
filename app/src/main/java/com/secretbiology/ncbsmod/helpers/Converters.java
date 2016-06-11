package com.secretbiology.ncbsmod.helpers;

import android.util.Log;

import com.secretbiology.ncbsmod.constants.Network;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Converters {

    public String topicConverter(String name){
        String returnString;
        switch (name) {
            case Network.CONFERENCE:
                returnString = "Conference";
                break;
            case Network.CAMP2016:
                returnString = "CAMP 2016";
                break;
            default:
                returnString = "N/A";
                break;
        }
        return returnString;
    }

    public Date convertToDate(String Date, String Time){
        Date returnDate=new Date();
        DateFormat eventFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault());
        try {
            returnDate = eventFormat.parse(Date+" "+Time);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.i("FAILED", "PARSING");
        }
        return returnDate;
    }

    public int getMiliseconds(String timestamp){
        Date dt = new Date();
        DateFormat currentformat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss",Locale.getDefault());
        try {
            dt = currentformat.parse(timestamp);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.i("FAILED", "PARSING");
        }
        return (int) dt.getTime();
    }

}
