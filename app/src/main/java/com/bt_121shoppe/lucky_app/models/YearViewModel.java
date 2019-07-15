package com.bt_121shoppe.lucky_app.models;

public class YearViewModel {
    private int id;
    private String year;
    private String created;
    private String modified;

    public YearViewModel(int id,String year){
        this.id=id;
        this.year=year;
    }

    public int getId() {
        return id;
    }
    public String getYear() {
        return year;
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

    public void setYear(String year) {
        this.year = year;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }
}
