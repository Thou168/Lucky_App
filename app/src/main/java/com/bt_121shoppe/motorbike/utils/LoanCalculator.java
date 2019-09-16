package com.bt_121shoppe.motorbike.utils;

public class LoanCalculator {
    public static double getLoanMonthPayment(double salePrice,double deposit_loan, double rate, int month){
        //Monthly Payment=(SalePrice-deposit)/((1-(1+r)^-n)/r)
        double convertRate=rate/100;
        return (salePrice-deposit_loan)/((1-Math.pow(1+convertRate,-month))/convertRate);
    }
}
