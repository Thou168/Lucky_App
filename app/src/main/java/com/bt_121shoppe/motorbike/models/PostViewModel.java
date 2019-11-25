package com.bt_121shoppe.motorbike.models;

import java.util.List;

public class PostViewModel {
    private int id;
    private String title;
    private int category;
    private int status;
    private String condition;
    private String discount_type;
    private String discount;
    private String user;
    private String front_image_path;
    private String right_image_path;
    private String left_image_path;
    private String back_image_path;
    private String extra_image1;
    private String extra_image2;
    private String created;
    private String created_by;
    private String modified;
    private String modified_by;
    private String approved_date;
    private String approved_by;
    private String rejected_date;
    private String rejected_by;
    private String rejected_comments;
    private int year;
    private int modeling;
    private String description;
    private String cost;
    private String post_type;
    private String vin_code;
    private String machine_code;
    private int type;
    private String contact_phone;
    private String contact_email;
    private String contact_address;
    private String color;
    private SaleViewModel[] sales;
    private BuyViewModel[] buys;
    private RentViewModel[] rents;
    private String post_code;
    private String post_sub_title;
    private String used_eta1;
    private String used_eta2;
    private String used_eta3;
    private String used_eta4;
    private String used_machine1;
    private String used_machine2;
    private String used_machine3;
    private String used_machine4;
    private String used_other1;
    private List<ShopViewModel> dealer_shops;

    public String getUsed_eta1() {
        return used_eta1;
    }

    public void setUsed_eta1(String used_eta1) {
        this.used_eta1 = used_eta1;
    }

    public String getUsed_eta2() {
        return used_eta2;
    }

    public void setUsed_eta2(String used_eta2) {
        this.used_eta2 = used_eta2;
    }

    public String getUsed_eta3() {
        return used_eta3;
    }

    public void setUsed_eta3(String used_eta3) {
        this.used_eta3 = used_eta3;
    }

    public String getUsed_eta4() {
        return used_eta4;
    }

    public void setUsed_eta4(String used_eta4) {
        this.used_eta4 = used_eta4;
    }

    public String getUsed_machine1() {
        return used_machine1;
    }

    public void setUsed_machine1(String used_machine1) {
        this.used_machine1 = used_machine1;
    }

    public String getUsed_machine2() {
        return used_machine2;
    }

    public void setUsed_machine2(String used_machine2) {
        this.used_machine2 = used_machine2;
    }

    public String getUsed_machine3() {
        return used_machine3;
    }

    public void setUsed_machine3(String used_machine3) {
        this.used_machine3 = used_machine3;
    }

    public String getUsed_machine4() {
        return used_machine4;
    }

    public void setUsed_machine4(String used_machine4) {
        this.used_machine4 = used_machine4;
    }

    public String getUsed_other1() {
        return used_other1;
    }

    public void setUsed_other1(String used_other1) {
        this.used_other1 = used_other1;
    }

    public List<ShopViewModel> getDealer_shops() {
        return dealer_shops;
    }

    public void setDealer_shops(List<ShopViewModel> dealer_shops) {
        this.dealer_shops = dealer_shops;
    }

    public int getId() {
        return id;
    }

    public String getCreated() {
        return created;
    }

    public String getDescription() {
        return description;
    }

    public int getCategory() {
        return category;
    }

    public String getModified() {
        return modified;
    }

    public int getModeling() {
        return modeling;
    }

    public int getStatus() {
        return status;
    }

    public int getYear() {
        return year;
    }

    public String getApproved_by() {
        return approved_by;
    }

    public String getApproved_date() {
        return approved_date;
    }

    public String getBack_image_path() {
        return back_image_path;
    }

    public String getCondition() {
        return condition;
    }

    public int getType() {
        return type;
    }

    public String getCreated_by() {
        return created_by;
    }

    public String getCost() {
        return cost;
    }

    public String getDiscount() {
        return discount;
    }

    public String getDiscount_type() {
        return discount_type;
    }

    public String getFront_image_path() {
        return front_image_path;
    }

    public String getLeft_image_path() {
        return left_image_path;
    }

    public String getExtra_image1() {
        return extra_image1;
    }

    public void setExtra_image1(String extra_image1) {
        this.extra_image1 = extra_image1;
    }

    public String getExtra_image2() {
        return extra_image2;
    }

    public void setExtra_image2(String extra_image2) {
        this.extra_image2 = extra_image2;
    }

    public String getMachine_code() {
        return machine_code;
    }

    public String getModified_by() {
        return modified_by;
    }

    public String getRejected_by() {
        return rejected_by;
    }

    public String getRejected_comments() {
        return rejected_comments;
    }

    public String getContact_phone() {
        return contact_phone;
    }

    public String getRejected_date() {
        return rejected_date;
    }

    public String getRight_image_path() {
        return right_image_path;
    }

    public String getPost_type() {
        return post_type;
    }

    public String getTitle() {
        return title;
    }

    public String getUser() {
        return user;
    }

    public String getVin_code() {
        return vin_code;
    }

    public BuyViewModel[] getBuys() {
        return buys;
    }

    public RentViewModel[] getRents() {
        return rents;
    }

    public SaleViewModel[] getSales() {
        return sales;
    }

    public String getColor() {
        return color;
    }

    public String getContact_address() {
        return contact_address;
    }
    public String getContact_email() {
        return contact_email;
    }

    public String getPost_code() {
        return post_code;
    }

    public String getPost_sub_title() {
        return post_sub_title;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setBack_image_path(String back_image_path) {
        this.back_image_path = back_image_path;
    }

    public void setApproved_by(String approved_by) {
        this.approved_by = approved_by;
    }

    public void setApproved_date(String approved_date) {
        this.approved_date = approved_date;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public void setContact_email(String contact_email) {
        this.contact_email = contact_email;
    }

    public void setContact_phone(String contact_phone) {
        this.contact_phone = contact_phone;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public void setContact_address(String contact_address) {
        this.contact_address = contact_address;
    }

    public void setDiscount_type(String discount_type) {
        this.discount_type = discount_type;
    }

    public void setFront_image_path(String front_image_path) {
        this.front_image_path = front_image_path;
    }

    public void setLeft_image_path(String left_image_path) {
        this.left_image_path = left_image_path;
    }

    public void setMachine_code(String machine_code) {
        this.machine_code = machine_code;
    }

    public void setModeling(int modeling) {
        this.modeling = modeling;
    }

    public void setModified_by(String modified_by) {
        this.modified_by = modified_by;
    }

    public void setRight_image_path(String right_image_path) {
        this.right_image_path = right_image_path;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setRejected_by(String rejected_by) {
        this.rejected_by = rejected_by;
    }

    public void setPost_type(String post_type) {
        this.post_type = post_type;
    }

    public void setRejected_comments(String rejected_comments) {
        this.rejected_comments = rejected_comments;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRejected_date(String rejected_date) {
        this.rejected_date = rejected_date;
    }

    public void setBuys(BuyViewModel[] buys) {
        this.buys = buys;
    }

    public void setVin_code(String vin_code) {
        this.vin_code = vin_code;
    }

    public void setRents(RentViewModel[] rents) {
        this.rents = rents;
    }

    public void setSales(SaleViewModel[] sales) {
        this.sales = sales;
    }

    public void setPost_code(String post_code) {
        this.post_code = post_code;
    }

    public void setPost_sub_title(String post_sub_title) {
        this.post_sub_title = post_sub_title;
    }


}

