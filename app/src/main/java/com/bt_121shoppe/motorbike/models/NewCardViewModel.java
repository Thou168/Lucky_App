package com.bt_121shoppe.motorbike.models;

import android.graphics.Bitmap;

public class NewCardViewModel {
    private int id;
    private int shopId;
    private String wing_account;
    private String wing_number;
    private int record_status;
    private boolean isEdit;
    private boolean isAddNew;

    public NewCardViewModel(){}

    public NewCardViewModel(int id,int shopId, String wing_account, String wing_number, int record_status){
        this.id=id;
        this.shopId=shopId;
        this.wing_account=wing_account;
        this.wing_number=wing_number;
        this.record_status=record_status;
    }

    public int getId(){ return id; }

    public int getRecord_status() {
        return record_status;
    }

    public int getShopId() {
        return shopId;
    }

    public String getWing_account() {
        return wing_account;
    }

    public String getWing_number() {
        return wing_number;
    }

    public void setRecord_status(int record_status) {
        this.record_status = record_status;
    }

    public void setWing_number(String wing_number) {
        this.wing_number = wing_number;
    }

    public void setWing_account(String wing_account) {
        this.wing_account = wing_account;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public void setId(int id){ id = id;}
}
