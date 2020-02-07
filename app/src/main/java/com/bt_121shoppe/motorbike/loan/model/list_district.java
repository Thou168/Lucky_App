package com.bt_121shoppe.motorbike.loan.model;

import com.google.gson.annotations.SerializedName;

class list_district extends district_Item {
    @SerializedName("wallpaper_image")
    private String img;
    public list_district(int loan_status, int loan_record_status) {
        super(loan_status, loan_record_status);
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
