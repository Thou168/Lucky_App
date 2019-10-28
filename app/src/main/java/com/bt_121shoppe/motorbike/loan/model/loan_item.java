package com.bt_121shoppe.motorbike.loan.model;

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
    @SerializedName("name")
    private String Name;
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
    @SerializedName("district")
    private String Distrmict;
    @SerializedName("commune")
    private String Commune;
    @SerializedName("village")
    private String Village;
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
    @SerializedName("is_coborrower")
    private boolean mIs_Co_borrower;
    public loan_item(float loan_amount, float loan_interest_rate, int loan_duration, float average_income,
                     float average_expense, int loan_status, int record_status, int created_by, int post,
                     int loan_to, int modified_by ,String loan_purpose, String username, String gender,
                     int age, String job, String telephone, String address,String distrmict,String commune,String village, boolean state_id, boolean family_book,
                     boolean staff_id,  boolean is_borrower_photo,int province_id,String borrower_job, int borrower_job_period,
                     String loan_repayment_type, float loan_deposit_amount, boolean is_product_insurance,
                     boolean is_home_visit, int lending_intitution_owned, boolean is_coborrower_idcard,
                     boolean is_coborrower_familybook, boolean is_coborrower_photo,boolean is_coborrower_payslip,
                     String relationship,String coborrower_job,int mcoborrower_job_period,boolean mis_Co_borrower,float amount_paid_intitution) {
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
        Distrmict = distrmict;
        Commune = commune;
        Village = village;
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
        mCoborrower_job_period = mcoborrower_job_period;
        mIs_Co_borrower = mis_Co_borrower;
        Amount_paid_intitution = amount_paid_intitution;
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

    public boolean ismIs_Co_borrower() {
        return mIs_Co_borrower;
    }

    public void setmIs_Co_borrower(boolean mIs_Co_borrower) {
        this.mIs_Co_borrower = mIs_Co_borrower;
    }

    public int getLoan_to() {
        return Loan_to;
    }

    public void setLoan_to(int loan_to) {
        Loan_to = loan_to;
    }

    public String getLoan_purpose() {
        return Loan_purpose;
    }

    public void setLoan_purpose(String loan_purpose) {
        Loan_purpose = loan_purpose;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public String getJob() {
        return Job;
    }

    public void setJob(String job) {
        Job = job;
    }

    public String getTelephone() {
        return Telephone;
    }

    public void setTelephone(String telephone) {
        Telephone = telephone;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getDistrmict() {
        return Distrmict;
    }

    public void setDistrmict(String distrmict) {
        Distrmict = distrmict;
    }

    public String getCommune() {
        return Commune;
    }

    public void setCommune(String commune) {
        Commune = commune;
    }

    public String getVillage() {
        return Village;
    }

    public void setVillage(String village) {
        Village = village;
    }

    public boolean isState_id() {
        return State_id;
    }

    public void setState_id(boolean state_id) {
        State_id = state_id;
    }

    public boolean isFamily_book() {
        return Family_book;
    }

    public void setFamily_book(boolean family_book) {
        Family_book = family_book;
    }

    public boolean isStaff_id() {
        return Staff_id;
    }

    public void setStaff_id(boolean staff_id) {
        Staff_id = staff_id;
    }

    public boolean isHouse_plant() {
        return House_plant;
    }

    public void setHouse_plant(boolean house_plant) {
        House_plant = house_plant;
    }

    public String getModified() {
        return Modified;
    }

    public void setModified(String modified) {
        Modified = modified;
    }

    public int getModified_by() {
        return Modified_by;
    }

    public void setModified_by(int modified_by) {
        Modified_by = modified_by;
    }

    public String getReceived_date() {
        return Received_date;
    }

    public void setReceived_date(String received_date) {
        Received_date = received_date;
    }

    public String getRejected_by() {
        return Rejected_by;
    }

    public void setRejected_by(String rejected_by) {
        Rejected_by = rejected_by;
    }

    public String getRejected_comments() {
        return Rejected_comments;
    }

    public void setRejected_comments(String rejected_comments) {
        Rejected_comments = rejected_comments;
    }

    public String getRejected_date() {
        return Rejected_date;
    }

    public void setRejected_date(String rejected_date) {
        Rejected_date = rejected_date;
    }

    public int getProvince_id() {
        return Province_id;
    }

    public void setProvince_id(int province_id) {
        Province_id = province_id;
    }

    public String getBorrower_job() {
        return Borrower_job;
    }

    public void setBorrower_job(String borrower_job) {
        Borrower_job = borrower_job;
    }

    public int getBorrower_job_period() {
        return Borrower_job_period;
    }

    public void setBorrower_job_period(int borrower_job_period) {
        Borrower_job_period = borrower_job_period;
    }

    public String getLoan_repayment_type() {
        return Loan_repayment_type;
    }

    public void setLoan_repayment_type(String loan_repayment_type) {
        Loan_repayment_type = loan_repayment_type;
    }

    public float getLoan_deposit_amount() {
        return Loan_deposit_amount;
    }

    public void setLoan_deposit_amount(float loan_deposit_amount) {
        Loan_deposit_amount = loan_deposit_amount;
    }

    public boolean isIs_product_insurance() {
        return Is_product_insurance;
    }

    public void setIs_product_insurance(boolean is_product_insurance) {
        Is_product_insurance = is_product_insurance;
    }

    public boolean isIs_home_visit() {
        return Is_home_visit;
    }

    public void setIs_home_visit(boolean is_home_visit) {
        Is_home_visit = is_home_visit;
    }

    public int getLending_intitution_owned() {
        return Lending_intitution_owned;
    }

    public void setLending_intitution_owned(int lending_intitution_owned) {
        Lending_intitution_owned = lending_intitution_owned;
    }

    public float getAmount_paid_intitution() {
        return Amount_paid_intitution;
    }

    public void setAmount_paid_intitution(float amount_paid_intitution) {
        Amount_paid_intitution = amount_paid_intitution;
    }

    public boolean isIs_borrower_photo() {
        return Is_borrower_photo;
    }

    public void setIs_borrower_photo(boolean is_borrower_photo) {
        Is_borrower_photo = is_borrower_photo;
    }

    public boolean isIs_coborrower_idcard() {
        return Is_coborrower_idcard;
    }

    public void setIs_coborrower_idcard(boolean is_coborrower_idcard) {
        Is_coborrower_idcard = is_coborrower_idcard;
    }

    public boolean isIs_coborrower_familybook() {
        return Is_coborrower_familybook;
    }

    public void setIs_coborrower_familybook(boolean is_coborrower_familybook) {
        Is_coborrower_familybook = is_coborrower_familybook;
    }

    public boolean isIs_coborrower_photo() {
        return Is_coborrower_photo;
    }

    public void setIs_coborrower_photo(boolean is_coborrower_photo) {
        Is_coborrower_photo = is_coborrower_photo;
    }

    public boolean isIs_coborrower_payslip() {
        return Is_coborrower_payslip;
    }

    public void setIs_coborrower_payslip(boolean is_coborrower_payslip) {
        Is_coborrower_payslip = is_coborrower_payslip;
    }

    public String getmRelationship() {
        return mRelationship;
    }

    public void setmRelationship(String mRelationship) {
        this.mRelationship = mRelationship;
    }

    public String getmCoborrower_job() {
        return mCoborrower_job;
    }

    public void setmCoborrower_job(String mCoborrower_job) {
        this.mCoborrower_job = mCoborrower_job;
    }

    public int getmCoborrower_job_period() {
        return mCoborrower_job_period;
    }

    public void setmCoborrower_job_period(int mCoborrower_job_period) {
        this.mCoborrower_job_period = mCoborrower_job_period;
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
