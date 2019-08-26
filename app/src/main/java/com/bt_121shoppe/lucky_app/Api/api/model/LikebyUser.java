package com.bt_121shoppe.lucky_app.Api.api.model;

import com.google.gson.annotations.SerializedName;

public class LikebyUser extends Item{


    private int post;
    private float like_by;
    private int record_status;

    public float getPost() {
        return post;
    }

    public int getRecord_status() {
        return record_status;
    }

    public void setRecord_status(int record_status) {
        this.record_status = record_status;
    }

    public void setPost(int post) {
        this.post = post;
    }

    public float getLike_by() {
        return like_by;
    }

    public void setLike_by(float like_by) {
        this.like_by = like_by;
    }
}
