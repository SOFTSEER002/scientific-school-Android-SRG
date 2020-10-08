package com.jeannypr.scientificstudy.Inventory.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Timetable.model.DayWisePeriodsModel;

import java.util.List;

public class PurchaseSummaryItemsModel implements Parcelable {

    @SerializedName("itemId")
    @Expose
    public int ItemId;

    @SerializedName("title")
    @Expose
    public String Title;

    @SerializedName("description")
    @Expose
    public String Description;

    @SerializedName("openingStock")
    @Expose
    public int OpeningStock;

    @SerializedName("quantity")
    @Expose
    public int Quantity;

    @SerializedName("rate")
    @Expose
    public float Rate;

    public int getQuantity() {
        return Quantity == -1 ? 0 : Quantity;
    }

    public float getRate() {
        return Rate == -1 ? 0 : Rate;
    }

    public float getAmount() {
        return Amount == -1 ? 0 : Amount;
    }

    @SerializedName("amount")
    @Expose
    public float Amount;

    @SerializedName("voucharType")
    @Expose
    public String VoucharType;

    @SerializedName("returnQnty")
    @Expose
    public int ReturnQnty;

    public PurchaseSummaryItemsModel() {
    }

    public PurchaseSummaryItemsModel(Parcel parcel) {
        ItemId = parcel.readInt();
        Title = parcel.readString();
        Description = parcel.readString();
        OpeningStock = parcel.readInt();
        Rate = parcel.readFloat();
        Quantity = parcel.readInt();
        Amount = parcel.readFloat();
        VoucharType = parcel.readString();
        ReturnQnty = parcel.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ItemId);
        dest.writeString(Title);
        dest.writeString(Description);
        dest.writeInt(OpeningStock);
        dest.writeFloat(Rate);
        dest.writeInt(Quantity);
        dest.writeFloat(Amount);
        dest.writeString(VoucharType);
        dest.writeInt(ReturnQnty);
    }

    public static final Parcelable.Creator<PurchaseSummaryItemsModel> CREATOR = new Parcelable.Creator<PurchaseSummaryItemsModel>() {
        public PurchaseSummaryItemsModel createFromParcel(Parcel in) {
            return new PurchaseSummaryItemsModel(in);
        }

        @Override
        public PurchaseSummaryItemsModel[] newArray(int size) {
            return new PurchaseSummaryItemsModel[size];
        }
    };
}
