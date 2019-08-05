package com.bt_121shoppe.lucky_app.Api.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by delaroy on 12/07/19.
 */
public class AllResponse {
    @SerializedName("results")
    @Expose
    private List<Year> categories;
    public List getCategory(){ return categories; }
    public void setCategory(List items){ this.categories = items; }
//    @SerializedName("results")
//    @Expose
//    private List<Breand> breands;
//    public List<Breand> getBreands(){ return breands; }
//    public void setCategories(List<Breand> breands){
//        this.breands = breands;
//    }
//
//    @SerializedName("count")
//    @Expose
//    private int count;
//    public int getCount() { return count; }

//    @SerializedName("username")
//    @Expose
//    private String username;
//    public String getUsername() { return username; }
//    public void setUsername(String username) { this.username = username; }
//
}
