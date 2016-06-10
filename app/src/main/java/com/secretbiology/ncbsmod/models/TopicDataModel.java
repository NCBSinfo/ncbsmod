package com.secretbiology.ncbsmod.models;


public class TopicDataModel {

    int id;
    String timestamp;
    String topicName;
    String message;
    String ExtraVariables;

    public TopicDataModel(int id, String timestamp, String topicName, String message, String extraVariables) {
        this.id = id;
        this.timestamp = timestamp;
        this.topicName = topicName;
        this.message = message;
        ExtraVariables = extraVariables;
    }

    public TopicDataModel() {
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

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
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
