package com.bt_121shoppe.motorbike.Api.api.model;

public class dealershop {
    private int post;
    private int shop;
    private int record_status;

    public dealershop(int post, int shop, int record_status) {
        this.post = post;
        this.shop = shop;
        this.record_status = record_status;
    }

    public int getPost() {
        return post;
    }

    public void setPost(int post) {
        this.post = post;
    }

    public int getShop() {
        return shop;
    }

    public void setShop(int shop) {
        this.shop = shop;
    }

    public int getRecord_status() {
        return record_status;
    }

    public void setRecord_status(int record_status) {
        this.record_status = record_status;
    }
}
