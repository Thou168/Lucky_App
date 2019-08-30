package com.bt_121shoppe.motorbike.Fragment

import android.content.Context
import androidx.fragment.app.Fragment
import com.bt_121shoppe.motorbike.useraccount.User_post

abstract class Intent_data: Fragment(){
    lateinit var ACTIVITY: User_post

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ACTIVITY = context as User_post
    }
}