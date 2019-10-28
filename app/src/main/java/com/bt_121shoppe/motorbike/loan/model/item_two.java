package com.bt_121shoppe.motorbike.loan.model;

import android.os.Parcel;
import android.os.Parcelable;

public class item_two implements Parcelable {
    private String Loan_Term,Loan_RepaymentType,
    Number_institution,Monthly_AmountPaid_Institurion;
    private item_one itemOne;
    float Loan_Amount,Deposit_Amount;
    boolean Allow_visito_home,Buying_InsuranceProduct;

    public item_two(float loan_Amount, String loan_Term, String loan_RepaymentType, float deposit_Amount, boolean buying_InsuranceProduct, boolean allow_visito_home, String number_institution, String monthly_AmountPaid_Institurion, item_one itemOne) {
        Loan_Amount = loan_Amount;
        Loan_Term = loan_Term;
        Loan_RepaymentType = loan_RepaymentType;
        Deposit_Amount = deposit_Amount;
        Buying_InsuranceProduct = buying_InsuranceProduct;
        Allow_visito_home = allow_visito_home;
        Number_institution = number_institution;
        Monthly_AmountPaid_Institurion = monthly_AmountPaid_Institurion;
        this.itemOne = itemOne;
    }

    protected item_two(Parcel in) {
        Loan_Amount = in.readFloat();
        Loan_Term = in.readString();
        Loan_RepaymentType = in.readString();
        Deposit_Amount = in.readFloat();
        Buying_InsuranceProduct = in.readInt() == 1;
        Allow_visito_home = in.readInt() == 1;
        Number_institution = in.readString();
        Monthly_AmountPaid_Institurion = in.readString();
        itemOne = in.readParcelable(item_one.class.getClassLoader());
    }

    public static final Creator<item_two> CREATOR = new Creator<item_two>() {
        @Override
        public item_two createFromParcel(Parcel in) {
            return new item_two(in);
        }

        @Override
        public item_two[] newArray(int size) {
            return new item_two[size];
        }
    };

    public float getLoan_Amount() {
        return Loan_Amount;
    }

    public void setLoan_Amount(float loan_Amount) {
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

    public float getDeposit_Amount() {
        return Deposit_Amount;
    }

    public void setDeposit_Amount(float deposit_Amount) {
        Deposit_Amount = deposit_Amount;
    }

    public boolean getBuying_InsuranceProduct() {
        return Buying_InsuranceProduct;
    }

    public void setBuying_InsuranceProduct(boolean buying_InsuranceProduct) {
        Buying_InsuranceProduct = buying_InsuranceProduct;
    }

    public boolean getAllow_visito_home() {
        return Allow_visito_home;
    }

    public void setAllow_visito_home(boolean allow_visito_home) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    public item_one getItemOne() {
        return itemOne;
    }

    public void setItemOne(item_one itemOne) {
        this.itemOne = itemOne;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(Loan_Amount);
        dest.writeString(Loan_Term);
        dest.writeString(Loan_RepaymentType);
        dest.writeFloat(Deposit_Amount);
        dest.writeInt(Buying_InsuranceProduct?1:0);
        dest.writeInt(Allow_visito_home?1:0);
        dest.writeString(Number_institution);
        dest.writeString(Monthly_AmountPaid_Institurion);
        dest.writeParcelable(itemOne, flags);
    }
}
