package com.example.lucky_app.Product_dicount

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.widget.TextView
import com.custom.sliderimage.logic.SliderImage
import com.example.lucky_app.R
import com.example.lucky_app.Startup.Item
import de.hdodenhof.circleimageview.CircleImageView

class Detail_Discount : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_discount)

        findViewById<TextView>(R.id.tv_back).setOnClickListener { finish() }

        val sliderImage = findViewById<SliderImage>(R.id.slider)
        val images = listOf(
                intent.getStringExtra("Image"),
                "https://i.redd.it/obx4zydshg601.jpg")
        sliderImage.setItems(images)
        sliderImage.addTimerToSlide(3000)
        sliderImage.removeTimerSlide()
        sliderImage.getIndicator()

        val id = intent.getIntExtra("ID",0)

        val title = findViewById<TextView>(R.id.title)
        title.text = intent.getStringExtra("Title")
        val price = findViewById<TextView>(R.id.tv_price)
        price.text = intent.getStringExtra("Price")

        val discount = findViewById<TextView>(R.id.tv_discount)
        val st = intent.getStringExtra("Discount")
        val ms = SpannableString(st)
        val mst = StrikethroughSpan()
        ms.setSpan(mst,0,st.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        discount.text = ms

        val name = findViewById<TextView>(R.id.tv_name)
        name.text = intent.getStringExtra("Name")
        val img_user = findViewById<CircleImageView>(R.id.cr_img)
        img_user.setImageResource(intent.getIntExtra("Image_user",R.drawable.thou))

        findViewById<CircleImageView>(R.id.cr_img).setOnClickListener {
            val intent = Intent(this@Detail_Discount,User_post::class.java)
            intent.putExtra("Image_user",getIntent().getIntExtra("Image_user",R.drawable.thean))
            intent.putExtra("ID",id)
            startActivity(intent)
        }

    }
}
