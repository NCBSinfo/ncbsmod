package com.secretbiology.ncbsmod.dashboard;

public class DashboardModel {

    String title;
    String message;

    public DashboardModel(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public DashboardModel() {
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
}
