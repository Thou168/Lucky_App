package com.bt_121shoppe.lucky_app.models;

public class UserChat {
    String userId;
    String postId;

    private int postIdd;
    private String postTitle;
    private String postPrice;
    private String postFrontImage;
    private String postType;
    private int userPk;
    private String postUsername;
    private String firebaseUsename;

    public UserChat(){}

    public UserChat(String userId,String postId){
        this.userId=userId;
        this.postId=postId;
    }

    public UserChat(int postIdd,String postTitle,String postPrice,String postFrontImage,String postType,int userPk,String postUsername,String firebaseUsename){
        this.postIdd=postIdd;
        this.postTitle=postTitle;
        this.postPrice=postPrice;
        this.postFrontImage=postFrontImage;
        this.postType=postType;
        this.userPk=userPk;
        this.postUsername=postUsername;
        this.firebaseUsename=firebaseUsename;
    }

    public String getPostId() {
        return postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostPrice() {
        return postPrice;
    }

    public String getPostType() {
        return postType;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public int getPostIdd() {
        return postIdd;
    }

    public int getUserPk() {
        return userPk;
    }

    public String getFirebaseUsename() {
        return firebaseUsename;
    }

    public String getPostFrontImage() {
        return postFrontImage;
    }

    public String getPostUsername() {
        return postUsername;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public void setPostPrice(String postPrice) {
        this.postPrice = postPrice;
    }

    public void setFirebaseUsename(String firebaseUsename) {
        this.firebaseUsename = firebaseUsename;
    }

    public void setPostFrontImage(String postFrontImage) {
        this.postFrontImage = postFrontImage;
    }

    public void setPostIdd(int postIdd) {
        this.postIdd = postIdd;
    }

    public void setPostUsername(String postUsername) {
        this.postUsername = postUsername;
    }

    public void setUserPk(int userPk) {
        this.userPk = userPk;
    }
}

