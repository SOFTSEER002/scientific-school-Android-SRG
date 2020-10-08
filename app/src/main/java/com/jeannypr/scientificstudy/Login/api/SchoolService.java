package com.jeannypr.scientificstudy.Login.api;
import com.jeannypr.scientificstudy.Dashboard.model.SchoolSettingBean;
import com.jeannypr.scientificstudy.Login.model.CityBean;
import com.jeannypr.scientificstudy.Login.model.CountryBean;
import com.jeannypr.scientificstudy.Login.model.SchoolBean;
import com.jeannypr.scientificstudy.Login.model.StateBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SchoolService {
    @GET("school/countries")
    Call<CountryBean> getCountries();

    @GET("school/states")
    Call<StateBean> getStates(@Query("countryName") String countryName);

    @GET("school/cities")
    Call<CityBean> getCities(@Query("stateName") String stateName);

    @GET("school/names")
    Call<SchoolBean> getSchools(@Query("address") String address);

    @GET("school/setting/schoolid")
    Call<SchoolSettingBean> CheckSchoolSettings(@Query("schoolid")int SchoolId);

}
