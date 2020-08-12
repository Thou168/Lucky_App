package com.bt_121shoppe.motorbike.settings

import android.os.Build
import android.os.Bundle
import android.text.Layout
import android.view.View
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bt_121shoppe.motorbike.R
import com.github.barteksc.pdfviewer.PDFView

class TermPrivacyActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_term_privacy)
        val sv = findViewById<ScrollView>(R.id.sv)
        sv.visibility=View.VISIBLE

        val pdf = findViewById<com.pdfview.PDFView>(R.id.pdf_view)
        pdf.visibility = View.GONE

        val txtBack = findViewById<View>(R.id.tvBack_account) as ImageView
        txtBack.setOnClickListener(View.OnClickListener {
            finish()
        })
        val term_title_1=findViewById<TextView>(R.id.first)
        val term_title_2=findViewById<TextView>(R.id.second)
        val term_title_3=findViewById<TextView>(R.id.third)

        val txt_term_privacy_description=findViewById<TextView>(R.id.txt_term_privacy_description)
        val txt_suspend_description=findViewById<TextView>(R.id.txt_suspend_description)
        val txt_general_term_description=findViewById<TextView>(R.id.txt_general_term_description)


        term_title_1.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD)
//        term_title_1.text = R.string.term_of_pri_1.toString()
        term_title_2.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD)
//        term_title_2.text = R.string.term_of_pri_2.toString()
        term_title_3.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD)
//        term_title_3.text = R.string.term_of_pri_3.toString()

        txt_term_privacy_description.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD)
//        txt_term_privacy_description.text = R.string.term_des_1.toString()
        txt_suspend_description.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD)
//        txt_suspend_description.text = R.string.term_des_2.toString()
        txt_general_term_description.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD)
//        txt_general_term_description.text = R.string.term_des_3.toString()

    }
}
