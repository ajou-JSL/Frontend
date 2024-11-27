package com.example.moum.data.entity;

public class MoumPracticeroom {
    Integer id;
    Integer moumId;
    Integer roomId;
    String roomName;

    public Integer getMoumId() {
        return moumId;
    }

    public Integer getId() {
        return id;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setMoumId(Integer moumId) {
        this.moumId = moumId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
