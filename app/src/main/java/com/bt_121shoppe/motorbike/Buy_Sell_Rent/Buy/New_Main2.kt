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
import com.bt_121shoppe.motorbike.activities.*
import com.bt_121shoppe.motorbike.Api.api.Active_user
import com.bt_121shoppe.motorbike.Buy_Sell_Rent.Buy.Buy_eletronic
import com.bt_121shoppe.motorbike.Login_Register.UserAccountActivity
import com.bt_121shoppe.motorbike.R
import com.bt_121shoppe.motorbike.chats.ChatMainActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class New_Main2 : AppCompatActivity() {
    private var pk=0
    private var content: FrameLayout? = null
    private var bnavigation: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        locale()
        val sharedPref: SharedPreferences = getSharedPreferences("RegisterActivity", Context.MODE_PRIVATE)
        if (sharedPref.contains("token")) {
            pk = sharedPref.getInt("Pk",0)
        } else if (sharedPref.contains("id")) {
            pk = sharedPref.getInt("id", 0)
        }

 //check active and deactive account by samang 2/09/19
        val activeUser = Active_user()
        val active: String
        active = activeUser.isUserActive(pk, this)
//end
        content = findViewById(R.id.content) as FrameLayout
        supportFragmentManager.beginTransaction().replace(R.id.content, Buy_eletronic()).commit()
         bnavigation = findViewById(R.id.navigation)
        bnavigation!!.menu.getItem(0).isChecked = true
        bnavigation!!.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    val intent = Intent(this@New_Main2,Home::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
                }
                R.id.notification -> {
                    if (sharedPref.contains("token") || sharedPref.contains("id")) {
                        //check active and deactive account by samang 2/09/19  (all)
                        if (active.equals("false")){
                            activeUser.clear_session(this)
                        }else {
                            val intent = Intent(this@New_Main2, NotificationActivity::class.java)
                            startActivity(intent)
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        }
                    }else{
                        val intent = Intent(this@New_Main2, UserAccountActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }
                }
                R.id.camera ->{
                    if (sharedPref.contains("token") || sharedPref.contains("id")) {
                        if (active.equals("false")){
                            activeUser.clear_session(this)
                        }else {
                            val intent = Intent(this@New_Main2, Camera::class.java)
                            intent.putExtra("process_type", 1)
                            intent.putExtra("post_type", "buy")
                            intent.putExtra("category", 1) //buy eletronic=1
                            startActivity(intent)
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        }
                    }else{
                        val intent = Intent(this@New_Main2, UserAccountActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }
                }
                R.id.message -> {
                    if (sharedPref.contains("token") || sharedPref.contains("id")) {
                        if (active.equals("false")){
                            activeUser.clear_session(this)
                        }else {
                            val intent = Intent(this@New_Main2, ChatMainActivity::class.java)
                            startActivity(intent)
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        }
                    }else{
                        val intent = Intent(this@New_Main2, UserAccountActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }

                }
                R.id.account ->{
                    if (sharedPref.contains("token") || sharedPref.contains("id")) {
                        if (active.equals("false")){
                            activeUser.clear_session(this)
                        }else {
                            val intent = Intent(this@New_Main2, Account::class.java)
                            startActivity(intent)
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        }
                    }else{
                        val intent = Intent(this@New_Main2, UserAccountActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }
                }
            }
            true
        }

    }//onCtreate

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
