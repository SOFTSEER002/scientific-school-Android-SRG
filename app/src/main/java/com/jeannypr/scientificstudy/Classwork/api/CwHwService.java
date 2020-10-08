package com.jeannypr.scientificstudy.Classwork.api;

import com.jeannypr.scientificstudy.Base.Model.Bean;
import com.jeannypr.scientificstudy.Classwork.model.ActivityBean;
import com.jeannypr.scientificstudy.Classwork.model.ActivityDetailBean;
import com.jeannypr.scientificstudy.Classwork.model.AssignmentBean;
import com.jeannypr.scientificstudy.Classwork.model.CommentInputModel;
import com.jeannypr.scientificstudy.Classwork.model.CwHwSaveModel;
import com.jeannypr.scientificstudy.Classwork.model.HWCommentsBean;
import com.jeannypr.scientificstudy.Classwork.model.SaveCwHwResponseBean;
import com.jeannypr.scientificstudy.Syllabus.model.SyllabusBean;

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

public interface CwHwService {
    @GET("class/latestactivities")
    Call<ActivityBean> getClassworkListForStaff(@Query("userid") int UserId,
                                                @Query("profile") String Profile,
                                                @Query("schoolid") int SchoolId,
                                                @Query("academicYearid") int AcademicYearId,
                                                @Query("classid") int Classid,
                                                @Query("subjectId") int SubjectId,
                                                @Query("activityType") int ActivityType);

    @GET("class/activities/student/id")
    Call<ActivityBean> getClassworkListForParent(@Query("studentid") int Studentid,
                                                 @Query("schoolid") int SchoolId,
                                                 @Query("academicYearid") int AcademicYearId,
                                                 @Query("classid") int Classid,
                                                 @Query("subjectId") int SubjectId,
                                                 @Query("activityType") int ActivityType);

    @GET("class/activity/id")
    Call<ActivityDetailBean> getClassworkDetail(@Query("activityId") int ActivityId,
                                                @Query("schoolid") int SchoolId,
                                                @Query("academicYearid") int AcademicYearId);

    @POST("class/activity/material/delete")
    Call<Bean> DeleteAttachment(@Query("activityId") int ActivityId,
                                @Query("schoolid") int SchoolId,
                                @Query("academicYearid") int AcademicYearId,
                                @Query("attachmentName") String AttachmentName,
                                @Query("activityType") int ActivityType);

    @POST("class/activity/save")
    Call<SaveCwHwResponseBean> SaveCwHw(@Body CwHwSaveModel model);

    @Multipart
    @POST("class/activity/upload")
    Observable<Bean> postImage(@Query("schoolId") int schoolId, @Query("activityId") int activityId, @Query("activityType") int type,
                               @Part MultipartBody.Part image, @Part("name") RequestBody name);

    @GET("admin/syllabus/detail")
    Call<SyllabusBean> getSyllabusForAdmin(@Query("classId") int ClassId,
                                           @Query("schoolId") int SchoolId,
                                           @Query("academicYearId") int AcademicYearId);

    @GET("teacher/syllabus/detail")
    Call<SyllabusBean> getSyllabusForTeacher(@Query("userId") int UserId,
                                             @Query("schoolId") int SchoolId,
                                             @Query("academicYearId") int AcademicYearId);

    @GET("student/syllabus/detail")
    Call<SyllabusBean> getSyllabusForParent(@Query("studentId") int StudentId,
                                            @Query("schoolId") int SchoolId,
                                            @Query("academicYearId") int AcademicYearId);

    @GET("assignmentcomments")
    Call<HWCommentsBean> getAllHWComments(@Query("assignmentId") int AssignmentId,
                                          @Query("studentId") int StudentId,
                                          @Query("teacherUserId") int TeacherId);

    @GET("assignmentsummary")
    Call<AssignmentBean> getStudents(@Query("assignmentId") int AssignmentId);

    @POST("updateassignmentstatus")
    Call<Bean> bulkHWOperation(@Query("assignmentId") int assignmentId, @Query("teacherUserId") int teacherUserId, @Query("studentIds") String studentIds,
                               @Query("status") String status, @Query("commentedBy") int commentedBy);

    //http://mobileqa.scientificstudy.in/api/updateassignmentstatus?assignmentId=43&teacherUserId=1&studentIds=2,3&status=Completed&commentedBy=2
    @Multipart
    @POST("saveassignmentcomment")
    Observable<Bean> saveCommentFile(@Query("assignmentId") int assignmentId, @Query("studentId") int studentId, @Query("teacherUserId") int teacherUserId,
                                     @Query("Comment") String Comment, @Query("currentStatus") String CurrentStatus,
                                     @Query("fileType") int FileType, @Query("commentedBy") int CommentedBy,
                                     @Part MultipartBody.Part image);

    @POST("saveassignmentcomment")
    Observable<Bean> saveComment(@Query("assignmentId") int assignmentId, @Query("studentId") int studentId, @Query("teacherUserId") int teacherUserId,
                                 @Query("fileType") int FileType, @Query("commentedBy") int CommentedBy,
                                 @Query("Comment") String Comment, @Query("currentStatus") String CurrentStatus);
}
