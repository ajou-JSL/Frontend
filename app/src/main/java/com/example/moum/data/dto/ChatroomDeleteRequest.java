package com.example.moum.data.dto;

import java.util.ArrayList;

public class ChatroomDeleteRequest {
    private Integer type;
    private ArrayList<Integer> members;

    public ChatroomDeleteRequest(Integer type, ArrayList<Integer> members) {
        this.type = type;
        this.members = members;
    }
}
