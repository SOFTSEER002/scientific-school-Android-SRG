package com.jeannypr.scientificstudy.Base.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Bean {
    @SerializedName("rcode")
    @Expose
    public Integer rcode;

    @SerializedName("msg")
    @Expose
    public String msg;

    public Integer getRcode() {
        return rcode;
    }

    public void setRcode(Integer rcode) {
        this.rcode = rcode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
