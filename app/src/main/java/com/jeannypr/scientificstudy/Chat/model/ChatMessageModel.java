package com.jeannypr.scientificstudy.Chat.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class ChatMessageModel implements Comparable<ChatMessageModel> {

    public int Id;
    public int ChatRoomId;
    public String Message;

    public String GroupOrReceiverName;
    public int GroupOrReceiverId;
    public boolean IsGroup;
    public boolean IsClass;
    public boolean IsAllTeacherGroup;

    @SerializedName("SentOnMilli")
    public long SentOn;
    public int SentBy;
    public String UserName;
    public boolean IsRemoved;
    public boolean IsSent;
    public boolean IsRead;
    public int position;

    @Override
    public int compareTo(@NonNull ChatMessageModel m) {
        if (SentOn == m.SentOn) {
            return 0;
        } else if (SentOn > m.SentOn) {
            return 1;
        } else {
            return -1;
        }
    }
}


