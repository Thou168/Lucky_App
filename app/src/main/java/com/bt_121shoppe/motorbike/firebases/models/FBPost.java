package com.bt_121shoppe.motorbike.firebases.models;

public class FBPost {
    private boolean isProduction;
    private String id;
    private String title;
    private String type;
    private String coverUrl;
    private Long price;
    private String discountAmount;
    private String discountType;
    private String location;
    private String createdAt;
    private int viewCount;
    private int status;

    public FBPost(){}
    public FBPost(boolean isProduction, String id, String title, String type, String coverUrl, Long price, String discountAmount, String discountType, String location, String createdAt, int viewCount,int status){
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
        this.status=status;
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

    public Long getPrice() {
        return price;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public boolean getIsProduction() {
        return isProduction;
    }

    public String getLocation() {
        return location;
    }

    public int getViewCount() {
        return viewCount;
    }

    public int getStatus() {
        return status;
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

    public void setPrice(Long price) {
        this.price = price;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setIsProduction(boolean isProduction) {
        this.isProduction = isProduction;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
