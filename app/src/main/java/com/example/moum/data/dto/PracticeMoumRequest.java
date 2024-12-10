package com.example.moum.data.dto;

public class PracticeMoumRequest {
    private Integer moumId;
    private Integer roomId;
    private String practiceRoom;

    public PracticeMoumRequest(Integer moumId, Integer roomId, String practiceRoom) {
        this.moumId = moumId;
        this.roomId = roomId;
        this.practiceRoom = practiceRoom;
    }
}
