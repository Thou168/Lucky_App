package com.bt_121shoppe.motorbike.loan.model;

import com.bt_121shoppe.motorbike.Api.api.model.Item_loan;
import com.google.gson.annotations.SerializedName;

public class commune_Item extends Item_loan {

    @SerializedName("commune")
    private String commune;
    @SerializedName("commune_kh")
    private String commune_kh;
    @SerializedName("district")
    private int districtId;

    public commune_Item(int loan_status, int loan_record_status) {
        super(loan_status, loan_record_status);
    }

    public int getDistrictId(){
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public String getCommune_kh() {
        return commune_kh;
    }

    public void setCommune_kh(String commune_kh) {
        this.commune_kh = commune_kh;
    }
}
