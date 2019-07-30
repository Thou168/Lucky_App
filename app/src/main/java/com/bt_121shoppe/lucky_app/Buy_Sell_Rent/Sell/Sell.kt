package com.bt_121shoppe.lucky_app.Buy_Sell_Rent.Sell

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.bt_121shoppe.lucky_app.Buy_Sell_Rent.Sell_Main1
import com.bt_121shoppe.lucky_app.Buy_Sell_Rent.Sell_Main2
import com.bt_121shoppe.lucky_app.R

class Sell : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_sell_rent)

        var toolbar:androidx.appcompat.widget.Toolbar=findViewById(R.id.toolbar)
        toolbar.setBackgroundColor(getColor(R.color.logo_green))

        val title = findViewById<TextView>(R.id.title)
        //title.text = intent.getStringExtra("Title")
        title.text=getString(R.string.sell)

        val back = findViewById<TextView>(R.id.tv_back)
        back.setOnClickListener { finish() }

        val vehicle = findViewById<TextView>(R.id.vehicle)
        vehicle.setOnClickListener {
            val intent = Intent(this@Sell, Sell_Main1::class.java)
            intent.putExtra("Back","Sell")
            startActivity(intent)
        }
        val eletronic = findViewById<TextView>(R.id.eletronic)
        eletronic.setOnClickListener {
            val intent = Intent(this@Sell, Sell_Main2::class.java)
            intent.putExtra("Back","Sell")
            startActivity(intent)
        }
    }
}
