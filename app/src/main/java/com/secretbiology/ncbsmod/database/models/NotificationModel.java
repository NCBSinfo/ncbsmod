package com.secretbiology.ncbsmod.database.models;

public class NotificationModel {
    int id;
    String timestamp;
    String title;
    String message;
    String from;
    String rcode;
    String rcodeValue;
    String ExtraParameters;

    public NotificationModel(int id, String timestamp, String title, String message, String from, String rcode, String rcodeValue, String extraParameters) {
        this.id = id;
        this.timestamp = timestamp;
        this.title = title;
        this.message = message;
        this.from = from;
        this.rcode = rcode;
        this.rcodeValue = rcodeValue;
        ExtraParameters = extraParameters;
    }

    public NotificationModel() {
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getRcode() {
        return rcode;
    }

    public void setRcode(String rcode) {
        this.rcode = rcode;
    }

    public String getRcodeValue() {
        return rcodeValue;
    }

    public void setRcodeValue(String rcodeValue) {
        this.rcodeValue = rcodeValue;
    }

    public String getExtraParameters() {
        return ExtraParameters;
    }

    public void setExtraParameters(String extraParameters) {
        ExtraParameters = extraParameters;
    }
}



