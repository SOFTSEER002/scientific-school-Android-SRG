package com.jeannypr.scientificstudy.Article.api;

import com.google.gson.JsonObject;
import com.jeannypr.scientificstudy.Article.model.ArticleBean;
import com.jeannypr.scientificstudy.Article.model.ArticleCategoryBean;
import com.jeannypr.scientificstudy.Article.model.ArticleSaveBean;
import com.jeannypr.scientificstudy.Article.model.ArticleSaveModel;
import com.jeannypr.scientificstudy.Base.Model.Bean;
import com.jeannypr.scientificstudy.Classwork.model.CwHwSaveModel;
import com.jeannypr.scientificstudy.LearnSubject.model.LearnSubjectContentBean;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ArticleService {
    @GET("mobile/article/list")
    Call<ArticleBean> getArticleList(@Query("userId") int userid, @Query("categoryId") int categoryId);


    //mobile/learn-tab/subjectcontent
    @GET("mobile/learn-tab/subjectcontent")
    Call<LearnSubjectContentBean> getLearnSubjectContent(@Query("userid") int userid, @Query("subjectid") int subjectId, @Query("classid") int classid,
                                                         @Query("schoolid") int schoolId, @Query("academicyearid") int academicyearId,
                                                         @Query("chapterid") int chapterId);

    ///mobile/article/SaveArticle
    @POST("mobile/article/SaveArticle")
    Call<ArticleSaveBean> saveSaveArticle(@Body ArticleSaveModel model);

    @POST("class/activity/save")
    Call<ArticleSaveBean> SaveCwHw(@Body CwHwSaveModel model);
    //Job Completed List
    @POST("mobile/article/SaveArticle")
    Observable<Bean> saveSaveArticle2(@Body JsonObject request);

    ///api/mobile/article/category
    @GET("mobile/article/category/list")
    Call<ArticleCategoryBean> getArticleCategory(@Query("userId") int userid, @Query("categoryId") int categoryId);


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
