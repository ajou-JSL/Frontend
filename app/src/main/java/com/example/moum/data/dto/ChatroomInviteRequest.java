package com.example.moum.data.dto;

import java.util.ArrayList;

public class ChatroomInviteRequest {
    private Integer type;
    private ArrayList<Integer> memberIds;

    public ChatroomInviteRequest(Integer type, ArrayList<Integer> memberIds) {
        this.type = type;
        this.memberIds = memberIds;
    }
}
