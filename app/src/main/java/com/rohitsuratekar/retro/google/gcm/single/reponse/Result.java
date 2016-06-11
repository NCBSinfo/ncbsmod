
package com.rohitsuratekar.retro.google.gcm.single.reponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("message_id")
    @Expose
    private String messageId;

    /**
     * 
     * @return
     *     The messageId
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * 
     * @param messageId
     *     The message_id
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

}
