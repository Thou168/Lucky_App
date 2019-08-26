package com.bt_121shoppe.lucky_app.Api.api.model;

import com.google.gson.annotations.SerializedName;

public class Item_loan extends LikebyUser{

//    @SerializedName("loan_to")
    private int loan_to;
//    @SerializedName("loan_amount")
    private String loan_amount;
//    @SerializedName("loan_interest_rate")
    private String loan_interest_rate;
//    @SerializedName("loan_duration")
    private int loan_duration;
//    @SerializedName("loan_purpose")
    private String loan_purpose;
//    @SerializedName("loan_status")
    private int loan_status;
//    @SerializedName("record_status")
    private int loan_record_status;
//    @SerializedName("username")
    private String username;
//    @SerializedName("gender")
    private String gender;
//    @SerializedName("age")
    private int age;
//    @SerializedName("job")
    private String job;
//    @SerializedName("average_income")
    private String average_income;
//    @SerializedName("average_expense")
    private String average_expense;
//    @SerializedName("telephone")
    private String telephone;
//    @SerializedName("address")
    private String address;
//    @SerializedName("state_id")
    private boolean state_id;
//    @SerializedName("family_book")
    private boolean family_book;
//    @SerializedName("staff_id")
    private boolean staff_id;
//    @SerializedName("house_plant")
    private boolean house_plant;
//    @SerializedName("mfi")
    private String mfi = null;
//    @SerializedName("created")
    private String loan_created = getCreated();
//    @SerializedName("created_by")
    private int loan_created_by = getCreated_by();
//    @SerializedName("modified")
    private String loan_modified = getModified();
//    @SerializedName("modified_by")
    private String loan_modified_by = getModified_by();
//    @SerializedName("received_date")
    private String loan_received_date = getReceived_date();
//    @SerializedName("received_by")
    private String loan_received_by = getReceived_by();
//    @SerializedName("rejected_date")
    private String loan_rejected_date = getRejected_date();
//    @SerializedName("rejected_by")
    private String loan_rejected_by = getRejected_by();
//    @SerializedName("rejected_comments")
    private String loan_rejected_comments = getRejected_comments();
//    @SerializedName("post")
//    private int loan_post = getPost();

    public Item_loan(int loan_status, int loan_record_status) {
        this.loan_status = loan_status;
        this.loan_record_status = loan_record_status;
    }

    public Item_loan(int loan_to, String loan_amount, String loan_interest_rate, int loan_duration, String loan_purpose, int loan_status, int loan_record_status, String username, String gender, int age, String job, String average_income, String average_expense, String telephone, String address, boolean state_id, boolean family_book, boolean staff_id, boolean house_plant, String mfi, String loan_created, int loan_created_by, String modified, String modified_by, String received_date, String received_by, String rejected_date, String rejected_by, String rejected_comments, int loan_post) {
        this.loan_to = loan_to;
        this.loan_amount = loan_amount;
        this.loan_interest_rate = loan_interest_rate;
        this.loan_duration = loan_duration;
        this.loan_purpose = loan_purpose;
        this.loan_status = loan_status;
        this.loan_record_status = loan_record_status;
        this.username = username;
        this.gender = gender;
        this.age = age;
        this.job = job;
        this.average_income = average_income;
        this.average_expense = average_expense;
        this.telephone = telephone;
        this.address = address;
        this.state_id = state_id;
        this.family_book = family_book;
        this.staff_id = staff_id;
        this.house_plant = house_plant;
        this.mfi = mfi;
        this.loan_created = loan_created;
        this.loan_created_by = loan_created_by;
        this.loan_modified = modified;
        this.loan_modified_by = modified_by;
        this.loan_received_date = received_date;
        this.loan_received_by = received_by;
        this.loan_rejected_date = rejected_date;
        this.loan_rejected_by = rejected_by;
        this.loan_rejected_comments = rejected_comments;
//        this.loan_post = loan_post;
        this.setRecord_status(loan_record_status);
        this.setCreated(String.valueOf(loan_created_by));
        this.setPost(loan_post);
        this.setCreated_by(loan_created_by);
    }

    public int getLoan_to() {
        return loan_to;
    }

    public void setLoan_to(int loan_to) {
        this.loan_to = loan_to;
    }

    public String getLoan_amount() {
        return loan_amount;
    }

    public void setLoan_amount(String loan_amount) {
        this.loan_amount = loan_amount;
    }

    public String getLoan_interest_rate() {
        return loan_interest_rate;
    }

    public void setLoan_interest_rate(String loan_interest_rate) {
        this.loan_interest_rate = loan_interest_rate;
    }

    public int getLoan_duration() {
        return loan_duration;
    }

    public void setLoan_duration(int loan_duration) {
        this.loan_duration = loan_duration;
    }

    public String getLoan_purpose() {
        return loan_purpose;
    }

    public void setLoan_purpose(String loan_purpose) {
        this.loan_purpose = loan_purpose;
    }

    public int getLoan_status() {
        return loan_status;
    }

    public void setLoan_status(int loan_status) {
        this.loan_status = loan_status;
    }

    public int getLoan_record_status() {
        return loan_record_status;
    }

    public void setLoan_record_status(int loan_record_status) {
        this.loan_record_status = loan_record_status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getAverage_income() {
        return average_income;
    }

    public void setAverage_income(String average_income) {
        this.average_income = average_income;
    }

    public String getAverage_expense() {
        return average_expense;
    }

    public void setAverage_expense(String average_expense) {
        this.average_expense = average_expense;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isState_id() {
        return state_id;
    }

    public void setState_id(boolean state_id) {
        this.state_id = state_id;
    }

    public boolean isFamily_book() {
        return family_book;
    }

    public void setFamily_book(boolean family_book) {
        this.family_book = family_book;
    }

    public boolean isStaff_id() {
        return staff_id;
    }

    public void setStaff_id(boolean staff_id) {
        this.staff_id = staff_id;
    }

    public boolean isHouse_plant() {
        return house_plant;
    }

    public void setHouse_plant(boolean house_plant) {
        this.house_plant = house_plant;
    }

    public String getMfi() {
        return mfi;
    }

    public void setMfi(String mfi) {
        this.mfi = mfi;
    }

    public String getLoan_created() {
        return loan_created;
    }

    public void setLoan_created(String loan_created) {
        this.loan_created = loan_created;
    }

    public float getLoan_created_by() {
        return loan_created_by;
    }

    public void setLoan_created_by(int loan_created_by) {
        this.loan_created_by = loan_created_by;
    }

    @Override
    public String getModified() {
        return loan_modified;
    }

    @Override
    public void setModified(String modified) {
        this.loan_modified = modified;
    }

    @Override
    public String getModified_by() {
        return loan_modified_by;
    }

    @Override
    public void setModified_by(String modified_by) {
        this.loan_modified_by = modified_by;
    }

    public String getReceived_date() {
        return loan_received_date;
    }

    public void setReceived_date(String received_date) {
        this.loan_received_date = received_date;
    }

    public String getReceived_by() {
        return loan_received_by;
    }

    public void setReceived_by(String received_by) {
        this.loan_received_by = received_by;
    }

    @Override
    public String getRejected_date() {
        return loan_rejected_date;
    }

    @Override
    public void setRejected_date(String rejected_date) {
        this.loan_rejected_date = rejected_date;
    }

    @Override
    public String getRejected_by() {
        return loan_rejected_by;
    }

    @Override
    public void setRejected_by(String rejected_by) {
        this.loan_rejected_by = rejected_by;
    }

    @Override
    public String getRejected_comments() {
        return loan_rejected_comments;
    }

    @Override
    public void setRejected_comments(String rejected_comments) {
        this.loan_rejected_comments = rejected_comments;
    }

//    public float getLoan_post() {
//        return loan_post;
//    }
//
//    public void setLoan_post(float loan_post) {
//        this.loan_post = loan_post;
//    }
}
