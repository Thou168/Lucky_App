package com.bt_121shoppe.motorbike.models;

public class TypeViewModel {
    private int id;
    private String type;
    private String type_kh;
    private String record_status;
    private String created;
    private String modified;

    public TypeViewModel(int id,String type,String type_kh,String record_status){
        this.id=id;
        this.type=type;
        this.type_kh=type_kh;
        this.record_status=record_status;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getType_kh() {
        return type_kh;
    }

    public String getRecord_status() {
        return record_status;
    }

    public String getCreated() {
        return created;
    }

    public String getModified() {
        return modified;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setType_kh(String type_kh) {
        this.type_kh = type_kh;
    }

    public void setRecord_status(String record_status) {
        this.record_status = record_status;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
