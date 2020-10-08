package com.jeannypr.scientificstudy.Dashboard.api;

import com.jeannypr.scientificstudy.Base.Model.Bean;
import com.jeannypr.scientificstudy.Dashboard.model.BroadCastRequest;
import com.jeannypr.scientificstudy.Dashboard.model.DashboardModulesBean;
import com.jeannypr.scientificstudy.Dashboard.model.FeeReminderRequest;
import com.jeannypr.scientificstudy.Dashboard.model.GrantedModulesBean;
import com.jeannypr.scientificstudy.Dashboard.model.HomeTabBean;
import com.jeannypr.scientificstudy.Dashboard.model.LearnTabBean;
import com.jeannypr.scientificstudy.Dashboard.model.NotificationSettingBean;
import com.jeannypr.scientificstudy.Dashboard.model.NotificationSettingInputModel;
import com.jeannypr.scientificstudy.Dashboard.model.ReminderRequest;
import com.jeannypr.scientificstudy.Dashboard.model.SettingBean;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AppSettingService {

    @POST("notification/setting/update")
    Call<NotificationSettingBean> SaveAppSetting(@Body NotificationSettingInputModel input);

    @GET("notification/setting")
    Call<SettingBean> GetNotificationSettings(@Query("schoolId") int SchoolId);

    @GET("mobile/GetAllMobileModulesByUserRole")
    Call<DashboardModulesBean> GetAllModulesList(@Query("userRole") String UserRole);

    @GET("mobile/GetModulePermissionByUserId")
    Call<GrantedModulesBean> GetGrantedFeatures(@Query("userId") int UserId, @Query("userRole") String UserRole);

    @GET("mobile/today-tab")
    Call<HomeTabBean> GetTodayTabDetails(@Query("userid") int UserId, @Query("schoolid") int SchoolId,
                                         @Query("academicyearid") int AcademicYearId, @Query("role") String Role);

    @GET("mobile/home/page/details")
    Call<HomeTabBean> GetHomeTabDetails(@Query("userid") int UserId, @Query("schoolid") int SchoolId,
                                        @Query("academicyearid") int AcademicYearId, @Query("role") String Role);

    @POST("mobile/remindme")
    Call<Bean> SaveReminder(@Body ReminderRequest input);

    @POST("mobile/checkIn")
    Call<Bean> SaveCheckIn(@Body ReminderRequest input);

    @POST("mobile/interestedinevent")
    Call<Bean> RSVP(@Body ReminderRequest input);

    @POST("mobile/send/broadcastorbulksms")
    Call<Bean> BroadcastMsg(@Body BroadCastRequest input);

    @POST("mobile/send/feedue/reminder")
    Call<Bean> SendFeeReminder(@Body FeeReminderRequest input);


    @GET("mobile/learn-tab")
    Call<LearnTabBean> GetLearnTabDetails(@Query("userid") int UserId, @Query("schoolid") int SchoolId,
                                          @Query("academicyearid") int AcademicYearId, @Query("classId") int classId);
}
