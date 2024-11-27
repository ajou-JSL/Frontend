package com.example.moum.data.entity;

public class MoumPerformHall {
    private Integer id;
    private Integer moumId;
    private Integer hallId;
    private String hallName;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setMoumId(Integer moumId) {
        this.moumId = moumId;
    }

    public void setHallId(Integer hallId) {
        this.hallId = hallId;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    public Integer getHallId() {
        return hallId;
    }

    public Integer getMoumId() {
        return moumId;
    }

    public Integer getId() {
        return id;
    }

    public String getHallName() {
        return hallName;
    }
}
