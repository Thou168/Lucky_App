package com.example.lucky_app.Buy_Sell_Rent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.example.lucky_app.Fragment.Fragment_home
import com.example.lucky_app.Fragment.Fragment_notification
import com.example.lucky_app.Buy_Sell_Rent.Buy.fragment_buy_vehicle
import com.example.lucky_app.Buy_Sell_Rent.Sell.fragment_rent_eletronics
import com.example.lucky_app.Buy_Sell_Rent.Sell.fragment_rent_vehicle
import com.example.lucky_app.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class Rent_Main1 : AppCompatActivity() {

    private var content: FrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        content = findViewById(R.id.content) as FrameLayout
        val navigation = findViewById(R.id.navigation) as BottomNavigationView
        supportFragmentManager.beginTransaction().replace(R.id.content, fragment_rent_vehicle()).commit()
        navigation.setOnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.home -> selectedFragment = Fragment_home()
                R.id.notification -> selectedFragment = Fragment_notification()
                R.id.message -> selectedFragment = Fragment_home()
            }
            val transaction = supportFragmentManager.beginTransaction()
            if (selectedFragment != null) {
                transaction.replace(R.id.content, selectedFragment)
            }
            transaction.commit()
            true
        }
    }
}
