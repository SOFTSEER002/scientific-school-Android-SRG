package com.jeannypr.scientificstudy.Chat.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatRoomBean {

    @SerializedName("chatRooms")
    public List<ChatRoomModel> ChatRooms;
}
