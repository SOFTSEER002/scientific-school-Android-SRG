package com.jeannypr.scientificstudy.Transport.api;

import com.jeannypr.scientificstudy.Base.Model.Bean;
import com.jeannypr.scientificstudy.Transport.model.AssignedRouteBean;
import com.jeannypr.scientificstudy.Transport.model.ClassSummaryBean;
import com.jeannypr.scientificstudy.Transport.model.CurrentJourneyDetailBean;
import com.jeannypr.scientificstudy.Transport.model.DriverBean;
import com.jeannypr.scientificstudy.Transport.model.EmergencyContactBean;
import com.jeannypr.scientificstudy.Transport.model.GetNotificationBean;
import com.jeannypr.scientificstudy.Transport.model.JourneyDetailModel;
import com.jeannypr.scientificstudy.Transport.model.NotificationBean;
import com.jeannypr.scientificstudy.Transport.model.NotificationModel;
import com.jeannypr.scientificstudy.Transport.model.RouteBean;
import com.jeannypr.scientificstudy.Transport.model.RouteDetailBean;
import com.jeannypr.scientificstudy.Transport.model.RouteListBean;
import com.jeannypr.scientificstudy.Transport.model.RouteSummaryBean;
import com.jeannypr.scientificstudy.Transport.model.SaveJourneyBean;
import com.jeannypr.scientificstudy.Transport.model.StoppageSummaryBean;
import com.jeannypr.scientificstudy.Transport.model.LocationDetailModel;
import com.jeannypr.scientificstudy.Transport.model.TransportBean;
import com.jeannypr.scientificstudy.Transport.model.VehicleCurrentLoctionBean;
import com.jeannypr.scientificstudy.Transport.model.VehicleRouteBean;
import com.jeannypr.scientificstudy.Transport.model.VehiclesBean;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TransportService {
    @GET("transport/student/view")
    Call<TransportBean> GetTransportDetails(@Query("studentid") int StudentId, @Query("schoolid") int SchoolId,
                                            @Query("academicYearid") int AcademicyearId);

    @GET("route/summary")
    Call<RouteSummaryBean> GetRouteSummary(@Query("schoolid") int SchoolId, @Query("academicYearid") int AcademicyearId);

    @GET("route/students/summary")
    Call<RouteDetailBean> GetRouteDetail(@Query("schoolid") int SchoolId, @Query("academicYearid") int AcademicyearId,
                                         @Query("routeId") int RouteId);

    @GET("stoppage/summary")
    Call<StoppageSummaryBean> GetStoppageSummary(@Query("schoolid") int SchoolId, @Query("academicYearid") int AcademicyearId);

    @GET("stoppgae/students/summary")
    Call<RouteDetailBean> GetStoppageDetail(@Query("schoolid") int SchoolId, @Query("academicYearid") int AcademicyearId,
                                            @Query("stoppageName") String StoppageName);

    @GET("class/transport")
    Call<ClassSummaryBean> GetClassWiseSummary(@Query("schoolid") int SchoolId, @Query("academicYearid") int AcademicyearId);

    @GET("class/student/transport")
    Call<RouteDetailBean> GetClassWiseDetail(@Query("schoolid") int SchoolId, @Query("academicYearid") int AcademicyearId,
                                             @Query("classId") int ClassId);

    @GET("routes")
    Call<RouteBean> GetRoutes(@Query("schoolid") int SchoolId, @Query("academicYearid") int AcademicyearId);

    @GET("vehicles")
    Call<VehiclesBean> GetVehicles(@Query("schoolid") int SchoolId, @Query("academicYearid") int AcademicyearId);

    @GET("vehicle/route/id")
    Call<VehicleRouteBean> GetAssignedVehicle(@Query("schoolid") int SchoolId, @Query("academicYearid") int AcademicyearId, @Query("routeid") int RouteId);

    @POST("vehicle/journey")
    Call<SaveJourneyBean> SaveJourneyDetails(@Body JourneyDetailModel input);

    @POST("vehicle/location")
    Call<Bean> SaveLocationDetails(@Body LocationDetailModel input);

    @GET("vehicle/current/location")
    Call<VehicleCurrentLoctionBean> GetVehicleCurrentLocation(@Query("studentId") int StudentId, @Query("schoolId") int SchoolId, @Query("academicYearId") int AcademicYearId);

    @GET("routes/driver/userid")
    Call<AssignedRouteBean> GetAssignedRoute(@Query("userid") int UserId, @Query("schoolId") int SchoolId);

    @POST("vehicle/journey/push/notification")
    Call<NotificationBean> SaveNotificationDetails(@Body NotificationModel input);

    @GET("transport/vehicle/drivers")
    Call<DriverBean> GetDrivers(@Query("schoolid") int SchoolId);

    @GET("transport/journey/detail")
    Call<CurrentJourneyDetailBean> GetCurrentJourneyDetail(@Query("schoolid") int SchoolId, @Query("routeid") int RouteId);

    @GET("transport/routes/detail")
    Call<RouteListBean> GetRouteAndDriver(@Query("schoolId") int SchoolId);

    @GET("school/schoolcontact")
    Call<EmergencyContactBean> GetContacts(@Query("schoolid") int SchoolId);

    @GET("/getAllNotifications")
    Call<GetNotificationBean> GetAllNotifications(@Query("SchoolCode") String SchoolCode,@Query("UserId") int UserId,
                                                  @Query("Skip") int Skip, @Query("Take") int Take);
}
