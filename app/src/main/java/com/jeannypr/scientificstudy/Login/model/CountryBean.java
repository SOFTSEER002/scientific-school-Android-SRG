package com.jeannypr.scientificstudy.Login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

import java.util.List;

public class CountryBean extends Bean {

    @SerializedName("data")
    @Expose
    public List<CountryModel> data;

//    class CountryModel{
//        @SerializedName("id")
//        @Expose
//        public Integer Id;
//        @SerializedName("countryName")
//        @Expose
//        public String CountryName;
//    }
}
