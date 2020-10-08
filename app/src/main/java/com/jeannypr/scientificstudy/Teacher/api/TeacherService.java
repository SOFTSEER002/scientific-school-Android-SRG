package com.jeannypr.scientificstudy.Teacher.api;

import com.jeannypr.scientificstudy.Attendance.model.MonthTeacherAttendanceBean;
import com.jeannypr.scientificstudy.Attendance.model.StaffWiseAttendanceBean;
import com.jeannypr.scientificstudy.Attendance.model.TeacherAttendanceSaveModel;
import com.jeannypr.scientificstudy.Base.Model.Bean;
import com.jeannypr.scientificstudy.Base.Model.ClassBean;
import com.jeannypr.scientificstudy.Exam.model.AbsentExamBean;
import com.jeannypr.scientificstudy.Exam.model.ClassWiseSubjectBean;
import com.jeannypr.scientificstudy.Exam.model.TeacherSubjectBean;
import com.jeannypr.scientificstudy.Student.model.StudentDetailBean;
import com.jeannypr.scientificstudy.Teacher.model.SelfAttendanceBean;
import com.jeannypr.scientificstudy.Teacher.model.StaffInputModel;
import com.jeannypr.scientificstudy.Teacher.model.StaffProfileBean;
import com.jeannypr.scientificstudy.Teacher.model.TeacherBean;
import com.jeannypr.scientificstudy.Teacher.model.TeacherDetailBean;
import com.jeannypr.scientificstudy.Teacher.model.TeacherProfileBean;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TeacherService {
    @GET("teachers")
    Call<TeacherBean> getTeachers(@Query("schoolid") int schoolId, @Query("academicYearid") int academicYearid);

    @GET("profile/teacher")
    Call<TeacherProfileBean> getTeacherProfile(@Query("userid") int userId, @Query("schoolid") int schoolId, @Query("academicYearid") int academicYearid);

    @GET("class/teacher/userid")
    Call<ClassBean> GetMyClasses(@Query("schoolid") int SchoolId, @Query("academicYearid") int AcademicYearId, @Query("teacherUserid") int TeacherUserId);

    @GET("attendance/teacher/today")
    Call<SelfAttendanceBean> GetSelfAttendanceOfToday(@Query("userid") int userId, @Query("schoolid") int schoolId, @Query("academicYearid") int academicYearid);

    @POST("teacher/self/attendance/save")
    Call<SelfAttendanceBean> SaveSelfAttendanceOfToday(@Body TeacherAttendanceSaveModel input);

    @GET("attendance/teacher/id")
    Call<StaffWiseAttendanceBean> getstaffAttendance(@Query("teacherid") int teacherId, @Query("schoolid") int schoolId,
                                                     @Query("academicyearid") int academicyearId);

    @GET("test/absent/report")
    Call<AbsentExamBean> GetExamAbsentList(@Query("classId") int classId, @Query("testId") int testId,
                                           @Query("subjectId") int subjectId,
                                           @Query("schoolid") int schoolId, @Query("academicyearid") int academicYearId);

    @GET("class/subjects/teacher")
    Call<ClassWiseSubjectBean> GetClassSubject(@Query("classid") int classId,
                                               @Query("schoolid") int schoolId,
                                               @Query("academicYearid") int academicYearid);

    @GET("teacher/subjects")
    Call<TeacherSubjectBean> GetStaffSubject(@Query("teacherId") int teacherUserId, @Query("schoolid") int schoolId,
                                             @Query("academicyearid") int academicYearId);

    @GET("teacher/myAllClasses")
    Call<ClassBean> GetMyAllClasses(@Query("schoolid") int SchoolId, @Query("academicYearid") int AcademicYearId, @Query("teacherUserid") int TeacherUserId);

    @GET("staff/detail")
    Call<StaffProfileBean> getTeacherDetails(@Query("userid") int userId, @Query("schoolid") int schoolId, @Query("academicYearid") int academicYearid);

    @POST("mobile/add/edit/teacher")
    Call<Bean> saveTeacher(@Body StaffInputModel input);

    @GET("teacher/detail/id")
    Call<TeacherDetailBean> getTeacherDetails(@Query("Id") int teacherId);

}
