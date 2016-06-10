package com.secretbiology.ncbsmod.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.secretbiology.ncbsmod.models.UserDataModel;

import java.util.ArrayList;
import java.util.List;

public class UserData {

    //Get Constants
    static String TABLE_NAME = "UserDataTable";
    static String ID = "usrdata_id";
    static String TIMESTAMP = "usrdata_timestamp";
    static String USERID = "usrdata_userid";
    static String MESSAGE = "usrdata_message";
    static String EXTRA_VARIABLES = "usrdata_variables";


    SQLiteDatabase db;

    public UserData(Context context) {
        Database db = new Database(context);
        this.db = db.getWritableDatabase();
    }

    public int add(UserDataModel data){
        ContentValues values = new ContentValues();
        values.put(TIMESTAMP, data.getTimestamp());
        values.put(USERID, data.getUserID());
        values.put(MESSAGE, data.getMessage());
        values.put(EXTRA_VARIABLES, data.getExtraVariables());
        int returnValue = (int) db.insert(TABLE_NAME, null, values);
        db.close();
        return returnValue;
    }

    public UserDataModel get(int dataID){
        Cursor cursor = db.query(TABLE_NAME, new String[]{ID,TIMESTAMP, USERID, MESSAGE, EXTRA_VARIABLES},
                ID + "=?",
                new String[]{String.valueOf(dataID)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        UserDataModel entry = null;
        if (cursor != null) {
            entry = new UserDataModel(cursor.getInt(0), cursor.getString(1),
                    cursor.getInt(2), cursor.getString(3), cursor.getString(4));
            cursor.close();
        }
        db.close();
        return entry;
    }

    public List<UserDataModel> getAll() {
        List<UserDataModel> entryList = new ArrayList<UserDataModel>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                UserDataModel model = new UserDataModel();
                model.setId(cursor.getInt(0));
                model.setTimestamp(cursor.getString(1));
                model.setUserID(cursor.getInt(2));
                model.setMessage(cursor.getString(3));
                model.setExtraVariables(cursor.getString(4));
                entryList.add(model);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return entryList;
    }

    public void delete(UserDataModel data) {
        db.delete(TABLE_NAME, ID + " = ?", new String[] { String.valueOf(data.getId()) });
        db.close();
    }

}
