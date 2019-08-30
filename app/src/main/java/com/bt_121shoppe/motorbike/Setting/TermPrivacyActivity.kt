package com.bt_121shoppe.motorbike.Setting

import android.os.Build
import android.os.Bundle
import android.text.Layout
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bt_121shoppe.motorbike.R

class TermPrivacyActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_term_privacy)

        var txtBack = findViewById<View>(R.id.tvBack_account) as TextView
        txtBack.setOnClickListener(View.OnClickListener { finish() })

        val txt_term_privacy_description=findViewById<TextView>(R.id.txt_term_privacy_description)
        val txt_suspend_description=findViewById<TextView>(R.id.txt_suspend_description)
        val txt_general_term_description=findViewById<TextView>(R.id.txt_general_term_description)


        txt_term_privacy_description.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD)
        txt_suspend_description.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD)
        txt_general_term_description.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD)

    }
}
