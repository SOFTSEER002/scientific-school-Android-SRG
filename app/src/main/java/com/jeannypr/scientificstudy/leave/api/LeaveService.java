package com.jeannypr.scientificstudy.leave.api;

import com.jeannypr.scientificstudy.Base.Model.Bean;
import com.jeannypr.scientificstudy.leave.model.ApproveLeaveInputModel;
import com.jeannypr.scientificstudy.leave.model.ApproversBean;
import com.jeannypr.scientificstudy.leave.model.DetailBean;
import com.jeannypr.scientificstudy.leave.model.IsApproverBean;
import com.jeannypr.scientificstudy.leave.model.AvailableLeavesBean;
import com.jeannypr.scientificstudy.leave.model.LeaveBean;
import com.jeannypr.scientificstudy.leave.model.LeaveHistoryBean;
import com.jeannypr.scientificstudy.leave.model.LeaveHistoryInputModel;
import com.jeannypr.scientificstudy.leave.model.LogsBean;
import com.jeannypr.scientificstudy.leave.model.MonthLeaveSummaryBean;
import com.jeannypr.scientificstudy.leave.model.MonthWiseLeaveBean;
import com.jeannypr.scientificstudy.leave.model.RequestLeaveInputModel;
import com.jeannypr.scientificstudy.leave.model.SchoolHolidaysBean;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LeaveService {
    @GET("school/holidays")
    Call<SchoolHolidaysBean> GetSchoolHolidays(@Query("schoolid") int SchoolId, @Query("academicYearid") int Academicyearid);

    @GET("leave/teacher/available/summary")
    Call<AvailableLeavesBean> GetAvailableLeaves(@Query("schoolid") int SchoolId, @Query("academicyearId") int Academicyearid,
                                                 @Query("teacherUserId") int TeacherUserId);

    @GET("leave/requests")
    Call<LeaveHistoryBean> GetTeacherLeaveHistory(@Query("schoolid") int SchoolId, @Query("academicyearid") int Academicyearid,
                                                  @Query("userId") int TeacherUserId, @Query("status") int Status,
                                                  @Query("month") int MonthId, @Query("teacher") int TeacherId,
                                                  @Query("pageFor") String PageFor, @Query("viewFor") String LeaveFor);

    @GET("leave/isapprover")
    Call<IsApproverBean> IsApprover(@Query("schoolid") int SchoolId,
                                    @Query("userId") int TeacherUserId);

    @POST("teacher/leave/approve")
    Call<Bean> ApproveOrRejectLeave(@Body ApproveLeaveInputModel model);

    @GET("leave/approvers")
    Call<ApproversBean> GetApproversList(@Query("schoolid") int SchoolId);

    @GET("leave/types")
    Call<LeaveBean> GetLeaveTypes(@Query("schoolid") int SchoolId);

    @POST("teacher/leave/request")
    Call<Bean> RequestTeacherLeave(@Body RequestLeaveInputModel model);

    @GET("leave/days")
    Call<DetailBean> GetLeaveDetail(@Query("leaveId") int leaveId);

    @POST("student/leave/request")
    Call<Bean> RequestStudentLeave(@Body RequestLeaveInputModel model);

    @GET("student/leave/history")
    Call<LeaveHistoryBean> GetStudentLeaveHistory(@Query("schoolid") int SchoolId, @Query("academicYearId") int Academicyearid,
                                                  @Query("studentId") int StudentId);

    @GET("leave/teacher/month")
    Call<MonthWiseLeaveBean> GetMonthLeaveSummary(@Query("month") int Month, @Query("schoolid") int SchoolId,
                                                  @Query("academicyearid") int AcademicyearId);

    @GET(" ")
    Call<MonthLeaveSummaryBean>GetTeacherLeaveMonthSummary(@Query("month")int Month,
                                              @Query("schoolid") int SchoolId,
                                              @Query("academicyearid") int AcademicyearId);

    @GET("leave/teacher/date")
    Call<MonthWiseLeaveBean> GetOnLeaveToday(@Query("leavedate") String LeaveDate,
                                             @Query("schoolid") int SchoolId,
                                             @Query("academicyearid") int AcademicyearId);
}
