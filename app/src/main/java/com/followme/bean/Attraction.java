package com.followme.bean;

import java.math.BigDecimal;

public class Attraction {
    private Long attractionid;

    private String name;

    private String type;

    private BigDecimal price;

    private String area;

    private String addr;

    private String description;

    private Double latitude;

    private Double longitude;

    private String level;

    private Double hot;

    private String imageurl;

    public Attraction(Long attractionid, String name, String type, BigDecimal price, String area, String addr, String description, Double latitude, Double longitude, String level, Double hot, String imageurl) {
        this.attractionid = attractionid;
        this.name = name;
        this.type = type;
        this.price = price;
        this.area = area;
        this.addr = addr;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.level = level;
        this.hot = hot;
        this.imageurl = imageurl;
    }

    public Attraction() {
        super();
    }

    public Long getAttractionid() {
        return attractionid;
    }

    public void setAttractionid(Long attractionid) {
        this.attractionid = attractionid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr == null ? null : addr.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level == null ? null : level.trim();
    }

    public Double getHot() {
        return hot;
    }

    public void setHot(Double hot) {
        this.hot = hot;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl == null ? null : imageurl.trim();
    }

    @Override
    public String toString() {
        return "Attraction{" +
                "attractionid=" + attractionid +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", price=" + price +
                ", area='" + area + '\'' +
                ", addr='" + addr + '\'' +
                ", description='" + description + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", level='" + level + '\'' +
                ", hot=" + hot +
                ", imageurl='" + imageurl + '\'' +
                '}';
    }
}