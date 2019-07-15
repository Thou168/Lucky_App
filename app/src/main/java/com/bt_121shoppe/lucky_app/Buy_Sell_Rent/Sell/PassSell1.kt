package com.bt_121shoppe.lucky_app.Buy_Sell_Rent.Sell

import android.content.Context
import androidx.fragment.app.Fragment
import com.bt_121shoppe.lucky_app.Buy_Sell_Rent.Sell_Main1

abstract class PassSell1: Fragment(){
    lateinit var BACK_SELL: Sell_Main1

    override fun onAttach(context: Context) {
        super.onAttach(context)
        BACK_SELL = context as Sell_Main1
    }
}