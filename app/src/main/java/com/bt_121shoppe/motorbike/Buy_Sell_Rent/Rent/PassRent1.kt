package com.bt_121shoppe.motorbike.Buy_Sell_Rent.Sell

import android.content.Context
import androidx.fragment.app.Fragment
import com.bt_121shoppe.motorbike.Buy_Sell_Rent.Rent_Main1

abstract class PassRent1: Fragment(){
    lateinit var BACK_SELL: Rent_Main1

    override fun onAttach(context: Context) {
        super.onAttach(context)
        BACK_SELL = context as Rent_Main1
    }
}