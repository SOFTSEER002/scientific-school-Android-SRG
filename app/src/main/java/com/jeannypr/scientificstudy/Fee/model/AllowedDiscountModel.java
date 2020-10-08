package com.jeannypr.scientificstudy.Fee.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllowedDiscountModel {
    @SerializedName("canGiveDiscount")
    @Expose
    private boolean CanGiveDiscount;

    public boolean isCanGiveDiscount() {
        return CanGiveDiscount;
    }
}
