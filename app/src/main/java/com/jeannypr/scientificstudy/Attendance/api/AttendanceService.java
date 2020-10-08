package com.jeannypr.scientificstudy.Attendance.api;

import com.jeannypr.scientificstudy.Attendance.model.ExtraDayReportBean;
import com.jeannypr.scientificstudy.Attendance.model.MonthAttendanceBean;
import com.jeannypr.scientificstudy.Attendance.model.MonthBean;
import com.jeannypr.scientificstudy.Attendance.model.MonthTeacherAttendanceBean;
import com.jeannypr.scientificstudy.Attendance.model.StudentAttendanceBean;
import com.jeannypr.scientificstudy.Attendance.model.StudentAttendanceSaveModel;
import com.jeannypr.scientificstudy.Attendance.model.TeacherAttendanceBean;
import com.jeannypr.scientificstudy.Attendance.model.TeacherAttendanceSaveModel;
import com.jeannypr.scientificstudy.Holiday.model.HolidayBean;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AttendanceService {
    @GET("attendance/student")
    Call<StudentAttendanceBean> getStudentAttendance(@Query("classid") int classId, @Query("schoolid") int schoolId,
                                                     @Query("academicyearid") int academicyearId,
                                                     @Query("day") int day, @Query("month") int month, @Query("year") int year);

    @POST("attendance/student/save")
    Call<StudentAttendanceBean> saveStudentAttendance(@Body StudentAttendanceSaveModel data);

    @GET("attendance/teacher/date")
    Call<TeacherAttendanceBean> getTeacherAttendance(@Query("schoolid") int schoolId, @Query("academicyearid") int academicyearId,
                                                     @Query("selecteddate") String date);

    @POST("teacher/attendance/save")
    Call<TeacherAttendanceBean> saveTeacherAttendance(@Body TeacherAttendanceSaveModel data);

    @GET("attendance/student/month")
    Call<MonthAttendanceBean> getMonthStudentAttendance(@Query("classid") int classId, @Query("schoolId") int schoolId,
                                                        @Query("academicYearId") int academicyearId, @Query("monthId") int monthId);

    @GET("attendance/teacher/month")
    Call<MonthTeacherAttendanceBean> getMonthTeacherAttendance(@Query("month") int monthId, @Query("schoolid") int schoolId,
                                                               @Query("academicYearId") int academicyearId);

    @GET("attendance/teacher/extradays")
    Call<ExtraDayReportBean> getExtraDay(@Query("teacherid") int teachreId, @Query("monthId") int monthId, @Query("schoolid") int schoolId,
                                         @Query("academicyearid") int academicyearId);

    @GET("attendance/teacher/outdoors")
    Call<ExtraDayReportBean> getOutDoorAttendance(@Query("teacherid") int teachreId, @Query("monthId") int monthId, @Query("schoolid") int schoolId,
                                                  @Query("academicyearid") int academicyearId);

    @GET("payment/months")
    Call<MonthBean> GetMonthList(@Query("schoolid") int SchoolId, @Query("academicYearid") int AcademicYearId);

    @GET("mobile/holiday/list")
    Call<HolidayBean> getHolidays(@Query("schoolid") int SchoolId, @Query("academicyearid") int AcademicYearId);

}
