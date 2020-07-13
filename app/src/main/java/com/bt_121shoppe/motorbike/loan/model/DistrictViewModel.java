package com.bt_121shoppe.motorbike.loan.model;

import com.google.gson.annotations.SerializedName;

public class DistrictViewModel {
    @SerializedName("id")
    private int id;
    @SerializedName("district")
    private String district;
    @SerializedName("district_kh")
    private String district_kh;
    @SerializedName("province")
    private int province;

    public int getId() {
        return id;
    }

    public int getProvince() {
        return province;
    }

    public String getDistrict() {
        return district;
    }

    public String getDistrict_kh() {
        return district_kh;
    }
}
