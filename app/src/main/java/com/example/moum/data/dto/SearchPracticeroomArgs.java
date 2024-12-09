package com.example.moum.data.dto;

public class SearchPracticeroomArgs {
    private String sortBy;
    private String orderBy;
    private String name;
    private Double latitude;
    private Double longitude;
    private Integer minPrice;
    private Integer maxPrice;
    private Integer type;
    private Integer minCapacity;
    private Integer maxCapacity;
    private Integer minStand;
    private Integer maxStand;
    private Boolean hasPiano;
    private Boolean hasAmp;
    private Boolean hasSpeaker;
    private Boolean hasMic;
    private Boolean hasDrums;

    public SearchPracticeroomArgs() {
        sortBy = "distance";
        orderBy = "asc";
    }

    public void clear() {
        sortBy = "distance";
        orderBy = "asc";
        type = null;
        name = null;
        latitude = null;
        longitude = null;
        minPrice = null;
        maxPrice = null;
        minCapacity = null;
        maxCapacity = null;
        minStand = null;
        maxStand = null;
        hasPiano = null;
        hasAmp = null;
        hasSpeaker = null;
        hasMic = null;
        hasDrums = null;
    }

    public Integer getType() {
        return type;
    }

    public Boolean getHasDrums() {
        return hasDrums;
    }

    public Boolean getHasAmp() {
        return hasAmp;
    }

    public String getName() {
        return name;
    }

    public Boolean getHasPiano() {
        return hasPiano;
    }

    public Boolean getHasSpeaker() {
        return hasSpeaker;
    }

    public Boolean getHasMic() {
        return hasMic;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public Integer getMaxPrice() {
        return maxPrice;
    }

    public Integer getMaxStand() {
        return maxStand;
    }

    public Integer getMinCapacity() {
        return minCapacity;
    }

    public Integer getMinPrice() {
        return minPrice;
    }

    public Integer getMinStand() {
        return minStand;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setHasAmp(Boolean hasAmp) {
        this.hasAmp = hasAmp;
    }

    public void setHasSpeaker(Boolean hasSpeaker) {
        this.hasSpeaker = hasSpeaker;
    }

    public void setHasDrums(Boolean hasDrums) {
        this.hasDrums = hasDrums;
    }

    public void setHasPiano(Boolean hasPiano) {
        this.hasPiano = hasPiano;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHasMic(Boolean hasMic) {
        this.hasMic = hasMic;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public void setMaxPrice(Integer maxPrice) {
        this.maxPrice = maxPrice;
    }

    public void setMaxStand(Integer maxStand) {
        this.maxStand = maxStand;
    }

    public void setMinCapacity(Integer minCapacity) {
        this.minCapacity = minCapacity;
    }

    public void setMinPrice(Integer minPrice) {
        this.minPrice = minPrice;
    }

    public void setMinStand(Integer minStand) {
        this.minStand = minStand;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
