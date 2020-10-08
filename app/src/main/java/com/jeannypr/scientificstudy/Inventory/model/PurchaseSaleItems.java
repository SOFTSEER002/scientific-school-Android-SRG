package com.jeannypr.scientificstudy.Inventory.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PurchaseSaleItems implements Parcelable {
    public String Description;
    public int ItemId;
    public int AdapterPosition;
    public String Title;
    public double Amount;
    public double Quantity;
    public double Rate;
    public String OpeningStock;

    protected PurchaseSaleItems(Parcel in) {
        ItemId = in.readInt();
        Title = in.readString();
        Description = in.readString();
        Amount = in.readFloat();
        AdapterPosition = in.readInt();
        Quantity = in.readFloat();
        Rate = in.readFloat();
        OpeningStock = in.readString();
    }

    public PurchaseSaleItems() {

    }

    public String getDescription() {
        return Description == null ? "" : Description;
    }

    public int getLedgerId() {
        return ItemId;
    }

    public String getLedgerName() {
        return Title;
    }

    public double getAmount() {
        return Amount == -1 ? 0 : Amount;
    }

    public double getQuantity() {
        return Quantity == -1 ? 0 : Quantity;
    }

    public double getRate() {
        return Rate == -1 ? 0 : Rate;
    }

    public String getCurrentBalance() {
        return CurrentBalance == null ? "" : CurrentBalance;
    }

    public String CurrentBalance;

    public static final Creator<PurchaseSaleItems> CREATOR = new Creator<PurchaseSaleItems>() {
        @Override
        public PurchaseSaleItems createFromParcel(Parcel in) {
            return new PurchaseSaleItems(in);
        }

        @Override
        public PurchaseSaleItems[] newArray(int size) {
            return new PurchaseSaleItems[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ItemId);
        dest.writeString(Title);
        dest.writeString(Description);
        dest.writeDouble(Amount);
        dest.writeInt(AdapterPosition);
        dest.writeDouble(Quantity);
        dest.writeDouble(Rate);
        dest.writeString(OpeningStock);
    }
}
