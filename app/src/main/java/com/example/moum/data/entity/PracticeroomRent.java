package com.example.moum.data.entity;

public class PracticeroomRent {
    private Integer id;
    private Integer roomId;
    private Integer teamId;
    private Integer lifecycleId;
    private String start;
    private String finish;
    private Integer duration;
    private Integer fee;

    public Integer getId() {
        return id;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public Integer getDuration() {
        return duration;
    }

    public Integer getFee() {
        return fee;
    }

    public Integer getLifecycleId() {
        return lifecycleId;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public String getFinish() {
        return finish;
    }

    public String getStart() {
        return start;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public void setFee(Integer fee) {
        this.fee = fee;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public void setLifecycleId(Integer lifecycleId) {
        this.lifecycleId = lifecycleId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public void setStart(String start) {
        this.start = start;
    }

    @Override
    public String toString() {
        return "PracticeroomRent{" +
                "id=" + id +
                ", roomId=" + roomId +
                ", teamId=" + teamId +
                ", lifecycleId=" + lifecycleId +
                ", start='" + start + '\'' +
                ", finish='" + finish + '\'' +
                ", duration=" + duration +
                ", fee=" + fee +
                '}';
    }
}
