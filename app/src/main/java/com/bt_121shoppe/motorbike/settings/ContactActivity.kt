package com.bt_121shoppe.motorbike.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bt_121shoppe.motorbike.R

class ContactActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

        var txtBack = findViewById<View>(R.id.tvBack_account) as ImageView
        txtBack.setOnClickListener(View.OnClickListener {
            finish()
        })
    }
}
