package com.bt_121shoppe.lucky_app.Setting

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.bt_121shoppe.lucky_app.Activity.Home
import com.bt_121shoppe.lucky_app.R
import com.google.firebase.auth.FirebaseAuth
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
                    FirebaseAuth.getInstance().signOut()

                    startActivity(Intent(this@Setting, Home::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                }
                                     .setNegativeButton(Html.fromHtml("<font color='#1616FA'>Cancel</font>"), object: DialogInterface.OnClickListener {

                override fun onClick(dialog: DialogInterface, id:Int) {
                dialog.cancel()
                }
                }).show()
        }
        val Changepassword = findViewById<TextView>(R.id.tvChangePass)
        Changepassword.setOnClickListener {
            val intent = Intent(this@Setting,ChangePassword::class.java)
            startActivity(intent)
        }

        val lange2 = findViewById<TextView>(R.id.tvLanguage2)
        lange2.setOnClickListener{
            val lang = Changelanguage()
            lang.show(supportFragmentManager,lang.tag)
        }
        val lange1 = findViewById<TextView>(R.id.tvLanguage)
        lange1.setOnClickListener {
            val lang = Changelanguage()
            lang.show(supportFragmentManager,lang.tag)
        }

        val verson = findViewById<TextView>(R.id.tvVersion2)
        try {
            val pInfo = this.getPackageManager().getPackageInfo(packageName, 0)
            val version = pInfo.versionName
            verson.setText(version)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        var txtBack = findViewById<View>(R.id.tvBack_account) as TextView
        txtBack.setOnClickListener(View.OnClickListener { finish() })

    }  // onCreate

    override fun language(lang : String) {
        val locale = Locale(lang!!)
        Locale.setDefault(locale)
        val confi = Configuration()
        confi.locale = locale
        baseContext.resources.updateConfiguration(confi, baseContext.resources.displayMetrics)
        val editor = getSharedPreferences("Settings", MODE_PRIVATE).edit()
        editor.putString("My_Lang", lang)
        editor.apply()
          recreate()
    }
    override fun locale() {
        val prefer = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language = prefer.getString("My_Lang", "")
        language(language)

    }
}
