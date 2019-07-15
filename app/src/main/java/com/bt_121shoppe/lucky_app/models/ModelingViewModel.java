package com.bt_121shoppe.lucky_app.models;

public class ModelingViewModel {
    private int id;
    private int brand;
    private String modeling_name;
    private String modeling_name_kh;
    private String description;
    private String modeling_image_path;
    private int record_status;
    public ModelingViewModel(int id,int brand,String modeling_name,String modeling_name_kh,String description,String modeling_image_path,int record_status){
        this.id=id;
        this.brand=brand;
        this.modeling_name=modeling_name;
        this.modeling_name_kh=modeling_name_kh;
        this.description=description;
        this.modeling_image_path=modeling_image_path;
        this.record_status=record_status;
    }

    public int getId() {
        return id;
    }

    public int getBrand() {
        return brand;
    }

    public String getModeling_name() {
        return modeling_name;
    }

    public String getModeling_name_kh() {
        return modeling_name_kh;
    }

    public String getDescription() {
        return description;
    }

    public String getModeling_image_path() {
        return modeling_image_path;
    }

    public int getRecord_status() {
        return record_status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBrand(int brand) {
        this.brand = brand;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRecord_status(int record_status) {
        this.record_status = record_status;
    }

    public void setModeling_image_path(String modeling_image_path) {
        this.modeling_image_path = modeling_image_path;
    }

    public void setModeling_name(String modeling_name) {
        this.modeling_name = modeling_name;
    }

    public void setModeling_name_kh(String modeling_name_kh) {
        this.modeling_name_kh = modeling_name_kh;
    }
}
