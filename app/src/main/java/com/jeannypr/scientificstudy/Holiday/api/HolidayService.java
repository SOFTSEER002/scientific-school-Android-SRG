package com.jeannypr.scientificstudy.Holiday.api;

import com.jeannypr.scientificstudy.Base.Model.Bean;
import com.jeannypr.scientificstudy.Holiday.model.HolidayBean;
import com.jeannypr.scientificstudy.Holiday.model.HolidayDetailBean;
import com.jeannypr.scientificstudy.Holiday.model.HolidayInputModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface HolidayService {
    @GET("holidays")
    Call<HolidayBean> GetholidayList(@Query("schoolid") int schoolId,
                                        @Query("academicYearid") int academicyearId);

    @POST("holiday/add")
    Call<Bean> saveHoliday(@Body HolidayInputModel input);

    @GET("holiday/id")
    Call<HolidayDetailBean> getHolidayDetails(@Query("id") int holidayId);

}
