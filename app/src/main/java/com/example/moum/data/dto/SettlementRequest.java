package com.example.moum.data.dto;

public class SettlementRequest {
    private String settlementName;
    private Integer fee;
    private Integer moumId;

    public SettlementRequest(String settlementName, Integer fee, Integer moumId){
        this.settlementName = settlementName;
        this.fee = fee;
        this.moumId = moumId;
    }
}
