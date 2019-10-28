package com.bt_121shoppe.motorbike.loan.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;

public class item_one implements Parcelable {
    private String Name,Phone_Number,Job,Relationship,Co_borrower_Job,Net_Income,Province,District,Commune,Village;
    private int Job_Period,Co_Job_Period,Index,mProductId,mProvinceID,mLoanID;
    private float Total_Income,Total_Expense;
    private String Price;
    private boolean Co_borrower,FromLoan;
    public item_one(String name, String phone_Number, String province,String district,String commune,String village, String job, boolean co_borrower,int index, String relationship,
                    String co_borrower_Job, float total_Income, float total_Expense, String net_Income, int job_Period,
                    int co_Job_Period,int ProductId,int provinceID,String price,int mloanID,boolean fromLoan) {
        Name = name;
        Phone_Number = phone_Number;
        Province = province;
        District = district;
        Commune = commune;
        Village = village;
        Job = job;
        Co_borrower = co_borrower;
        Index = index;
        Relationship = relationship;
        Co_borrower_Job = co_borrower_Job;
        Total_Income = total_Income;
        Total_Expense = total_Expense;
        Net_Income = net_Income;
        Job_Period = job_Period;
        Co_Job_Period = co_Job_Period;
        mProductId = ProductId;
        mProvinceID = provinceID;
        Price = price;
        mLoanID = mloanID;
        FromLoan = fromLoan;
    }

    public int getmLoanID() {
        return mLoanID;
    }

    public void setmLoanID(int mLoanID) {
        this.mLoanID = mLoanID;
    }

    public boolean isFromLoan() {
        return FromLoan;
    }

    public void setFromLoan(boolean fromLoan) {
        FromLoan = fromLoan;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public int getmProvinceID() {
        return mProvinceID;
    }

    public void setmProvinceID(int mProvinceID) {
        this.mProvinceID = mProvinceID;
    }

    public boolean isCo_borrower() {
        return Co_borrower;
    }

    public static final Creator<item_one> CREATOR = new Creator<item_one>() {
        @Override
        public item_one createFromParcel(Parcel in) {
            return new item_one(in);
        }

        @Override
        public item_one[] newArray(int size) {
            return new item_one[size];
        }
    };

    public int getmProductId() {
        return mProductId;
    }

    public void setmProductId(int mProductId) {
        this.mProductId = mProductId;
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

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
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

    public String getJob() {
        return Job;
    }

    public int getIndex() {
        return Index;
    }

    public void setIndex(int index) {
        Index = index;
    }

    public void setJob(String job) {
        Job = job;
    }

    public boolean getCo_borrower() {
        return Co_borrower;
    }

    public void setCo_borrower(boolean co_borrower) {
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

    public float getTotal_Income() {
        return Total_Income;
    }

    public void setTotal_Income(float total_Income) {
        Total_Income = total_Income;
    }

    public float getTotal_Expense() {
        return Total_Expense;
    }

    public void setTotal_Expense(float total_Expense) {
        Total_Expense = total_Expense;
    }

    public String getNet_Income() {
        return Net_Income;
    }

    public void setNet_Income(String net_Income) {
        Net_Income = net_Income;
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

    @Override
    public int describeContents() {
        return 0;
    }

    protected item_one(Parcel in) {
        Name = in.readString();
        Phone_Number = in.readString();
        Province = in.readString();
        District = in.readString();
        Commune = in.readString();
        Village = in.readString();
        Job = in.readString();
        Co_borrower = in.readInt() == 1;
        Index = in.readInt();
        Relationship = in.readString();
        Co_borrower_Job = in.readString();
        Total_Income = in.readFloat();
        Total_Expense = in.readFloat();
        Net_Income = in.readString();
        Job_Period = in.readInt();
        Co_Job_Period = in.readInt();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Name);
        dest.writeString(Phone_Number);
        dest.writeString(Province);
        dest.writeString(District);
        dest.writeString(Commune);
        dest.writeString(Village);
        dest.writeString(Job);
        dest.writeInt(Co_borrower?1:0);
        dest.writeInt(Index);
        dest.writeString(Relationship);
        dest.writeString(Co_borrower_Job);
        dest.writeFloat(Total_Income);
        dest.writeFloat(Total_Expense);
        dest.writeString(Net_Income);
        dest.writeInt(Job_Period);
        dest.writeInt(Co_Job_Period);
    }
}
