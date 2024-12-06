package com.example.moum.data.dto;

import java.util.ArrayList;

public class ChatroomInviteRequest {
    private Integer type;
    private ArrayList<Integer> members;

    public ChatroomInviteRequest(Integer type, ArrayList<Integer> members) {
        this.type = type;
        this.members = members;
    }
}
