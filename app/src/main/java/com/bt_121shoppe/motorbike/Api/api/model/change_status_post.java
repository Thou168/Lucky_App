package com.bt_121shoppe.motorbike.Api.api.model;

import com.google.gson.annotations.SerializedName;

public class change_status_post {
    @SerializedName("status")
    private int status;

    public change_status_post(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
