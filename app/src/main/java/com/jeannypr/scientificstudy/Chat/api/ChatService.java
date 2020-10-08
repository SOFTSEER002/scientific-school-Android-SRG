package com.jeannypr.scientificstudy.Chat.api;

import com.google.gson.JsonObject;
import com.jeannypr.scientificstudy.Chat.model.ChatMessageBean;
import com.jeannypr.scientificstudy.Chat.model.ChatMessageModel;
import com.jeannypr.scientificstudy.Chat.model.ChatRoomBean;
import com.jeannypr.scientificstudy.Chat.model.ChatRoomModel;
import com.jeannypr.scientificstudy.Chat.model.ChatStartBean;
import com.jeannypr.scientificstudy.Chat.model.UpdateTokenModel;
import com.jeannypr.scientificstudy.Login.model.LogDeviceAccessModel;
import com.jeannypr.scientificstudy.Login.model.LogDeviceModel;
import com.jeannypr.scientificstudy.Login.model.LogSignInOutInputModel;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ChatService {

    @GET("getChatMessages")
    Call<ChatMessageBean> GetChatMessages(@Query("chatRoomId") int chatRoomId, @Query("schoolCode") String schoolCode,
                                          @Query("userId") int userId, @Query("roleTitle") String roleTitle,
                                          @Query("skip") int skip, @Query("take") int take);

    @GET("getUserOpenChats")
    Call<ChatRoomBean> GetUserOpenChatRooms(@Query("userId") int userId, @Query("roleTitle") String roleTitle,
                                            @Query("classIds") String classIds, @Query("schoolCode") String schoolCode);

    @POST("sendChatMessage")
    Call<JsonObject> SendChatMessage(@Body ChatMessageModel model, @Query("schoolCode") String schoolCode);


    @POST("startChat")
    Call<ChatStartBean> StartIndividualChatRoom(@Body ChatRoomModel model, @Query("schoolCode") String schoolCode);

    @POST("startChat")
    Observable<ChatStartBean> StartIndividualChatRoomUsingRX(@Body ChatRoomModel model, @Query("schoolCode") String schoolCode);

    @POST("startGroupChat")
    Call<ChatStartBean> StartGroupChatRoom(@Body ChatRoomModel model, @Query("schoolCode") String schoolCode);

    @POST("logUser")
    Call<JsonObject> LogUser(@Query("userId") int userId, @Query("schoolCode") String schoolCode);

    @GET("isUserOnline")
    Call<JsonObject> IsUserOnline(@Query("userId") int userId, @Query("myUserId") int myUserId, @Query("schoolCode") String schoolCode);

    //msgId can be comma seperated values of message ids
    @POST("notifyMessageRead")
    Call<JsonObject> NotifyMessageRead(@Query("msgIds") String msgId, @Query("schoolCode") String schoolCode);

    @POST("logDevice")
    Call<JsonObject> SaveLogDevice(@Body LogDeviceModel info);

    @POST("logDeviceAccess")
    Call<JsonObject> SaveLogDeviceAccess(@Body LogDeviceAccessModel info);

    @POST("logSignInOut")
    Call<JsonObject> SaveLogSignInOut(@Body LogSignInOutInputModel info, @Query("logType") int LogType);

    @POST("updateTokenOnRefresh")
    Call<JsonObject> UpdateTokenOnRefresh(@Body UpdateTokenModel info);

}
