package com.bt_121shoppe.motorbike.models;

import com.bt_121shoppe.motorbike.Api.api.model.Item_loan;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FilterConditionViewModel {
    private int id;
    private String name;

    private int postType;
    private int categoryId;
    private int brandId;
    private int yearId;
    private double minPrice;
    private double maxPrice;

    public FilterConditionViewModel(){}

    public FilterConditionViewModel(int id,String name){
        this.id=id;
        this.name=name;
    }

    public FilterConditionViewModel(int postType,int categoryId,int brandId,int yearId,double minPrice,double maxPrice){
        this.postType=postType;
        this.categoryId=categoryId;
        this.brandId=brandId;
        this.yearId=yearId;
        this.minPrice=minPrice;
        this.maxPrice=maxPrice;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public int getBrandId() {
        return brandId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getPostType() {
        return postType;
    }

    public int getYearId() {
        return yearId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public void setPostType(int postType) {
        this.postType = postType;
    }

    public void setYearId(int yearId) {
        this.yearId = yearId;
    }

    @SerializedName("results")
    @Expose
    private List<FilterConditionViewModel> results;
    public List getresults(){ return results; }
    public void setresults(List items){ this.results = items; }

    @SerializedName("count")
    @Expose
    private int count;
    public int getCount() { return count; }
}
