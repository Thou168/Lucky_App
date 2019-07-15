package com.bt_121shoppe.lucky_app.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bt_121shoppe.lucky_app.R

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [contact_user]interface.
 */
class contact_user: Fragment() {

    @SuppressLint("WrongConstant")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.contact_user, container, false)


//        val tvphone = ACTIVITY.intent.getStringExtra("Phone")
//        val phone = view.findViewById<TextView>(R.id.phone)
//        phone.text = tvphone

        return view
    }
}
