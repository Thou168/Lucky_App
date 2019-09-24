package com.bt_121shoppe.motorbike.models;

import android.graphics.Bitmap;

public class UserShopViewModel {
    private int id;
    private int user;
    private String shop_name;
    private String shop_address;
    private Bitmap shop_image;
    private int record_status;
    private String shop_image_path;

    public UserShopViewModel(){}

    public UserShopViewModel(int id,int user,String shop_name,String shop_address,Bitmap shop_image,int record_status,String shop_image_path){
        this.id=id;
        this.user=user;
        this.shop_name=shop_name;
        this.shop_address=shop_address;
        this.shop_image=shop_image;
        this.record_status=record_status;
        this.shop_image_path=shop_image_path;
    }

    public int getId() { return id; }

    public int getRecord_status() {
        return record_status;
    }

    public int getUser() {
        return user;
    }

    public String getShop_address() {
        return shop_address;
    }

    public Bitmap getShop_image() {
        return shop_image;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRecord_status(int record_status) {
        this.record_status = record_status;
    }

    public void setShop_address(String shop_address) {
        this.shop_address = shop_address;
    }

    public void setShop_image(Bitmap shop_image) {
        this.shop_image = shop_image;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public String getShop_image_path() {
        return shop_image_path;
    }

    public void setShop_image_path(String shop_image_path) {
        this.shop_image_path = shop_image_path;
    }
}
