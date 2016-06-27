package com.secretbiology.ncbsmod.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class Tables extends Database{

    SQLiteDatabase db;

    public Tables(Context context) {
        super(context);
        this.db = getWritableDatabase();
    }

    public void makeContactTable() {
        String CREATE_NOTIFICATION_TABLE = "CREATE TABLE " + NotificationData.TABLE_NOTIFICATIONS + "("
                + NotificationData.KEY_ID + " INTEGER PRIMARY KEY,"
                + NotificationData.TIMESTAMP + " TEXT,"
                + NotificationData.TITLE + " TEXT,"
                + NotificationData.MESSAGE + " TEXT,"
                + NotificationData.FROM + " TEXT,"
                + NotificationData.RCODE + " TEXT,"
                + NotificationData.RCODE_VALUE + " TEXT,"
                + NotificationData.EXTRA_PARAMETERS + " TEXT " + ")";
        db.execSQL(CREATE_NOTIFICATION_TABLE);
    }


}
