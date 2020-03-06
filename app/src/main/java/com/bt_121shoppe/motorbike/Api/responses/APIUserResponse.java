package com.bt_121shoppe.motorbike.Api.responses;

import com.bt_121shoppe.motorbike.Api.api.model.Slider;
import com.bt_121shoppe.motorbike.Api.api.model.UserResponseModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class APIUserResponse {
    @SerializedName("results")
    @Expose
    private List<UserResponseModel> results;
    public List getresults(){ return results; }
    public void setresults(List items){ this.results = items; }

    @SerializedName("count")
    @Expose
    private int count;
    public int getCount() { return count; }

    @SerializedName("username")
    @Expose
    private String username;
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    @SerializedName("first_name")
    @Expose
    private String first_name;
    public String getFirst_name() { return first_name; }
    public void setFirst_name(String first_name) { this.first_name = first_name; }
}
