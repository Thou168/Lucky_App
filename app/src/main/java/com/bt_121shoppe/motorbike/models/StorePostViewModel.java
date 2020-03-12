package com.bt_121shoppe.motorbike.models;

import com.google.gson.annotations.SerializedName;
import com.bt_121shoppe.motorbike.Api.api.model.Item;

public class StorePostViewModel {
    @SerializedName("id")
    private int id;
    @SerializedName("post")
    private int post;
    @SerializedName("shop")
    private int shop;
    @SerializedName("record_status")
    private int record_status;
    private int countView;
    private String cost;
    public StorePostViewModel(){}
    public StorePostViewModel(int id,int post,int shop,int record_status){
        this.id=id;
        this.post=post;
        this.shop=shop;
        this.record_status=record_status;
    }

    public int getRecord_status() {
        return record_status;
    }

    public int getId() {
        return id;
    }

    public int getPost() {
        return post;
    }

    public int getShop() {
        return shop;
    }

    public void setRecord_status(int record_status) {
        this.record_status = record_status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPost(int post) {
        this.post = post;
    }

    public void setShop(int shop) {
        this.shop = shop;
    }

    public int getCountView() {
        return countView;
    }

    public void setCountView(int countView) {
        this.countView = countView;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}
