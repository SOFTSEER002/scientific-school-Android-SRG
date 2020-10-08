package com.jeannypr.scientificstudy.Database.table;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "notifications", indices = {@Index(value = "notification_id", unique = true)})
public class TransportNotificationModel {

    public TransportNotificationModel(String Title, String Body,
                                      String NotificationTime, String NotificationDate, String NotificationFor, int SenderUserId, String ReceiverIds, int NotificationId) {
        this.Title = Title;
        this.Body = Body;
        this.NotificationTime = NotificationTime;
        this.NotificationDate = NotificationDate;
        this.NotificationFor = NotificationFor;
        this.SenderUserId = SenderUserId;
        this.ReceiverIds = ReceiverIds;
        this.NotificationId = NotificationId;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getId() {
        return Id == -1 ? 0 : Id;
    }

    @PrimaryKey(autoGenerate = true)
    private int Id;

    @SerializedName("Title")
    @Expose
    @ColumnInfo(name = "title")
    private String Title;

    public String getTitle() {
        return Title == null ? "" : Title;
    }

    public String getBody() {
        return Body == null ? "" : Body;
    }

    public String getNotificationTime() {
        return NotificationTime == null ? "" : NotificationTime;
    }

    public String getNotificationDate() {
        return NotificationDate == null ? "" : NotificationDate;
    }

    public String getNotificationFor() {
        return NotificationFor == null ? "" : NotificationFor;
    }

    public int getSenderUserId() {
        return SenderUserId == -1 ? 0 : SenderUserId;
    }


    @SerializedName("Body")
    @Expose
    @ColumnInfo(name = "body")
    private String Body;

    public void setNotificationTime(String notificationTime) {
        NotificationTime = notificationTime;
    }

    public void setNotificationDate(String notificationDate) {
        NotificationDate = notificationDate;
    }

    @ColumnInfo(name = "notification_time")
    private String NotificationTime;

    @SerializedName("CreatedOn_str")
    @Expose
    @ColumnInfo(name = "notification_date")
    private String NotificationDate;

    @SerializedName("NType") //NotificationFor
    @Expose
    @ColumnInfo(name = "notification_for")
    private String NotificationFor;

    @SerializedName("SentBy") //CreatedBy
    @Expose
    @ColumnInfo(name = "sender_userid")
    private int SenderUserId;

    @SerializedName("ParentUserIds")
    @Expose
    @ColumnInfo(name = "receiver_user_ids")
    private String ReceiverIds;

    public int getNotificationId() {
        return NotificationId == -1 ? 0 : NotificationId;
    }

    @SerializedName("NotificationId")
    @Expose
    @ColumnInfo(name = "notification_id")
    private int NotificationId;

    public String getReceiverIds() {
        return ReceiverIds == null ? "" : ReceiverIds;
    }

    public void setReceiverIds(String receiverIds) {
        ReceiverIds = receiverIds;
    }
}
