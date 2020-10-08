package com.jeannypr.scientificstudy.Fee.api;

import com.jeannypr.scientificstudy.Fee.model.AllowedDiscountBean;
import com.jeannypr.scientificstudy.Fee.model.ClassWiseCollectionBean;
import com.jeannypr.scientificstudy.Fee.model.ClassWiseDuesBean;
import com.jeannypr.scientificstudy.Fee.model.ConsolidatedDuesBean;
import com.jeannypr.scientificstudy.Fee.model.DateWiseCollectionBean;
import com.jeannypr.scientificstudy.Fee.model.DiscountPermissionBean;
import com.jeannypr.scientificstudy.Fee.model.DiscountPermissionModel;
import com.jeannypr.scientificstudy.Fee.model.DiscountStudentDetailBean;
import com.jeannypr.scientificstudy.Fee.model.FeeBean;
import com.jeannypr.scientificstudy.Fee.model.FeeCategoryBean;
import com.jeannypr.scientificstudy.Fee.model.FeeInstallmentBean;
import com.jeannypr.scientificstudy.Fee.model.FeeInstallmentDetailBean;
import com.jeannypr.scientificstudy.Fee.model.FeeSummaryBean;
import com.jeannypr.scientificstudy.Fee.model.HeadWiseCollectionBean;
import com.jeannypr.scientificstudy.Fee.model.InstallmentSummaryBean;
import com.jeannypr.scientificstudy.Fee.model.StudentWiseDueBean;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface FeeService {

    @GET("fee/installment")
    Call<FeeInstallmentBean> GetFeeInstallments(@Query("schoolid") int schoolId, @Query("academicYearid") int academicYearId);

    @GET("fee/categories")
    Call<FeeCategoryBean> GetFeeCategories(@Query("schoolid") int schoolId);

    @GET("fee/student/installments")
    Call<FeeSummaryBean> GetFeeSummary(@Query("studentid") int studentId,
                                       @Query("schoolid") int schoolId, @Query("academicYearid") int academicYearId);

    @GET("fee/payment/detail")
    Call<FeeInstallmentDetailBean> GetFeeInstallmentDetail(@Query("paymentId") int PaymentId,
                                                           @Query("installmentId") int InstallmentId,
                                                           @Query("schoolid") int schoolId,
                                                           @Query("academicYearid") int academicYearId);

    @GET("fee/report/classcollection")
    Call<ClassWiseCollectionBean> GetClassWiseColection(@Query("schoolid") int schoolId, @Query("academicYearid") int academicYearId);

    @GET("fee/report/date")
    Call<DateWiseCollectionBean> GetDateWiseColection(@Query("startDate") String StartDate, @Query("endDate") String EndDate,
                                                      @Query("schoolid") int schoolId, @Query("academicYearid") int academicYearId);

    @GET("fee/report/monthid")
    Call<DateWiseCollectionBean> GetMonthWiseColection(@Query("schoolid") int schoolId, @Query("academicYearid") int academicYearId,
                                                       @Query("month") int month, @Query("year") int year);

    @GET("fee/dues/consolidated")
    Call<ConsolidatedDuesBean> GetConsolidatedDues(@Query("academicYearid") int AcademicYearId,
                                                   @Query("categoryId") int CategoryId,
                                                   @Query("installmentFrom") int InstallmentFrom,
                                                   @Query("installmentTo") int InstallmentTo,
                                                   @Query("schoolid") int SchoolId);

    @GET("fee/report/monthcollection")
    Call<InstallmentSummaryBean> GetInstallmentSummary(@Query("schoolid") int SchoolId, @Query("academicYearid") int AcademicYearId);


    @GET("fee/mis")
    Call<FeeBean> GetFeeCollection(@Query("schoolid") int SchoolId, @Query("academicYearid") int AcademicYearId);

    @GET("fee/class/dues")
    Call<ClassWiseDuesBean> GetClassWiseDueFeeDetail(@Query("schoolid") int SchoolId, @Query("academicyearid") int AcademicYearId,
                                                     @Query("classId") int ClassId, @Query("fromInstallmentId") int FromInstallmentId,
                                                     @Query("toInstallmentId") int ToInstallmentId, @Query("feeCategoryId") int FeeCategoryId,
                                                     @Query("isVoucherInclude") boolean IsVoucherInclude, @Query("isincludeLateFee") boolean IsIncludeLateFee,
                                                     @Query("feegroupId") int FeeGroupId);


    @GET("fee/class/student/dues")
    Call<StudentWiseDueBean> GetStudentWiseDueFeeDetail(@Query("studentId") int StudentId, @Query("schoolid") int SchoolId, @Query("academicyearid") int AcademicyearId,
                                                        @Query("classId") int ClassId,
                                                        @Query("fromInstallmentId") int FromInstallmentId, @Query("toInstallmentId") int ToInstallmentId,
                                                        @Query("feeCategoryId") int FeeCategoryId, @Query("isVoucherInclude") boolean IsVoucherInclude,
                                                        @Query("isincludeLateFee") boolean IsIncludeLateFee, @Query("feegroupId") int FeegroupId);

    @GET("fee/report/headwisefeecollection")
    Call<HeadWiseCollectionBean> GetHeadWiseCollection(@Query("startdate") String StartDate, @Query("enddate") String EndDate, @Query("schoolid") int SchoolId, @Query("academicyearid") int AcademicYearId);

    @GET("fee/userpemissionfordiscount")
    Call<AllowedDiscountBean> GetPermissionDiscount(@Query("userid") int UserId, @Query("schoolid") int Schoolid);

    @GET("fee/studentfeedetail")
    Call<DiscountStudentDetailBean> GetStudentFeeDeatil(@Query("admNo") String AdmNo, @Query("schoolId") int SchoolId, @Query("academicYearid") int AcademicYearId);

    @POST("fee/studentdiscountpermission")
    Call<DiscountPermissionBean> SaveDiscountPermission(@Body DiscountPermissionModel input);
}
