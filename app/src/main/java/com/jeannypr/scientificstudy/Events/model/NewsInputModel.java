package com.jeannypr.scientificstudy.Events.model;

public class NewsInputModel {
    private int Id;
    private int SchoolId;
    private int AcademicYearId;
    private int CreatedBy;
    private int NewsType;
    private int audience;
    private Boolean isPublished;
    private String title;
    private String description;
    private String newsDate;
    private String attachmentName;

    public int getId() {
        return Id == -1 ? 0 : Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getSchoolId() {
        return SchoolId == -1 ? 0 : SchoolId;
    }

    public void setSchoolId(int schoolId) {
        SchoolId = schoolId;
    }

    public int getAcademicYearId() {
        return AcademicYearId == -1 ? 0 : AcademicYearId;
    }

    public void setAcademicYearId(int academicYearId) {
        AcademicYearId = academicYearId;
    }


    public int getCreatedBy() {
        return CreatedBy == -1 ? 0 : CreatedBy;
    }

    public void setCreatedBy(int createdBy) {
        CreatedBy = createdBy;
    }

    public int getNewsType() {
        return NewsType == -1 ? 0 : NewsType;
    }

    public void setNewsType(int newsType) {
        NewsType = newsType;
    }

    public int getAudience() {
        return audience == -1 ? 0 : audience;
    }

    public void setAudience(int audience) {
        this.audience = audience;
    }

    public Boolean isPublished() {
        return isPublished == null ? false : isPublished;
    }

    public void setPublished(Boolean published) {
        isPublished = published;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description == null ? "" : description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNewsDate() {
        return newsDate == null ? "" : newsDate;
    }

    public void setNewsDate(String newsDate) {
        this.newsDate = newsDate;
    }

    public String getAttachmentName() {
        return attachmentName == null ? "" : attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }
}
