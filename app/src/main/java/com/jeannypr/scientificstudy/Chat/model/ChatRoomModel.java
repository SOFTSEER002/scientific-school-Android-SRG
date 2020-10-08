package com.jeannypr.scientificstudy.Chat.model;

import com.google.gson.annotations.SerializedName;

public class ChatRoomModel {

    public int Id;
    public Integer ClassId;
    public int CreatedBy;
    public String CreatedOn;
    public boolean IsClass;

    public String getLastMessage() {
        return LastMessage == null ? "" : LastMessage;
    }

    public void setLastMessage(String lastMessage) {
        LastMessage = lastMessage;
    }

    private String LastMessage;

    @SerializedName("LastMessageTimeStampMilli")
    public long LastMessageTimeStamp;

    public String getLastUserName() {
        return LastUserName == null ? "" : LastUserName;
    }

    public void setLastMessageTimeStamp(long lastMessageTimeStamp) {
        LastMessageTimeStamp = lastMessageTimeStamp;
    }

    public void setLastUserName(String lastUserName) {
        LastUserName = lastUserName;
    }

    private String LastUserName;
    public String RoomName;
    public String OwnerName;
    public String OtherUserName;
    public String Type;
    public int UserId;
    public boolean IsAllTeacherGroup;

}
