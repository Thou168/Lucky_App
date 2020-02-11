package com.bt_121shoppe.motorbike.models;

public class FBPostViewModel {
    private String color;
    private String coverUrl;
    private String createdAt;
    private int createdBy;
    private Long discountAmount;
    private String discountType;
    private String id;
    //private String isProduction;
    private String location;
    private String postCode;
    private Long price;
    private int status;
    private String subTitle;
    private String title;
    private String type;
    private String viewCount;

    public String getColor() {
        return color;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public int getStatus() {
        return status;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public Long getDiscountAmount() {
        return discountAmount;
    }

    public String getDiscountType() {
        return discountType;
    }

    public String getId() {
        return id;
    }

//    public String getIsProduction() {
//        return isProduction;
//    }

    public String getLocation() {
        return location;
    }

    public String getPostCode() {
        return postCode;
    }

    public Long getPrice() {
        return price;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public String getType() {
        return type;
    }

    public String getViewCount() {
        return viewCount;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public void setDiscountAmount(Long discountAmount) {
        this.discountAmount = discountAmount;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

//    public void setIsProduction(String isProduction) {
//        this.isProduction = isProduction;
//    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }
}
