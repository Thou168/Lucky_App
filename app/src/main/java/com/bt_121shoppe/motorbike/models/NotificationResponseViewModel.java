package com.bt_121shoppe.motorbike.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NotificationResponseViewModel {
    @SerializedName("results")
    @Expose
    private ArrayList<NotificationViewModel> results;
    public ArrayList<NotificationViewModel> getresults(){ return results; }
    public void setresults(ArrayList<NotificationViewModel> items){ this.results = items; }

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
