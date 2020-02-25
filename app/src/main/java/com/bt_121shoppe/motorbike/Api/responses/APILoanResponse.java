package com.bt_121shoppe.motorbike.Api.responses;

import com.bt_121shoppe.motorbike.Api.api.model.Slider;
import com.bt_121shoppe.motorbike.loan.model.loan_item;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class APILoanResponse {
    @SerializedName("results")
    @Expose
    private List<loan_item> results;

    public List<loan_item> getResults() {
        return results;
    }

    public void setResults(List<loan_item> results) {
        this.results = results;
    }

    @SerializedName("count")
    @Expose
    private int count;
    public int getCount() { return count; }

    public void setCount(int count) {
        this.count = count;
    }
}
