package com.jeannypr.scientificstudy.Classwork.model;

import android.net.Uri;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HWCommentModel {
    public HWCommentModel(int homeworkId, int studentId, int teacherId, String comment, String filePath, String fileName, int fileType,
                          String fileSize, String sentOn, String currentStatus, String mode, String fileExtension, String thumbnailPath, Boolean hasThumbnail) {
        HomeworkId = homeworkId;
        StudentId = studentId;
        TeacherUserId = teacherId;
        Comment = comment;
        FilePath = filePath;
        FileName = fileName;
        FileType = fileType;
        FileSize = fileSize;
        SentOn = sentOn;
        CurrentStatus = currentStatus;
//        Mode = mode;
        FileExtension = fileExtension;
        ThumbnailPath = thumbnailPath;
        HasThumbnail = hasThumbnail;
//        this.uri = uri;
//        CommentedBy = commentedBy;
    }

    public int getHomeworkId() {
        return HomeworkId == -1 ? 0 : HomeworkId;
    }

    public int getStudentId() {
        return StudentId == -1 ? 0 : StudentId;
    }

    public String getComment() {
        return Comment == null ? "" : Comment;
    }

    public String getFilePath() {
        return FilePath == null ? "" : FilePath;
    }

    public String getFileName() {
        return FileName == null ? "" : FileName;
    }

   /* public String getFileType() {
        return FileType == null ? "" : FileType;
    }*/

    public String getFileSize() {
        return FileSize == null ? "" : FileSize;
    }

    public String getSentOn() {
        return SentOn == null ? "" : SentOn;
    }

    public String getCurrentStatus() {
        return CurrentStatus == null ? "" : CurrentStatus;
    }

//    public String getMode() {
//        return Mode == null ? "" : Mode;
//    }

    @SerializedName("assignmentId")
    @Expose
    private int HomeworkId;

    @SerializedName("studentId")
    @Expose
    private int StudentId;

    public int getTeacherUserId() {
        return TeacherUserId == -1 ? 0 : TeacherUserId;
    }

    public void setTeacherUserId(int teacherUserId) {
        TeacherUserId = teacherUserId;
    }

    @SerializedName("teacherUserId")
    @Expose
    private int TeacherUserId;

    public void setHomeworkId(int homeworkId) {
        HomeworkId = homeworkId;
    }

    public void setStudentId(int studentId) {
        StudentId = studentId;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public void setFilePath(String filePath) {
        FilePath = filePath;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public void setFileType(int fileType) {
        FileType = fileType;
    }

    public void setFileSize(String fileSize) {
        FileSize = fileSize;
    }

    public void setSentOn(String sentOn) {
        SentOn = sentOn;
    }

    public void setCurrentStatus(String currentStatus) {
        CurrentStatus = currentStatus;
    }

//    public void setMode(String mode) {
//        Mode = mode;
//    }

    public void setFileExtension(String fileExtension) {
        FileExtension = fileExtension;
    }

    public void setThumbnailPath(String thumbnailPath) {
        ThumbnailPath = thumbnailPath;
    }

    public void setHasThumbnail(Boolean hasThumbnail) {
        HasThumbnail = hasThumbnail;
    }

    @SerializedName("comment")
    @Expose
    private String Comment; //Title

    @SerializedName("filePath")
    @Expose
    private String FilePath;

    @SerializedName("fileName")
    @Expose
    private String FileName;

    public int getFileType() {
        return FileType == -1 ? 0 : FileType;
    }

    @SerializedName("fileType")
    @Expose
    private int FileType;

    @SerializedName("fileSize")
    @Expose
    private String FileSize;

    @SerializedName("sentOn")
    @Expose
    private String SentOn;

    @SerializedName("currentStatus")
    @Expose
    private String CurrentStatus;

    /* @SerializedName("mode")
     @Expose
     private String Mode;*/
    public int AdapterPosition;

    public String getFileExtension() {
        return FileExtension == null ? "" : FileExtension;
    }

    @SerializedName("fileExtension")
    @Expose
    private String FileExtension; //not available

    public String getThumbnailPath() {
        return ThumbnailPath == null ? "" : ThumbnailPath;
    }

    public Boolean getHasThumbnail() {
        return HasThumbnail == null ? false : HasThumbnail;
    }

    @SerializedName("thumbnailPath")
    @Expose
    private String ThumbnailPath; //not available

    @SerializedName("hasThumbnail")
    @Expose
    private Boolean HasThumbnail; //not available
    public Uri uri;
    public Boolean isMsgSent = false;
    public int adapterPosition;

    public int getCommentedBy() {
        return CommentedBy == -1 ? 0 : CommentedBy;
    }

    public void setCommentedBy(int commentedBy) {
        CommentedBy = commentedBy;
    }

    @SerializedName("commentedBy")
    @Expose
    private int CommentedBy;

   /* {
        "assignmentId": 43,
            "studentId": 5,
            "teacherId": 1,
            "comment": "Test message as a comment",
            "fileName": "Fee Reciept.pdf",
            "filePath": "SCHOOLDATA/dev/AssignmentComment//1/357bbfc9-ac94-4f76-9112-52e97a305425_Fee Reciept.pdf",
            "fileType": 3,
            "fileExtension": ".pdf",
            "fileSize": "104 KB",
            "thumbnailPath": "",
            "hasThumbnail": false,
            "sentOn": "Sep 02, 11:06 AM",
            "currentStatus": "Completed"
    }*/
}
