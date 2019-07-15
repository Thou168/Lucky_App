package com.bt_121shoppe.lucky_app.models;

public class BrandViewModel {
    private int id;
    private int category;
    private String created;
    private String brand_name;
    private String brand_name_kh;
    private String description;
    private String brand_image_path;
    private int  record_status;

    public BrandViewModel(int id,int category,String brand_name,String brand_name_kh,String description,String brand_image_path){
        this.id=id;
        this.category=category;
        this.brand_name=brand_name;
        this.brand_name_kh=brand_name_kh;
        this.description=description;
        this.brand_image_path=brand_image_path;
    }

    public int getId() {
        return id;
    }

    public int getCategory() {
        return category;
    }

    public String getCreated() {
        return created;
    }

    public int getRecord_status() {
        return record_status;
    }

    public String getBrand_image_path() {
        return brand_image_path;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public String getBrand_name_kh() {
        return brand_name_kh;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setRecord_status(int record_status) {
        this.record_status = record_status;
    }

    public void setBrand_image_path(String brand_image_path) {
        this.brand_image_path = brand_image_path;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public void setBrand_name_kh(String brand_name_kh) {
        this.brand_name_kh = brand_name_kh;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
