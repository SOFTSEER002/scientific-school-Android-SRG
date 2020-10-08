package com.jeannypr.scientificstudy.Student.api;

import com.jeannypr.scientificstudy.Base.Model.Bean;
import com.jeannypr.scientificstudy.Exam.model.SubjectExamBean;
import com.jeannypr.scientificstudy.Student.model.AdmissionBean;
import com.jeannypr.scientificstudy.Student.model.StudentDetailBean;
import com.jeannypr.scientificstudy.Student.model.StudentInputModel;
import com.jeannypr.scientificstudy.Student.model.AttendanceBean;
import com.jeannypr.scientificstudy.Student.model.ClassTeachersBean;
import com.jeannypr.scientificstudy.Student.model.MonthwiseAttendanceBean;
import com.jeannypr.scientificstudy.Student.model.MyTeachersBean;
import com.jeannypr.scientificstudy.Student.model.StudentBean;
import com.jeannypr.scientificstudy.Student.model.StudentProfileBean;
import com.jeannypr.scientificstudy.Student.model.SubjectTeacherBean;

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

public interface StudentService {

    @GET("class/student")
    Call<StudentBean> getStudents(@Query("classId") int classId);

    @GET("profile/student")
    Call<StudentProfileBean> getStudentProfile(@Query("studentid") int studentId, @Query("schoolid") int schoolId,
                                               @Query("academicYearid") int academicyearId);

    @GET("class/teachers")
    Call<ClassTeachersBean> getClassTeachers(@Query("classid") int classId, @Query("schoolid") int schoolId,
                                             @Query("academicYearid") int academicyearId);

    @GET("student/teachers")
    Call<MyTeachersBean> getMyTeachers(@Query("studentid") int UserId, @Query("schoolid") int schoolId);

    @GET("class/assignedteachers")
    Call<SubjectTeacherBean> getSubjectTeachers(@Query("classid") int classId,
                                                @Query("academicYearid") int academicyearId);

    @GET("attendance/class/student/id")
    Call<MonthwiseAttendanceBean> GetMonthwiseAttendanceSummary(@Query("classid") int ClassId,
                                                                @Query("studentid") int StudentId,
                                                                @Query("schoolid") int SchoolId,
                                                                @Query("academicYearid") int AcademicYearId);

    @GET("attendance/student/days/report")
    Call<AttendanceBean> GetMonthlyAttendanceReport(@Query("month") int Month,
                                                    @Query("classid") int ClassId,
                                                    @Query("studentid") int StudentId,
                                                    @Query("schoolid") int SchoolId,
                                                    @Query("academicYearid") int AcademicYearId);

    @GET("Teacher/GetTestAndSubjects")
    Call<SubjectExamBean> getSubjectsForParent(@Query("classId") int classId, @Query("mode") String mode,
                                               @Query("schoolId") int schoolId, @Query("academicYearid") int academicyearId);

    @Multipart
    @POST("student/image/upload")
    Call<Bean> UploadStudentPic(@Query("id") int Id, @Query("schoolId") int SchoolId,
                                @Part MultipartBody.Part image, @Part("name") RequestBody name);

    @Multipart
    @POST("teacher/image/upload")
    Call<Bean> UploadTeacherPic(@Query("schoolId") int SchoolId, @Query("id") int Id,
                                @Part MultipartBody.Part image, @Part("name") RequestBody name);

    @GET("attendance/class/student/id")
    Call<MonthwiseAttendanceBean> GetStudentWiseAttendanceReport(@Query("classid") int ClassId,
                                                                 @Query("studentid") int StudentId,
                                                                 @Query("schoolid") int SchoolId,
                                                                 @Query("academicyearid") int AcademicYearId);

    @Multipart
    @POST("student/image/upload")
    Observable<Bean> UploadStudentphoto(@Query("schoolId") int SchoolId, @Query("id") int StudentId, @Query("mode") String Mode,
                                        @Part MultipartBody.Part image, @Part("name") RequestBody name);

    @GET("student/details")
    Call<StudentProfileBean> GetStudentDetails(@Query("schoolId") int schoolId, @Query("academicYearId") int academicYearId, @Query("studentId") int studentId,
                                               @Query("classId") int classId);

    @POST("mobile/add/edit/student")
    Call<Bean> addStudnet(@Body StudentInputModel input);

    @GET("profile/student")
    Call<StudentDetailBean> getStudentDetails(@Query("studentid") int studentId, @Query("schoolid") int schoolId, @Query("academicYearid") int academicYearId);

    @GET("student/admission/number")
    Call<AdmissionBean> getValidAdmissionNo(@Query("schoolId") int schoolId, @Query("academicYearId") int academicYearId);
}