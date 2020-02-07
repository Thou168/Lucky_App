package com.bt_121shoppe.motorbike.loan.model;

import com.google.gson.annotations.SerializedName;

class list_commune extends commune_Item {
    @SerializedName("wallpaper_image")
    private String img;
    public list_commune(int loan_status, int loan_record_status) {
        super(loan_status, loan_record_status);
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
