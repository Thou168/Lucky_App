package com.bt_121shoppe.lucky_app.Startup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.bt_121shoppe.lucky_app.Fragment.Fragment_account
import com.bt_121shoppe.lucky_app.Fragment.Fragment_home
import com.bt_121shoppe.lucky_app.Fragment.Fragment_notification
import com.bt_121shoppe.lucky_app.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class StartUp : AppCompatActivity() {

    private var content: FrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        content = findViewById(R.id.content) as FrameLayout
        val navigation = findViewById(R.id.navigation) as BottomNavigationView
        supportFragmentManager.beginTransaction().replace(R.id.content,Fragment_home()).commit()
        navigation.setOnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.home ->selectedFragment = Fragment_home()
                R.id.notification -> selectedFragment = Fragment_notification()
                R.id.message -> selectedFragment = Fragment_account()
            }
            val transaction = supportFragmentManager.beginTransaction()
            if (selectedFragment != null) {
                transaction.replace(R.id.content, selectedFragment)
            }
            transaction.commit()
            true
        }
//
//        val fragment = Fragment_home.Companion.newInstance()
//        addFragment(fragment)

    }
//    private fun addFragment(fragment: Fragment) {
//        supportFragmentManager
//                .beginTransaction()
//                .setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
//                .replace(R.id.content, fragment, fragment.javaClass.getSimpleName())
//                .addToBackStack(fragment.javaClass.getSimpleName())
//                .commit()
//    }
}





