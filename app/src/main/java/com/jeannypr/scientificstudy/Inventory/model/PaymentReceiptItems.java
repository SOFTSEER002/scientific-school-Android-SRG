package com.jeannypr.scientificstudy.Inventory.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PaymentReceiptItems implements Parcelable {
    public String getDescription() {
        return Description == null ? "" : Description;
    }

    public int getLedgerId() {
        return LedgerId;
    }

    public String getLedgerName() {
        return LedgerName;
    }

    public double getAmount() {
        return Amount == -1 ? 0 : Amount;
    }

    public String Description;
    public int LedgerId;
    public int AdapterPosition;
    public String LedgerName;
    public double Amount;

    public int getRowIndex() {
        return RowIndex == -1 ? 0 : RowIndex;
    }

    public int RowIndex;

    public String getCurrentBalance() {
        return CurrentBalance == null ? "" : CurrentBalance;
    }

    public String CurrentBalance;

    protected PaymentReceiptItems(Parcel in) {
        Description = in.readString();
        LedgerId = in.readInt();
        LedgerName = in.readString();
        Amount = in.readDouble();
        RowIndex = in.readInt();
        AdapterPosition = in.readInt();
    }

    public static final Creator<PaymentReceiptItems> CREATOR = new Creator<PaymentReceiptItems>() {
        @Override
        public PaymentReceiptItems createFromParcel(Parcel in) {
            return new PaymentReceiptItems(in);
        }

        @Override
        public PaymentReceiptItems[] newArray(int size) {
            return new PaymentReceiptItems[size];
        }
    };

    public PaymentReceiptItems() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Description);
        dest.writeInt(LedgerId);
        dest.writeString(LedgerName);
        dest.writeDouble(Amount);
        dest.writeInt(RowIndex);
        dest.writeInt(AdapterPosition);
    }
}
