package com.example.lucky_app.consumeapi;

public class UserModel {
    private int id;
    private int pk;
    private String key;
    private String token;
    private String username;
    private String email;
    private String first_name;
    private String last_name;

    public void setId(int id) {
        this.id = id;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public int getId() {
        return id;
    }

    public int getPk() {
        return pk;
    }

    public String getToken() {
        return token;
    }

    public String getKey() {
        return key;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getFirst_name() {
        return first_name;
    }
}
