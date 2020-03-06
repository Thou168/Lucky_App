package com.bt_121shoppe.motorbike.Api.api;

import com.bt_121shoppe.motorbike.models.CountViewModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CountViewResponse {
    @SerializedName("results")
    @Expose
    private ArrayList<CountViewModel> results;
    public ArrayList<CountViewModel> getresults(){ return results; }
    public void setresults(ArrayList<CountViewModel> items){ this.results = items; }
}
