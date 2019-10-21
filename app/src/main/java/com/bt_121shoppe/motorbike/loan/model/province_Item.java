package com.bt_121shoppe.motorbike.loan.model;

import com.bt_121shoppe.motorbike.Api.api.model.Item_loan;
import com.google.gson.annotations.SerializedName;

public class province_Item extends Item_loan {

    @SerializedName("province")
    private String province;
    @SerializedName("province_kh")
    private String province_kh;

    public province_Item(int loan_status, int loan_record_status) {
        super(loan_status, loan_record_status);
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
