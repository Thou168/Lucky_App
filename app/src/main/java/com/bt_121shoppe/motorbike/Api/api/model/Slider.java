package com.bt_121shoppe.motorbike.Api.api.model;

import com.bt_121shoppe.motorbike.loan.model.province_Item;
import com.google.gson.annotations.SerializedName;

public class Slider extends province_Item {
    @SerializedName("wallpaper_image")
    private String img;
    public Slider(int loan_status, int loan_record_status) {
        super(loan_status, loan_record_status);
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
