package com.bt_121shoppe.motorbike.loan.model;

import com.bt_121shoppe.motorbike.loan.Create_Load;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class loan_item implements Serializable{

    //field required
    @SerializedName("loan_amount")
    private float Loan_amount;
    @SerializedName("loan_interest_rate")
    private float Loan_interest_rate;
    @SerializedName("loan_duration")
    private int Loan_duration;
    @SerializedName("average_income")
    private float Average_income;
    @SerializedName("average_expense")
    private float Average_expense;
    @SerializedName("loan_status")
    private int Loan_status;
    @SerializedName("record_status")
    private int Record_status;
    @SerializedName("created_by")
    private int Created_by;
    @SerializedName("post")
    private int Post;

    //field not required
    @SerializedName("loan_to")
    private int Loan_to;
    @SerializedName("loan_purpose")
    private String Loan_purpose;
    @SerializedName("username")
    private String Username;
    @SerializedName("gender")
    private String Gender;
    @SerializedName("age")
    private int Age;
    @SerializedName("job")
    private String Job;
    @SerializedName("telephone")
    private String Telephone;
    @SerializedName("address")
    private String Address;
    @SerializedName("state_id")
    private boolean State_id;
    @SerializedName("family_book")
    private boolean Family_book;
    @SerializedName("staff_id")
    private boolean Staff_id;
    @SerializedName("house_plant")
    private boolean House_plant;
//    @SerializedName("mfi")
//    private int Mfi;
    @SerializedName("modified")
    private String Modified;
    @SerializedName("modified_by")
    private int Modified_by;
    @SerializedName("Received date")
    private String Received_date;
    @SerializedName("Rejected by")
    private String Rejected_by;
    @SerializedName("rejected_comments")
    private String Rejected_comments;
    @SerializedName("received_date")
    private String Rejected_date;
    @SerializedName("province_id")
    private int Province_id;
    @SerializedName("district")
    private String District;
    @SerializedName("commune")
    private String Commune;
//    @SerializedName("village")
//    private String Village;
    @SerializedName("borrower_job")
    private String Borrower_job;
    @SerializedName("borrower_job_period")
    private int Borrower_job_period;
    @SerializedName("loan_repayment_type")
    private String Loan_repayment_type;
    @SerializedName("loan_deposit_amount")
    private float Loan_deposit_amount;
    @SerializedName("is_product_insurance")
    private boolean Is_product_insurance;
    @SerializedName("is_home_visit")
    private boolean Is_home_visit;
    @SerializedName("lending_intitution_owned")
    private int Lending_intitution_owned;
    @SerializedName("amount_paid_intitution")
    private float Amount_paid_intitution;
    @SerializedName("is_borrower_photo")
    private boolean Is_borrower_photo;
    @SerializedName("is_coborrower_idcard")
    private boolean Is_coborrower_idcard;
    @SerializedName("is_coborrower_familybook")
    private boolean Is_coborrower_familybook;
    @SerializedName("is_coborrower_photo")
    private boolean Is_coborrower_photo;
    @SerializedName("is_coborrower_payslip")
    private boolean Is_coborrower_payslip;
    @SerializedName("coborrower_relationship")
    private String mRelationship;
    @SerializedName("coborrower_job")
    private String mCoborrower_job;
    @SerializedName("coborrower_job_period")
    private int mCoborrower_job_period;
    public loan_item(float loan_amount, float loan_interest_rate, int loan_duration, float average_income,
                     float average_expense, int loan_status, int record_status, int created_by, int post,
                     int loan_to, int modified_by ,String loan_purpose, String username, String gender,
                     int age, String job, String telephone, String address, boolean state_id, boolean family_book,
                     boolean staff_id,  boolean is_borrower_photo,int province_id,String borrower_job, int borrower_job_period,
                     String loan_repayment_type, float loan_deposit_amount, boolean is_product_insurance,
                     boolean is_home_visit, int lending_intitution_owned, boolean is_coborrower_idcard,
                     boolean is_coborrower_familybook, boolean is_coborrower_photo,boolean is_coborrower_payslip,
                     String relationship,String coborrower_job,int mCoborrower_job_period) {
        Loan_amount = loan_amount;
        Loan_interest_rate = loan_interest_rate;
        Loan_duration = loan_duration;
        Average_income = average_income;
        Average_expense = average_expense;
        Loan_status = loan_status;
        Record_status = record_status;
        Created_by = created_by;
        Post = post;
        Loan_to = loan_to;
        Modified_by = modified_by;
        Loan_purpose = loan_purpose;
        Username = username;
        Gender = gender;
        Age = age;
        Job = job;
        Telephone = telephone;
        Address = address;
        State_id = state_id;
        Family_book = family_book;
        Staff_id = staff_id;
        Province_id = province_id;
        Borrower_job = borrower_job;
        Borrower_job_period = borrower_job_period;
        Loan_repayment_type = loan_repayment_type;
        Loan_deposit_amount = loan_deposit_amount;
        Is_product_insurance = is_product_insurance;
        Is_home_visit = is_home_visit;
        Lending_intitution_owned = lending_intitution_owned;
        Is_borrower_photo = is_borrower_photo;
        Is_coborrower_idcard = is_coborrower_idcard;
        Is_coborrower_familybook = is_coborrower_familybook;
        Is_coborrower_photo = is_coborrower_photo;
        Is_coborrower_payslip = is_coborrower_payslip;
        mRelationship = relationship;
        mCoborrower_job = coborrower_job;
        mCoborrower_job_period = mCoborrower_job_period;
    }

    public loan_item(float loan_amount, float loan_interest_rate, int loan_duration, float average_income, float average_expense, int created_by, int post, int loan_status, int record_status) {
        Loan_amount = loan_amount;
        Loan_interest_rate = loan_interest_rate;
        Loan_duration = loan_duration;
        Average_income = average_income;
        Average_expense = average_expense;
        Created_by = created_by;
        Post = post;
        Loan_status = loan_status;
        Record_status = record_status;
    }

    public int getRecord_status() {
        return Record_status;
    }

    public void setRecord_status(int record_status) {
        Record_status = record_status;
    }

    public int getLoan_status() {
        return Loan_status;
    }

    public void setLoan_status(int loan_status) {
        Loan_status = loan_status;
    }

    public float getLoan_amount() {
        return Loan_amount;
    }

    public void setLoan_amount(float loan_amount) {
        Loan_amount = loan_amount;
    }

    public float getLoan_interest_rate() {
        return Loan_interest_rate;
    }

    public void setLoan_interest_rate(float loan_interest_rate) {
        Loan_interest_rate = loan_interest_rate;
    }

    public int getLoan_duration() {
        return Loan_duration;
    }

    public void setLoan_duration(int loan_duration) {
        Loan_duration = loan_duration;
    }

    public float getAverage_income() {
        return Average_income;
    }

    public void setAverage_income(float average_income) {
        Average_income = average_income;
    }

    public float getAverage_expense() {
        return Average_expense;
    }

    public void setAverage_expense(float average_expense) {
        Average_expense = average_expense;
    }

    public int getCreated_by() {
        return Created_by;
    }

    public void setCreated_by(int created_by) {
        Created_by = created_by;
    }

    public int getPost() {
        return Post;
    }

    public void setPost(int post) {
        Post = post;
    }
}
