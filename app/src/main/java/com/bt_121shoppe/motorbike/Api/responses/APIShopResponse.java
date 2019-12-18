package com.bt_121shoppe.motorbike.Api.responses;

import com.bt_121shoppe.motorbike.models.PostViewModel;
import com.bt_121shoppe.motorbike.models.ShopViewModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class APIShopResponse {
    @SerializedName("results")
    @Expose
    private ArrayList<ShopViewModel> results;
    public ArrayList<ShopViewModel> getresults(){ return results; }
    public void setresults(ArrayList<ShopViewModel> items){ this.results = items; }

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
