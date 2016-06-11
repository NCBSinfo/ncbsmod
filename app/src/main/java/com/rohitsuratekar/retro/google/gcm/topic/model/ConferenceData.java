package com.rohitsuratekar.retro.google.gcm.topic.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConferenceData {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("ExternalCode")
    @Expose
    private String ExternalCode;
    @SerializedName("gcm_extra")
    @Expose
    private String gcm_extra;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExternalCode() {
        return ExternalCode;
    }

    public void setExternalCode(String externalCode) {
        ExternalCode = externalCode;
    }

    public String getGcm_extra() {
        return gcm_extra;
    }

    public void setGcm_extra(String gcm_extra) {
        this.gcm_extra = gcm_extra;
    }
}
