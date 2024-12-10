package com.example.moum.data.entity;

import java.util.ArrayList;

public class Moum {
    private Integer moumId;
    private String moumName;
    private String moumDescription;
    private String performLocation;
    private String startDate;
    private String endDate;
    private Integer price;
    private ArrayList<String> imageUrls;
    private Integer leaderId;
    private String leaderName;
    private Integer teamId;
    private Process process;
    private ArrayList<Member> members;
    private ArrayList<Record> records;
    private ArrayList<Music> music;
    private Genre genre;

    public static class Process {
        private Boolean recruitStatus;
        private Boolean chatroomStatus;
        private Boolean practiceroomStatus;
        private Boolean performLocationStatus;
        private Boolean promoteStatus;
        private Boolean paymentStatus;
        private Boolean finishStatus;
        private Integer processPercentage;

        public Boolean getChatroomStatus() {
            return chatroomStatus;
        }

        public Boolean getFinishStatus() {
            return finishStatus;
        }

        public Boolean getPaymentStatus() {
            return paymentStatus;
        }

        public Boolean getPerformLocationStatus() {
            return performLocationStatus;
        }

        public Boolean getPracticeroomStatus() {
            return practiceroomStatus;
        }

        public Boolean getPromoteStatus() {
            return promoteStatus;
        }

        public Boolean getRecruitStatus() {
            return recruitStatus;
        }

        public Integer getProcessPercentage() {
            return processPercentage;
        }

        public void setChatroomStatus(Boolean chatroomStatus) {
            this.chatroomStatus = chatroomStatus;
        }

        public void setFinishStatus(Boolean finishStatus) {
            this.finishStatus = finishStatus;
        }

        public void setPerformLocationStatus(Boolean performLocationStatus) {
            this.performLocationStatus = performLocationStatus;
        }

        public void setPaymentStatus(Boolean paymentStatus) {
            this.paymentStatus = paymentStatus;
        }

        public void setPracticeroomStatus(Boolean practiceroomStatus) {
            this.practiceroomStatus = practiceroomStatus;
        }

        public void setPromoteStatus(Boolean promoteStatus) {
            this.promoteStatus = promoteStatus;
        }

        public void setProcessPercentage(Integer processPercentage) {
            this.processPercentage = processPercentage;
        }

        public void setRecruitStatus(Boolean recruitStatus) {
            this.recruitStatus = recruitStatus;
        }

        @Override
        public String toString() {
            return "Process{" +
                    "recruitStatus=" + recruitStatus +
                    ", chatroomStatus=" + chatroomStatus +
                    ", practiceroomStatus=" + practiceroomStatus +
                    ", performLocationStatus=" + performLocationStatus +
                    ", promoteStatus=" + promoteStatus +
                    ", paymentStatus=" + paymentStatus +
                    ", finishStatus=" + finishStatus +
                    ", processPercentage=" + processPercentage +
                    '}';
        }
    }

    public void setMembers(ArrayList<Member> members) {
        this.members = members;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public void setLeaderId(Integer leaderId) {
        this.leaderId = leaderId;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public void setMoumDescription(String moumDescription) {
        this.moumDescription = moumDescription;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public void setMoumId(Integer moumId) {
        this.moumId = moumId;
    }

    public void setMoumName(String moumName) {
        this.moumName = moumName;
    }

    public void setPerformLocation(String performLocation) {
        this.performLocation = performLocation;
    }

    public void setRecords(ArrayList<Record> records) {
        this.records = records;
    }

    public void setImageUrls(ArrayList<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setMusic(ArrayList<Music> music) {
        this.music = music;
    }

    public Integer getLeaderId() {
        return leaderId;
    }

    public ArrayList<Member> getMembers() {
        return members;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public Integer getMoumId() {
        return moumId;
    }

    public Integer getPrice() {
        return price;
    }

    public Genre getGenre() {
        return genre;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public String getMoumDescription() {
        return moumDescription;
    }

    public String getMoumName() {
        return moumName;
    }

    public String getPerformLocation() {
        return performLocation;
    }

    public ArrayList<String> getImageUrls() {
        return imageUrls;
    }

    public Process getProcess() {
        return process;
    }

    public ArrayList<Record> getRecords() {
        return records;
    }

    public ArrayList<Music> getMusic() {
        return music;
    }

    @Override
    public String toString() {
        return "Moum{" +
                "moumId=" + moumId +
                ", moumName='" + moumName + '\'' +
                ", moumDescription='" + moumDescription + '\'' +
                ", performLocation='" + performLocation + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", price=" + price +
                ", imageUrls=" + imageUrls +
                ", leaderId=" + leaderId +
                ", leaderName='" + leaderName + '\'' +
                ", teamId=" + teamId +
                ", process=" + process +
                ", members=" + members +
                ", records=" + records +
                ", music=" + music +
                ", genre=" + genre +
                '}';
    }
}
