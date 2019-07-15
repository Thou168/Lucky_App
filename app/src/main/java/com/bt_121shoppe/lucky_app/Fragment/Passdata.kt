package com.bt_121shoppe.lucky_app.Fragment

import android.content.Context
import androidx.fragment.app.Fragment
import com.bt_121shoppe.lucky_app.useraccount.User_post

abstract class Passdata: Fragment(){
    lateinit var ACTIVITY: User_post

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ACTIVITY = context as User_post
    }
}