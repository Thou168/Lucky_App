package com.bt_121shoppe.lucky_app.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bt_121shoppe.lucky_app.R

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [contact_user]interface.
 */
class contact_user: Intent_data() {

    var user_id:Int=0
    var username:String=""
    var password:String=""
    var encode=""
    var phone: TextView? = null
    var email: TextView? = null

    @SuppressLint("WrongConstant")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.contact_user, container, false)

        val tvphone = ACTIVITY.intent.getStringExtra("Phone")
        phone = view.findViewById<TextView>(R.id.phone)
        phone!!.text = tvphone
        email = view.findViewById(R.id.email)
        if(ACTIVITY.intent.getStringExtra("Email") == null){
            email!!.text = "Null"
        }else
        email!!.text = ACTIVITY.intent.getStringExtra("Email")

        return view
    }
}
