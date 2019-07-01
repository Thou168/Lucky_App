package com.example.lucky_app.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.lucky_app.Fragment.Fragment_home
import com.example.lucky_app.Fragment.user_post_list

class Tab_Adapter_Acc (fm: FragmentManager, private var tabcoun: Int):FragmentPagerAdapter(fm){

    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> return account_user_list()
            1 -> return account_like_list()
            else -> return null
        }
    }
    override fun getCount(): Int {
        return tabcoun
    }

}