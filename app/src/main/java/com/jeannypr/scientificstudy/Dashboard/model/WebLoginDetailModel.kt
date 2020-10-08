package com.jeannypr.scientificstudy.Dashboard.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.jeannypr.scientificstudy.Base.Model.Bean

class WebLoginDetailModel : Bean() {
    @SerializedName("userId")
    @Expose
    var userId: Int? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("url")
    @Expose
    var url: String? = null

    @SerializedName("userName")
    @Expose
    var userName: String? = null

    @SerializedName("password")
    @Expose
    var password: String? = null
}