package com.jeannypr.scientificstudy.Login.api;

import com.jeannypr.scientificstudy.Base.Model.Bean;
import com.jeannypr.scientificstudy.Dashboard.model.WebLoginDetailBean;
import com.jeannypr.scientificstudy.Login.model.AccountExpiredBean;
import com.jeannypr.scientificstudy.Login.model.CheckAppVersionBean;
import com.jeannypr.scientificstudy.Login.model.CheckSessionExpiryBean;
import com.jeannypr.scientificstudy.Login.model.CurrentAcademicYearBean;
import com.jeannypr.scientificstudy.Login.model.FedratedLoginBean;
import com.jeannypr.scientificstudy.Login.model.FedratedLoginInput;
import com.jeannypr.scientificstudy.Login.model.FeedbackInput;
import com.jeannypr.scientificstudy.Login.model.ForgetPasswordBean;
import com.jeannypr.scientificstudy.Login.model.LoginBean;
import com.jeannypr.scientificstudy.Login.model.LoginInputModel;
import com.jeannypr.scientificstudy.Login.model.SchoolDetailBean;
import com.jeannypr.scientificstudy.Login.model.UnableToLoginBean;
import com.jeannypr.scientificstudy.Login.model.UnableToLoginInputModel;
import com.jeannypr.scientificstudy.Login.model.UserAuthenticationBean;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoginService {
    @POST("login/v2")
    Call<LoginBean> login(@Body LoginInputModel input);

    @GET("schooldetailbykey")
    Call<SchoolDetailBean> getSchoolDetailByKey(@Query("schoolkey") String schoolKey);

    @POST("app/unableToLogin")
    Call<UnableToLoginBean> unableToLogin(@Body UnableToLoginInputModel input);

    @GET("getapkversiondetail")
    Call<CheckAppVersionBean> checkAppVersion(@Query("stage") String Stage);

    @GET("current/academicyear")
    Call<CurrentAcademicYearBean> GetCurrentAcademicYear(@Query("schoolid") int SchoolId);

    @GET("login/status")
    Call<CheckSessionExpiryBean> CheckSessionExpiry(@Query("uId") int userId);

    @GET("school/account/status")
    Call<AccountExpiredBean> CheckAccountExpired(@Query("schoolCode") String schoolCode);

    @POST("feedback/add")
    Call<Bean> sendFeedback(@Body FeedbackInput input);

    //TODO set urls.
    @GET("")
    Call<ForgetPasswordBean> getUserDetailsByAdm(@Query("adNo") String admNo);

    //TODO set urls.
    @GET("")
    Call<UserAuthenticationBean> authenticateUser(@Query("") String otp, @Query("") String admNo);

    @POST("federatedlogin")
    Call<FedratedLoginBean> getJWTToken(@Body FedratedLoginInput input);

    @GET("weblogin")
    Call<WebLoginDetailBean> getWebLoginDetails(@Query("userid") int UserId);
}