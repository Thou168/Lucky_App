package com.bt_121shoppe.lucky_app.firebases.models;

public class FBPost {
    private String isProduction;
    private String id;
    private String title;
    private String type;
    private String coverUrl;
    private String price;
    private String discountAmount;
    private String discountType;
    private String location;
    private String createdAt;
    private String viewCount;

    public FBPost(){}
    public FBPost(String isProduction,String id,String title,String type,String coverUrl,String price,String discountAmount,String discountType,String location,String createdAt,String viewCount){
        this.isProduction=isProduction;
        this.id=id;
        this.title=title;
        this.type=type;
        this.coverUrl=coverUrl;
        this.price=price;
        this.discountAmount=discountAmount;
        this.discountType=discountType;
        this.location=location;
        this.createdAt=createdAt;
        this.viewCount=viewCount;
    }

    public String getDiscountType() {
        return discountType;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getIsProduction() {
        return isProduction;
    }

    public String getLocation() {
        return location;
    }

    public String getViewCount() {
        return viewCount;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setIsProduction(String isProduction) {
        this.isProduction = isProduction;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }
}
