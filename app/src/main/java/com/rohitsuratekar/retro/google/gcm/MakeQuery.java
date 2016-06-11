
package com.rohitsuratekar.retro.google.gcm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rohitsuratekar.retro.google.gcm.topic.model.ConferenceData;

public class MakeQuery {

    @SerializedName("to")
    @Expose
    private String to;
    @SerializedName("data")
    @Expose
   private ConferenceData data;

    public String getTo() {
        return to;
    }

     public void setTo(String to) {
        this.to = to;
    }

  public ConferenceData getData() {
        return data;
    }

   public void setData(ConferenceData data) {
        this.data = data;
    }

}
