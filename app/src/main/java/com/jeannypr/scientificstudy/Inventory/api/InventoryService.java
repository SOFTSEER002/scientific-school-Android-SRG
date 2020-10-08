package com.jeannypr.scientificstudy.Inventory.api;

import com.jeannypr.scientificstudy.Base.Model.Bean;
import com.jeannypr.scientificstudy.Inventory.model.AccountGroupBean;
import com.jeannypr.scientificstudy.Inventory.model.AddLedgerBean;
import com.jeannypr.scientificstudy.Inventory.model.AddLedgerInputModel;
import com.jeannypr.scientificstudy.Inventory.model.AllLedgerBean;
import com.jeannypr.scientificstudy.Inventory.model.CreditLedgerBean;
import com.jeannypr.scientificstudy.Inventory.model.CurrentBalanceBean;
import com.jeannypr.scientificstudy.Inventory.model.DayBookBean;
import com.jeannypr.scientificstudy.Inventory.model.ItemDetailsBean;
import com.jeannypr.scientificstudy.Inventory.model.ItemsBean;
import com.jeannypr.scientificstudy.Inventory.model.LedgerReportBean;
import com.jeannypr.scientificstudy.Inventory.model.PartyAccountBean;
import com.jeannypr.scientificstudy.Inventory.model.PaymentReceiptInputModel;
import com.jeannypr.scientificstudy.Inventory.model.PaymentReceiptSummaryBean;
import com.jeannypr.scientificstudy.Inventory.model.PurchaseLedgerBean;
import com.jeannypr.scientificstudy.Inventory.model.PurchaseSaleItemBean;
import com.jeannypr.scientificstudy.Inventory.model.PurchaseSaleSummaryDateBean;
import com.jeannypr.scientificstudy.Inventory.model.PurchaseSaleSummaryBean;
import com.jeannypr.scientificstudy.Inventory.model.PurchaseSummaryBean;
import com.jeannypr.scientificstudy.Inventory.model.VoucharBean;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface InventoryService {
    @GET("inv/purchase/report")
    Call<PurchaseSummaryBean> GetPurchaseSummary(@Query("startDate") String StartDate, @Query("endDate") String EndDate,
                                                 @Query("schoolid") int SchoolId, @Query("academicyearid") int Academicyearid);

    @GET("inv/sale/report")
    Call<PurchaseSummaryBean> GetSaleSummary(@Query("startDate") String StartDate, @Query("endDate") String EndDate,
                                             @Query("schoolid") int SchoolId, @Query("academicyearid") int Academicyearid);

    @GET("inv/payment/report")
    Call<PaymentReceiptSummaryBean> GetPaymentReceiptSummary(@Query("startDate") String StartDate, @Query("endDate") String EndDate,
                                                             @Query("schoolid") int SchoolId, @Query("academicyearid") int Academicyearid, @Query("paymentType") String PaymentType);

    @GET("account/day/book")
    Call<DayBookBean> GetTransactionRecord(@Query("startDate") String StartDate, @Query("endDate") String EndDate,
                                           @Query("schoolid") int SchoolId, @Query("academicyearid") int AcademicyearId);

    @GET("inv/credit/ledgers")
    Call<CreditLedgerBean> GetCreditLedgerRecord(@Query("schoolid") int SchoolId);

    @GET("inv/vouchar/detail")
    Call<VoucharBean> GetVoucharDetail(@Query("schoolid") int SchoolId, @Query("voucharType") String VoucharType, @Query("academicYearid") int AcademicId);// @Query("academicYearid") int AcademicId

    @GET("inv/ledger/balance")
    Call<CurrentBalanceBean> GetLedgerDetail(@Query("schoolid") int SchoolId, @Query("ledgerId") int LedegrId);

    @GET("inv/all/ledgers")
    Call<AllLedgerBean> GetAllLedgerDetail(@Query("schoolid") int SchoolId);


    @GET("account/transaction/summary")
    Call<PurchaseSaleSummaryBean> GetPurchaseSaleSummary(@Query("schoolid") int SchoolId, @Query("academicyearid") int AcademicyearId, @Query("voucherType") String VoucharType);

    @GET("inv/itemdatewise/transaction/summary/date")
    Call<PurchaseSaleItemBean> GetPurchaseSaleItemSummary(@Query("voucherType") String VoucharType, @Query("date") String Date,
                                                          @Query("schoolid") int SchoolId, @Query("academicYearid") int AcademicyearId);

    @GET("inv/item/transaction/summary/month")
    Call<PurchaseSaleSummaryDateBean> GetPurchaseSaleSummaryDate(@Query("schoolid") int SchoolId, @Query("academicyearid") int AcademicyearId,
                                                                 @Query("voucherType") String VoucharType, @Query("monthId") int MonthId);

    @POST("inv/payment/save")
    Call<Bean> SavePaymentReceipt(@Body PaymentReceiptInputModel input);

    @GET("account/ledger/report")
    Call<LedgerReportBean> GetLedgerReport(@Query("startDate") String StarDate, @Query("endDate") String EndDate, @Query("schoolid") int SchoolId,
                                           @Query("academicyearid") int AcademicyearId, @Query("ledgerId") int LedgerId);

    @GET("account/party/accounts")
    Call<PartyAccountBean> GetPartyAccountDetail(@Query("schoolid") int SchholId);

    @GET("inv/purchase/ledgers")
    Call<PurchaseLedgerBean> GetPurchaseLedgerDetail(@Query("schoolid") int SchoolId);

    @GET("inv/items")
    Call<ItemsBean> GetItems(@Query("schoolid") int SchoolId, @Query("academicYearid") int AcademicYearId);

    @GET("inv/item/cost/price")
    Call<ItemDetailsBean> GetItemDetails(@Query("schoolid") int SchoolId, @Query("academicyearid") int AcademicyearId, @Query("itemId") int ItemId);

    @GET("inv/account/groups")
    Call<AccountGroupBean> GetAccountGroupDetail(@Query("schoolid") int SchholId);

    @POST("inv/add/ledger")
    Call<AddLedgerBean> SaveLedger(@Body AddLedgerInputModel input);
}
