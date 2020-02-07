package com.bt_121shoppe.motorbike.loan.model;

import com.bt_121shoppe.motorbike.Api.api.model.Slider;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class District {

    @SerializedName("results")
    @Expose
    private List<list_district> results;
    public List getresults(){ return results; }
    public void setresults(List items){ this.results = items; }

    @SerializedName("id")
    private int id;
    @SerializedName("district")
    private String district;
    @SerializedName("district_kh")
    private String district_kh;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
