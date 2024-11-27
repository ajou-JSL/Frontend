package com.example.moum.data.entity;

public class Settlement {
    private Integer settlementId;
    private String settlementName;
    private Integer fee;
    private Integer moumId;

    public Integer getSettlementId() {
        return settlementId;
    }

    public Integer getFee() {
        return fee;
    }

    public Integer getMoumId() {
        return moumId;
    }

    public String getSettlementName() {
        return settlementName;
    }

    public void setSettlementId(Integer settlementId) {
        this.settlementId = settlementId;
    }

    public void setFee(Integer fee) {
        this.fee = fee;
    }

    public void setMoumId(Integer moumId) {
        this.moumId = moumId;
    }

    public void setSettlementName(String settlementName) {
        this.settlementName = settlementName;
    }

    @Override
    public String toString() {
        return "Settlement{" +
                "settlementId=" + settlementId +
                ", settlementName='" + settlementName + '\'' +
                ", fee=" + fee +
                ", moumId=" + moumId +
                '}';
    }
}
