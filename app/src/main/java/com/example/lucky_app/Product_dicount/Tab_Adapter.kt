package com.example.lucky_app.Product_dicount

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.lucky_app.Fragment.Fragment_home
import com.example.lucky_app.Fragment.user_post_list

class Tab_Adapter (fm: FragmentManager,private var tabcoun: Int):FragmentPagerAdapter(fm){

    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> return user_post_list()
            1 -> return Fragment_home()
            else -> return null
        }
    }
    override fun getCount(): Int {
        return tabcoun
    }

}