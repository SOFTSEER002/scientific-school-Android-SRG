package com.jeannypr.scientificstudy.Login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserModel {

    @SerializedName("userId")
    @Expose
    public Integer UserId;

    @SerializedName("schoolId")
    @Expose
    public Integer SchoolId;
    @SerializedName("roleId")
    @Expose
    public Integer RoleId;
    @SerializedName("academicyearId")
    @Expose
    public Integer AcademicyearId;

   // public String schoolName;
   // public String schoolLogo;
   // public String schoolcode;
   // public String subdomain;
   @SerializedName("academicYearName")
   @Expose
    public String AcademicYearName;
    @SerializedName("roleTitle")
    @Expose
    public String RoleTitle;
    @SerializedName("firstName")
    @Expose
    public String FirstName;
    @SerializedName("email")
    @Expose
    public String Email;
    @SerializedName("mobile")
    @Expose
    public String Mobile;
    @SerializedName("isClassTeacher")
    @Expose
    public Boolean IsClassTeacher;
    @SerializedName("classId")
    @Expose
    public Integer ClassId;
    @SerializedName("className")
    @Expose
    public String ClassName;
}
