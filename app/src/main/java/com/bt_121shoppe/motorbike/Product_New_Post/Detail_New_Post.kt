package com.bt_121shoppe.motorbike.Product_New_Post

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
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
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.*
import android.text.format.DateUtils
import android.text.style.CharacterStyle
import android.text.style.StrikethroughSpan
import android.util.Base64
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.KeyEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import android.view.animation.ScaleAnimation
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bt_121shoppe.motorbike.Activity.Home
import com.custom.sliderimage.logic.SliderImage
import com.bt_121shoppe.motorbike.Activity.Item_API
import com.bt_121shoppe.motorbike.Api.ConsumeAPI
import com.bt_121shoppe.motorbike.Api.User
import com.bt_121shoppe.motorbike.Login_Register.UserAccountActivity
import com.bt_121shoppe.motorbike.Product_dicount.Detail_Discount
import com.bt_121shoppe.motorbike.useraccount.User_post
import com.bt_121shoppe.motorbike.R
import com.bt_121shoppe.motorbike.chats.ChatActivity
import com.bt_121shoppe.motorbike.firebases.FBPostCommonFunction
import com.bt_121shoppe.motorbike.loan.Create_Load
import com.bt_121shoppe.motorbike.loan.LoanCreateActivity
import com.bt_121shoppe.motorbike.models.PostViewModel
import com.bt_121shoppe.motorbike.utils.CommomAPIFunction
import com.bt_121shoppe.motorbike.utils.CommonFunction
import com.bt_121shoppe.motorbike.utils.LoanCalculator

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.CameraUpdateFactory

//import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.google.gson.JsonParseException
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.content_home.*
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import org.w3c.dom.CharacterData
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.StringWriter
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.CharacterIterator
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*
import java.util.EventListener
import kotlin.collections.ArrayList
import kotlin.math.round

class Detail_New_Post : AppCompatActivity() , OnMapReadyCallback {
    private val TAG = Detail_Discount::class.java.simpleName
    private lateinit var mMap: GoogleMap
    private val REQUEST_LOCATION = 1
    internal lateinit var locationManager: LocationManager
    internal var latitude:Double = 0.toDouble()
    internal var longtitude:Double = 0.toDouble()
    private var list_rela: RecyclerView? = null
    private var relativecal: RelativeLayout? = null
    private lateinit var txtundercal:TextView
    private lateinit var show_amount:String
//    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
//    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
//    private var mLocationPermissionGranted: Boolean = false
//    private var mLastKnownLocation: Location? = null
    private var REQUEST_PHONE_CALL =1;
    private var postId:Int=0
    private var pk=0
    private var name = ""
    private var pass = ""
    private var Encode=""
    private var p=0
    private var pt=0
    internal lateinit var Layout_call_chat_like_loan: ConstraintLayout
    internal lateinit var txt_detail_new: TextView
    private lateinit var tvPostTitle:TextView
    private lateinit var tvPostCode:TextView
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
    private lateinit var call_phone:ImageView
    private var postTitle:String=""
    private lateinit var postPrice:String
    private lateinit var postFrontImage:String
    private lateinit var postUsername:String
    private lateinit var postUserId:String
    private lateinit var postType:String
    private lateinit var con:String
    private lateinit var col:String
    private lateinit var address_map: String
    var discount: Double = 0.0
    var encodeAuth:String = ""
    lateinit var sharedPref: SharedPreferences
    lateinit var loan:ImageView

    var postDetail = PostViewModel()
    lateinit var mprgressbar: ProgressBar
    lateinit var mtext_onresult: TextView
    private lateinit var show_deposit:TextView
    var st:String = "0"
    var st2: Double = 0.0

    val decimal:Int = 0
    lateinit var afc:String

    var jakl:String = "0"
    var ko:String = "0"
    var ms:Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_new_post)
        locale()

        if (ContextCompat.checkSelfPermission(this@Detail_New_Post, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@Detail_New_Post, arrayOf(Manifest.permission.CALL_PHONE), REQUEST_PHONE_CALL)
        }

        show_deposit = findViewById(R.id.view_deposit)
        relativecal = findViewById(R.id.rlLoanCalculation)
        txtundercal = findViewById(R.id.text)
//        checkPermission()
        postId = intent.getIntExtra("ID",0)
        discount = intent.getDoubleExtra("Discount",0.0)
        Log.d("123456789 :",discount.toString())
        sharedPref = getSharedPreferences("Register", Context.MODE_PRIVATE)
        name = sharedPref.getString("name", "")
        pass = sharedPref.getString("pass", "")
        Encode = getEncodedString(name,pass)

        if (sharedPref.contains("token")) {
           pk = sharedPref.getInt("Pk",0)
        } else if (sharedPref.contains("id")) {
           pk = sharedPref.getInt("id", 0)
        }
        if (pk!=0) {
            encodeAuth = "Basic "+ getEncodedString(name,pass)
//            getMyLoan()
        }
        p = intent.getIntExtra("ID",0)
        pt=intent.getIntExtra("postt",0)
        initialProductPostDetail(Encode)
        submitCountView(Encode)
        countPostView(Encode)
        list_rela = findViewById(R.id.list_rela)
        //Back
        val back = findViewById<TextView>(R.id.tv_back)
        back.setOnClickListener {
            if (postId!= null) {
                startActivity(Intent(this@Detail_New_Post, Home::class.java))
            } else finish()
        }
        //Slider

        Layout_call_chat_like_loan = findViewById(R.id.Constrainlayout_call_chat_like_loan)

        sliderImage = findViewById(R.id.slider)
        tvPostTitle = findViewById(R.id.title)
        tvPrice = findViewById(R.id.tv_price)
//        tvPrice1 = findViewById(R.id.tv_price1)
        tvDiscount = findViewById(R.id.tv_discount)
//        tvBrand=findViewById(R.id.tvBrand)
//        tvModel=findViewById(R.id.tv_Model)
//        tvYear=findViewById(R.id.tv_Year)
        tvCondition=findViewById(R.id.tv_Condition)
//        tvColor=findViewById(R.id.tv_Color)
        tvDescription=findViewById(R.id.tv_Description)
        tvPostCode = findViewById(R.id.tvPostCode)
        tvMonthlyPayment=findViewById(R.id.tvMonthlyPayment)
        edLoanPrice=findViewById(R.id.ed_loan_price)
        edLoanInterestRate=findViewById(R.id.ed_loan_interest_rate)
        edLoanDeposit=findViewById(R.id.ed_loan_deposit)
        edLoanTerm=findViewById(R.id.ed_loan_term)
        user_name = findViewById(R.id.tv_name)
        img_user = findViewById(R.id.cr_img)
        user_telephone=findViewById(R.id.tv_phone)
        user_email=findViewById(R.id.tv_email)
        tv_count_view=findViewById(R.id.view)
//        tv_location_duration=findViewById(R.id.tv_location_duration)
        address_detial = findViewById(R.id.address)
        call_phone = findViewById(R.id.btn_call)

        mprocessBar = findViewById(R.id.mprogressbar)
        mprocessBar.visibility = View.VISIBLE
        tex_noresult = findViewById(R.id.txt_noresult)

        //Button Share
        val share = findViewById<ImageButton>(R.id.btn_share)
//        share.setOnClickListener{
//            val shareIntent = Intent()
//            shareIntent.action = Intent.ACTION_SEND
//            shareIntent.type="text/plain"
//            shareIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
//            startActivity(Intent.createChooser(shareIntent,getString(R.string.title_activity_account)))
//        }
        loan = findViewById(R.id.btn_loan)
        loan.setOnClickListener{
            if (!(sharedPref.contains("token") || sharedPref.contains("id"))) {
                val intent = Intent(this@Detail_New_Post, UserAccountActivity::class.java)
                intent.putExtra("verify","detail")
                intent.putExtra("product_id",postId)
                startActivity(intent)
            }else{
                val intent = Intent(this@Detail_New_Post, Create_Load::class.java)
                intent.putExtra("product_id",postId)
//                Log.e("343434343",cuteString(tvPrice.text.toString(),1))
                intent.putExtra("price",cuteString(tvPrice.text.toString(),1))
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            }
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
                val intent = Intent(this@Detail_New_Post, UserAccountActivity::class.java)
                intent.putExtra("verify","detail")
                intent.putExtra("product_id",postId)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            }
        }

        val like = findViewById<ImageView>(R.id.btn_like)
        like.setOnClickListener {

            if (sharedPref.contains("token") || sharedPref.contains("id")) {
                Like_post(Encode)
            }else{
                val intent = Intent(this@Detail_New_Post, UserAccountActivity::class.java)
                intent.putExtra("verify","detail")
                intent.putExtra("product_id",postId)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            }
        }

        edLoanDeposit.setHint("0.0")
        edLoanInterestRate.setText("1.5")
        edLoanTerm.setText("24")

        tvMonthlyPayment.setText("$ 0.00")

        edLoanPrice.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN /***&& event.action == KeyEvent.KeyCode*/) {
                //Perform Code
                calculateLoanMonthlyPayment()
                return@OnKeyListener true
            }
            false
        })
        edLoanPrice.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                calculateLoanMonthlyPayment()
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })

        edLoanDeposit.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                //Perform Code
//                Toast.makeText(this@Detail_New_Post, calculateLoanMonthlyPayment().toString(), Toast.LENGTH_SHORT).show()
//                calculateLoanMonthlyPayment()

                return@OnKeyListener true
            }
            false
        })

        edLoanInterestRate.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                //Perform Code
//                Toast.makeText(this@Detail_New_Post, edLoanPrice.getText(), Toast.LENGTH_SHORT).show()
//                calculateLoanMonthlyPayment()
                return@OnKeyListener true
            }
            false
        })

        edLoanInterestRate.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0:CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                calculateLoanMonthlyPayment()

                jakl = p0.toString()
                if (jakl.isEmpty()){
                    jakl = "0"
                    Log.d("When empty",jakl)
                }

                var lol:Double = jakl.toDouble()
                var dota:Double = ko.toDouble() // =0

//                var koca:String = lol.toString()
//                var kaca:String = dota.toString()

                if (lol.equals(dota)){
                    tvMonthlyPayment.text = "$0"
//                    ms = jakl.toDouble()
                    Log.d("For sero", lol.toString())
                }

                else{
                    Log.d("For do", lol.toString())
                    ms = jakl.toDouble()
//                    jakl = "0"
                }

//                if (jakl != null) {
//
//                    if (jakl.isEmpty()){
//                        st2 = jakl.toDouble()
//                        tvMonthlyPayment.setText("$0")
//                    }
//                    else if (jakl.equals("0".toString())){
//                        jakl = "0"
//                        Log.d("Dota2esports",jakl)
//                        st2 = jakl.toDouble()
//                        tvMonthlyPayment.setText("$0")
//                        Log.d("kaksdnansh",jakl)
//                    }
//                    else {
//                        st2 = jakl.toDouble()
//                        Log.d("lolesports",jakl)
//                        tvMonthlyPayment.visibility = View.VISIBLE
//                    }
//                }

            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })

        edLoanTerm.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                //Perform Code
//                Toast.makeText(this@Detail_New_Post, calculateLoanMonthlyPayment().toString(), Toast.LENGTH_SHORT).show()
                return@OnKeyListener true
            }
            false
        })

        edLoanTerm.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                calculateLoanMonthlyPayment()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                calculateLoanMonthlyPayment()
                var hamu:String = p0.toString()
                if (hamu.equals(ko)){
                    tvMonthlyPayment.setText("$0.00")
                    tvMonthlyPayment.visibility = View.VISIBLE
                }
                else if (hamu.isEmpty()){
                    tvMonthlyPayment.setText("$0.00")
                    tvMonthlyPayment.visibility = View.INVISIBLE
                }
                else if (hamu.isNotEmpty()){
                    tvMonthlyPayment.visibility = View.VISIBLE
                    Log.d("moskdadafa",hamu)
                }
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })

    }  // oncreate

//    fun dialContactPhone(phoneNumber:String) {
//        startActivity( Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)))
//    }
//    fun sms(phoneNumber:String) {
//        val sendIntent =  Intent(Intent.ACTION_VIEW)
//        sendIntent.putExtra("address"  ,phoneNumber)
//        sendIntent.putExtra("sms_body", "")
//        sendIntent.setType("vnd.android-dir/mms-sms")
//        startActivity(sendIntent)
//    }

    override fun onBackPressed() {
        val intent = Intent (this, Home::class.java)
        startActivity(intent)
        finish()
    }

    fun cuteString(st: String, indext: Int): String {
        val separated = st.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return separated[indext]
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
                Log.d(TAG+"3333",mMessage)
                val gson = Gson()
                try {
                    runOnUiThread {

                        postDetail = gson.fromJson(mMessage, PostViewModel::class.java)
                        Log.e(TAG,"D"+ mMessage)
 // hide button call chat like loan by samang 27/08/19

                        var create_by =  postDetail.created_by.toInt()
                        if (create_by == pk){
                            Layout_call_chat_like_loan.visibility = View.GONE
                        }

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
//                                        tvYear.setText(jsonObject.getString("year"))
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
//                                .header("Authorization", auth)
                                .build()
                        client2.newCall(request2).enqueue(object : Callback {
                            @Throws(IOException::class)
                            override fun onResponse(call: Call, response: Response) {
                                val mMessage = response.body()!!.string()
                                runOnUiThread {
                                    try {
                                        val jsonObject = JSONObject(mMessage)
                                        Log.d("modeling_name",jsonObject.getString("modeling_name"))
                                        Log.d("Language",language)
                                        if(language.equals("km")){
//                                            tvModel.setText(jsonObject.getString("modeling_name_kh"))
                                        }else if(language.equals("en")){
//                                            tvModel.setText(jsonObject.getString("modeling_name"))
                                        }

                                        val url3=ConsumeAPI.BASE_URL+"api/v1/brands/"+jsonObject.getString("brand")
                                        val client3=OkHttpClient()
                                        val request3 = Request.Builder()
                                                .url(url3)
                                                .header("Accept", "application/json")
                                                .header("Content-Type", "application/json")
//                                                .header("Authorization", auth)
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
//                                                            tvBrand.setText(jsonObject.getString("brand_name_as_kh"))
                                                        }else if(language.equals("en")){
//                                                            tvBrand.setText(jsonObject.getString("brand_name"))
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
//Add by Raksmey 02/10/2019
                        var ptitle:String
                        if (postDetail.post_sub_title.isEmpty()){
                            postTitle=CommonFunction.generatePostSubTitle(postDetail.modeling,postDetail.year,postDetail.color)
                            if (language.equals("en"))
                                ptitle = postTitle.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0]
                            else
                                ptitle = postTitle.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1]
                        } else
                            if (language.equals("en"))
                                ptitle = postDetail.getPost_sub_title().split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0]
                            else
                                ptitle = postDetail.getPost_sub_title().split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1]
                        tvPostTitle.setText(ptitle)

                        postTitle=ptitle

                        postPrice=discount.toString()

                        postFrontImage=postDetail.front_image_path.toString()
                        postType=postDetail.post_type
//                        tvPostTitle.setText(postDetail.title.toString())

//                        if (postDetail.title.toString().isNotEmpty()){
//                            tvPostTitle.setText(postDetail.title.toString())
//                        }else{
//                            tvPostTitle.setText(postDetail.post_sub_title.toString())
//                        }
//
                        tvPostTitle.setTextSize(22F)
                        tvPostTitle.setTextColor(getColor(R.color.sunflower_black))

                        tvPrice.setText("$ "+ discount)

                        edLoanPrice.setText(""+discount)
                        tvPostCode.setText(postDetail.post_code.toString())
//                        tvPostCode.setText(postDetail.id.toString())

                        show_amount = "$"+discount.toString()

//                        tvPrice1.setText("$"+ postDetail.cost)

                        edLoanDeposit.addTextChangedListener(object: TextWatcher {
                            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                                calculateLoanMonthlyPayment()
                            }

                            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                                calculateLoanMonthlyPayment()
                                var jk:String = "$0.0"
//                                var str:String = "."
//                                str = String.format("%08d")
                                st = p0.toString()
                                if (st.isEmpty()){
                                    st = "0"
                                    Log.d("vetaneateanea","oracle")
                                    st2  = st.toDouble()
                                }else{
                                    Log.d("hamehameha",st)
                                    st2  = st.toDouble()
                                }

//                                Log.d("jakata",ds.toString())
                                Log.d("jakata", "212121$st2")
                                if(st2 > discount){
                                    show_deposit.setTextColor(resources.getColor(R.color.red))
                                    show_deposit.setText(R.string.deposit_message)
                                    tvMonthlyPayment.text = "$0.00"
                                    tvMonthlyPayment.setTextColor(resources.getColor(R.color.red))
                                }
                                else if (st2 <= discount){
                                    show_deposit.setText("")
                                    tvMonthlyPayment.setTextColor(resources.getColor(R.color.colorSubmitButton))
                                    tvMonthlyPayment.visibility = View.VISIBLE
                                }
                            }
                            override fun afterTextChanged(p0: Editable?) {
//                                calculateLoanMonthlyPayment()
                            }
                        })

//                        Log.d("767676", discount.toString())
                        if (discount == 0.00){
                            tvDiscount.visibility = View.GONE
//                            tvPrice.visibility = View.GONE
                            show_amount = "$"+postDetail.cost.toString()
                            tvPrice.text ="$ "+postDetail.cost
//                            show_amount_loan = postDetail.cost.toString()
                            edLoanPrice.setText(""+postDetail.cost)
                            postPrice=postDetail.cost.toString()

                            edLoanDeposit.addTextChangedListener(object: TextWatcher {
                                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                                    calculateLoanMonthlyPayment()
                                }
                                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                                    calculateLoanMonthlyPayment()
                                    var ds:Double = postDetail.cost.toDouble()

                                    st = p0.toString()
                                    if (st.isEmpty()){
                                        st = "0"
                                        st2  = st.toDouble()
                                    }else{
                                        Log.d("hamehameha",st)
                                        st2  = st.toDouble()
                                    }

                                    Log.d("jakata",ds.toString())
                                    Log.d("jakata","212121"+st2)
                                    if(st2 > ds){
                                        show_deposit.setTextColor(resources.getColor(R.color.red))
                                        show_deposit.setText(R.string.deposit_message)
//                                    tvMonthlyPayment.visibility = View.INVISIBLE
                                        tvMonthlyPayment.text = "$0.00"
                                        tvMonthlyPayment.setTextColor(resources.getColor(R.color.red))

                                    }
                                    else if (st2 <= ds){
                                        show_deposit.setText("")
                                        tvMonthlyPayment.setTextColor(resources.getColor(R.color.colorSubmitButton))
                                        tvMonthlyPayment.visibility = View.VISIBLE
                                    }
                                }
                                override fun afterTextChanged(p0: Editable?) {
//                                    calculateLoanMonthlyPayment()
                                }
                            })
                        }else{
//                            tvPrice.visibility = View.GONE
                        }

                        var st = "$ "+postDetail.cost
                        st = st.substring(0, st.length-1)
                        val ms = SpannableString(st)
                        val mst = StrikethroughSpan()
                        ms.setSpan(mst,0,st.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        tvDiscount.text = ms
                        con=postDetail.condition.toString()
                        col=postDetail.color.toString()
                        if (con == "new") {
                            tvCondition.setText(R.string.newl)
                        } else if (con == "used") {
                            tvCondition.setText(R.string.used)
                        }
//                        if (col == "blue") {
//                            tvColor.setText(R.string.blue)
//                        } else if (col == "black") {
//                            tvColor.setText(R.string.black)
//                        } else if (col == "silver") {
//                            tvColor.setText(R.string.silver)
//                        } else if (col == "red") {
//                            tvColor.setText(R.string.red)
//                        } else if (col == "gray") {
//                            tvColor.setText(R.string.gray)
//                        } else if (col == "yellow") {
//                            tvColor.setText(R.string.yellow)
//                        } else if (col == "pink") {
//                            tvColor.setText(R.string.pink)
//                        } else if (col == "purple") {
//                            tvColor.setText(R.string.purple)
//                        } else if (col == "orange") {
//                            tvColor.setText(R.string.orange)
//                        } else if (col == "green") {
//                            tvColor.setText(R.string.green)
//                        }
                        tvDescription.setText(postDetail.description.toString())
                        //user_email.setText(postDetail.contact_email.toString())
                        user_email.setText(postDetail.contact_email.toString())
                        user_name.text=postDetail.machine_code.toString()

                        val contact_phone = postDetail.contact_phone.toString()
                            Phone_call(contact_phone)
                        val splitPhone = contact_phone.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                            if(splitPhone.size == 1){
                                user_telephone.text = splitPhone[0]
                            }else if (splitPhone.size == 2){
                                user_telephone.text = splitPhone[0]+" / "+splitPhone[1]
                            }else if (splitPhone.size == 3){
                                user_telephone.text = splitPhone[0]+" / "+splitPhone[1]+" / "+splitPhone[2]
                            }

                        val addr = postDetail.contact_address.toString()
//                        Log.d("LAAAAA",addr)
                        address_map = addr     // use for user detail when user no address shop
                        var time:Long
                        if(addr.isEmpty()) {

                        }else{
                            val splitAddr = addr.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                            latitude = java.lang.Double.valueOf(splitAddr[0])
                            longtitude = java.lang.Double.valueOf(splitAddr[1])

                            get_location(latitude, longtitude)
                            val mapFragment = supportFragmentManager
                                    .findFragmentById(R.id.map_detail_newpost) as SupportMapFragment?
                            mapFragment?.getMapAsync(OnMapReadyCallback {
                                this@Detail_New_Post.onMapReady(it)
                            })
                        }

                        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                        sdf.timeZone = TimeZone.getTimeZone("GMT+7")
                        if (pt == 1){
                            time = sdf.parse(postDetail.created).time
                        }else if(pt == 2){ //pt = 2 for detail history post user
                            if (postDetail.modified == null){
                                time = sdf.parse(postDetail.created).time
                            }else{
                                val mdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                                mdf.timeZone = TimeZone.getTimeZone("GMT")
                                time = mdf.parse(postDetail.modified).time
                            }
                        } else{
                            time = sdf.parse(postDetail.approved_date).time
                            //time=0\
                        }
//closed post date by Raksmey 11/09/2019
//                        val now:Long = System.currentTimeMillis()
//                        val ago:CharSequence = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)
//                        tv_location_duration.setText(ago)

                        val base64_front_image=postDetail.front_image_path.toString()
                        val base64_right_image=postDetail.right_image_path.toString()
                        val base64_left_image=postDetail.left_image_path.toString()
                        val base64_back_image=postDetail.back_image_path.toString()
                        var front_image:String=""
                        var right_image:String=""
                        var back_image:String=""
                        var left_image:String=""
                        var extra_image1:String=""
                        var extra_image2:String=""
                        front_image=postDetail.front_image_path
                        right_image=postDetail.right_image_path
                        left_image=postDetail.left_image_path
                        back_image=postDetail.back_image_path

                        val arrayList2 = ArrayList<String>(6)
                        arrayList2.add(front_image)
                        arrayList2.add(right_image)
                        arrayList2.add(left_image)
                        arrayList2.add(back_image)
                        if (postDetail.extra_image1!=null){
                            arrayList2.add(postDetail.extra_image1)
                        }
                        if (postDetail.extra_image2!=null){
                            arrayList2.add(postDetail.extra_image2)
                        }

                        Log.d("@ moret image","numbers:"+extra_image1+","+extra_image2)
//                        val images = listOf(front_image,
//                                right_image,
//                                left_image,
//                                back_image
//                                extra_image1,
//                                extra_image2
//                        )
                        sliderImage.setItems(arrayList2)
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
                        if(rent.count()>0) {
                            postType = "rent"
                            txtundercal.visibility = View.GONE
                            relativecal!!.visibility = View.GONE
                        }
                        if(sale.count()>0) {
                            postType = "sell"
                            txtundercal.visibility = View.VISIBLE
                            relativecal!!.visibility = View.VISIBLE
                        }
                        if(buy.count()>0) {
                            postType = "buy"
                            txtundercal.visibility = View.GONE
                            relativecal!!.visibility = View.GONE
                        }
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
                    Log.d(TAG,"TAH"+mMessage)
                    runOnUiThread {

//                        val profilepicture: String=if(user1.profile.base64_profile_image==null) "" else user1.profile.base64_profile_image
//                        if(profilepicture.isNullOrEmpty()){
//                            img_user.setImageResource(R.drawable.user)
//                        }else {
//                            val decodedString = Base64.decode(profilepicture, Base64.DEFAULT)
//                            var decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
//                            img_user.setImageBitmap(decodedByte)
//                        }

                        CommomAPIFunction.getUserProfileFB(this@Detail_New_Post,img_user,user1.username)

                        if(user1.profile.first_name==null)
                            postUsername=user1.username
                        else
                            postUsername=user1.profile.first_name
                        postUserId=user1.username
//
//                        if(user1.getFirst_name().isEmpty()){
//                            user_name!!.setText(user1.getUsername())
//                        }else{
//                            user_name!!.setText(user1.getFirst_name())
//                        }

//                        user_telephone.setText(user1.profile.telephone)
//                        user_email.setText(user1.email)
                        findViewById<CircleImageView>(R.id.cr_img).setOnClickListener {
                            //                            Log.d(TAG,"Tdggggggggggggg"+user1.profile.telephone)
                            val intent = Intent(this@Detail_New_Post, User_post::class.java)
                            intent.putExtra("ID",user1.id.toString())
                            intent.putExtra("Phone",user1.profile.telephone)
                            intent.putExtra("Email",user1.email)
                            intent.putExtra("map_address",user1.profile.address)
                            intent.putExtra("map_post",address_map)         // use for user detail when user no address shop
                             //intent.putExtra("Phone",phone.text)
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

    fun Phone_call(contactPhone: String) {
        val splitPhone = contactPhone.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()

        call_phone.setOnClickListener {

            val dialog = BottomSheetDialog(it.context)
            val view = layoutInflater.inflate(R.layout.call_sheet_dialog,null)
            dialog.setContentView(view)
            dialog.show()

            val phone1= view.findViewById<TextView>(R.id.call_phone1)
            val phone2= view.findViewById<TextView>(R.id.call_phone2)
            val phone3= view.findViewById<TextView>(R.id.call_phone3)
            if (splitPhone.size == 1){
                phone1.visibility = View.VISIBLE
                phone1.text = "  "+splitPhone[0]
                phone1.setOnClickListener {
//                    val intent =  Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", splitPhone[0], null))
//                    startActivity(intent)
                    val intent = Intent().apply {
                        action = Intent.ACTION_DIAL
                        data = Uri.parse("tel:"+splitPhone[0])
                    }
                    startActivity(intent)
                }
            }else if (splitPhone.size == 2){
                phone1.visibility = View.VISIBLE
                phone2.visibility = View.VISIBLE
                phone1.text = "  "+splitPhone[0]
                phone2.text = "  "+splitPhone[1]
                phone1.setOnClickListener {
//                    val intent =  Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", splitPhone[0], null))
//                    startActivity(intent)
                    val intent = Intent().apply {
                        action = Intent.ACTION_DIAL
                        data = Uri.parse("tel:"+ splitPhone[0])
                    }
                    startActivity(intent)
                }
                phone2.setOnClickListener {
//                    val intent =  Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", splitPhone[1], null))
//                    startActivity(intent)
                    val intent = Intent().apply {
                        action = Intent.ACTION_DIAL
                        data = Uri.parse("tel:"+splitPhone[1])
                    }
                    startActivity(intent)
                }
            }else if (splitPhone.size == 3){

                phone1.visibility = View.VISIBLE
                phone2.visibility = View.VISIBLE
                phone3.visibility = View.VISIBLE
                phone1.text = "  "+splitPhone[0]
                phone2.text = "  "+splitPhone[1]
                phone3.text = "  "+splitPhone[2]

                Log.d("Phone 3:",splitPhone[0]+","+ splitPhone[1] +","+ splitPhone[2])
                phone1.setOnClickListener {
//                    val intent =  Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", splitPhone[0], null))
//                    startActivity(intent)
                    val intent = Intent().apply {
                        action = Intent.ACTION_DIAL
                        data = Uri.parse("tel:"+splitPhone[0])
                    }
                    startActivity(intent)
                }
                phone2.setOnClickListener {
//                    val intent =  Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", splitPhone[1], null))
//                    startActivity(intent)
                    val intent = Intent().apply {
                        action = Intent.ACTION_DIAL
                        data = Uri.parse("tel:"+splitPhone[1])
                    }
                    startActivity(intent)
                }
                phone3.setOnClickListener {
//                    val intent =  Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", splitPhone[2], null))
//                    startActivity(intent)
                    val intent = Intent().apply {
                        action = Intent.ACTION_DIAL
                        data = Uri.parse("tel:"+splitPhone[2])
                    }
                    startActivity(intent)
                }
            }
        }  // call
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
                                            val alertDialog = AlertDialog.Builder(this@Detail_New_Post).create()
                                            alertDialog.setMessage(getString(R.string.like_post))
                                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok)
                                            ) { dialog, which ->
                                                dialog.dismiss()
                                            }
                                            alertDialog.show()
                                        }

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
                Log.d("URL123",URL_ENDPOINT)
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
                        if (jsonCount == 1 || jsonCount == 0){
                            mprocessBar.visibility = View.GONE
                            tex_noresult.visibility = View.VISIBLE
                        }
                        mprocessBar.visibility = View.GONE

                        for (i in 0 until jsonArray.length()) {
                            val obj = jsonArray.getJSONObject(i)
                            val title = obj.getString("title")
                            val id = obj.getInt("id")
                            val user_id = obj.getInt("created_by")
                            val condition = obj.getString("condition")
                            val cost = obj.getDouble("cost")
                            val image = obj.getString("front_image_path")
                            val img_user = obj.getString("right_image_path")
                            val postType = obj.getString("post_type")
                            val phoneNumber = obj.getString("contact_phone")
                            val discount_type = obj.getString("discount_type")
                            val discount = obj.getDouble("discount")
                            val postsubtitle = obj.getString("post_sub_title")
                            val color = obj.getString("color")
                            val model = obj.getInt("modeling")
                            val year = obj.getInt("year")
                            var location_duration = ""
                            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                            sdf.setTimeZone(TimeZone.getTimeZone("GMT"))
                            val time: Long = sdf.parse(obj.getString("approved_date")).getTime()
                            val now: Long = System.currentTimeMillis()
                            val ago: CharSequence = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)
                            Log.d("1212121UserId ",user_id.toString())
                            if(postId != id) {

                                itemApi.add(Item_API(id,user_id,img_user, image, title, cost, condition, postType, ago.toString(), jsonCount.toString(),color,model,year,discount_type,discount,postsubtitle))
//                                itemApi.add(Modeling(id,userId,img_user,image,title,cost,condition,postType,location_duration,jsonCount.toString(),discount_type,discount))
                            }
                            list_rela!!.adapter = MyAdapter_list_grid_image(itemApi, "Grid",this@Detail_New_Post)
                            list_rela!!.layoutManager = GridLayoutManager(this@Detail_New_Post,2) as RecyclerView.LayoutManager?
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
                Log.d("JJJJJJJJJJJJJ",mMessage)
                val gson = Gson()
                try {
                    val jsonObject = JSONObject(mMessage)
                    val jsonCount=jsonObject.getInt("count")
                    Log.d("1212121 id",postId.toString())
                    Log.d("121212121view",jsonCount.toString())
                    runOnUiThread {
                        tv_count_view.setText(""+jsonCount)
                        //submit count view to firebase
                        FBPostCommonFunction.addCountView(postId.toString(),jsonCount)
                    }

                } catch (e: JsonParseException) {
                    e.printStackTrace()
                }

            }
        })
    }

    fun calculateLoanMonthlyPayment(){
        var loanPrice:String=edLoanPrice.text.toString()
        var loanDeposit:String=edLoanDeposit.text.toString()
        var loanInterestRate:String=edLoanInterestRate.text.toString()
        var loanTerm:String=edLoanTerm.text.toString()

        var aPrice: Double=0.0
        var aDeposit: Double=0.0
        var aInterestRate:Double=0.0
        var aLoanTerm:Int=0

        if(loanPrice.isNullOrEmpty()) aPrice=0.0 else aPrice=loanPrice.toDouble()
        if(loanDeposit.isNullOrEmpty()) aDeposit= 0.0 else aDeposit=loanDeposit.toDouble()
        if(loanInterestRate.isNullOrEmpty()) aInterestRate = 0.0 else aInterestRate=loanInterestRate.toDouble()
        if(loanTerm.isNullOrEmpty()) aLoanTerm=0 else aLoanTerm=loanTerm.toInt()

        val monthlyPayment=LoanCalculator.getLoanMonthPayment(aPrice,aDeposit,aInterestRate,aLoanTerm)
        //Log.d(TAG,loanPrice+" "+loanInterestRate+" "+monthlyPayment.toString() +" "+aPrice+" "+aInterestRate+" "+aLoanTerm)
//        val df:String
//        df = BigDecimal(monthlyPayment).setScale(2,RoundingMode.CEILING).toString()
//        tvMonthlyPayment.text = "$" + df

        var df = DecimalFormatSymbols(Locale.US)

        val me = DecimalFormat("#.##", df)
        me.roundingMode = RoundingMode.HALF_EVEN
        tvMonthlyPayment.setText("$" + me.format(monthlyPayment))
    }

    private fun getEncodedString(username: String, password: String): String {
        val userpass = "$username:$password"
        return Base64.encodeToString(userpass.toByteArray(),
                Base64.NO_WRAP)
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

                    address_detial.text = road

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
                .zoom(14f)
                .bearing(90f)
                .tilt(30f)
                .build()
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        mMap.addMarker(MarkerOptions().position(LatLng(latitude, longtitude)))
    }
    fun language(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val confi = Configuration()
        confi.locale = locale
        baseContext.resources.updateConfiguration(confi, baseContext.resources.displayMetrics)
        val editor = getSharedPreferences("Settings", MODE_PRIVATE).edit()
        editor.putString("My_Lang", lang)
        editor.apply()
    }

    fun locale() {
        val prefer = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language = prefer.getString("My_Lang", "")
        Log.d("language",language)
        language(language)
    }

    private fun getMyLoan() {
        val IDPOST = ArrayList<Int>()
        var loaned = false
        Log.d("12345","Hello90"+encodeAuth)
        val URL_ENDPOINT= ConsumeAPI.BASE_URL+"loanbyuser/"
        val client= OkHttpClient()
        val request= Request.Builder()
                .url(URL_ENDPOINT)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .header("Authorization",encodeAuth)
                .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                val mMessage = e.message.toString()
                Log.w("failure Response", mMessage)
            }
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val mMessage = response.body()!!.string()
                Log.d(TAG,"Laon_status "+mMessage)
                val jsonObject = JSONObject(mMessage)
                val jsonArray = jsonObject.getJSONArray("results")
                try {
                    runOnUiThread {
                        for (i in 0 until jsonArray.length()) {
                            val obj = jsonArray.getJSONObject(i)
                            val post_id = obj.getInt("post")

                            Log.d("Status Id123",post_id.toString())
                            Log.d("Status 123",postId.toString())
                            IDPOST.add(post_id)

                        }
                        Log.d("ARRayList",IDPOST.size.toString())
                        for (i in 0 until IDPOST.size)
                            if(IDPOST.get(i) == postId){
                               loaned = true
                            }
                        loan.setOnClickListener {
//                            Log.d("IDFOR","Loanded"+loaned.toString())
//                            Log.d("IDPOST","HeyPro"+postId.toString())
                            if (loaned){
//                                Toast.makeText(this@Detail_New_Post,"Created",Toast.LENGTH_SHORT).show()
                                withStyle()

                            }else{
                                val intent = Intent(this@Detail_New_Post, LoanCreateActivity::class.java)
//                                intent.putExtra("Show_amount",show_amount_loan)
                                intent.putExtra("PutIDLoan",postId)
                                startActivity(intent)
                            }
                        }
                    }

                } catch (e: JsonParseException) {
                    e.printStackTrace() }

            }
        })
    }

    fun withStyle() {
        val builder = AlertDialog.Builder(ContextThemeWrapper(this,R.style.AppTheme))
//        val builder = AlertDialog.Builder(this)
        with(builder)
        {
            setTitle(R.string.for_loan_title)
            setIcon(R.drawable.ic_message_black_24dp)
            setMessage(R.string.already_created)
            setPositiveButton(getString(R.string.ok), DialogInterface.OnClickListener { dialog, which ->
                refresh
                dialog.dismiss()
            })
            show()
            setCancelable(false)
        }
    }

    /*------------------ check call permission -------------------*/
//    fun isPermissionGrandted():Boolean{
//        if(Build.VERSION.SDK_INT>=23){
//            if(checkSelfPermission(android.Manifest.permission.CALL_PHONE)==PackageManager.PERMISSION_GRANTED){
//                Log.d(TAG,"Permission is granted.")
//                //return true
//            }
//        }
//    }

}
