package com.bt_121shoppe.motorbike.Buy_Sell_Rent.Buy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.bt_121shoppe.motorbike.Buy_Sell_Rent.New_Main1
import com.bt_121shoppe.motorbike.Buy_Sell_Rent.New_Main2
import com.bt_121shoppe.motorbike.R

class Buy : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_sell_rent)
        var toolbar:androidx.appcompat.widget.Toolbar=findViewById(R.id.toolbar)
        toolbar.setBackgroundColor(getColor(R.color.logo_orange))

        val title = findViewById<TextView>(R.id.title)
        //title.text = intent.getStringExtra("Title")
        title.text=getString(R.string.buy)

        val back = findViewById<TextView>(R.id.tv_back)
        back.setOnClickListener { finish() }

        val vehicle = findViewById<TextView>(R.id.vehicle)
//        val string: String = getString(R.string.hello)
        vehicle.setOnClickListener {
            val intent = Intent(this@Buy, New_Main1::class.java)
            intent.putExtra("Back","Buy")
            intent.putExtra("Title",getString(R.string.motor))
            startActivity(intent)
        }
        val eletronic = findViewById<TextView>(R.id.eletronic)
        eletronic.setOnClickListener {
            val intent = Intent(this@Buy, New_Main2::class.java)
            intent.putExtra("Back","Buy")
            intent.putExtra("Title",getString(R.string.electronic))
            startActivity(intent)
        }

    }

}
