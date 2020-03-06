package com.bt_121shoppe.motorbike.models;

import com.google.gson.annotations.SerializedName;

public class ShopUpdateViewModel {
    @SerializedName("id")
    private int id;
    @SerializedName("user")
    private int user;
    @SerializedName("shop_view")
    private int shop_view;
    @SerializedName("shop_image")
    private String shop_image;

    public ShopUpdateViewModel(){}
    public ShopUpdateViewModel(int id,int user,int shop_view,String shop_image){
        this.id=id;
        this.user=user;
        this.shop_view=shop_view;
        this.shop_image=shop_image;
    }

    public int getId() {
        return id;
    }

    public int getShop_view() {
        return shop_view;
    }

    public int getUser() {
        return user;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setShop_view(int shop_view) {
        this.shop_view = shop_view;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public String getShop_image() {
        return shop_image;
    }

    public void setShop_image(String shop_image) {
        this.shop_image = shop_image;
    }
}
