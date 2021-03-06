package com.secretbiology.retro.google.fcm.single.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SingleData {

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
    @SerializedName("rcode")
    @Expose
    private String rcode;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("venue")
    @Expose
    private String venue;
    @SerializedName("speaker")
    @Expose
    private String speaker;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("nextspeaker")
    @Expose
    private String nextspeaker;

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

    public String getRcode() {
        return rcode;
    }

    public void setRcode(String rcode) {
        this.rcode = rcode;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNextspeaker() {
        return nextspeaker;
    }

    public void setNextspeaker(String nextspeaker) {
        this.nextspeaker = nextspeaker;
    }

    public String getGcm_extra() {
        return gcm_extra;
    }

    public void setGcm_extra(String gcm_extra) {
        this.gcm_extra = gcm_extra;
    }

    public String getExternalCode() {
        return ExternalCode;
    }

    public void setExternalCode(String externalCode) {
        ExternalCode = externalCode;
    }
}
