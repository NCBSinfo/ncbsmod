package com.secretbiology.ncbsmod.models;

public class ExternalDataModel {

    int id;
    String timestamp;
    String name;
    String email;
    String token;
    String ExternalCode;

    public ExternalDataModel(int id, String timestamp, String name, String email, String token, String externalCode) {
        this.id = id;
        this.timestamp = timestamp;
        this.name = name;
        this.email = email;
        this.token = token;
        ExternalCode = externalCode;
    }

    public ExternalDataModel() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExternalCode() {
        return ExternalCode;
    }

    public void setExternalCode(String externalCode) {
        ExternalCode = externalCode;
    }
}
