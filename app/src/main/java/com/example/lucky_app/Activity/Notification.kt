package com.example.lucky_app.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lucky_app.Login_Register.UserAccount
import com.example.lucky_app.Product_New_Post.MyAdapter_notification
import com.example.lucky_app.R
import com.example.lucky_app.Startup.Item
import com.example.lucky_app.Startup.MyAdapter_list
import com.example.lucky_app.notifications.NotificationActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.ArrayList

class Notification : AppCompatActivity() {
//    internal abstract var items: ArrayList<Item>
//    val items: ArrayList<Item> = ArrayList()
//    internal abstract var ap: MyAdapter_notification

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        val recyclerView_notification = findViewById<RecyclerView>(R.id.layout_notification)
        val items = ArrayList<Item>()
        items.addAll(Item.getList())
        //  listview.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        recyclerView_notification.layoutManager = GridLayoutManager(this@Notification, 1) as RecyclerView.LayoutManager?
        recyclerView_notification.adapter = MyAdapter_notification(items,"List")

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
//                R.id.notification -> {
//                    val intent = Intent(this@Notification,Notification::class.java)
//                    startActivity(intent)
//                }
                R.id.camera ->{
                    val intent = Intent(this@Notification,Camera::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
                }
                R.id.message -> {val intent = Intent(this@Notification,Message::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
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
