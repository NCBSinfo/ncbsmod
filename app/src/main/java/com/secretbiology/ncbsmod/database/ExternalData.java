package com.secretbiology.ncbsmod.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.secretbiology.ncbsmod.models.ExternalDataModel;

import java.util.ArrayList;
import java.util.List;

public class ExternalData {

    //Get Constants
    static String TABLE_NAME = "ExternalDataTable";
    static String ID = "ext_id";
    static String TIMESTAMP = "ext_timestamp";
    static String NAME = "ext_name";
    static String EMAIL = "ext_email";
    static String TOKEN = "ext_token";
    static String CODE = "ext_code";


    SQLiteDatabase db;

    public ExternalData(Context context) {
        Database db = new Database(context);
        this.db = db.getWritableDatabase();
    }

    public int add(ExternalDataModel data){
        ContentValues values = new ContentValues();
        values.put(TIMESTAMP, data.getTimestamp());
        values.put(NAME, data.getName());
        values.put(EMAIL, data.getEmail());
        values.put(TOKEN, data.getToken());
        values.put(CODE, data.getExternalCode());
        int returnValue = (int) db.insert(TABLE_NAME, null, values);
        db.close();
        return returnValue;
    }

    public ExternalDataModel get(int dataID){
        Cursor cursor = db.query(TABLE_NAME, new String[]{ID,TIMESTAMP, NAME, EMAIL, TOKEN, CODE},
                ID + "=?",
                new String[]{String.valueOf(dataID)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        ExternalDataModel entry = null;
        if (cursor != null) {
            entry = new ExternalDataModel(cursor.getInt(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
            cursor.close();
        }
        db.close();
        return entry;
    }

    public List<ExternalDataModel> getAll() {
        List<ExternalDataModel> entryList = new ArrayList<ExternalDataModel>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                ExternalDataModel model = new ExternalDataModel();
                model.setId(cursor.getInt(0));
                model.setTimestamp(cursor.getString(1));
                model.setName(cursor.getString(2));
                model.setEmail(cursor.getString(3));
                model.setToken(cursor.getString(4));
                model.setExternalCode(cursor.getString(5));
                entryList.add(model);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return entryList;
    }

    public int update(ExternalDataModel data) {
        ContentValues values = new ContentValues();
        values.put(ID, data.getId());
        values.put(TIMESTAMP, data.getTimestamp());
        values.put(NAME, data.getName());
        values.put(EMAIL, data.getEmail());
        values.put(TOKEN, data.getToken());
        values.put(CODE, data.getExternalCode());
        return db.update(TABLE_NAME, values, ID + " = ?",
                new String[]{String.valueOf(data.getId())});
    }

    public void delete(ExternalDataModel data) {
        db.delete(TABLE_NAME, ID + " = ?", new String[] { String.valueOf(data.getId()) });
        db.close();
    }

    //Check if record is already ther
    public boolean isAlreadyThere(String timestamp) {
        String Query = "SELECT * FROM " + TABLE_NAME + " WHERE " + TIMESTAMP + " = '" + timestamp+"'";
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){ cursor.close(); return false;
        } cursor.close(); return true; }
}
