package com.example.moum.data.dto;

public class SearchPerformHallArgs {
    private String sortBy;
    private String orderBy;
    private String name;
    private Double latitude;
    private Double longitude;
    private Integer minPrice;
    private Integer maxPrice;
    private Integer maxHallSize;
    private Integer minHallSize;
    private Integer minCapacity;
    private Integer maxCapacity;
    private Integer minStand;
    private Integer maxStand;
    private Boolean hasPiano;
    private Boolean hasAmp;
    private Boolean hasSpeaker;
    private Boolean hasMic;
    private Boolean hasDrums;

    public SearchPerformHallArgs() {
        sortBy = "distance";
        orderBy = "asc";
    }

    public void clear() {
        sortBy = "distance";
        orderBy = "asc";
        name = null;
        latitude = null;
        longitude = null;
        minPrice = null;
        maxPrice = null;
        maxHallSize = null;
        minHallSize = null;
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

    public String getOrderBy() {
        return orderBy;
    }

    public Integer getMinStand() {
        return minStand;
    }

    public String getSortBy() {
        return sortBy;
    }

    public Integer getMinCapacity() {
        return minCapacity;
    }

    public Integer getMaxStand() {
        return maxStand;
    }

    public Integer getMaxPrice() {
        return maxPrice;
    }

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Integer getMinPrice() {
        return minPrice;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Boolean getHasMic() {
        return hasMic;
    }

    public Boolean getHasSpeaker() {
        return hasSpeaker;
    }

    public Boolean getHasPiano() {
        return hasPiano;
    }

    public String getName() {
        return name;
    }

    public Boolean getHasAmp() {
        return hasAmp;
    }

    public Boolean getHasDrums() {
        return hasDrums;
    }

    public Integer getMaxHallSize() {
        return maxHallSize;
    }

    public Integer getMinHallSize() {
        return minHallSize;
    }

    public void setMinStand(Integer minStand) {
        this.minStand = minStand;
    }

    public void setMinPrice(Integer minPrice) {
        this.minPrice = minPrice;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public void setMinCapacity(Integer minCapacity) {
        this.minCapacity = minCapacity;
    }

    public void setMaxStand(Integer maxStand) {
        this.maxStand = maxStand;
    }

    public void setMaxPrice(Integer maxPrice) {
        this.maxPrice = maxPrice;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public void setHasMic(Boolean hasMic) {
        this.hasMic = hasMic;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
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

    public void setMaxHallSize(Integer maxHallSize) {
        this.maxHallSize = maxHallSize;
    }

    public void setMinHallSize(Integer minHallSize) {
        this.minHallSize = minHallSize;
    }
}
