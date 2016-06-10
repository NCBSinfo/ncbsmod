package com.secretbiology.ncbsmod.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.secretbiology.ncbsmod.models.TopicDataModel;
import com.secretbiology.ncbsmod.models.UserDataModel;

import java.util.ArrayList;
import java.util.List;

public class TopicData {

    //Get Constants
    static String TABLE_NAME = "TopicDataTable";
    static String ID = "topicdata_id";
    static String TIMESTAMP = "topicdata_timestamp";
    static String TOPIC_NAME = "topicdata_topicname";
    static String MESSAGE = "topicdata_message";
    static String EXTRA_VARIABLES = "topicdata_variables";

    SQLiteDatabase db;

    public TopicData(Context context) {
        Database db = new Database(context);
        this.db = db.getWritableDatabase();
    }

    public int add(TopicDataModel data){
        ContentValues values = new ContentValues();
        values.put(TIMESTAMP, data.getTimestamp());
        values.put(TOPIC_NAME, data.getTopicName());
        values.put(MESSAGE, data.getMessage());
        values.put(EXTRA_VARIABLES, data.getExtraVariables());
        int returnValue = (int) db.insert(TABLE_NAME, null, values);
        db.close();
        return returnValue;
    }

    public TopicDataModel get(int dataID){
        Cursor cursor = db.query(TABLE_NAME, new String[]{ID,TIMESTAMP, TOPIC_NAME, MESSAGE, EXTRA_VARIABLES},
                ID + "=?",
                new String[]{String.valueOf(dataID)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        TopicDataModel entry = null;
        if (cursor != null) {
            entry = new TopicDataModel(cursor.getInt(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3), cursor.getString(4));
            cursor.close();
        }
        db.close();
        return entry;
    }

    public List<TopicDataModel> getAll() {
        List<TopicDataModel> entryList = new ArrayList<TopicDataModel>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                TopicDataModel model = new TopicDataModel();
                model.setId(cursor.getInt(0));
                model.setTimestamp(cursor.getString(1));
                model.setTopicName(cursor.getString(2));
                model.setMessage(cursor.getString(3));
                model.setExtraVariables(cursor.getString(4));
                entryList.add(model);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return entryList;
    }

    public void delete(TopicDataModel data) {
        db.delete(TABLE_NAME, ID + " = ?", new String[] { String.valueOf(data.getId()) });
        db.close();
    }
}
