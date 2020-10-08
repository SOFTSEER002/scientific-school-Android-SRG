package com.jeannypr.scientificstudy.Login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

/**
 * Created by kannuk on
 **/
public class ForgetPasswordBean extends Bean {
    @SerializedName("data")
    @Expose
    public ForgetPasswordModel data;
}
