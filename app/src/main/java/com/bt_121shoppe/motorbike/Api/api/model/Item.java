package com.bt_121shoppe.motorbike.Api.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.*;

public class Item implements Serializable {

    private float id;
    private String title;
    private float category;
    private float status;
    private String condition;
    private String discount_type;
    private String discount;
    private float user;
    @SerializedName("front_image_path")
    private String front_image_path;
    private String right_image_path;
    private String left_image_path;
    private String back_image_path;
    private String created;
    private int created_by;
    @SerializedName("modified")
    private String modified;
    private String modified_by = null;
    private String approved_date;
    private float approved_by;
    private String rejected_date = null;
    private String rejected_by = null;
    private String rejected_comments;
    private float modeling;
    private String description;
    private String cost;
    private String post_type;
    private String vin_code;
    private String machine_code;
    private float type;
    private String contact_phone;
    private String contact_email;
    private String contact_address;
    private String color;

    @SerializedName("sales")
    private List<Type_item> sales;
    @SerializedName("buys")
    private List<buy_item> buy;
    @SerializedName("rents")
    private List<rent_item> rent;

    public List<Type_item> getSales() {
        return sales;
    }

    public List<buy_item> getBuy() {
        return buy;
    }

    public List<rent_item> getRent() {
        return rent;
    }

    //    private Type_item buys;
//    private Type_item rents;
    public float getId() {
        return id;
    }

    public void setId(float id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getCategory() {
        return category;
    }

    public void setCategory(float category) {
        this.category = category;
    }

    public float getStatus() {
        return status;
    }

    public void setStatus(float status) {
        this.status = status;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getDiscount_type() {
        return discount_type;
    }

    public void setDiscount_type(String discount_type) {
        this.discount_type = discount_type;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public float getUser() {
        return user;
    }

    public void setUser(float user) {
        this.user = user;
    }

    public String getFront_image_path() {
        return front_image_path;
    }

    public void setFront_image_path(String front_image_path) {
        this.front_image_path = front_image_path;
    }

    public String getRight_image_path() {
        return right_image_path;
    }

    public void setRight_image_path(String right_image_path) {
        this.right_image_path = right_image_path;
    }

    public String getLeft_image_path() {
        return left_image_path;
    }

    public void setLeft_image_path(String left_image_path) {
        this.left_image_path = left_image_path;
    }

    public String getBack_image_path() {
        return back_image_path;
    }

    public void setBack_image_path(String back_image_path) {
        this.back_image_path = back_image_path;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public int getCreated_by() {
        return created_by;
    }

    public void setCreated_by(int created_by) {
        this.created_by = created_by;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getModified_by() {
        return modified_by;
    }

    public void setModified_by(String modified_by) {
        this.modified_by = modified_by;
    }

    public String getApproved_date() {
        return approved_date;
    }

    public void setApproved_date(String approved_date) {
        this.approved_date = approved_date;
    }

    public float getApproved_by() {
        return approved_by;
    }

    public void setApproved_by(float approved_by) {
        this.approved_by = approved_by;
    }

    public String getRejected_date() {
        return rejected_date;
    }

    public void setRejected_date(String rejected_date) {
        this.rejected_date = rejected_date;
    }

    public String getRejected_by() {
        return rejected_by;
    }

    public void setRejected_by(String rejected_by) {
        this.rejected_by = rejected_by;
    }

    public String getRejected_comments() {
        return rejected_comments;
    }

    public void setRejected_comments(String rejected_comments) {
        this.rejected_comments = rejected_comments;
    }

    public float getModeling() {
        return modeling;
    }

    public void setModeling(float modeling) {
        this.modeling = modeling;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getPost_type() {
        return post_type;
    }

    public void setPost_type(String post_type) {
        this.post_type = post_type;
    }

    public String getVin_code() {
        return vin_code;
    }

    public void setVin_code(String vin_code) {
        this.vin_code = vin_code;
    }

    public String getMachine_code() {
        return machine_code;
    }

    public void setMachine_code(String machine_code) {
        this.machine_code = machine_code;
    }

    public float getType() {
        return type;
    }

    public void setType(float type) {
        this.type = type;
    }

    public String getContact_phone() {
        return contact_phone;
    }

    public void setContact_phone(String contact_phone) {
        this.contact_phone = contact_phone;
    }

    public String getContact_email() {
        return contact_email;
    }

    public void setContact_email(String contact_email) {
        this.contact_email = contact_email;
    }

    public String getContact_address() {
        return contact_address;
    }

    public void setContact_address(String contact_address) {
        this.contact_address = contact_address;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public static class buy_item{

        private float type_id;
        private float buy_status;
        private float record_status;
       private  String created;

        public float getBuy_status() {
            return buy_status;
        }

        public String getCreated() {
            return created;
        }

        public float getType_id() {
            return type_id;
        }

        public float getRecord_status() {
            return record_status;
        }
    }
    public static class rent_item{
        private float id;
        private float rent_status;
        private float record_status;
        private String rent_type;
        private String rent_date = null;
        private String return_date = null;
        private String price;
        private String total_price;
        private float rent_count_number;

        public float getId() {
            return id;
        }

        public float getRent_status() {
            return rent_status;
        }

        public float getRecord_status() {
            return record_status;
        }

        public String getRent_type() {
            return rent_type;
        }

        public String getRent_date() {
            return rent_date;
        }

        public String getReturn_date() {
            return return_date;
        }

        public String getPrice() {
            return price;
        }

        public String getTotal_price() {
            return total_price;
        }

        public float getRent_count_number() {
            return rent_count_number;
        }
    }

}
