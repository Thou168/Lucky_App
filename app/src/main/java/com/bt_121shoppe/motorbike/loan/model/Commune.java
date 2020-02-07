package com.bt_121shoppe.motorbike.loan.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Commune {

    @SerializedName("results")
    @Expose
    private List<list_commune> results;
    public List getresults(){ return results; }
    public void setresults(List items){ this.results = items; }

    @SerializedName("id")
    private int id;
    @SerializedName("commune")
    private String commune;
    @SerializedName("commune_kh")
    private String commune_kh;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
