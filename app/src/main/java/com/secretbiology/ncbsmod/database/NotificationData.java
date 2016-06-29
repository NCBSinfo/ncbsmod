package com.secretbiology.ncbsmod.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.secretbiology.ncbsmod.database.models.NotificationModel;

import java.util.ArrayList;
import java.util.List;

public class NotificationData {

    public static final String TABLE_NOTIFICATIONS = "table_notifications";
    public static final String KEY_ID = "id";
    public static final String TIMESTAMP = "timestamp";
    public static final String TITLE = "title";
    public static final String MESSAGE = "message";
    public static final String FROM = "fromTopic";
    public static final String RCODE = "rcode";
    public static final String RCODE_VALUE = "rcodeValue";
    public static final String EXTRA_PARAMETERS = "extraParameters";

    SQLiteDatabase db;

    public NotificationData(Context context) {
        Database data = new Database(context);
        this.db = data.getWritableDatabase();
    }

    public void add(NotificationModel notification) {
        ContentValues values = new ContentValues();
        values.put(TIMESTAMP, notification.getTimestamp());
        values.put(TITLE, notification.getTitle());
        values.put(MESSAGE, notification.getMessage());
        values.put(FROM, notification.getFrom());
        values.put(RCODE, notification.getRcode());
        values.put(RCODE_VALUE, notification.getRcodeValue());
        values.put(EXTRA_PARAMETERS, notification.getExtraParameters());
        // Inserting Row
        db.insert(TABLE_NOTIFICATIONS, null, values);
        db.close();
    }

    public NotificationModel get(int id) {
        Cursor cursor = db.query(TABLE_NOTIFICATIONS, new String[]{KEY_ID, TIMESTAMP, TITLE, MESSAGE, FROM, RCODE, RCODE_VALUE, EXTRA_PARAMETERS}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

        }
        NotificationModel notification = new NotificationModel(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
        // return contact
        cursor.close();
        return notification;
    }

    public List<NotificationModel> getAll() {
        List<NotificationModel> notificationModelList = new ArrayList<NotificationModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NOTIFICATIONS;
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                NotificationModel notificationModel = new NotificationModel();
                notificationModel.setId(Integer.parseInt(cursor.getString(0)));
                notificationModel.setTimestamp(cursor.getString(1));
                notificationModel.setTitle(cursor.getString(2));
                notificationModel.setMessage(cursor.getString(3));
                notificationModel.setFrom(cursor.getString(4));
                notificationModel.setRcode(cursor.getString(5));
                notificationModel.setRcodeValue(cursor.getString(6));
                notificationModel.setExtraParameters(cursor.getString(7));
                // Adding contact to list
                notificationModelList.add(notificationModel);
            } while (cursor.moveToNext());
        }

        // return contact list
        cursor.close();
        return notificationModelList;
    }

    // Delete all data
    public void clearAll() {
        db.execSQL("DELETE FROM " + TABLE_NOTIFICATIONS);
        db.close();
    }

    // Deleting single contact
    public void delete(NotificationModel notification) {
        db.delete(TABLE_NOTIFICATIONS, KEY_ID + " = ?", new String[]{String.valueOf(notification.getId())});
        db.close();
    }


}
