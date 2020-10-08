package com.jeannypr.scientificstudy.Base.Model

import androidx.databinding.ObservableField
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserModel {
    fun getIsLoading(): Boolean? {
        return isLoading.get() as Boolean?
    }

    fun setIsLoading(isLoading: Boolean?) {
        this.isLoading.set(isLoading)
    }

    private val isLoading = ObservableField(false)

    @SerializedName("userId")
    @Expose
    var UserId = 0

    @SerializedName("schoolId")
    @Expose
    var SchoolId = 0

    @SerializedName("roleId")
    @Expose
    var RoleId: Int? = null

    @SerializedName("academicYearId")
    @Expose
    var AcademicyearId = 0

    @SerializedName("academicYearName")
    @Expose
    var AcademicYearName: String? = null

    @SerializedName("roleTitle")
    @Expose
    var RoleTitle = ""

    @SerializedName("firstName")
    @Expose
    var FirstName: String? = null

    @SerializedName("lastName")
    @Expose
    var LastName: String? = null

    @SerializedName("email")
    @Expose
    var Email: String? = null

    @SerializedName("mobile")
    @Expose
    var Mobile: String? = null

    @SerializedName("isClassTeacher")
    @Expose
    var IsClassTeacher = false

    @SerializedName("classId")
    @Expose
    var ClassId: Int? = null

    @SerializedName("className")
    @Expose
    var ClassName: String? = null

    @SerializedName("shiftId")
    @Expose
    var ShiftId = 0

    @SerializedName("shiftName")
    @Expose
    var ShiftNamel: String? = null

    @SerializedName("familyName")
    @Expose
    var FamilyName: String? = null
        get() = if (field == null) "" else field

    @SerializedName("userImagePath")
    @Expose
    var UserImagePath: String? = null
        get() = if (field == null) "" else field

    var UserName = ""

    @SerializedName("parentHelpdeskUrl")
    @Expose
    var ParentHelpdeskUrl: String? = null

}