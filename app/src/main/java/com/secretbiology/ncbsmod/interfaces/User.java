package com.secretbiology.ncbsmod.interfaces;

public interface User {

    String AUTHORIZED = "authorizedUser"; //boolean

    interface profile{
        String NAME = "userName"; //String
        String EMAIL = "userEmail"; //String
        String TOPICS = "allowedTopics"; //String
        String KEY = "userKey"; //String
        String TOKEN = "userToken"; //String
    }

}
