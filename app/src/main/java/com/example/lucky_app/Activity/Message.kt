package com.example.lucky_app.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lucky_app.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class Message : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        val bnavigation = findViewById<BottomNavigationView>(R.id.bnaviga)
        bnavigation.menu.getItem(3).isChecked = true
        bnavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    val intent = Intent(this@Message,Home::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
                }
                R.id.notification -> {
                    val intent = Intent(this@Message,Notification::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
                }
                R.id.camera ->{
                    val intent = Intent(this@Message,Camera::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
                }
                R.id.message -> {
//                    val intent = Intent(this@Message,Message::class.java)
//                    startActivity(intent)
                }
                R.id.account ->{
                    val intent = Intent(this@Message,Account::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)

                }
            }
            true
        }

    }
}
