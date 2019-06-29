package com.example.lucky_app.Buy_Sell_Rent.Sell

import android.content.Context
import androidx.fragment.app.Fragment
import com.example.lucky_app.Buy_Sell_Rent.New_Main1
import com.example.lucky_app.Buy_Sell_Rent.Sell_Main1
import com.example.lucky_app.Product_dicount.User_post

abstract class PassSell1: Fragment(){
    lateinit var BACK_SELL: Sell_Main1

    override fun onAttach(context: Context) {
        super.onAttach(context)
        BACK_SELL = context as Sell_Main1
    }
}