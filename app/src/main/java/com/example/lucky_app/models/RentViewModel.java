package com.example.lucky_app.models;

public class RentViewModel {
    private int id;
    private int rent_status;
    private int record_status;
    private String rent_type;
    private String rent_date;
    private String return_date;
    private String price;
    private String total_price;
    private String rent_count_number;

    public int getId() {
        return id;
    }

    public int getRent_status() {
        return rent_status;
    }

    public int getRecord_status() {
        return record_status;
    }

    public String getTotal_price() {
        return total_price;
    }

    public String getPrice() {
        return price;
    }

    public String getRent_count_number() {
        return rent_count_number;
    }

    public String getRent_date() {
        return rent_date;
    }

    public String getRent_type() {
        return rent_type;
    }

    public String getReturn_date() {
        return return_date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRecord_status(int record_status) {
        this.record_status = record_status;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setRent_count_number(String rent_count_number) {
        this.rent_count_number = rent_count_number;
    }

    public void setRent_date(String rent_date) {
        this.rent_date = rent_date;
    }

    public void setRent_status(int rent_status) {
        this.rent_status = rent_status;
    }

    public void setRent_type(String rent_type) {
        this.rent_type = rent_type;
    }

    public void setReturn_date(String return_date) {
        this.return_date = return_date;
    }
}
