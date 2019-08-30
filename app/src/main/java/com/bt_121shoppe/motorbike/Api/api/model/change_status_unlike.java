package com.bt_121shoppe.motorbike.Api.api.model;

import com.google.gson.annotations.SerializedName;

public class change_status_unlike {

    @SerializedName("modified")
    private String modified = null;
    @SerializedName("post")
    private int post;
    @SerializedName("like_by")
    private int like_by;
    @SerializedName("record_status")
    private int record_status;

    public change_status_unlike(String modified, int post, int like_by, int record_status) {
        this.modified = modified;
        this.post = post;
        this.like_by = like_by;
        this.record_status = record_status;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public int getPost() {
        return post;
    }

    public void setPost(int post) {
        this.post = post;
    }

    public int getLike_by() {
        return like_by;
    }

    public void setLike_by(int like_by) {
        this.like_by = like_by;
    }

    public int getRecord_status() {
        return record_status;
    }

    public void setRecord_status(int record_status) {
        this.record_status = record_status;
    }
}
