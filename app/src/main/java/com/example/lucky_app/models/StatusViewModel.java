package com.example.lucky_app.models;

public class StatusViewModel {
    public int id;
    public String status_name;
    public String status_type;
    public String created;
    public String modified;

    public int getId() {
        return id;
    }

    public String getStatus_name() {
        return status_name;
    }

    public String getStatus_type() {
        return status_type;
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

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public void setStatus_type(String status_type) {
        this.status_type = status_type;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }
}
