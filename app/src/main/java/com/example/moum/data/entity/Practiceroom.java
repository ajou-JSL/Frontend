package com.example.moum.data.entity;

import java.util.ArrayList;

public class Practiceroom {
    private Integer id;
    private String name;
    private Integer price;
    private Integer capacity;
    private String address;
    private String owner;
    private String phone;
    private String email;
    private String mapUrl;
    private ArrayList<String> imageUrls;
    private Integer type;
    private Integer stand;
    private Boolean hasPiano;
    private Boolean hasAmp;
    private Boolean hasMic;
    private Boolean hasSpeaker;
    private Boolean hasDrums;
    private String details;
    private Float latitude;
    private Float longitude;

    public Boolean getHasMic() {
        return hasMic;
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public Boolean getHasSpeaker() {
        return hasSpeaker;
    }

    public Boolean getHasPiano() {
        return hasPiano;
    }

    public Boolean getHasAmp() {
        return hasAmp;
    }

    public Boolean getHasDrums() {
        return hasDrums;
    }

    public ArrayList<String> getImageUrls() {
        return imageUrls;
    }

    public Integer getPrice() {
        return price;
    }

    public String getEmail() {
        return email;
    }

    public Float getLatitude() {
        return latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public Integer getStand() {
        return stand;
    }

    public Integer getType() {
        return type;
    }

    public String getDetails() {
        return details;
    }

    public String getMapUrl() {
        return mapUrl;
    }

    public String getOwner() {
        return owner;
    }

    public String getPhone() {
        return phone;
    }

    public void setHasMic(Boolean hasMic) {
        this.hasMic = hasMic;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public void setMapUrl(String mapUrl) {
        this.mapUrl = mapUrl;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setHasPiano(Boolean hasPiano) {
        this.hasPiano = hasPiano;
    }

    public void setHasDrums(Boolean hasDrums) {
        this.hasDrums = hasDrums;
    }

    public void setHasSpeaker(Boolean hasSpeaker) {
        this.hasSpeaker = hasSpeaker;
    }

    public void setHasAmp(Boolean hasAmp) {
        this.hasAmp = hasAmp;
    }

    public void setImageUrls(ArrayList<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public void setStand(Integer stand) {
        this.stand = stand;
    }

    @Override
    public String toString() {
        return "Practiceroom{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", capacity=" + capacity +
                ", address='" + address + '\'' +
                ", owner='" + owner + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", mapUrl='" + mapUrl + '\'' +
                ", imageUrls=" + imageUrls +
                ", type=" + type +
                ", stand=" + stand +
                ", hasPiano=" + hasPiano +
                ", hasAmp=" + hasAmp +
                ", hasSpeaker=" + hasSpeaker +
                ", hasDrums=" + hasDrums +
                ", details='" + details + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}

