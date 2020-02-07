package com.bt_121shoppe.motorbike.loan.model;

import com.bt_121shoppe.motorbike.Api.api.model.Item_loan;
import com.google.gson.annotations.SerializedName;

public class district_Item extends Item_loan {

    @SerializedName("district")
    private String district;
    @SerializedName("district_kh")
    private String district_kh;
    @SerializedName("province")
    private int provinceId;

    public district_Item(int loan_status, int loan_record_status) {
        super(loan_status, loan_record_status);
    }

    public int getProvinceId(){
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDistrict_kh() {
        return district_kh;
    }

    public void setDistrict_kh(String district_kh) {
        this.district_kh = district_kh;
    }
}
