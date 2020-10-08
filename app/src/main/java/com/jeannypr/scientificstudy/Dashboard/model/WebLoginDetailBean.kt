package com.jeannypr.scientificstudy.Dashboard.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.jeannypr.scientificstudy.Base.Model.Bean

class WebLoginDetailBean : Bean() {
    @SerializedName("data")
    @Expose
    var data: WebLoginDetailModel? = null
}