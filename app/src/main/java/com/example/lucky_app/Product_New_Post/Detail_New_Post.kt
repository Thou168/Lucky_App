package com.example.lucky_app.Product_New_Post

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.custom.sliderimage.logic.SliderImage
import com.example.lucky_app.Product_dicount.User_post
import com.example.lucky_app.R
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_detail_new_post.*

class Detail_New_Post : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_new_post)
//Back
        val back = findViewById<TextView>(R.id.tv_back)
        back.setOnClickListener { finish() }
//Slider
        val sliderImage = findViewById<SliderImage>(R.id.slider)
        val images = listOf(intent.getStringExtra("Image"),"https://i.redd.it/obx4zydshg601.jpg")
        sliderImage.setItems(images)
        sliderImage.addTimerToSlide(3000)
        sliderImage.removeTimerSlide()
        sliderImage.getIndicator()

        val id = intent.getIntExtra("ID",0)
        val phone = findViewById<TextView>(R.id.tv_phone)

        val title = findViewById<TextView>(R.id.title)
        title.text = intent.getStringExtra("Title")
        val price = findViewById<TextView>(R.id.tv_price)
        price.text = intent.getStringExtra("Price")

        val name = findViewById<TextView>(R.id.tv_name)
        name.text = intent.getStringExtra("Name")
        val img_user = findViewById<CircleImageView>(R.id.cr_img)
        img_user.setImageResource(intent.getIntExtra("Image_user",R.drawable.thou))
//User Post
        findViewById<CircleImageView>(R.id.cr_img).setOnClickListener {
            val intent = Intent(this@Detail_New_Post, User_post::class.java)
            intent.putExtra("Image_user",getIntent().getIntExtra("Image_user",R.drawable.thean))
            intent.putExtra("ID",id)
            intent.putExtra("Phone",phone.text)
            startActivity(intent)
        }
//Button Share
        val share = findViewById<ImageButton>(R.id.btn_share)
        share.setOnClickListener{
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type="text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
            startActivity(Intent.createChooser(shareIntent,getString(R.string.title_activity_account)))
        }
        //Button Call
        val call = findViewById<Button>(R.id.btn_call)
        call.setOnClickListener{
            //                checkPermission()
            makePhoneCall("0962363929")
        }
//Button SMS
        val sms = findViewById<Button>(R.id.btn_sms)
        sms.setOnClickListener {
//                val phoneNumber = "0962363929"
//                val uri = Uri.parse("smsto:0962363929")
//                intent.putExtra("sms_body", "Here goes your message...")
//                val smsManager = SmsManager.getDefault() as SmsManager

            val smsIntent = Intent(Intent.ACTION_VIEW)
            smsIntent.type = "vnd.android-dir/mms-sms"
            smsIntent.putExtra("address", "0962363929")
//            smsIntent.putExtra("sms_body", "Body of Message")
            startActivity(smsIntent)
        }
//Button Like
        val like = findViewById<Button>(R.id.btn_like)
        like.setOnClickListener {
            Toast.makeText(this@Detail_New_Post,"This Product add to Your Liked",Toast.LENGTH_SHORT).show()
        }

    }

    fun makePhoneCall(number: String) : Boolean {
        try {
            val intent = Intent(Intent.ACTION_CALL)
            intent.setData(Uri.parse("tel:$number"))
            startActivity(intent)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
    fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE),
                        42)
            }
        } else {
            // Permission has already been granted
            callPhone()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 42) {
            // If request is cancelled, the result arrays are empty.
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // permission was granted, yay!
                callPhone()
            } else {
                // permission denied, boo! Disable the
                // functionality
            }
            return
        }
    }

    fun callPhone(){
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "0962363929"))
        startActivity(intent)
    }
}
