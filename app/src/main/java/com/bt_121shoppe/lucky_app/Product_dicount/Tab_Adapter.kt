package com.bt_121shoppe.lucky_app.Product_dicount

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.bt_121shoppe.lucky_app.Fragment.contact_user
import com.bt_121shoppe.lucky_app.Fragment.user_post_list

class Tab_Adapter (fm: FragmentManager,private var tabcoun: Int):FragmentPagerAdapter(fm){

    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> return user_post_list()
            1 -> return contact_user()
            else -> return null
        }
    }
    override fun getCount(): Int {
        return tabcoun
    }

}