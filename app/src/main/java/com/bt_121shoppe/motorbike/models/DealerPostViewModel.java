package com.bt_121shoppe.motorbike.models;

public class DealerPostViewModel {
    private int id;
    private int post;
    private int shop;
    private int record_status;

    public DealerPostViewModel(){}
    public DealerPostViewModel(int id,int post,int shop,int record_status){
        this.id=id;
        this.post=post;
        this.shop=shop;
        this.record_status=record_status;
    }

    public int getShop() {
        return shop;
    }

    public int getPost() {
        return post;
    }

    public int getId() {
        return id;
    }

    public int getRecord_status() {
        return record_status;
    }

    public void setShop(int shop) {
        this.shop = shop;
    }

    public void setPost(int post) {
        this.post = post;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRecord_status(int record_status) {
        this.record_status = record_status;
    }
}
