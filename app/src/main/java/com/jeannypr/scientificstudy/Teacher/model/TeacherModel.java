package com.jeannypr.scientificstudy.Teacher.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TeacherModel extends BaseObservable {
    @SerializedName("teacherId")
    @Expose
    public int Id;

    @Bindable
    @SerializedName("teacherName")
    @Expose
    public String Name;

    @SerializedName("isTeaching")
    @Expose
    public Boolean IsTeaching;

    @Bindable
    @SerializedName("email")
    @Expose
    public String Email;

    @Bindable
    @SerializedName("mobile")
    @Expose
    public String Mobile;

    @Bindable
    @SerializedName("imagePath")
    @Expose
    public String ImagePath;

    @SerializedName("userId")
    @Expose
    public int UserId;

    @SerializedName("className")
    @Expose
    public String ClassName;

    /*@BindingAdapter({"bind:ImagePath"})
    public static void loadImage(CircleImageView view, String ImagePath) {

        if(ImagePath != null && !ImagePath.equals("") ){
            String imgUrl = Constants.IMAGE_BASE_URL + ImagePath;
            Glide.with(view.getContext()).load(imgUrl).into(view);
        }
    }*/
}
