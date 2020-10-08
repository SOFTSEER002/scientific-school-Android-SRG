package com.jeannypr.scientificstudy.Exam.api;

import com.jeannypr.scientificstudy.Base.Model.Bean;
import com.jeannypr.scientificstudy.Exam.model.ExamListBean;
import com.jeannypr.scientificstudy.Exam.model.ExamRoasterBean;
import com.jeannypr.scientificstudy.Exam.model.MarkResponseBean;
import com.jeannypr.scientificstudy.Exam.model.StudentMarkBean;
import com.jeannypr.scientificstudy.Exam.model.StudentMarkSaveModel;
import com.jeannypr.scientificstudy.Exam.model.StudentRemarkBean;
import com.jeannypr.scientificstudy.Exam.model.StudentRemarkSaveModel;
import com.jeannypr.scientificstudy.Exam.model.SubjectExamBean;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ExamService {
    @GET("exam/student/marks")
    Call<StudentMarkBean> getStudentMarks(@Query("classId") int classId, @Query("subjectId") int subjectId, @Query("testId") int testId,
                                          @Query("schoolId") int schoolId, @Query("academicYearid") int academicyearId);

    @GET("exam/student/remarks")
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
                                               @Query("schoolid") int SchoolId, @Query("academicyearid") int AcademicYearId);

}
