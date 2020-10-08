package com.jeannypr.scientificstudy.Registration.api;

import com.jeannypr.scientificstudy.Base.Model.Bean;
import com.jeannypr.scientificstudy.Registration.model.AcademicYearsBean;
import com.jeannypr.scientificstudy.Registration.model.ClassWiseRegistrationBean;
import com.jeannypr.scientificstudy.Registration.model.RegSourceBean;
import com.jeannypr.scientificstudy.Registration.model.RegistrationBean;
import com.jeannypr.scientificstudy.Registration.model.RegistrationFeeBean;
import com.jeannypr.scientificstudy.Registration.model.TakeRegModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RegistrationService {

    @GET("admission/mis")
    Call<RegistrationBean> GetRegistrationCollection(@Query("schoolid") int SchoolId, @Query("academicYearid") int AcademicYearId);

    @GET("admission/summary")
    Call<ClassWiseRegistrationBean> GetAddmissionSummary(@Query("schoolid") int SchoolId, @Query("academicYearid") int AcademicYearId);

    @POST("SaveRegistration")
    Call<Bean> registration(@Body TakeRegModel input);

    @GET("academicyear/list")
    Call<AcademicYearsBean> getAcademicYearList(@Query("schoolId") int SchoolId);

    @GET("registrationfee")
    Call<RegistrationFeeBean> getRegistrationFee(@Query("masterClassId") int MasterClassId, @Query("schoolId") int SchoolId, @Query("academicYearId") int AcademicYearId);

    @GET("registrationsource/list")
    Call<RegSourceBean> getRegSource();
}
