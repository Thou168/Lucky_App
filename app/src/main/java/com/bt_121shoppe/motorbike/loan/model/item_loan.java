package com.bt_121shoppe.motorbike.loan.model;

import com.google.gson.annotations.SerializedName;

public class item_loan {
    @SerializedName("username")
    private String Name;
    @SerializedName("telephone")
    private String Phone_Number;
    @SerializedName("address")
    private String Address;
    @SerializedName("borrower_job")
    private String Job;
    @SerializedName("is_coborrower")
    private String Co_borrower;
    @SerializedName("coborrower_relationship")
    private String Relationship;
    @SerializedName("coborrower_job")
    private String Co_borrower_Job;
    @SerializedName("average_income")
    private String Total_Income;
    @SerializedName("average_expense")
    private String Total_Expense;
    @SerializedName("borrower_job_period")
    private int Job_Period;
    @SerializedName("coborrower_job_period")
    private int Co_Job_Period;
    private  int Index;
    @SerializedName("loan_amount")
    private String Loan_Amount;
    @SerializedName("loan_amount")
    private String Loan_Term;
    private String Loan_RepaymentType;
    private String Deposit_Amount;
    private String Buying_InsuranceProduct;
    private String Allow_visito_home;
    private String Number_institution;
    private String Monthly_AmountPaid_Institurion;
    private String ID_card;
    private String Family_book;
    private String Photos;
    private String Employment_card;
    private String ID_card1;
    private String Family_book1;
    private String Photos1;
    private String Employment_card1;

    public item_loan(String name, String phone_Number, String address, String job, String co_borrower,
                     String relationship, String co_borrower_Job, String total_Income, String total_Expense,
                     int job_Period, int co_Job_Period, int index, String loan_Amount,
                     String loan_Term, String loan_RepaymentType, String deposit_Amount, String buying_InsuranceProduct,
                     String allow_visito_home, String number_institution, String monthly_AmountPaid_Institurion,
                     String ID_card, String family_book, String photos, String employment_card, String ID_card1,
                     String family_book1, String photos1, String employment_card1) {
        Name = name;
        Phone_Number = phone_Number;
        Address = address;
        Job = job;
        Co_borrower = co_borrower;
        Relationship = relationship;
        Co_borrower_Job = co_borrower_Job;
        Total_Income = total_Income;
        Total_Expense = total_Expense;
        Job_Period = job_Period;
        Co_Job_Period = co_Job_Period;
        Index = index;
        Loan_Amount = loan_Amount;
        Loan_Term = loan_Term;
        Loan_RepaymentType = loan_RepaymentType;
        Deposit_Amount = deposit_Amount;
        Buying_InsuranceProduct = buying_InsuranceProduct;
        Allow_visito_home = allow_visito_home;
        Number_institution = number_institution;
        Monthly_AmountPaid_Institurion = monthly_AmountPaid_Institurion;
        this.ID_card = ID_card;
        Family_book = family_book;
        Photos = photos;
        Employment_card = employment_card;
        this.ID_card1 = ID_card1;
        Family_book1 = family_book1;
        Photos1 = photos1;
        Employment_card1 = employment_card1;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone_Number() {
        return Phone_Number;
    }

    public void setPhone_Number(String phone_Number) {
        Phone_Number = phone_Number;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getJob() {
        return Job;
    }

    public void setJob(String job) {
        Job = job;
    }

    public String getCo_borrower() {
        return Co_borrower;
    }

    public void setCo_borrower(String co_borrower) {
        Co_borrower = co_borrower;
    }

    public String getRelationship() {
        return Relationship;
    }

    public void setRelationship(String relationship) {
        Relationship = relationship;
    }

    public String getCo_borrower_Job() {
        return Co_borrower_Job;
    }

    public void setCo_borrower_Job(String co_borrower_Job) {
        Co_borrower_Job = co_borrower_Job;
    }

    public String getTotal_Income() {
        return Total_Income;
    }

    public void setTotal_Income(String total_Income) {
        Total_Income = total_Income;
    }

    public String getTotal_Expense() {
        return Total_Expense;
    }

    public void setTotal_Expense(String total_Expense) {
        Total_Expense = total_Expense;
    }

    public int getJob_Period() {
        return Job_Period;
    }

    public void setJob_Period(int job_Period) {
        Job_Period = job_Period;
    }

    public int getCo_Job_Period() {
        return Co_Job_Period;
    }

    public void setCo_Job_Period(int co_Job_Period) {
        Co_Job_Period = co_Job_Period;
    }

    public int getIndex() {
        return Index;
    }

    public void setIndex(int index) {
        Index = index;
    }

    public String getLoan_Amount() {
        return Loan_Amount;
    }

    public void setLoan_Amount(String loan_Amount) {
        Loan_Amount = loan_Amount;
    }

    public String getLoan_Term() {
        return Loan_Term;
    }

    public void setLoan_Term(String loan_Term) {
        Loan_Term = loan_Term;
    }

    public String getLoan_RepaymentType() {
        return Loan_RepaymentType;
    }

    public void setLoan_RepaymentType(String loan_RepaymentType) {
        Loan_RepaymentType = loan_RepaymentType;
    }

    public String getDeposit_Amount() {
        return Deposit_Amount;
    }

    public void setDeposit_Amount(String deposit_Amount) {
        Deposit_Amount = deposit_Amount;
    }

    public String getBuying_InsuranceProduct() {
        return Buying_InsuranceProduct;
    }

    public void setBuying_InsuranceProduct(String buying_InsuranceProduct) {
        Buying_InsuranceProduct = buying_InsuranceProduct;
    }

    public String getAllow_visito_home() {
        return Allow_visito_home;
    }

    public void setAllow_visito_home(String allow_visito_home) {
        Allow_visito_home = allow_visito_home;
    }

    public String getNumber_institution() {
        return Number_institution;
    }

    public void setNumber_institution(String number_institution) {
        Number_institution = number_institution;
    }

    public String getMonthly_AmountPaid_Institurion() {
        return Monthly_AmountPaid_Institurion;
    }

    public void setMonthly_AmountPaid_Institurion(String monthly_AmountPaid_Institurion) {
        Monthly_AmountPaid_Institurion = monthly_AmountPaid_Institurion;
    }

    public String getID_card() {
        return ID_card;
    }

    public void setID_card(String ID_card) {
        this.ID_card = ID_card;
    }

    public String getFamily_book() {
        return Family_book;
    }

    public void setFamily_book(String family_book) {
        Family_book = family_book;
    }

    public String getPhotos() {
        return Photos;
    }

    public void setPhotos(String photos) {
        Photos = photos;
    }

    public String getEmployment_card() {
        return Employment_card;
    }

    public void setEmployment_card(String employment_card) {
        Employment_card = employment_card;
    }

    public String getID_card1() {
        return ID_card1;
    }

    public void setID_card1(String ID_card1) {
        this.ID_card1 = ID_card1;
    }

    public String getFamily_book1() {
        return Family_book1;
    }

    public void setFamily_book1(String family_book1) {
        Family_book1 = family_book1;
    }

    public String getPhotos1() {
        return Photos1;
    }

    public void setPhotos1(String photos1) {
        Photos1 = photos1;
    }

    public String getEmployment_card1() {
        return Employment_card1;
    }

    public void setEmployment_card1(String employment_card1) {
        Employment_card1 = employment_card1;
    }
}
