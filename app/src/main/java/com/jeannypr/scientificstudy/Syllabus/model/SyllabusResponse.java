package com.jeannypr.scientificstudy.Syllabus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

public class SyllabusResponse {
    public int getSyllabusId() {
        return SyllabusId == -1 ? 0 : SyllabusId;
    }

    public String getTitle() {
        return Title == null ? "" : Title;
    }

    public String getClassName() {
        return ClassName == null ? "" : ClassName;
    }

    public String getSubjectName() {
        return SubjectName == null ? "" : SubjectName;
    }

    public String getAttachment() {
        return Attachment == null ? "" : Attachment;
    }

    @SerializedName("id")
    @Expose
    private int SyllabusId;

    @SerializedName("title")
    @Expose
    private String Title;

    @SerializedName("className")
    @Expose
    private String ClassName;

    @SerializedName("subjectName")
    @Expose
    private String SubjectName;

    @SerializedName("attachment")
    @Expose
    private String Attachment;
}
