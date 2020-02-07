package com.bt_121shoppe.motorbike.models;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShopViewModel {
    @SerializedName("id")
    private int id;
    @SerializedName("user")
    private int user;
    @SerializedName("shop_name")
    private String shop_name;
    @SerializedName("shop_address")
    private String shop_address;
    @SerializedName("shop_image")
    private String shop_image;
    @SerializedName("record_status")
    private int record_status;
    @SerializedName("shop_view")
    private int shop_view;
    @SerializedName("shop_rate")
    private String shop_rate;
    @SerializedName("cards")
    private List<Cards> cards;

    public ShopViewModel(){}

    public ShopViewModel(int id,int user,String shop_name,String shop_address,String shop_image,int record_status){
        this.id=id;
        this.user=user;
        this.shop_name=shop_name;
        this.shop_address=shop_address;
        this.shop_image=shop_image;
        this.record_status=record_status;
    }
    public ShopViewModel(int id,int user,String shop_name,String shop_address,String shop_image,int record_status,int shop_view,String shop_rate){
        this.id=id;
        this.user=user;
        this.shop_name=shop_name;
        this.shop_address=shop_address;
        this.shop_image=shop_image;
        this.record_status=record_status;
        this.shop_rate=shop_rate;
        this.shop_view=shop_view;
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

    public String getShop_image() {
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

    public void setShop_image(String shop_image) {
        this.shop_image = shop_image;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getShop_view() {
        return shop_view;
    }

    public String getShop_rate() {
        return shop_rate;
    }

    public void setShop_rate(String shop_rate) {
        this.shop_rate = shop_rate;
    }

    public void setShop_view(int shop_view) {
        this.shop_view = shop_view;
    }

    public List<Cards> getCards() {
        return cards;
    }

    public void setCards(List<Cards> cards) {
        this.cards = cards;
    }

    public class Cards {
        @SerializedName("id")
        private int id;
        @SerializedName("shop")
        private int shopId;
        @SerializedName("wing_account_name")
        private String wing_account_name;
        @SerializedName("wing_account_number")
        private String wing_account_number;

        public int getId() {
            return id;
        }
        public int getShopId() {
            return shopId;
        }

        public String getWing_account_name() {
            return wing_account_name;
        }

        public String getWing_account_number() {
            return wing_account_number;
        }

        public void setId(int id) {
            this.id = id;
        }
        public void setShopId(int shopId) {
            this.shopId = shopId;
        }

        public void setWing_account_name(String wing_account_name) {
            this.wing_account_name = wing_account_name;
        }

        public void setWing_account_number(String wing_account_number) {
            this.wing_account_number = wing_account_number;
        }
    }
}
