package com.example.lucky_app.Api;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("pk")
    private int pk;
    private String username;
    private String email;
    private String first_name;
    private Integer post;
    private String last_name;
    private Profile profile;
    private int[] groups;


    public Integer getPost() {
        return post;
    }

    public void setPost(Integer post) {
        this.post = post;
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public void setGroups(int[] groups) {
        this.groups = groups;
    }

    public int[] getGroups() {
        return groups;
    }
}
