package com.jeannypr.scientificstudy.Base.Repo;


import com.jeannypr.scientificstudy.Base.Model.ClassAndSectionsBean;
import com.jeannypr.scientificstudy.Base.Model.ClassBean;
import com.jeannypr.scientificstudy.Student.model.StudentBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BaseService {

    @GET("class")
    Call<ClassBean> getClasses(@Query("schoolid") int schoolid, @Query("academicYearid") int academicYearid);  @GET("class/student")
    Call<StudentBean> getStudents(@Query("classId") int classId);

    @GET("masterclass")
    Call<ClassBean> getMasterClasses(@Query("schoolid") int SchoolId, @Query("academicYearid") int AcademicYearId);

    @GET("masterclass/id/sections")
    Call<ClassBean> getSectionsOfClass(@Query("masterClassid") int MasterClassId, @Query("schoolid") int SchoolId, @Query("academicYearid") int AcademicYearId);

    @GET("class/sections")
    Call<ClassAndSectionsBean> GetClassesAndSections(@Query("schoolid") int SchoolId, @Query("academicYearid") int AcademicYearId);
}
