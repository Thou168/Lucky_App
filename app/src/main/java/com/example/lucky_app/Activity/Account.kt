package com.example.lucky_app.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.example.lucky_app.Edit_Account.Edit_account
import com.example.lucky_app.Fragment.Tab_Adapter_Acc
import com.example.lucky_app.R
import com.example.lucky_app.Setting.Setting
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout

class Account : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)


        val bnavigation = findViewById<BottomNavigationView>(R.id.bnaviga)
        bnavigation.menu.getItem(4).isChecked = true
        bnavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    val intent = Intent(this@Account,Home::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
                }
                R.id.notification -> {
                    val intent = Intent(this@Account,Notification::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
                }
                R.id.camera ->{
                    val intent = Intent(this@Account,Camera::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
                }
                R.id.message -> {
                    val intent = Intent(this@Account,Message::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
                }
                R.id.account ->{
//                    val intent = Intent(this@Account,Account::class.java)
//                    startActivity(intent)
                }
            }
            true
        }

        val setting = findViewById<ImageButton>(R.id.btn_setting)
        setting.setOnClickListener {
            val intent = Intent(this@Account , Setting::class.java)
            startActivity(intent)
            Toast.makeText(this@Account,"Setting", Toast.LENGTH_SHORT).show()
        }

        val account_edit = findViewById<ImageButton>(R.id.btn_edit)
        account_edit.setOnClickListener {
            Toast.makeText(this@Account,"Home Edit", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@Account, Edit_account::class.java);
            startActivity(intent)
        }
        val tab = findViewById<TabLayout>(R.id.tab)
        val pager = findViewById<ViewPager>(R.id.pager)

        tab.addTab(tab.newTab().setText("Post"))
        tab.addTab(tab.newTab().setText("Like"))
        val adapter = Tab_Adapter_Acc(supportFragmentManager,tab.tabCount)
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


    } // create
}
