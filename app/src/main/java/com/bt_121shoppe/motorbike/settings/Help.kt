package com.bt_121shoppe.motorbike.settings

import android.os.Build
import android.os.Bundle
import android.text.Layout
import android.view.View
import android.webkit.WebView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bt_121shoppe.motorbike.R
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.listener.OnDrawListener


class Help : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_term_privacy)

        val txtBack = findViewById<View>(R.id.tvBack_account) as ImageView
        txtBack.setOnClickListener(View.OnClickListener {
            finish()
        })

        val pdf = findViewById<com.pdfview.PDFView>(R.id.pdf_view)
        pdf.visibility = View.VISIBLE
        pdf.fromAsset("guide_121.pdf").show()

        val sv=findViewById<ScrollView>(R.id.sv)
        sv.visibility = View.GONE

        val change_tp=findViewById<TextView>(R.id.change_topic)
        change_tp.text = getString(R.string.menu_help)

        val txt_term_privacy_description=findViewById<TextView>(R.id.txt_term_privacy_description)
        val txt_suspend_description=findViewById<TextView>(R.id.txt_suspend_description)
        val txt_general_term_description=findViewById<TextView>(R.id.txt_general_term_description)


        txt_term_privacy_description.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD)
        txt_suspend_description.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD)
        txt_general_term_description.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD)

    }
}
