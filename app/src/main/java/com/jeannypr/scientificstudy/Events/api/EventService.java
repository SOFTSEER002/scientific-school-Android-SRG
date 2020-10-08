package com.jeannypr.scientificstudy.Events.api;

import com.jeannypr.scientificstudy.Base.Model.Bean;
import com.jeannypr.scientificstudy.Events.model.EventDetailBean;
import com.jeannypr.scientificstudy.Events.model.EventInputModel;
import com.jeannypr.scientificstudy.Events.model.EventResponseBean;
import com.jeannypr.scientificstudy.Events.model.NewsInputModel;
import com.jeannypr.scientificstudy.Events.model.NewsNoticeDetailBean;
import com.jeannypr.scientificstudy.SptWall.model.EventTypeBean;
import com.jeannypr.scientificstudy.Teacher.model.TeacherDetailBean;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface EventService {

    @POST("news/add")
    Call<EventResponseBean> addNewsNotice(@Body NewsInputModel input);

    @GET("news/id")
    Call<NewsNoticeDetailBean> getNewsNoticeDetails(@Query("newsid") int postId);

    @POST("event/add")
    Call<EventResponseBean> addEventsAndPtm(@Body EventInputModel input);

    @GET("event/type")
    Call<EventTypeBean> getEventTypes();

    @Multipart
    @POST("news/doc/upload")
    Observable<Bean> uploadAttachment(@Query("schoolId") int schoolId, @Query("id") int postId, @Query("module") String postType,
                                      @Part MultipartBody.Part image, @Part("name") RequestBody name);

    @GET("events/id")
    Call<EventDetailBean> getEventDetails(@Query("eventId") int postId);
}