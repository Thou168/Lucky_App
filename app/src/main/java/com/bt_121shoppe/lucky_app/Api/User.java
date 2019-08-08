package com.bt_121shoppe.lucky_app.Api;

import com.bt_121shoppe.lucky_app.Api.api.Sales;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("pk")
    private int pk;
    private String username;
    private String email;
    private String first_name;
    private Integer post;
    private String last_name;
    private Profile profile;
    private Sales sales;
    private String Phone;
    private String gender;
    private String marital_status;
    private String date_of_birth;
    private String shop_name;
    private int[] groups;
    private int id;
    private int group;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getPost() {
        return post;
    }

    public void setPost(Integer post) {
        this.post = post;
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getShop_name() {     return shop_name;  }

    public void setShop_name(String shop_name) {     this.shop_name = shop_name;  }

    public String getDate_of_birth() {     return date_of_birth; }

    public void setDate_of_birth(String date_of_birth) {    this.date_of_birth = date_of_birth;  }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public void setGroups(int[] groups) {
        this.groups = groups;
    }

    public int[] getGroups() {
        return groups;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMarital_status() {
        return marital_status;
    }

    public void setMarital_status(String marital_status) {
        this.marital_status = marital_status;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public Sales getSales() {
        return sales;
    }

    public void setSales(Sales sales) {
        this.sales = sales;
    }
}
