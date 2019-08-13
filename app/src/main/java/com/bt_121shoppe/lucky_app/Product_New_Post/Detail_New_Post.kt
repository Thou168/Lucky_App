package com.bt_121shoppe.lucky_app.Product_New_Post

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.SpannableString
import android.text.Spanned
import android.text.format.DateUtils
import android.text.style.StrikethroughSpan
import android.util.Base64
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bt_121shoppe.lucky_app.Activity.Account
import com.custom.sliderimage.logic.SliderImage
import com.bt_121shoppe.lucky_app.Activity.Item_API
import com.bt_121shoppe.lucky_app.Api.ConsumeAPI
import com.bt_121shoppe.lucky_app.Api.User
import com.bt_121shoppe.lucky_app.Login_Register.UserAccount
import com.bt_121shoppe.lucky_app.Product_dicount.Detail_Discount
import com.bt_121shoppe.lucky_app.useraccount.User_post
import com.bt_121shoppe.lucky_app.R
import com.bt_121shoppe.lucky_app.chats.ChatActivity
import com.bt_121shoppe.lucky_app.loan.LoanCreateActivity
import com.bt_121shoppe.lucky_app.models.Chat
import com.bt_121shoppe.lucky_app.models.PostViewModel
import com.bt_121shoppe.lucky_app.utils.CommomAPIFunction
import com.bt_121shoppe.lucky_app.utils.LoanCalculator
import com.bumptech.glide.Glide

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.CameraUpdateFactory

//import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.google.gson.JsonParseException
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_acount.*
import kotlinx.android.synthetic.main.activity_detail_new_post.*
import kotlinx.android.synthetic.main.contact_seller.*
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import org.w3c.dom.Text
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class Detail_New_Post : AppCompatActivity() , OnMapReadyCallback {
    private val TAG = Detail_Discount::class.java.simpleName
    private lateinit var mMap: GoogleMap
    private val REQUEST_LOCATION = 1
    internal lateinit var locationManager: LocationManager
    internal var latitude: Double = 0.toDouble()
    internal var longtitude:Double = 0.toDouble()
    private var list_rela: RecyclerView? = null
    private var postId:Int=0
    private var pk=0
    private var name=""
    private var pass=""
    private var Encode=""
    private var p=0
    private var pt=0
    internal lateinit var txt_detail_new: TextView
    private lateinit var tvPostTitle:TextView
    private lateinit var tvPrice:TextView
    private lateinit var tvPrice1:TextView
    private lateinit var tvDiscount: TextView
    private lateinit var tvBrand:TextView
    private lateinit var tvModel:TextView
    private lateinit var tvYear:TextView
    private lateinit var tvCondition:TextView
    private lateinit var tvColor:TextView
    private lateinit var tvDescription:TextView
    private lateinit var tvMonthlyPayment:TextView
    private lateinit var edLoanPrice:EditText
    private lateinit var edLoanInterestRate:EditText
    private lateinit var edLoanDeposit:EditText
    private lateinit var edLoanTerm:EditText
    private lateinit var sliderImage:SliderImage
    private lateinit var img_user:CircleImageView
    private lateinit var user_name:TextView
    private lateinit var user_telephone:TextView
    private lateinit var user_email:TextView
    private lateinit var user_location:TextView
    private lateinit var tv_count_view:TextView
    private lateinit var tv_location_duration:TextView
    private lateinit var tex_noresult:TextView
    private lateinit var mprocessBar:ProgressBar
    private lateinit var address_detial:TextView
    private lateinit var postTitle:String
    private lateinit var postPrice:String
    private lateinit var postFrontImage:String
    private lateinit var postUsername:String
    private lateinit var postUserId:String
    private lateinit var postType:String
    var discount: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_new_post)

        checkPermission()
        postId = intent.getIntExtra("ID",0)
        discount = intent.getDoubleExtra("Discount",0.0)

        val sharedPref: SharedPreferences = getSharedPreferences("Register", Context.MODE_PRIVATE)
        name = sharedPref.getString("name", "")
        pass = sharedPref.getString("pass", "")
        Encode = getEncodedString(name,pass)

        if (sharedPref.contains("token")) {
           pk = sharedPref.getInt("Pk",0)
        } else if (sharedPref.contains("id")) {
           pk = sharedPref.getInt("id", 0)
        }

        Log.d("Response pk:",pk.toString())
        p = intent.getIntExtra("ID",0)
        pt=intent.getIntExtra("postt",0)

        initialProductPostDetail(Encode)
        submitCountView(Encode)
        countPostView(Encode)
        list_rela = findViewById(R.id.list_rela)
        //Back
        val back = findViewById<TextView>(R.id.tv_back)
        back.setOnClickListener { finish() }
        //Slider
        sliderImage = findViewById<SliderImage>(R.id.slider)
        tvPostTitle = findViewById<TextView>(R.id.title)
        tvPrice = findViewById<TextView>(R.id.tv_price)
        tvPrice1 = findViewById<TextView>(R.id.tv_price1)
        tvDiscount = findViewById<TextView>(R.id.tv_discount)
        tvBrand=findViewById<TextView>(R.id.tvBrand)
        tvModel=findViewById<TextView>(R.id.tv_Model)
        tvYear=findViewById<TextView>(R.id.tv_Year)
        tvCondition=findViewById<TextView>(R.id.tv_Condition)
        tvColor=findViewById<TextView>(R.id.tv_Color)
        tvDescription=findViewById<TextView>(R.id.tv_Description)
        tvMonthlyPayment=findViewById<TextView>(R.id.tvMonthlyPayment)
        edLoanPrice=findViewById<EditText>(R.id.ed_loan_price)
        edLoanInterestRate=findViewById<EditText>(R.id.ed_loan_interest_rate)
        edLoanDeposit=findViewById<EditText>(R.id.ed_loan_deposit)
        edLoanTerm=findViewById<EditText>(R.id.ed_loan_term)
        user_name = findViewById<TextView>(R.id.tv_name)
        img_user = findViewById<CircleImageView>(R.id.cr_img)
        user_telephone=findViewById<TextView>(R.id.tv_phone)
        user_email=findViewById<TextView>(R.id.tv_email)
        tv_count_view=findViewById(R.id.view)
        tv_location_duration=findViewById<TextView>(R.id.tv_location_duration)
        address_detial = findViewById<TextView>(R.id.address)

        //Button Share
        val share = findViewById<ImageButton>(R.id.btn_share)
        share.setOnClickListener{
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type="text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
            startActivity(Intent.createChooser(shareIntent,getString(R.string.title_activity_account)))
        }
        val chat = findViewById<ImageView>(R.id.btn_sms)
        chat.setOnClickListener {
            if (sharedPref.contains("token") || sharedPref.contains("id")) {
                val intent = Intent(this@Detail_New_Post, ChatActivity::class.java)
                intent.putExtra("postId",postId)
                intent.putExtra("postTitle",postTitle)
                intent.putExtra("postPrice",postPrice)
                intent.putExtra("postImage",postFrontImage)
                intent.putExtra("postUserPk",pk)
                intent.putExtra("postUsername",postUsername)
                intent.putExtra("postUserId",postUserId)
                intent.putExtra("postType",postType)
                startActivity(intent)
            }else{
                val intent = Intent(this@Detail_New_Post, UserAccount::class.java)
                startActivity(intent)
            }
        }

        val like = findViewById<ImageView>(R.id.btn_like)
        like.setOnClickListener {
            if (sharedPref.contains("token") || sharedPref.contains("id")) {
                Like_post(Encode)
            }else{
                val intent = Intent(this@Detail_New_Post, UserAccount::class.java)
                startActivity(intent)
            }
        }

        val loan= findViewById<ImageView>(R.id.btn_loan)
        loan.setOnClickListener{
            if (sharedPref.contains("token") || sharedPref.contains("id")) {
                val intent = Intent(this@Detail_New_Post, LoanCreateActivity::class.java)
                intent.putExtra("PutIDLoan",postId)
                startActivity(intent)
            }else{
                val intent = Intent(this@Detail_New_Post, UserAccount::class.java)
                startActivity(intent)
            }
        }

        edLoanInterestRate.setText("1.5")
        edLoanTerm.setText("1")
        tvMonthlyPayment.setText("$ 0.00")

        edLoanPrice.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                //Perform Code
                Toast.makeText(this@Detail_New_Post, edLoanPrice.getText(), Toast.LENGTH_SHORT).show()
                calculateLoanMonthlyPayment()
                return@OnKeyListener true
            }
            false
        })

        edLoanInterestRate.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                //Perform Code
                Toast.makeText(this@Detail_New_Post, edLoanPrice.getText(), Toast.LENGTH_SHORT).show()
                calculateLoanMonthlyPayment()
                return@OnKeyListener true
            }
            false
        })

        edLoanTerm.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                //Perform Code
                Toast.makeText(this@Detail_New_Post, edLoanPrice.getText(), Toast.LENGTH_SHORT).show()
                calculateLoanMonthlyPayment()
                return@OnKeyListener true
            }
            false
        })

    }  // oncreate
    fun dialContactPhone(phoneNumber:String) {
        startActivity( Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)))
    }
    fun sms(phoneNumber:String) {
        val sendIntent =  Intent(Intent.ACTION_VIEW)
        sendIntent.putExtra("address"  ,phoneNumber)
        sendIntent.putExtra("sms_body", "")
        sendIntent.setType("vnd.android-dir/mms-sms")
        startActivity(sendIntent)
    }

    fun initialProductPostDetail(encode: String){

        val prefer = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language = prefer.getString("My_Lang", "")
        var url:String
        var request:Request
        val auth = "Basic $encode"
        if(pt==1) {
            url = ConsumeAPI.BASE_URL + "postbyuser/" + postId
            request=Request.Builder()
                    .url(url)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .header("Authorization", auth)
                    .build()
        }
        else {
            url = ConsumeAPI.BASE_URL + "detailposts/" + postId
            request = Request.Builder()
                    .url(url)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    //.header("Authorization", auth)
                    .build()
        }

        val client=OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                val mMessage = e.message.toString()
                Log.w("failure Response", mMessage)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val mMessage = response.body()!!.string()
                Log.d(TAG,mMessage)
                val gson = Gson()
                try {
                    runOnUiThread {
                        var postDetail = PostViewModel()
                        postDetail = gson.fromJson(mMessage, PostViewModel::class.java)
                        Log.e(TAG,"D"+ mMessage)

                        val url1=ConsumeAPI.BASE_URL+"api/v1/years/"+postDetail.year
                        var client1=OkHttpClient()
                        val request1 = Request.Builder()
                                .url(url1)
                                .header("Accept", "application/json")
                                .header("Content-Type", "application/json")
                                .header("Authorization", auth)
                                .build()
                        client1.newCall(request1).enqueue(object : Callback {
                            @Throws(IOException::class)
                            override fun onResponse(call: Call, response: Response) {
                                val mMessage = response.body()!!.string()
                                runOnUiThread {
                                    try {
                                        val jsonObject = JSONObject(mMessage)
                                        tvYear.setText(jsonObject.getString("year"))
                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    }
                                }
                            }

                            override fun onFailure(call: Call, e: IOException) {

                            }

                        })

                        val url2=ConsumeAPI.BASE_URL+"api/v1/models/"+postDetail.modeling
                        val client2=OkHttpClient()
                        val request2 = Request.Builder()
                                .url(url2)
                                .header("Accept", "application/json")
                                .header("Content-Type", "application/json")
                                .header("Authorization", auth)
                                .build()
                        client2.newCall(request2).enqueue(object : Callback {
                            @Throws(IOException::class)
                            override fun onResponse(call: Call, response: Response) {
                                val mMessage = response.body()!!.string()
                                runOnUiThread {
                                    try {
                                        val jsonObject = JSONObject(mMessage)
                                        if(language.equals("km")){
                                            tvModel.setText(jsonObject.getString("modeling_name_kh"))
                                        }else if(language.equals("en")){
                                            tvModel.setText(jsonObject.getString("modeling_name"))
                                        }

                                        val url3=ConsumeAPI.BASE_URL+"api/v1/brands/"+jsonObject.getString("brand")
                                        val client3=OkHttpClient()
                                        val request3 = Request.Builder()
                                                .url(url3)
                                                .header("Accept", "application/json")
                                                .header("Content-Type", "application/json")
                                                .header("Authorization", auth)
                                                .build()
                                        client3.newCall(request3).enqueue(object : Callback {
                                            @Throws(IOException::class)
                                            override fun onResponse(call: Call, response: Response) {
                                                val mMessage = response.body()!!.string()
                                                runOnUiThread {
                                                    try {
                                                        val jsonObject = JSONObject(mMessage)
                                                        //Log.d(TAG,"Year "+jsonObject.getString("year"))
                                                        if(language.equals("km")){
                                                            tvBrand.setText(jsonObject.getString("brand_name_as_kh"))
                                                        }else if(language.equals("en")){
                                                            tvBrand.setText(jsonObject.getString("brand_name"))
                                                        }

                                                    } catch (e: JSONException) {
                                                        e.printStackTrace()
                                                    }
                                                }
                                            }

                                            override fun onFailure(call: Call, e: IOException) {

                                            }

                                        })

                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    }
                                }
                            }

                            override fun onFailure(call: Call, e: IOException) {

                            }
                        })

                        postTitle=postDetail.title.toString()
                        postPrice=postDetail.cost.toString()
                        postFrontImage=postDetail.base64_front_image.toString()
                        postType=postDetail.post_type
                        tvPostTitle.setText(postDetail.title.toString())
                        tvPrice.setText("$ "+ discount)
                        edLoanPrice.setText(""+discount)
                        tvPrice1.setText("$ "+ postDetail.cost.toString())
                        if (discount == 0.0){
                            tvDiscount.visibility = View.GONE
                            tvPrice.visibility = View.GONE
                        }else{
                            tvPrice1.visibility = View.GONE
                        }
                        var st = "$"+postDetail.cost
                        st = st.substring(0, st.length-1)
                        val ms = SpannableString(st)
                        val mst = StrikethroughSpan()
                        ms.setSpan(mst,0,st.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        tvDiscount.text = ms
                        tvCondition.setText(postDetail.condition.toString())
                        tvColor.setText(postDetail.color.toString())
                        tvDescription.setText(postDetail.description.toString())
                        val addr = postDetail.contact_address.toString()
                        if(addr.isEmpty()) {

                        }else{
                            val splitAddr = addr.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                            latitude = java.lang.Double.valueOf(splitAddr[0])
                            longtitude = java.lang.Double.valueOf(splitAddr[1])

                            get_location(latitude, longtitude)
                            val mapFragment = supportFragmentManager
                                    .findFragmentById(R.id.map_detail_newpost) as SupportMapFragment?
                            mapFragment!!.getMapAsync(OnMapReadyCallback {
                                this@Detail_New_Post.onMapReady(it)
                            })
                        }

                        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                        sdf.timeZone = TimeZone.getTimeZone("GMT")
                        val time:Long = sdf.parse(postDetail.created).time
                        val now:Long = System.currentTimeMillis()
                        val ago:CharSequence = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)
                        tv_location_duration.setText(ago)

                        val base64_front_image=postDetail.base64_front_image.toString()
                        val base64_right_image=postDetail.base64_right_image.toString()
                        val base64_left_image=postDetail.base64_left_image.toString()
                        val base64_back_image=postDetail.base64_back_image.toString()

                        var front_image:String=""
                        var right_image:String=""
                        var back_image:String=""
                        var left_image:String=""

                        front_image=postDetail.front_image_path
                        right_image=postDetail.right_image_path
                        left_image=postDetail.left_image_path
                        back_image=postDetail.back_image_path
                        val images = listOf(front_image,
                                right_image,
                                left_image,
                                back_image)
                        sliderImage.setItems(images)
                        sliderImage.addTimerToSlide(3000)
                        sliderImage.removeTimerSlide()
                        sliderImage.getIndicator()

                        val created_by:Int=postDetail.created_by.toInt()
                        getUserProfile(created_by,auth)

                        //Initial Related Post
                        var postType:String=""
                        val rent=postDetail.rents
                        val sale=postDetail.sales
                        val buy=postDetail.buys
                        if(rent.count()>0)
                            postType="rent"
                        if(sale.count()>0)
                            postType="sell"
                        if(buy.count()>0)
                            postType="buy"

                        initialRelatedPost(encode,postType,postDetail.category,postDetail.modeling,postDetail.cost.toFloat())

                    }
                } catch (e: JsonParseException) {
                    e.printStackTrace()
                }
            }
        })
    }

    fun getUserProfile(id:Int,encode: String){
        var user1 = User()
        var URL_ENDPOINT=ConsumeAPI.BASE_URL+"api/v1/users/"+id
        var MEDIA_TYPE=MediaType.parse("application/json")
        var client= OkHttpClient()
        var request=Request.Builder()
                .url(URL_ENDPOINT)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                //.header("Authorization",encode)
                .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                val mMessage = e.message.toString()
                Log.w("failure Response", mMessage)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val mMessage = response.body()!!.string()
                val gson = Gson()
                try {
                    user1= gson.fromJson(mMessage, User::class.java)
                    runOnUiThread {
                        CommomAPIFunction.getUserProfileFB(this@Detail_New_Post,img_user,user1.username)
                        if(user1.profile.first_name==null)
                            postUsername=user1.username
                        else
                            postUsername=user1.profile.first_name
                        postUserId=user1.username

                        if(user1.getFirst_name().isEmpty()){
                            user_name!!.setText(user1.getUsername())
                        }else{
                            user_name!!.setText(user1.getFirst_name())
                        }
                        user_telephone.setText(user1.profile.telephone)
                        user_email.setText(user1.email)
                            findViewById<CircleImageView>(R.id.cr_img).setOnClickListener {
                                val intent = Intent(this@Detail_New_Post, User_post::class.java)
                                intent.putExtra("ID",user1.id.toString())
                                intent.putExtra("Phone",user1.profile.telephone)
                                intent.putExtra("Email",user1.profile.email)
                                intent.putExtra("map",user1.profile.address)
                                intent.putExtra("Username",user1.username)
                                intent.putExtra("Name",user1.first_name)
                                startActivity(intent)
                            }
                    }

                } catch (e: JsonParseException) {
                    e.printStackTrace()
                }

            }
        })
    }

    fun Like_post(encode: String) {
        val url_like = ConsumeAPI.BASE_URL+"like/?post="+p+"&like_by="+pk
        val client = OkHttpClient()
        val request = Request.Builder()
                .url(url_like)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build()
        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {
                var response = response.body()!!.string()
                Log.d("Response",response)

                try {
                    val jsonObject = JSONObject(response)
                    val count = jsonObject.getInt("count")
                    if(count==0) {

                        var url = ConsumeAPI.BASE_URL + "like/"
                        val MEDIA_TYPE = MediaType.parse("application/json")
                        val post = JSONObject()
                        try {
                            post.put("post", p)
                            post.put("like_by", pk)
                            post.put("record_status", 1)

                                val client = OkHttpClient()
                                val body = RequestBody.create(MEDIA_TYPE, post.toString())
                                val auth = "Basic $encode"
                                val request = Request.Builder()
                                        .url(url)
                                        .post(body)
                                        .header("Accept", "application/json")
                                        .header("Content-Type", "application/json")
                                        .header("Authorization", auth)
                                        .build()
                                client.newCall(request).enqueue(object : Callback {
                                    override fun onResponse(call: Call, response: Response) {
                                        var respon = response.body()!!.string()
                                        Log.d("Response", respon)

                                        runOnUiThread {
                                         Toast.makeText(this@Detail_New_Post,"This Product add to Your Liked",Toast.LENGTH_SHORT).show()
                                        }

                                        val alertDialog = AlertDialog.Builder(this@Detail_New_Post).create()
                                        alertDialog.setMessage(R.string.like_post.toString())
                                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK"
                                        ) { dialog, which -> dialog.dismiss() }
                                        alertDialog.show()
                                    }

                                override fun onFailure(call: Call, e: IOException) {
                                    Log.d("Error", call.toString())
                                }
                            })

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

        })
    }

    fun submitCountView(encode: String) {
        var url=ConsumeAPI.BASE_URL+"countview/"
        val MEDIA_TYPE = MediaType.parse("application/json")
        val post = JSONObject()
        try{
            post.put("post", p)
            post.put("number",1)

            val client = OkHttpClient()
            val body = RequestBody.create(MEDIA_TYPE, post.toString())
            val auth = "Basic $encode"
            val request = Request.Builder()
                    .url(url)
                    .post(body)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .header("Authorization", auth)
                    .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    var respon = response.body()!!.string()
                    Log.d("Response",respon)
                }

                override fun onFailure(call: Call, e: IOException) {
                    Log.d("Error",call.toString())
                }
            })

        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun initialRelatedPost(encode:String,postType:String,category:Int,modeling:Int,cost:Float){
        val itemApi = ArrayList<Item_API>()
        val URL_ENDPOINT=ConsumeAPI.BASE_URL+"relatedpost/?post_type="+postType+"&category="+category+"&modeling="+modeling+"&min_price="+(cost-500)+"&max_price="+(cost+500)
        val client= OkHttpClient()
        val request=Request.Builder()
                .url(URL_ENDPOINT)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .header("Authorization",encode)
                .build()
        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                val mMessage = e.message.toString()
                Log.w("failure Response", mMessage)
            }
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val mMessage = response.body()!!.string()
                val gson = Gson()
                runOnUiThread {
                    try {
                        val jsonObject = JSONObject(mMessage)
                        val jsonArray = jsonObject.getJSONArray("results")
                        val jsonCount=jsonObject.getInt("count")
                        for (i in 0 until jsonArray.length()) {
                            val obj = jsonArray.getJSONObject(i)
                            val title = obj.getString("title")
                            val id = obj.getInt("id")
                            val condition = obj.getString("condition")
                            val cost = obj.getDouble("cost")
                            val image = obj.getString("front_image_path")
                            val img_user = obj.getString("right_image_base64")
                            val postType = obj.getString("post_type")
                            val phoneNumber = obj.getString("contact_phone")
                            val discount_type = obj.getString("discount_type")
                            val discount = obj.getDouble("discount")
                            var location_duration = ""
                            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                            sdf.setTimeZone(TimeZone.getTimeZone("GMT"))
                            val time: Long = sdf.parse(obj.getString("created")).getTime()
                            val now: Long = System.currentTimeMillis()
                            val ago: CharSequence = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)
                            val call = findViewById<ImageView>(R.id.btn_call)
                            call.setOnClickListener{
                                dialContactPhone(phoneNumber)
                            }
                            if(postId != id) {
                                Log.d("PostId ",postId.toString())
                                itemApi.add(Item_API(id, img_user, image, title, cost, condition, postType, ago.toString(), jsonCount.toString(),discount_type,discount))
                            }
                            list_rela!!.adapter = MyAdapter_list_grid_image(itemApi, "Grid",this@Detail_New_Post)
                            list_rela!!.layoutManager = GridLayoutManager(this@Detail_New_Post,2)
                        }
                    } catch (e: JsonParseException) {
                        e.printStackTrace()
                    }
                }

            }
        })
    }

    fun countPostView(encode:String){
        val URL_ENDPOINT=ConsumeAPI.BASE_URL+"countview/?post="+postId
        var MEDIA_TYPE=MediaType.parse("application/json")
        val client= OkHttpClient()
        val request=Request.Builder()
                .url(URL_ENDPOINT)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .header("Authorization",encode)
                .build()
        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                val mMessage = e.message.toString()
                Log.w("failure Response", mMessage)
            }
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val mMessage = response.body()!!.string()
                val gson = Gson()
                try {
                    val jsonObject = JSONObject(mMessage)
                    val jsonCount=jsonObject.getInt("count")
                    runOnUiThread {
                        tv_count_view.setText(""+jsonCount)
                    }

                } catch (e: JsonParseException) {
                    e.printStackTrace()
                }

            }
        })
    }

    fun calculateLoanMonthlyPayment(){
        var loanPrice:String=edLoanPrice.text.toString()
        var loanInterestRate:String=edLoanInterestRate.text.toString()
        var loanTerm:String=edLoanTerm.text.toString()

        var aPrice: Double=0.0
        var aInterestRate:Double=0.0
        var aLoanTerm:Int=0

        if(loanPrice.isNullOrEmpty()) aPrice=0.0 else aPrice=loanPrice.toDouble()
        if(loanInterestRate.isNullOrEmpty()) aInterestRate=1.5 else aInterestRate=loanInterestRate.toDouble()
        if(loanTerm.isNullOrEmpty()) aLoanTerm=1 else aLoanTerm=loanTerm.toInt()

        val monthlyPayment=LoanCalculator.getLoanMonthPayment(aPrice,aInterestRate,aLoanTerm)
        Log.d(TAG,loanPrice+" "+loanInterestRate+" "+monthlyPayment.toString() +" "+aPrice+" "+aInterestRate+" "+aLoanTerm)
        val df = DecimalFormat("#.####")
        df.roundingMode = RoundingMode.CEILING
        tvMonthlyPayment.setText("$ "+ df.format(monthlyPayment).toString())
    }

    private fun getEncodedString(username: String, password: String): String {
        val userpass = "$username:$password"
        return Base64.encodeToString(userpass.toByteArray(),
                Base64.NO_WRAP)
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
            //callPhone()
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

    fun getImageLocal(filePath:String):Bitmap{
        return getImageLocal(filePath,BitmapUtil.REQUEST_WIDTH,BitmapUtil.REQUEST_HEIGHT)
    }

    fun getImageLocal(filePath:String,reqWidth:Int, reqHeight:Int):Bitmap{
        if(reqWidth==-1||reqHeight==-1){ // no subsample and no
            return BitmapFactory.decodeFile(filePath)
        }else {
            // First decode with inJustDecodeBounds=true to check dimensions
            val options:BitmapFactory.Options =BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(filePath, options)

            // Calculate inSampleSize
            options.inSampleSize = BitmapUtil.calculateInSampleSize(options, reqWidth, reqHeight)
            Log.d(TAG, "options inSampleSize "+options.inSampleSize)
            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false
            return BitmapFactory.decodeFile(filePath, options)
        }
    }
    class BitmapUtil {

        companion object{
            val REQUEST_WIDTH:Int = 100
            val REQUEST_HEIGHT:Int = 100

            fun calculateInSampleSize(options:BitmapFactory.Options,reqWidth:Int,reqHeight:Int):Int{

                // Raw height and width of image
                val height:Int = options.outHeight
                val width:Int = options.outWidth
                var inSampleSize:Int = 1

                 if (height > reqHeight || width > reqWidth) {
                     val heightRatio:Int = Math.round(height.toFloat() /reqHeight.toFloat())
                     val widthRatio:Int = Math.round(width.toFloat() / reqWidth.toFloat())

                     if(heightRatio<widthRatio)
                         inSampleSize=heightRatio
                     else
                         inSampleSize=widthRatio
                 }

                return inSampleSize
            }
            fun calculateInSampleSize(options:BitmapFactory.Options):Int{
                return calculateInSampleSize(options, REQUEST_WIDTH, REQUEST_HEIGHT)
            }
        }

    }

    fun getImageUri(inContext:Context,inImage:Bitmap):Uri {
        val bytes:ByteArrayOutputStream = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path:String = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null)
        return Uri.parse(path)
    }
    private fun get_location(latitude:Double,longtitude:Double) {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation(latitude, longtitude)
        }
    }
    private fun getLocation(latitude: Double,longtitude: Double) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION)
        } else {
            val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            if (location != null) {
                try {
                    val geocoder = Geocoder(this)
                    var addressList: List<Address>? = null
                    addressList = geocoder.getFromLocation(latitude, longtitude, 1)
                    val road = addressList!![0].getAddressLine(0)
                    address_detial!!.text = road

                } catch (e: IOException) {
                    e.printStackTrace()
                }


            } else {
                Toast.makeText(this, "Unble to Trace your location", Toast.LENGTH_SHORT).show()
            }
        }
    }  //
    override fun onMapReady(googleMap: GoogleMap?) {

        if (googleMap != null) {
            mMap = googleMap
        }
        val current_location = LatLng(latitude, longtitude)
        mMap.animateCamera(CameraUpdateFactory.zoomIn())
        mMap.animateCamera(CameraUpdateFactory.zoomTo(5f), 2000, null)
        val cameraPosition = CameraPosition.Builder()
                .target(current_location)
                .zoom(18f)
                .bearing(90f)
                .tilt(30f)
                .build()
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        mMap.addMarker(MarkerOptions().position(LatLng(latitude, longtitude)))
    }
}
