package com.secretbiology.retro.google.fcm.single;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.secretbiology.retro.google.fcm.single.model.SingleData;

public class SingleQuery {

    @SerializedName("to")
    @Expose
    private String to;
    @SerializedName("data")
    @Expose
    private SingleData data;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public SingleData getData() {
        return data;
    }

    public void setData(SingleData data) {
        this.data = data;
    }

}