package com.example.moum.data.dto;

public class MoumProcessRequest {
    private Boolean recruitStatus;
    private Boolean chatroomStatus;
    private Boolean practiceroomStatus;
    private Boolean performLocationStatus;
    private Boolean promoteStatus;
    private Boolean paymentStatus;

    public MoumProcessRequest(Boolean recruitStatus, Boolean chatroomStatus, Boolean practiceroomStatus, Boolean performLocationStatus,
            Boolean promoteStatus, Boolean paymentStatus) {
        this.recruitStatus = recruitStatus;
        this.chatroomStatus = chatroomStatus;
        this.practiceroomStatus = practiceroomStatus;
        this.performLocationStatus = performLocationStatus;
        this.promoteStatus = promoteStatus;
        this.paymentStatus = paymentStatus;
    }
}
