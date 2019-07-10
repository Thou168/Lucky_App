package com.example.lucky_app.models;

public class SaleViewModel {
    private int id;
    private int sale_status;
    private int record_status;
    private String sold_date;
    private String price;
    private String total_price;

    public void setId(int id) {
        this.id = id;
    }

    public void setSale_status(int sale_status) {
        this.sale_status = sale_status;
    }

    public void setRecord_status(int record_status) {
        this.record_status = record_status;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setSold_date(String sold_date) {
        this.sold_date = sold_date;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public int getId() {
        return id;
    }

    public int getRecord_status() {
        return record_status;
    }

    public int getSale_status() {
        return sale_status;
    }

    public String getPrice() {
        return price;
    }

    public String getSold_date() {
        return sold_date;
    }

    public String getTotal_price() {
        return total_price;
    }
}
