package com.bt_121shoppe.motorbike.Api.api.model;

public class Modeling {
    private int id;
    private int userId;
    private String img_user;
    private String image;
    private String title;
    private Double cost;
    private String condition;
    private String postType;
    private String location_duration;
    private String count_view;
    private String discount_type;
    private Double discount;

    public Modeling(int id, int userId, String img_user, String image, String title, Double cost, String condition, String postType, String location_duration, String count_view, String discount_type, Double discount) {
        this.id = id;
        this.userId = userId;
        this.img_user = img_user;
        this.image = image;
        this.title = title;
        this.cost = cost;
        this.condition = condition;
        this.postType = postType;
        this.location_duration = location_duration;
        this.count_view = count_view;
        this.discount_type = discount_type;
        this.discount = discount;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getImg_user() {
        return img_user;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public Double getCost() {
        return cost;
    }

    public String getCondition() {
        return condition;
    }

    public String getPostType() {
        return postType;
    }

    public String getLocation_duration() {
        return location_duration;
    }

    public String getCount_view() {
        return count_view;
    }

    public String getDiscount_type() {
        return discount_type;
    }

    public Double getDiscount() {
        return discount;
    }
}
