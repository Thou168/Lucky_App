package com.bt_121shoppe.motorbike.consumeapi;

public class LoginModel {
    private String token;
    private UserModel user;

    public String getToken() {
        return token;
    }

    public UserModel getUser() {
        return user;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
}
