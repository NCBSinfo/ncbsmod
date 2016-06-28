package com.secretbiology.retro.google.fcm;

import com.secretbiology.retro.google.fcm.single.SingleQuery;
import com.secretbiology.retro.google.fcm.single.reponse.SingleResponse;
import com.secretbiology.retro.google.fcm.topic.reponse.TopicResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Commands {

    @POST("/fcm/send")
    Call<TopicResponse> sendTopicMessage (@Body MakeQuery data);

    @POST("/fcm/send")
    Call<SingleResponse> sendMessage (@Body SingleQuery data);

}
