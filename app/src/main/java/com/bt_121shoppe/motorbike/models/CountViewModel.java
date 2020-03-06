package com.bt_121shoppe.motorbike.models;

import com.bt_121shoppe.motorbike.Api.api.model.detail_shop;

import java.util.List;

public class CountViewModel {
    private int id;
    private String number;
    private int postId;

    public int getId() {
        return id;
    }

    public int getPostId() {
        return postId;
    }

    public String getNumber() {
        return number;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }
}


