package com.bt_121shoppe.motorbike.Api.api.model;

import com.bt_121shoppe.motorbike.models.UserProfileModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserResponseModel {
    @SerializedName("is_active")
    @Expose
    private boolean is_active;
    public boolean isIs_active() {
        return is_active;
    }
    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    @SerializedName("username")
    @Expose
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @SerializedName("first_name")
    @Expose
    private String first_name;

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }
    @SerializedName("profile")
    @Expose
    private UserProfileModel profile;

    public UserProfileModel getProfile() {
        return profile;
    }

    public void setProfile(UserProfileModel profile) {
        this.profile = profile;
    }
}
