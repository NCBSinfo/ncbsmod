package com.rohitsuratekar.retro.google.gcm;

import com.rohitsuratekar.retro.google.gcm.single.SingleQuery;
import com.rohitsuratekar.retro.google.gcm.single.reponse.SingleResponse;
import com.rohitsuratekar.retro.google.gcm.topic.reponse.TopicResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Commands {

    @POST("/gcm/send")
    Call<TopicResponse> sendTopicMessage (@Body MakeQuery data);

    @POST("/gcm/send")
    Call<SingleResponse> sendMessage (@Body SingleQuery data);

}
