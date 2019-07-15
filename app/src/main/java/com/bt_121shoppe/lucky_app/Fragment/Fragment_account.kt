package com.bt_121shoppe.lucky_app.Fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import com.bt_121shoppe.lucky_app.R
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import android.util.Base64
import android.util.Log

class Fragment_account : Fragment() {

    private var PRIVATE_MODE = 0
    var username=""
    var password=""
    var encodeAuth=""
    var API_ENDPOINT=""
    var pk=0
    var prefs: SharedPreferences?=null

    companion object {
        fun newInstance(): Fragment_account {
            var fragmentHome = Fragment_account()
            var args = Bundle()
            fragmentHome.arguments = args
            return fragmentHome
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_acount, container, false)

        val setting = view.findViewById<ImageButton>(R.id.btn_setting)
        setting.setOnClickListener {
            Toast.makeText(context,"Setting",Toast.LENGTH_SHORT).show()
        }

        val account_edit = view.findViewById<ImageButton>(R.id.btn_edit)
        account_edit.setOnClickListener {
            Toast.makeText(context,"Home Edit",Toast.LENGTH_SHORT).show()
        }
        val tab = view.findViewById<TabLayout>(R.id.tab)
        val pager = view.findViewById<ViewPager>(R.id.pager)

        tab.addTab(tab.newTab().setText("Post"))
        tab.addTab(tab.newTab().setText("Like"))
        val adapter = Tab_Adapter_Acc(childFragmentManager,tab.tabCount)
        pager.adapter = adapter
        pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tab))
        tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                pager.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {
            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        val preferences = this.activity!!.getSharedPreferences("Register", Context.MODE_PRIVATE)
        username=preferences.getString("name","")
        password=preferences.getString("password","")
        encodeAuth=getEncodedString(username,password)
        if (preferences.contains("token")) {
            pk = preferences.getInt("Pk", 0)
        } else if (preferences.contains("id")) {
            pk = preferences.getInt("id", 0)
        }
        Toast.makeText(context,username,Toast.LENGTH_SHORT).show()
        Log.d("Account",username)
        return view
    }

    fun getEncodedString(username: String,password:String):String{
        val userpass = "$username:$password"
        return Base64.encodeToString(userpass.toByteArray(),
                Base64.NO_WRAP)
    }
}
