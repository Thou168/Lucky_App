package com.bt_121shoppe.motorbike.models;

public class User {

    private String id;
    private String username;
    private String imageURL;
    private String status;
    private String search;
    private String coverURL;
    private String password;
    private String email;
    private String group;

    public User(String id,String username,String imageURL,String status,String search,String coverURL,String password,String email,String group){
        this.id=id;
        this.username=username;
        this.imageURL=imageURL;
        this.status=status;
        this.search=search;
        this.coverURL=coverURL;
        this.password=password;
        this.email=email;
        this.group=group;
    }

    public User(){

    }

    public String getUsername() {
        return username;
    }

    public String getId() {
        return id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getStatus() {
        return status;
    }

    public String getSearch() {
        return search;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public void setCoverURL(String coverURL) {
        this.coverURL = coverURL;
    }

    public String getCoverURL() {
        return coverURL;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
