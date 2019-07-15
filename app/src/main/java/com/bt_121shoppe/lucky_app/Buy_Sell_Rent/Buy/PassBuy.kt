package com.bt_121shoppe.lucky_app.Buy_Sell_Rent.Buy

import android.content.Context
import androidx.fragment.app.Fragment
import com.bt_121shoppe.lucky_app.Buy_Sell_Rent.New_Main1

abstract class PassBuy: Fragment(){
    lateinit var BACK_BUY: New_Main1

    override fun onAttach(context: Context) {
        super.onAttach(context)
        BACK_BUY = context as New_Main1
    }
}