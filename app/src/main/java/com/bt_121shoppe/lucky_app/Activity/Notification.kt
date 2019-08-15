package com.bt_121shoppe.lucky_app.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.bt_121shoppe.lucky_app.Api.ConsumeAPI
import com.bt_121shoppe.lucky_app.Login_Register.UserAccount
import com.bt_121shoppe.lucky_app.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.bt_121shoppe.lucky_app.chats.ChatMainActivity
import com.bt_121shoppe.lucky_app.utils.CommonFunction

class Notification : AppCompatActivity() {

    private lateinit var back_noti: TextView
    var pk=0
    var prefs: SharedPreferences?=null
    var username=""
    var password=""
    var encodeAuth=""
    private var bnavigation: BottomNavigationView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //must be called before setContentView(...)
        setContentView(R.layout.activity_notification)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val mTitle = toolbar.findViewById(R.id.toolbar_title) as TextView
        toolbar.title =getString(R.string.notification)
        setSupportActionBar(toolbar)
        mTitle.setText(toolbar.getTitle())

        getSupportActionBar()!!.setDisplayShowTitleEnabled(false)

        val sharedPref: SharedPreferences = getSharedPreferences("Register", Context.MODE_PRIVATE)
        val preferences = getSharedPreferences("Register", Context.MODE_PRIVATE)
        username=preferences.getString("name","")
        password=preferences.getString("pass","")
        encodeAuth="Basic "+ CommonFunction.getEncodedString(username,password)

        if (preferences.contains("token")) {
            pk = preferences.getInt("Pk", 0)
        } else if (preferences.contains("id")) {
            pk = preferences.getInt("id", 0)
        }
        val url= ConsumeAPI.BASE_URL+"api/v1/users/"+pk
        val result=CommonFunction.doGetRequestwithAuth(url,encodeAuth)
        Log.d("Hello",result)

         bnavigation = findViewById<BottomNavigationView>(R.id.bnaviga)
        bnavigation!!.menu.getItem(1).isChecked = true
        bnavigation!!.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    val intent = Intent(this@Notification,Home::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
                }
//                R.id.notification -> {8
//                    val intent = Intent(this@Notification,Notification::class.java)
//                    startActivity(intent)
//                }
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

    } // onCreate


    override fun onStart() {
        super.onStart()
        bnavigation!!.menu.getItem(1).isChecked = true
    }
}
