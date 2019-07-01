package com.example.lucky_app.Setting

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.Button
import com.example.lucky_app.Activity.Home
import com.example.lucky_app.R

class Setting : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val prefer:SharedPreferences = getSharedPreferences("Register", MODE_PRIVATE);
        val Logout = findViewById<Button>(R.id.btnlogout);

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
    }
}
