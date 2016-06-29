package com.secretbiology.retro.google.form;

import com.secretbiology.ncbsmod.interfaces.Network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Commands {

    @FormUrlEncoded
    @POST(Network.logtable.TABLE_URL)
    Call<ResponseBody> submitLog(@Field(Network.logtable.TITLE) String title,
                                 @Field(Network.logtable.MESSAGE) String message,
                                 @Field(Network.logtable.RCODE) String rcode,
                                 @Field(Network.logtable.RCODE_VALUE) String rcodeValue,
                                 @Field(Network.logtable.OTHER_PARAMETERS) String otherParameter,
                                 @Field(Network.logtable.TOPIC) String topic,
                                 @Field(Network.logtable.USER_ID) String userID,
                                 @Field("submit") String submit);
}
