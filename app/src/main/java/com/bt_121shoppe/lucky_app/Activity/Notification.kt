package com.bt_121shoppe.lucky_app.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.bt_121shoppe.lucky_app.Login_Register.UserAccount
import com.bt_121shoppe.lucky_app.chats.ChatMainActivity
import com.bt_121shoppe.lucky_app.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class Notification : AppCompatActivity() {
//    internal abstract var items: ArrayList<Item>
//    val items: ArrayList<Item> = ArrayList()
//    internal abstract var ap: MyAdapter_notification

    private lateinit var back_noti: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
//        back_noti=findViewById<TextView>(R.id.back_notification)
//        back_noti.setOnClickListener{
//            finish()
//        }

//        val recyclerView_notification = findViewById<RecyclerView>(R.id.layout_notification)
//        val items = ArrayList<Item>()
//        items.addAll(Item.getList())
//        //  listview.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
//        recyclerView_notification.layoutManager = GridLayoutManager(this@Notification, 1) as RecyclerView.LayoutManager?
//        recyclerView_notification.adapter = MyAdapter_notification(items,"List")

        val sharedPref: SharedPreferences = getSharedPreferences("Register", Context.MODE_PRIVATE)
        val bnavigation = findViewById<BottomNavigationView>(R.id.bnaviga)
        bnavigation.menu.getItem(1).isChecked = true
        bnavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    val intent = Intent(this@Notification,Home::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
                }
                R.id.notification -> {
                    if (sharedPref.contains("token") || sharedPref.contains("id")) {
                        val intent = Intent(this@Notification, Notification::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }else{
                        val intent = Intent(this@Notification, UserAccount::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }
                }
                R.id.camera ->{
                    if (sharedPref.contains("token") || sharedPref.contains("id")) {
                        val intent = Intent(this@Notification, Camera::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }else{
                        val intent = Intent(this@Notification, UserAccount::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }
                }
                R.id.message -> {
                    if (sharedPref.contains("token") || sharedPref.contains("id")) {
                        val intent = Intent(this@Notification, ChatMainActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }else{
                        val intent = Intent(this@Notification, UserAccount::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }
                }
                R.id.account ->{
                    if (sharedPref.contains("token") || sharedPref.contains("id")) {
                        val intent = Intent(this@Notification, Account::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }else{
                        val intent = Intent(this@Notification, UserAccount::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }

                }
            }
            true
        }

    }
}
