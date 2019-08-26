package com.bt_121shoppe.lucky_app.Api.api;

import com.bt_121shoppe.lucky_app.Api.api.model.Item;
import com.bt_121shoppe.lucky_app.Api.api.model.Item_loan;
import com.bt_121shoppe.lucky_app.Api.api.model.LikebyUser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by delaroy on 12/07/19.
 */
public class AllResponse {
    @SerializedName("results")
    @Expose
    private List<Item_loan> results;
    public List getresults(){ return results; }
    public void setresults(List items){ this.results = items; }

    @SerializedName("count")
    @Expose
    private int count;
    public int getCount() { return count; }

//    @SerializedName("username")
//    @Expose
//    private String username;
//    public String getUsername() { return username; }
//    public void setUsername(String username) { this.username = username; }

}
