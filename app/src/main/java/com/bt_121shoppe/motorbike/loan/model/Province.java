package com.bt_121shoppe.motorbike.loan.model;

import com.google.gson.annotations.SerializedName;

public class Province {
    @SerializedName("id")
    private int id;
    @SerializedName("province")
    private String province;
    @SerializedName("province_kh")
    private String province_kh;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvince_kh() {
        return province_kh;
    }

    public void setProvince_kh(String province_kh) {
        this.province_kh = province_kh;
    }
}
