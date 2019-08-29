package com.bt_121shoppe.lucky_app.Product_dicount

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.util.Log
import android.widget.*
import com.custom.sliderimage.logic.SliderImage
import com.bt_121shoppe.lucky_app.R
import com.bt_121shoppe.lucky_app.loan.LoanCreateActivity
import com.bt_121shoppe.lucky_app.useraccount.User_post
import de.hdodenhof.circleimageview.CircleImageView

class Detail_Discount : AppCompatActivity(){
    private var postId:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_discount)

        postId = intent.getIntExtra("ID",0)
        findViewById<TextView>(R.id.tv_back).setOnClickListener { finish() }

        val sliderImage = findViewById<SliderImage>(R.id.slider)
        val images = listOf(intent.getStringExtra("Image"), "https://i.redd.it/obx4zydshg601.jpg")
        sliderImage.setItems(images)
        sliderImage.addTimerToSlide(3000)
        sliderImage.removeTimerSlide()
        sliderImage.getIndicator()

        val id = intent.getIntExtra("ID", 0)

        val title = findViewById<TextView>(R.id.title)
        title.text = intent.getStringExtra("Title")
        val price = findViewById<TextView>(R.id.tv_price)
        price.text = intent.getStringExtra("Price")
        val edLoanPrice = findViewById<EditText>(R.id.ed_loan_price)

        val discount = findViewById<TextView>(R.id.tv_discount)
        val st = intent.getStringExtra("Discount")
        val ms = SpannableString(st)
        val mst = StrikethroughSpan()
        ms.setSpan(mst, 0, st.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        discount.text = ms

        val name = findViewById<TextView>(R.id.tv_name)
        name.text = intent.getStringExtra("Name")
        val img_user = findViewById<CircleImageView>(R.id.cr_img)
        img_user.setImageResource(intent.getIntExtra("Image_user", R.drawable.thou))

        findViewById<CircleImageView>(R.id.cr_img).setOnClickListener {
            val intent = Intent(this@Detail_Discount, User_post::class.java)
            intent.putExtra("Image_user", getIntent().getIntExtra("Image_user", R.drawable.thean))
            intent.putExtra("ID", id)
            startActivity(intent)
        }
        //Button SMS
//        val sms = findViewById<Button>(R.id.btn_sms)
//        sms.setOnClickListener {
//            val smsIntent = Intent(Intent.ACTION_VIEW)
//            smsIntent.type = "vnd.android-dir/mms-sms"
//            smsIntent.putExtra("address", "0962363929")
//            startActivity(smsIntent)
//        }
        //Button Share
//        val share = findViewById<ImageButton>(R.id.btn_share)
//        share.setOnClickListener {
//            val shareIntent = Intent()
//            shareIntent.action = Intent.ACTION_SEND
//            shareIntent.type = "text/plain"
//            shareIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
//            startActivity(Intent.createChooser(shareIntent, getString(R.string.title_activity_account)))
//        }
        //Button Call
        val call = findViewById<Button>(R.id.btn_call)
        call.setOnClickListener {

        }
        //Button Like
        val like = findViewById<Button>(R.id.btn_like)
        like.setOnClickListener {
            Toast.makeText(this@Detail_Discount, "This Product add to Your Liked", Toast.LENGTH_SHORT).show()
        }

        val loan = findViewById<Button>(R.id.btn_loan)
        loan.setOnClickListener {
            val intent = Intent(this@Detail_Discount, LoanCreateActivity::class.java)
//            intent.putExtra("IDforLoanDes",postId)
            startActivity(intent)
        }
    }

//    fun makePhoneCall(number: String): Boolean {
//        try {
//            val intent = Intent(Intent.ACTION_CALL)
//            intent.data = Uri.parse("tel:$number")
//            startActivity(intent)
//            return true
//        } catch (e: Exception) {
//            e.printStackTrace()
//            return false
//        }
//    }
}
