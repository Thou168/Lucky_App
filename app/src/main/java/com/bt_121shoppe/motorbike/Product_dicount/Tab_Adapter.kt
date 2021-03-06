package com.bt_121shoppe.motorbike.Product_dicount

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.bt_121shoppe.motorbike.Fragment.contact_user2
import com.bt_121shoppe.motorbike.Fragment.user_post_list

class Tab_Adapter (fm: FragmentManager,private var tabcoun: Int):FragmentPagerAdapter(fm){

    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> return user_post_list()
            1 -> return contact_user2()
            else -> return null
        }
    }
    override fun getCount(): Int {
        return tabcoun
    }

}