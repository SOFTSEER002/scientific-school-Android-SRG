package com.jeannypr.scientificstudy.SptWall.api;

import com.jeannypr.scientificstudy.Base.Model.Bean;
import com.jeannypr.scientificstudy.Classwork.model.SaveCwHwResponseBean;
import com.jeannypr.scientificstudy.SptWall.model.EventInputModel;
import com.jeannypr.scientificstudy.SptWall.model.EventTypeBean;
import com.jeannypr.scientificstudy.SptWall.model.NewsNoticeBean;
import com.jeannypr.scientificstudy.SptWall.model.NewsNoticeInputModel;
import com.jeannypr.scientificstudy.SptWall.model.SavePostResponseBean;
import com.jeannypr.scientificstudy.SptWall.model.SptWallBean;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface SptWallService {
    @GET("news")
    Call<NewsNoticeBean> GetNewsNoticeList(@Query("schoolid") int SchoolId, @Query("academicYearid") int AcademicYearid,
                                           @Query("newsType") String NewsType, @Query("roleTitle") String RoleTitle);

    @POST("news/add")
    Call<SavePostResponseBean> CreateNewsOrNotice(@Body NewsNoticeInputModel model);

    @GET("school/posts")
    Call<SptWallBean> GetNewsNoticeEventList(@Query("schoolid") int schoolId, @Query("userRole") String UserRole);

    @Multipart
    @POST("news/doc/upload")
    Call<Bean> UploadNewsAttachment(@Query("schoolId") int schoolId, @Query("id") int postId, @Query("module") String postType,
                                    @Part MultipartBody.Part image, @Part("name") RequestBody name);

    @GET("event/type")
    Call<EventTypeBean> GetEventTypes();

    @GET("event/level")
    Call<EventTypeBean> GetEventLevels();

    @POST("event/add")
    Call<SavePostResponseBean> CreateEvents(@Body EventInputModel model);

    @Multipart
    @POST("event/image/upload")
    Call<Bean> UploadEventAttachment(@Query("schoolId") int schoolId, @Query("id") int postId, @Query("context") String postType,
                                     @Part MultipartBody.Part image, @Part("name") RequestBody name);
}
