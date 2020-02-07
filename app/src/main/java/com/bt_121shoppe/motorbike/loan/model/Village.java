package com.bt_121shoppe.motorbike.loan.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Village {

    @SerializedName("results")
    @Expose
    private List<list_village> results;
    public List getresults(){ return results; }
    public void setresults(List items){ this.results = items; }

    @SerializedName("id")
    private int id;
    @SerializedName("village")
    private String village;
    @SerializedName("village_kh")
    private String village_kh;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDistrict() {
        return village;
    }

    public void setDistrict(String village) {
        this.village = village;
    }

    public String getDistrict_kh() {
        return village_kh;
    }

    public void setDistrict_kh(String village_kh) {
        this.village_kh = village_kh;
    }
}
