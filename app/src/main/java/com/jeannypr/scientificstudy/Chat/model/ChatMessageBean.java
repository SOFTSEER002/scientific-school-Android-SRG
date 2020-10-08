package com.jeannypr.scientificstudy.Chat.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ChatMessageBean {

    public List<ChatMessageModel> getChatMessages() {
        return ChatMessages == null ? new ArrayList<ChatMessageModel>() : ChatMessages;
    }

    public int getTotalMessages() {
        return TotalMessages;
    }

    @SerializedName("messages")
    private List<ChatMessageModel> ChatMessages;

    @SerializedName("total")
    private int TotalMessages;
}
