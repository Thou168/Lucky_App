package com.bt_121shoppe.motorbike.Buy_Sell_Rent

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import com.bt_121shoppe.motorbike.Activity.*
import com.bt_121shoppe.motorbike.Buy_Sell_Rent.Rent.Rent_vehicle
import com.bt_121shoppe.motorbike.Login_Register.UserAccount
import com.bt_121shoppe.motorbike.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class Rent_Main1 : AppCompatActivity() {

    private var content: FrameLayout? = null
    private var bnavigation: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        locale()
        val sharedPref: SharedPreferences = getSharedPreferences("Register", Context.MODE_PRIVATE)
        content = findViewById(R.id.content) as FrameLayout
        supportFragmentManager.beginTransaction().replace(R.id.content, Rent_vehicle()).commit()
         bnavigation = findViewById(R.id.navigation)
        bnavigation!!.menu.getItem(0).isChecked = true
        bnavigation!!.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    val intent = Intent(this@Rent_Main1,Home::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
                }
                R.id.notification -> {
                    if (sharedPref.contains("token") || sharedPref.contains("id")) {
                        val intent = Intent(this@Rent_Main1,Notification::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
                    }else{
                        val intent = Intent(this@Rent_Main1, UserAccount::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }
                }
                R.id.camera ->{
                    if (sharedPref.contains("token") || sharedPref.contains("id")) {
                        val intent = Intent(this@Rent_Main1, Camera::class.java)
                        intent.putExtra("process_type",1)
                        intent.putExtra("post_type","rent")
                        intent.putExtra("category",2) //buy eletronic=1
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }else{
                        val intent = Intent(this@Rent_Main1, UserAccount::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }
                }
                R.id.message -> {
                    if (sharedPref.contains("token") || sharedPref.contains("id")) {
                        val intent = Intent(this@Rent_Main1,Message::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
                    }else{
                        val intent = Intent(this@Rent_Main1, UserAccount::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }
                }
                R.id.account ->{
                    if (sharedPref.contains("token") || sharedPref.contains("id")) {
                        val intent = Intent(this@Rent_Main1, Account::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }else{
                        val intent = Intent(this@Rent_Main1, UserAccount::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }
                }
            }
            true
        }

    }

    override fun onStart() {
        super.onStart()
        bnavigation!!.menu.getItem(0).isChecked = true
    }

    fun language(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val confi = Configuration()
        confi.locale = locale
        baseContext.resources.updateConfiguration(confi, baseContext.resources.displayMetrics)
        val editor = getSharedPreferences("Settings", MODE_PRIVATE).edit()
        editor.putString("My_Lang", lang)
        editor.apply()
    }

    fun locale() {
        val prefer = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language = prefer.getString("My_Lang", "")
        Log.d("language",language)
        language(language)
    }


}
