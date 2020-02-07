package com.bt_121shoppe.motorbike.loan.model;

import com.bt_121shoppe.motorbike.Api.api.model.Item_loan;
import com.google.gson.annotations.SerializedName;

public class village_Item extends Item_loan {

    @SerializedName("village")
    private String village;
    @SerializedName("village_kh")
    private String village_kh;
    @SerializedName("commune")
    private int communeId;

    public village_Item(int loan_status, int loan_record_status) {
        super(loan_status, loan_record_status);
    }

    public int getCommuneId(){
        return communeId;
    }

    public void setCommuneId(int communeId) {
        this.communeId = communeId;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getVillage_kh() {
        return village_kh;
    }

    public void setVillage_kh(String village_kh) {
        this.village_kh = village_kh;
    }
}
