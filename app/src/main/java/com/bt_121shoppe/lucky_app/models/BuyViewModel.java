package com.bt_121shoppe.lucky_app.models;

public class BuyViewModel {
    private int id;
    private int buy_status;
    private int record_status;
    private String created;

    public int getId() {
        return id;
    }

    public int getRecord_status() {
        return record_status;
    }

    public String getCreated() {
        return created;
    }

    public int getBuy_status() {
        return buy_status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRecord_status(int record_status) {
        this.record_status = record_status;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setBuy_status(int buy_status) {
        this.buy_status = buy_status;
    }
}
