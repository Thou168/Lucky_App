package com.bt_121shoppe.motorbike.classes;

import com.bt_121shoppe.motorbike.Api.api.model.Item_loan;
import com.bt_121shoppe.motorbike.models.PostViewModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class APIResponse {

    @SerializedName("results")
    @Expose
    private ArrayList<PostViewModel> results;
    public ArrayList<PostViewModel> getresults(){ return results; }
    public void setresults(ArrayList<PostViewModel> items){ this.results = items; }

    @SerializedName("count")
    @Expose
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
