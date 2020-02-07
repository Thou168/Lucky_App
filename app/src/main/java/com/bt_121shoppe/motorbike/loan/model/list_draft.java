package com.bt_121shoppe.motorbike.loan.model;

import com.google.gson.annotations.SerializedName;

class list_draft extends draft_Item {
    @SerializedName("wallpaper_image")
    private String img;

    public list_draft(float loan_amount, float loan_interest_rate, int loan_duration, float average_income, float average_expense,
                      int created_by, int post, int loan_status, int record_status) {
        super(loan_amount, loan_interest_rate, loan_duration, average_income, average_expense, created_by, post, loan_status, record_status);
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
