package com.example.moum.data.entity;

public class PerformaceHall {
    private Integer id;
    private String name;
    private Integer price;
    private Integer size;
    private Integer capacity;
    private String address;
    private String owner;
    private String phone;
    private String email;
    private String mapUrl;
    private String imageUrl;
    private Integer type;
    private Integer stand;
    private Boolean piano;
    private Boolean amp;
    private Boolean speaker;
    private Boolean drums;
    private String details;
    private Float latitude;
    private Float longitude;


    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getPrice() {
        return price;
    }

    public String getEmail() {
        return email;
    }

    public Boolean getAmp() {
        return amp;
    }

    public Boolean getDrums() {
        return drums;
    }

    public Boolean getPiano() {
        return piano;
    }

    public Boolean getSpeaker() {
        return speaker;
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

    public Integer getSize() {
        return size;
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

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setAmp(Boolean amp) {
        this.amp = amp;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setDrums(Boolean drums) {
        this.drums = drums;
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

    public void setPiano(Boolean piano) {
        this.piano = piano;
    }

    public void setSpeaker(Boolean speaker) {
        this.speaker = speaker;
    }

    public void setStand(Integer stand) {
        this.stand = stand;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "PerformaceHall{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", size=" + size +
                ", capacity=" + capacity +
                ", address='" + address + '\'' +
                ", owner='" + owner + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", mapUrl='" + mapUrl + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", type=" + type +
                ", stand=" + stand +
                ", piano=" + piano +
                ", amp=" + amp +
                ", speaker=" + speaker +
                ", drums=" + drums +
                ", details='" + details + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
