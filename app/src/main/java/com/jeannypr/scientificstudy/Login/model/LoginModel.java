package com.jeannypr.scientificstudy.Login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.ChildModel;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Dashboard.model.FamilyMembersModel;

import java.util.List;

public class LoginModel {
    @SerializedName("user")
    @Expose
    public UserModel User;

    @SerializedName("appMessage")
    @Expose
    public String AppMessage;

    @SerializedName("student")
    @Expose
    public List<ChildModel> Student;

    @SerializedName("father")
    @Expose
    public FamilyMembersModel FatherOrHubby;

    @SerializedName("mother")
    @Expose
    public FamilyMembersModel MotherOrWife;
}
