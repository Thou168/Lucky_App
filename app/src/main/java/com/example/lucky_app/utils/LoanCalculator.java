package com.example.lucky_app.utils;

public class LoanCalculator {
    public static double getLoanMonthPayment(double salePrice,double rate,int month){
        //Monthly Payment=SalePrice/((1-(1+r)^-n)/r)
        double convertRate=rate/100;
        return salePrice/((1-Math.pow(1+convertRate,-month))/convertRate);
    }
}
