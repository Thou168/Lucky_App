package com.bt_121shoppe.lucky_app.Setting

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.text.Layout
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bt_121shoppe.lucky_app.R

class AboutUsActivity : AppCompatActivity() {

    @TargetApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)

        var txtBack = findViewById<View>(R.id.tvBack_account) as TextView
        txtBack.setOnClickListener(View.OnClickListener { finish() })

        val txt_about_us_description=findViewById<TextView>(R.id.about_us_description)
        txt_about_us_description.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD)
    }

}
