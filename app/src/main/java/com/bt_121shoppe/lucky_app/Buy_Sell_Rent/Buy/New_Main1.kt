package com.bt_121shoppe.lucky_app.Buy_Sell_Rent

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import com.bt_121shoppe.lucky_app.Activity.*
import com.bt_121shoppe.lucky_app.Buy_Sell_Rent.Buy.fragment_buy_vehicle
import com.bt_121shoppe.lucky_app.Login_Register.UserAccount
import com.bt_121shoppe.lucky_app.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class New_Main1 : AppCompatActivity() {

    private var content: FrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref: SharedPreferences = getSharedPreferences("Register", Context.MODE_PRIVATE)
        content = findViewById(R.id.content) as FrameLayout
        supportFragmentManager.beginTransaction().replace(R.id.content, fragment_buy_vehicle()).commit()
        val bnavigation = findViewById<BottomNavigationView>(R.id.navigation)
        bnavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    val intent = Intent(this@New_Main1,Home::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
                }
                R.id.notification -> {
                    val intent = Intent(this@New_Main1,Notification::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
                }
                R.id.camera ->{
                    if (sharedPref.contains("token") || sharedPref.contains("id")) {
                        val intent = Intent(this@New_Main1, Camera::class.java)
                        intent.putExtra("process_type",1)
                        intent.putExtra("post_type","buy")
                        intent.putExtra("category",2) //buy eletronic=1
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }else{
                        val intent = Intent(this@New_Main1, UserAccount::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }
                }
                R.id.message -> {
                    val intent = Intent(this@New_Main1,Message::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
                }
                R.id.account ->{
                    if (sharedPref.contains("token") || sharedPref.contains("id")) {
                        val intent = Intent(this@New_Main1, Account::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }else{
                        val intent = Intent(this@New_Main1, UserAccount::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }
                }
            }
            true
        }

    }
}
