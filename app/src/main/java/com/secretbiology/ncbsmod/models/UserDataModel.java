package com.secretbiology.ncbsmod.models;

public class UserDataModel {

    int id;
    String timestamp;
    int userID;
    String message;
    String ExtraVariables;

    public UserDataModel(int id, String timestamp, int userID, String message, String extraVariables) {
        this.id = id;
        this.timestamp = timestamp;
        this.userID = userID;
        this.message = message;
        ExtraVariables = extraVariables;
    }

    public UserDataModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getExtraVariables() {
        return ExtraVariables;
    }

    public void setExtraVariables(String extraVariables) {
        ExtraVariables = extraVariables;
    }
}
