package com.secretbiology.ncbsmod.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    //Constants
    private static String DATABASE_NAME = "NCBSMod";
    private static int DATABASE_VERSION = 1;

    Context mContext;

    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        new Tables().makeExternalDataTable(db);
        new Tables().makeUserDataTable(db);
        new Tables().makeTopicDataTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        new Tables().dropTables(db);
        new Tables().makeExternalDataTable(db);
        new Tables().makeUserDataTable(db);
        new Tables().makeTopicDataTable(db);
    }
}
