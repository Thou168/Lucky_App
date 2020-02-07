package com.bt_121shoppe.motorbike.loan.model;

import com.bt_121shoppe.motorbike.Api.api.model.Item_loan;
import com.google.gson.annotations.SerializedName;

public class draft_Item extends loan_item {
    public draft_Item(float loan_amount, float loan_interest_rate, int loan_duration, float average_income, float average_expense,
                      int created_by, int post, int loan_status, int record_status) {
        super(loan_amount, loan_interest_rate, loan_duration, average_income, average_expense, created_by, post, loan_status, record_status);
    }
}
