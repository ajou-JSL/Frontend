package com.example.moum.data.entity;

public class PerformaceHallRent {
    private Integer id;
    private Integer hallId;
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

    public Integer getHallId() {
        return hallId;
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

    public void setHallId(Integer hallId) {
        this.hallId = hallId;
    }

    public void setStart(String start) {
        this.start = start;
    }

    @Override
    public String toString() {
        return "PracticeroomRent{" +
                "id=" + id +
                ", hallId=" + hallId +
                ", teamId=" + teamId +
                ", lifecycleId=" + lifecycleId +
                ", start='" + start + '\'' +
                ", finish='" + finish + '\'' +
                ", duration=" + duration +
                ", fee=" + fee +
                '}';
    }
}
