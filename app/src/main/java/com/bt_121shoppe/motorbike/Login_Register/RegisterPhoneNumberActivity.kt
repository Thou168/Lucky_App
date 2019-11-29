package com.bt_121shoppe.motorbike.Login_Register

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bt_121shoppe.motorbike.R
import com.rengwuxian.materialedittext.MaterialEditText

class RegisterPhoneNumberActivity : AppCompatActivity() {

    internal lateinit var mobile: MaterialEditText
    internal lateinit var button: Button
    internal lateinit var no: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_phone_number)

        mobile = findViewById<View>(R.id.mobile) as MaterialEditText
        button = findViewById<View>(R.id.button) as Button

        button.setOnClickListener {
            no = mobile.text!!.toString()
            validNo(no)
            val intent = Intent(this@RegisterPhoneNumberActivity, VerifyMobileActivity::class.java)
            intent.putExtra("mobile", no)
            startActivity(intent)
            Toast.makeText(this@RegisterPhoneNumberActivity, no, Toast.LENGTH_LONG).show()
        }
        mobile.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                button.performClick()
            }
            false
        })
    }

    private fun validNo(no: String) {
        if (no.isEmpty() || no.length < 7) {
            mobile.error = "Enter a valid phone number"
            mobile.hasFocus()
            return
        }
    }
}
