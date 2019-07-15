package com.bt_121shoppe.lucky_app.Setting

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.TextView
import com.bt_121shoppe.lucky_app.Activity.Home
import com.bt_121shoppe.lucky_app.R
import java.util.*

class Setting : AppCompatActivity(), Changelanguage.BottomSheetListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val prefer:SharedPreferences = getSharedPreferences("Register", MODE_PRIVATE)
        val Logout = findViewById<Button>(R.id.btnlogout)

        Logout.setOnClickListener {
          AlertDialog.Builder(this)
                .setMessage("Are you sure you want to logout?")
                .setIcon(R.drawable.logo)
                .setPositiveButton(Html.fromHtml("<font color='#F30E0E'>Logout</font>")
                ) { dialog, id ->
                    val editor = prefer.edit()
                    editor.clear()
                    editor.commit()
                    dialog.cancel()
                    startActivity(Intent(this@Setting, Home::class.java))
                }
                                     .setNegativeButton(Html.fromHtml("<font color='#1616FA'>Cancel</font>"), object: DialogInterface.OnClickListener {

                override fun onClick(dialog: DialogInterface, id:Int) {
                dialog.cancel()
                }
                }).show()
        }
        val lange = findViewById<TextView>(R.id.tvLanguage2)
        lange.setOnClickListener{
            val lang = Changelanguage()
            lang.show(supportFragmentManager,lang.tag)
        }
    }

    override fun language(lang : String) {
        val locale = Locale(lang!!)
        Locale.setDefault(locale)
        val confi = Configuration()
        confi.locale = locale
        baseContext.resources.updateConfiguration(confi, baseContext.resources.displayMetrics)
        val editor = getSharedPreferences("Settings", MODE_PRIVATE).edit()
        editor.putString("My_Lang", lang)
        editor.apply()
    }
    override fun locale() {
        val prefer = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language = prefer.getString("My_Lang", "")
        language(language)
    }
}
