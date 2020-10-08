package com.jeannypr.scientificstudy.Inventory.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PurchaseSummaryModel implements Parcelable {

    public void setTransactionId(int transactionId) {
        TransactionId = transactionId;
    }

    public void setDateString(String dateString) {
        DateString = dateString;
    }

    public void setLedgerName(String ledgerName) {
        LedgerName = ledgerName;
    }

    public void setPartyAccount(String partyAccount) {
        PartyAccount = partyAccount;
    }

    public void setVoucherType(String voucherType) {
        VoucherType = voucherType;
    }

    public void setBillNumber(String billNumber) {
        BillNumber = billNumber;
    }

    public int getTransactionId() {
        return TransactionId != -1 ? TransactionId : 0;
    }

    public String getDateString() {
        return DateString != null ? DateString : "";
    }

    public String getLedgerName() {
        return LedgerName != null ? LedgerName : "";
    }

    public String getPartyAccount() {
        return PartyAccount != null ? PartyAccount : "";
    }

    public String getVoucherType() {
        return VoucherType != null ? VoucherType : "";
    }

    public String getBillNumber() {
        return BillNumber != null ? BillNumber : "";
    }

    @SerializedName("transactionId")
    @Expose
    public int TransactionId;

    @SerializedName("dateString")
    @Expose
    public String DateString;

    @SerializedName("tax")
    @Expose
    public float Tax;

    @SerializedName("discount")
    @Expose
    public float Discount;

    @SerializedName("ledgerName")
    @Expose
    public String LedgerName;

    @SerializedName("partyAccount")
    @Expose
    public String PartyAccount;

    @SerializedName("voucherType")
    @Expose
    public String VoucherType;

    @SerializedName("graceTotal")
    @Expose
    public float GraceTotal;

    @SerializedName("grandTotal")
    @Expose
    public float GrandTotal;

    @SerializedName("billNumber")
    @Expose
    public String BillNumber;

    @SerializedName("items")
    @Expose
    public List<PurchaseSummaryItemsModel> Items;

    public PurchaseSummaryModel() {
    }

    public PurchaseSummaryModel(Parcel parcel) {
        Discount = parcel.readFloat();
        Tax = parcel.readFloat();
        GraceTotal = parcel.readFloat();
        GrandTotal = parcel.readFloat();
        LedgerName = parcel.readString();
        Items =new ArrayList<PurchaseSummaryItemsModel>();
        parcel.readList(Items, PurchaseSummaryItemsModel.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(Discount);
        dest.writeFloat(Tax);
        dest.writeFloat(GraceTotal);
        dest.writeFloat(GrandTotal);
        dest.writeString(LedgerName);
        dest.writeList(Items);
    }

    public static final Parcelable.Creator<PurchaseSummaryModel> CREATOR = new Parcelable.Creator<PurchaseSummaryModel>() {
        public PurchaseSummaryModel createFromParcel(Parcel in) {
            return new PurchaseSummaryModel(in);
        }

        @Override
        public PurchaseSummaryModel[] newArray(int size) {
            return new PurchaseSummaryModel[size];
        }
    };
}
