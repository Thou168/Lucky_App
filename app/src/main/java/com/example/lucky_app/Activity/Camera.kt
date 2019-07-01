package com.example.lucky_app.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lucky_app.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class Camera : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        val bnavigation = findViewById<BottomNavigationView>(R.id.bnaviga)
        bnavigation.menu.getItem(2).isChecked = true
        bnavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    val intent = Intent(this@Camera,Home::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
                }
                R.id.notification -> {
                    val intent = Intent(this@Camera,Notification::class.java)
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
                    startActivity(intent)
                }
                R.id.camera ->{
//                    val intent = Intent(this@Camera,Camera::class.java)
//                    startActivity(intent)
//                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
                }
                R.id.message -> {
                    val intent = Intent(this@Camera,Message::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
                }
                R.id.account ->{
                    val intent = Intent(this@Camera,Account::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)

                }
            }
            true
        }

    }
}
