package com.secretbiology.ncbsmod.database;

import android.database.sqlite.SQLiteDatabase;

public class Tables {

    SQLiteDatabase db;

    public Tables(SQLiteDatabase db) {
        this.db = db;
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

    public void dropAllTables() {
        db.execSQL("DROP TABLE IF EXISTS " + NotificationData.TABLE_NOTIFICATIONS);
    }


}
