package com.bt_121shoppe.motorbike.models;

public class PostDealerShopViewModel {
    private int id;
    private int post;
    private int shop;
    private int record_status;

    public PostDealerShopViewModel(){}
    public PostDealerShopViewModel(int id,int post,int shop,int record_status){
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
}
