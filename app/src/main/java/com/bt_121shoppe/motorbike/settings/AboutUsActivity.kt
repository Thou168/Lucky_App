package com.bt_121shoppe.motorbike.settings

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.text.Layout
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bt_121shoppe.motorbike.R

class AboutUsActivity : AppCompatActivity() {

    @TargetApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)

        var txtBack = findViewById<View>(R.id.tvBack_account) as TextView
        txtBack.setOnClickListener(View.OnClickListener { finish() })

        var txt_about_us_description=findViewById<View>(R.id.about_us_description) as TextView
      //  txt_about_us_description.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD)
// check version if < 8.0 no crash by samang 27/08/2019
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        txt_about_us_description.justificationMode = Layout.JUSTIFICATION_MODE_INTER_WORD
    }
}

}
