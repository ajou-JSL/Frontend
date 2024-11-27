package com.example.moum.data.dto;

public class PerformMoumRequest {
    private Integer moumId;
    private Integer hallId;
    private String performanceHall;

    public PerformMoumRequest(Integer moumId, Integer hallId, String performanceHall){
        this.moumId = moumId;
        this.hallId = hallId;
        this.performanceHall = performanceHall;
    }
}
