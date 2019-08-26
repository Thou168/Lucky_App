package com.bt_121shoppe.lucky_app.Api.api.model;

import com.google.gson.annotations.SerializedName;

public class change_status_delete {

    @SerializedName("status")
    private float status;
    @SerializedName("modified")
    private String modified = null;
    @SerializedName("modified_by")
    private int modified_by;
    @SerializedName("rejected_comments")
    private String rejected_comments;

    public change_status_delete(float status, String modified, int modified_by, String rejected_comments) {

        this.status = status;
        this.modified = modified;
        this.modified_by = modified_by;
        this.rejected_comments = rejected_comments;
    }

    public float getStatus() {
        return status;
    }

    public void setStatus(float status) {
        this.status = status;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public int getModified_by() {
        return modified_by;
    }

    public void setModified_by(int modified_by) {
        this.modified_by = modified_by;
    }

    public String getRejected_comments() {
        return rejected_comments;
    }

    public void setRejected_comments(String rejected_comments) {
        this.rejected_comments = rejected_comments;
    }
}
