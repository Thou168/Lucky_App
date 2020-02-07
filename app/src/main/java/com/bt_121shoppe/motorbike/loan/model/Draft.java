package com.bt_121shoppe.motorbike.loan.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Draft {

    @SerializedName("results")
    @Expose
    private List<list_draft> results;
    public List getresults(){ return results; }
    public void setresults(List items){ this.results = items; }
}
