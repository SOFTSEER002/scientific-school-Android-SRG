package com.jeannypr.scientificstudy.LearnSubject.api;

import com.jeannypr.scientificstudy.LearnSubject.model.LearnSubjectContentBean;
import com.jeannypr.scientificstudy.LearnSubject.model.LearnSubjectDetailsBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LearnSubjectService {
    @GET("mobile/learn-tab/subjectdetails")
    Call<LearnSubjectDetailsBean> getLearnSubjectDetails(@Query("userid") int userid, @Query("subjectid") int subjectId, @Query("classid") int classid,
                                                         @Query("schoolid") int schoolId, @Query("academicyearid") int academicyearId);


    //mobile/learn-tab/subjectcontent
    @GET("mobile/learn-tab/subjectcontent")
    Call<LearnSubjectContentBean> getLearnSubjectContent(@Query("userid") int userid, @Query("subjectid") int subjectId, @Query("classid") int classid,
                                                         @Query("schoolid") int schoolId, @Query("academicyearid") int academicyearId,
                                                         @Query("chapterid") int chapterId);

   /* @GET("exam/student/remarks")
    Call<StudentRemarkBean> getStudentRemarks(@Query("classId") int classId, @Query("testId") int testId, @Query("academicYearid") int academicyearId);

    @POST("exam/marks/save")
    Call<MarkResponseBean> saveStudentMarks(@Body StudentMarkSaveModel model);

    @POST("academic/exam/remarks/save")
    Call<Bean> saveStudentRemarks(@Body StudentRemarkSaveModel model);

    @GET("Teacher/GetTestAndSubjects")
    Call<SubjectExamBean> getSubjectsAndExams(@Query("classId") int classId, @Query("teacheruserId") int teacheruserId, @Query("mode") String mode,
                                              @Query("schoolId") int schoolId, @Query("academicYearid") int academicyearId);

    @GET("masterclass/id/subjects")
    Call<SubjectExamBean> GetSubjectsOfMasterClass(@Query("masterClassid") int classId,
                                                   @Query("schoolId") int schoolId, @Query("academicYearid") int academicyearId);

    @GET("test")
    Call<ExamListBean> GetExamsList(@Query("classId") int ClassId,
                                    @Query("schoolid") int SchoolId,
                                    @Query("academicYearid") int AcademicYearId);

    @GET("test/subjects")
    Call<ExamRoasterBean> GetExamDates(@Query("scheduletestid") int ExamId,
                                       @Query("classId") int ClassId);

    @GET("student/test/mark")
    Call<ExamRoasterBean> GetExamDatesAndMarks(@Query("scheduletestid") int ExamId,
                                               @Query("classId") int ClassId, @Query("studentid") int StudentId,
                                               @Query("schoolid") int SchoolId, @Query("academicyearid") int AcademicYearId);*/

}
