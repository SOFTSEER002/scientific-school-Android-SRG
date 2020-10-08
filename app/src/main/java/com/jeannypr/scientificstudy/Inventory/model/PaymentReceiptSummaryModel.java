package com.jeannypr.scientificstudy.Inventory.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PaymentReceiptSummaryModel implements Parcelable {

    @SerializedName("id")
    @Expose
    public int TransactionId;

    public String getPaymentDate() {
        return PaymentDate != null ? PaymentDate : "";
    }

    public String getLedgerName() {
        return LedgerName != null ? LedgerName : "";
    }

    public void setPaymentDate(String paymentDate) {
        PaymentDate = paymentDate;
    }

    public void setLedgerName(String ledgerName) {
        LedgerName = ledgerName;
    }

    public String getName() {
        return Name == null ? "" : Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @SerializedName("paymentDate")
    @Expose
    public String PaymentDate;

    @SerializedName("amount")
    @Expose
    public float Amount;

    @SerializedName("ledgerId")
    @Expose
    public int LedgerId;

    @SerializedName("ledgerName")
    @Expose
    public String LedgerName;

    @SerializedName("name")
    @Expose
    public String Name;

    @SerializedName("ledgers")
    @Expose
    public List<PurchaseSummaryItemsModel> Items;

    public PaymentReceiptSummaryModel() {
    }

    public PaymentReceiptSummaryModel(Parcel parcel) {

        Amount = parcel.readFloat();
        LedgerName = parcel.readString();
        Items = new ArrayList<PurchaseSummaryItemsModel>();
        parcel.readList(Items, PurchaseSummaryItemsModel.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(Amount);
        dest.writeString(LedgerName);
        dest.writeList(Items);
    }

    public static final Creator<PaymentReceiptSummaryModel> CREATOR = new Creator<PaymentReceiptSummaryModel>() {
        public PaymentReceiptSummaryModel createFromParcel(Parcel in) {
            return new PaymentReceiptSummaryModel(in);
        }

        @Override
        public PaymentReceiptSummaryModel[] newArray(int size) {
            return new PaymentReceiptSummaryModel[size];
        }
    };
}
