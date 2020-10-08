package com.jeannypr.scientificstudy.Classwork.model;

public class CommentInputModel {
    private int assignmentId;
    private int studentId;
    private int teacherId;
    private String Comment;
    private String FileName;
    private String CurrentStatus;

    public CommentInputModel(int assignmentId, int studentId, int teacherId, String comment, String fileName, String currentStatus) {
        this.assignmentId = assignmentId;
        this.studentId = studentId;
        this.teacherId = teacherId;
        Comment = comment;
        FileName = fileName;
        CurrentStatus = currentStatus;
    }
}
