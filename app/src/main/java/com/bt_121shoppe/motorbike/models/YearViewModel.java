package com.bt_121shoppe.motorbike.models;

import com.bt_121shoppe.motorbike.Api.api.model.Year;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

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

    @SerializedName("results")
    @Expose
    private List<YearViewModel> results;
    public List getresults(){ return results; }
    public void setresults(List items){ this.results = items; }

    @SerializedName("count")
    @Expose
    private int count;
    public int getCount() { return count; }
}
