package com.bt_121shoppe.motorbike.Api.responses;

import com.bt_121shoppe.motorbike.models.StorePostViewModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class APIStorePostResponse {
    @SerializedName("results")
    @Expose
    private ArrayList<StorePostViewModel> results;
    public ArrayList<StorePostViewModel> getResults(){
        return results;
    }

    public void setResults(ArrayList<StorePostViewModel> results) {
        this.results = results;
    }
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
