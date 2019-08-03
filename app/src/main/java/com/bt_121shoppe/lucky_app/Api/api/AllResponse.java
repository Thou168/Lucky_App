package com.bt_121shoppe.lucky_app.Api.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by delaroy on 12/07/19.
 */
public class AllResponse {
    @SerializedName("profile")
    @Expose
    private List<User.Profile> items;
    public List<User.Profile> getItems(){
        return items;
    }
    public void setItems(List<User.Profile> items){
        this.items = items;
    }

    @SerializedName("count")
    @Expose
    private int count;
    public int getCount() { return count; }

//    @SerializedName("username")
//    @Expose
//    private String username;
//    public String getUsername() { return username; }
//    public void setUsername(String username) { this.username = username; }
//
    @SerializedName("profile")
    @Expose
    private List<User> users;
    public List<User> getUsers() { return users; }
    public void setUsers(List<User> users) { this.users = users; }
}
