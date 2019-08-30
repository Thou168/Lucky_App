package com.bt_121shoppe.motorbike.Product_dicount

import android.content.Context
import androidx.fragment.app.Fragment
import com.bt_121shoppe.motorbike.Product_New_Post.Detail_New_Post

abstract class Passtocontact: Fragment(){
    lateinit var ACTIVITY: Detail_New_Post

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ACTIVITY = context as Detail_New_Post
    }
}