
package com.secretbiology.retro.google.fcm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.secretbiology.retro.google.fcm.topic.model.TopicData;

public class MakeQuery {

    @SerializedName("to")
    @Expose
    private String to;
    @SerializedName("data")
    @Expose
   private TopicData data;

    public String getTo() {
        return to;
    }

     public void setTo(String to) {
        this.to = to;
    }

  public TopicData getData() {
        return data;
    }

   public void setData(TopicData data) {
        this.data = data;
    }

}
