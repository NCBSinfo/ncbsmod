package com.secretbiology.ncbsmod.database;

import android.database.sqlite.SQLiteDatabase;

public class Tables{

    //Constants
    static String TABLE_EXTERNAL_DATA = ExternalData.TABLE_NAME;
    static String EXT_ID = ExternalData.ID;
    static String EXT_TIMESTAMP = ExternalData.TIMESTAMP;
    static String EXT_NAME = ExternalData.NAME;
    static String EXT_EMAIL = ExternalData.EMAIL;
    static String EXT_TOKEN = ExternalData.TOKEN;
    static String EXT_CODE = ExternalData.CODE;

    static String TABLE_USER_DATA = UserData.TABLE_NAME;
    static String USERDATA_ID = UserData.ID;
    static String USERDATA_TIMESTAMP = UserData.TIMESTAMP;
    static String USERDATA_USERID = UserData.USERID;
    static String USERDATA_MESSAGE = UserData.MESSAGE;
    static String USERDATA_EXTRA_VARIABLES = UserData.EXTRA_VARIABLES;

    static String TABLE_TOPIC_DATA = TopicData.TABLE_NAME;
    static String TOPICDATA_ID = TopicData.ID;
    static String TOPICDATA_TIMESTAMP = TopicData.TIMESTAMP;
    static String TOPICDATA_TOPIC_NAME = TopicData.TOPIC_NAME;
    static String TOPICDATA_MESSAGE = TopicData.MESSAGE;
    static String TOPICDATA_EXTRA_VARIABLES = TopicData.EXTRA_VARIABLES;


     //Table for external data
    public void makeExternalDataTable(SQLiteDatabase db) {
        String CREATE_EXT_TABLE = "CREATE TABLE " + TABLE_EXTERNAL_DATA + "("
                + EXT_ID + " INTEGER PRIMARY KEY,"
                + EXT_TIMESTAMP + " TEXT,"
                + EXT_NAME + " TEXT,"
                + EXT_EMAIL + " TEXT,"
                + EXT_TOKEN + " TEXT,"
                + EXT_CODE + " TEXT )";
        db.execSQL(CREATE_EXT_TABLE);
    }

    public void makeUserDataTable(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER_DATA + "("
                + USERDATA_ID + " INTEGER PRIMARY KEY,"
                + USERDATA_TIMESTAMP + " TEXT,"
                + USERDATA_USERID + " INTEGER,"
                + USERDATA_MESSAGE + " TEXT,"
                + USERDATA_EXTRA_VARIABLES + " TEXT )";
        db.execSQL(CREATE_USER_TABLE);
    }

    public void makeTopicDataTable(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_TOPIC_DATA + "("
                + TOPICDATA_ID + " INTEGER PRIMARY KEY,"
                + TOPICDATA_TIMESTAMP + " TEXT,"
                + TOPICDATA_TOPIC_NAME + " TEXT,"
                + TOPICDATA_MESSAGE + " TEXT,"
                + TOPICDATA_EXTRA_VARIABLES + " TEXT )";
        db.execSQL(CREATE_USER_TABLE);
    }

    //Drop all tables
    public void dropTables(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXTERNAL_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOPIC_DATA);
    }

}
