package com.example.lucky_app.models;

public class CategoryViewModel {
    private int id;
    private String cat_name;
    private String cat_name_kh;
    private String cat_description;
    private String cat_image_path;
    private int record_status;
    private String created;

    public CategoryViewModel(int id,String cat_name,String cat_name_kh,String cat_description,String cat_image_path,int record_status){
        this.id=id;
        this.cat_name=cat_name;
        this.cat_name_kh=cat_name_kh;
        this.cat_description=cat_description;
        this.cat_image_path=cat_image_path;
        this.record_status=record_status;
    }

    public int getId() {
        return id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public String getCat_name_kh() {
        return cat_name_kh;
    }

    public String getCat_description() {
        return cat_description;
    }

    public String getCat_image_path() {
        return cat_image_path;
    }

    public int getRecord_status() {
        return record_status;
    }

    public String getCreated() {
        return created;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public void setCat_name_kh(String cat_name_kh) {
        this.cat_name_kh = cat_name_kh;
    }

    public void setCat_description(String cat_description) {
        this.cat_description = cat_description;
    }

    public void setCat_image_path(String cat_image_path) {
        this.cat_image_path = cat_image_path;
    }

    public void setRecord_status(int record_status) {
        this.record_status = record_status;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
