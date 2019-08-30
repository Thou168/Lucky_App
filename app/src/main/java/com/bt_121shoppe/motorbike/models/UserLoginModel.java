package com.bt_121shoppe.motorbike.models;

public class UserLoginModel {
    private String username;
    private String email;
    private String key;
    private String status;
    private String token;
    private int id;
    private int pk;
    private int[] groups;
    private UserProfileModel profile;

    public int getPk() {
        return pk;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getKey() {
        return key;
    }

    public String getStatus() {
        return status;
    }

    public String getToken() {
        return token;
    }

    public int[] getGroups() {
        return groups;
    }

    public UserProfileModel getProfile() {
        return profile;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setGroups(int[] groups) {
        this.groups = groups;
    }

    public void setProfile(UserProfileModel profile) {
        this.profile = profile;
    }
}
