package com.secretbiology.retro.google.fcm.topic.reponse;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class TopicResponse {

    @SerializedName("message_id")
    @Expose
    private String messageId;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

}