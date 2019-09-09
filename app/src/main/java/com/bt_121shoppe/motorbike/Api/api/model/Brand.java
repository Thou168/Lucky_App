package com.bt_121shoppe.motorbike.Api.api.model;

public class Brand {
    private int id;
    private int category;
    private String created;
    private String brand_name;
    private String brand_name_as_kh;
    private String description;
    private String brand_image_path = null;
    private String record_status = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getBrand_name_as_kh() {
        return brand_name_as_kh;
    }

    public void setBrand_name_as_kh(String brand_name_as_kh) {
        this.brand_name_as_kh = brand_name_as_kh;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrand_image_path() {
        return brand_image_path;
    }

    public void setBrand_image_path(String brand_image_path) {
        this.brand_image_path = brand_image_path;
    }

    public String getRecord_status() {
        return record_status;
    }

    public void setRecord_status(String record_status) {
        this.record_status = record_status;
    }
}
