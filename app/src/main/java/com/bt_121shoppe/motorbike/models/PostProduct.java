package com.bt_121shoppe.motorbike.models;

import java.io.Serializable;

public class PostProduct implements Serializable {
    private static final long serialVersionUID=1L;
    private int postId;
    private int user_id;
    private String postTitle;
    private String postType;
    private String postImage;
    private String postPrice;
    private String locationDuration;
    private int countView;
    private String discountType;
    private String discountAmount;

    public PostProduct(){}

    public PostProduct(int postId,int user_id,String postTitle,String postType,String postImage,String postPrice,String locationDuration,int countView,String discountType,String discountAmount){
        this.postId=postId;
        this.user_id = user_id;
        this.postTitle=postTitle;
        this.postType=postType;
        this.postImage=postImage;
        this.postPrice=postPrice;
        this.locationDuration=locationDuration;
        this.countView=countView;
        this.discountType=discountType;
        this.discountAmount=discountAmount;
    }

    public int getPostId() {
        return postId;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public String getPostType() {
        return postType;
    }

    public String getPostImage() {
        return postImage;
    }

    public String getPostPrice() {
        return postPrice;
    }

    public String getLocationDuration() {
        return locationDuration;
    }

    public int getCountView() {
        return countView;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setCountView(int countView) {
        this.countView = countView;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public void setLocationDuration(String locationDuration) {
        this.locationDuration = locationDuration;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public void setPostPrice(String postPrice) {
        this.postPrice = postPrice;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }
}
