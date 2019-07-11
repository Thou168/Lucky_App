package com.example.lucky_app.Product_New_Post

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.drm.DrmStore
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.custom.sliderimage.logic.SliderImage
import com.example.lucky_app.Api.ConsumeAPI
import com.example.lucky_app.Api.User
import com.example.lucky_app.Product_dicount.Detail_Discount
import com.example.lucky_app.Product_dicount.User_post
import com.example.lucky_app.R
import com.example.lucky_app.loan.LoanCreateActivity
import com.example.lucky_app.models.PostViewModel
import com.example.lucky_app.utils.LoanCalculator
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.gson.Gson
import com.google.gson.JsonParseException
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

class Detail_New_Post : AppCompatActivity(){//, OnMapReadyCallback{
    private val TAG = Detail_Discount::class.java.simpleName
    private lateinit var mMap: GoogleMap

    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    private var mLocationPermissionGranted: Boolean = false
    private var mLastKnownLocation: Location? = null
    private var postID:Int=0
    private var pk=0
    private var name=""
    private var pass=""
    private var Encode=""
    private var p=0
    internal lateinit var txt_detail_new: TextView
    private var postId:Int=0

    private lateinit var tvPostTitle:TextView
    private lateinit var tvPrice:TextView
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

    /*
    private val REQUEST_LOCATION = 1
    internal lateinit var locationManager: LocationManager
    internal lateinit var locationListener: LocationListener
    override fun onMapReady(p0: GoogleMap) {
        mMap = p0

        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationListener = object : LocationListener {
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }

            override fun onProviderEnabled(provider: String?) {
            }

            override fun onProviderDisabled(provider: String?) {
            }

            override fun onLocationChanged(location: Location) {
                val geocoder = Geocoder(applicationContext, Locale.getDefault())
                try {
                    val listAddress = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    //txt_place.text = txt_place.text.toString() + "" + listAddress[0].getAddressLine(0)
                    txt_detail_new.text = listAddress[0].featureName + ", "+ listAddress[0].thoroughfare+", " + listAddress[0].adminArea + ", " + listAddress[0].countryName
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        if (ActivityCompat.checkSelfPermission(this@Detail_New_Post, Manifest.permission.ACCESS_FINE_LOCATION) !== PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this@Detail_New_Post, Manifest.permission.ACCESS_COARSE_LOCATION) !== PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@Detail_New_Post, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION)
        } else {

        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
        val lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        mMap.clear()
        val userLocation = LatLng(lastKnownLocation.latitude, lastKnownLocation.longitude)
        mMap.addMarker(MarkerOptions().position(userLocation).title("I`m here"))
        p0.moveCamera(CameraUpdateFactory.newLatLng(userLocation))
        mMap.animateCamera(CameraUpdateFactory.zoomBy(12f))
        mMap.moveCamera(CameraUpdateFactory.zoomBy(15f))
        mMap.isMyLocationEnabled = true
        //disable
        mMap.uiSettings.isScrollGesturesEnabled = false
        //mMap.getUiSettings().setZoomGesturesEnabled(false);
        getLocationPermission()

        getDeviceLocation()
    }

    private fun getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                val locationResult = mFusedLocationProviderClient!!.lastLocation
                locationResult.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        mLastKnownLocation = task.result
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(mLastKnownLocation!!.latitude, mLastKnownLocation!!.longitude), 15f))
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        mMap.uiSettings.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message)
        }

    }
    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.applicationContext,
                        Manifest.permission.ACCESS_FINE_LOCATION) === PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }
    */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_new_post)
        postId = intent.getIntExtra("ID",0)
        val sharedPref: SharedPreferences = getSharedPreferences("Register", Context.MODE_PRIVATE)
        name = sharedPref.getString("name", "")
        pass = sharedPref.getString("pass", "")
        Encode = getEncodedString(name,pass)
        if (sharedPref.contains("token")) {
           pk = sharedPref.getInt("Pk",0)
        } else if (sharedPref.contains("id")) {
           pk = sharedPref.getInt("id", 0)
        }
        p = intent.getIntExtra("ID",0)

        initialProductPostDetail(Encode)

//        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION)
//
//        txt_detail_new = findViewById(R.id.txt_show_place_detail_new_post) as TextView
//        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
//        val mapFragment = supportFragmentManager
//                .findFragmentById(R.id.map_detail_newpost) as SupportMapFragment
//        mapFragment.getMapAsync(this)
        /*
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION)

        txt_detail_new = findViewById(R.id.txt_show_place_detail_new_post) as TextView
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map_detail_newpost) as SupportMapFragment
        mapFragment.getMapAsync(this)
        */
        //Back
        val back = findViewById<TextView>(R.id.tv_back)
        back.setOnClickListener { finish() }
        //Slider
        sliderImage = findViewById<SliderImage>(R.id.slider)
        /*
        val images = listOf("https://www.allkpop.com/upload/2018/09/af_org/01133025/red-velvet.jpg","https://upload.wikimedia.org/wikipedia/commons/8/83/Red_Velvet_at_Korea_Popular_Music_Awards_red_carpet_on_December_20%2C_2018.png")
        sliderImage.setItems(images)
        sliderImage.addTimerToSlide(3000)
        sliderImage.removeTimerSlide()
        sliderImage.getIndicator()
        */
//        val id = intent.getIntExtra("ID",0)
//        val phone = findViewById<TextView>(R.id.tv_phone)
//
        tvPostTitle = findViewById<TextView>(R.id.title)
        tvPrice = findViewById<TextView>(R.id.tv_price)
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

//        val name = findViewById<TextView>(R.id.tv_name)
//        name.text = intent.getStringExtra("Name")
//        val img_user = findViewById<CircleImageView>(R.id.cr_img)
//        img_user.setImageResource(intent.getIntExtra("Image_user",R.drawable.thou))
////User Post
//        findViewById<CircleImageView>(R.id.cr_img).setOnClickListener {
//            val intent = Intent(this@Detail_New_Post, User_post::class.java)
//            intent.putExtra("Image_user",getIntent().getIntExtra("Image_user",R.drawable.thean))
//            intent.putExtra("ID",id)
//            intent.putExtra("Phone",phone.text)
//            startActivity(intent)
//        }
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
//            makePhoneCall("0962363929")
            checkPermission()
        }
////Button SMS
//        val sms = findViewById<Button>(R.id.btn_sms)
//        sms.setOnClickListener {
////                val phoneNumber = "0962363929"
////                val uri = Uri.parse("smsto:0962363929")
////                intent.putExtra("sms_body", "Here goes your message...")
////                val smsManager = SmsManager.getDefault() as SmsManager
//
//            val smsIntent = Intent(Intent.ACTION_VIEW)
//            smsIntent.type = "vnd.android-dir/mms-sms"
//            smsIntent.putExtra("address", "0962363929")
////            smsIntent.putExtra("sms_body", "Body of Message")
//            startActivity(smsIntent)
//        }
//Button Like
        val like = findViewById<Button>(R.id.btn_like)
        like.setOnClickListener {
            Toast.makeText(this@Detail_New_Post,"This Product add to Your Liked",Toast.LENGTH_SHORT).show()
            Like_post(Encode);
        }

        val loan= findViewById<Button>(R.id.btn_loan)
        loan.setOnClickListener{
            val intent = Intent(this@Detail_New_Post, LoanCreateActivity::class.java)
            startActivity(intent)
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

        edLoanInterestRate.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                //Perform Code
                Toast.makeText(this@Detail_New_Post, edLoanPrice.getText(), Toast.LENGTH_SHORT).show()
                calculateLoanMonthlyPayment()
                return@OnKeyListener true
            }
            false
        })

    }  // oncreate

    fun initialProductPostDetail(encode: String){
        val url=ConsumeAPI.BASE_URL+"allposts/"+postId
        val client=OkHttpClient()
        val auth = "Basic $encode"
        val request = Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", auth)
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
                                        //Log.d(TAG,"Year "+jsonObject.getString("year"))
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
                                        //Log.d(TAG,"Year "+jsonObject.getString("year"))
                                        tvModel.setText(jsonObject.getString("modeling_name"))

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
                                                        tvBrand.setText(jsonObject.getString("brand_name"))
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

                        tvPostTitle.setText(postDetail.title)
                        tvPrice.setText("$ "+postDetail.cost)
                        tvCondition.setText(postDetail.condition.toString())
                        tvColor.setText(postDetail.color.toString())
                        tvDescription.setText(postDetail.description.toString())

                        val base64_front_image=postDetail.base64_front_image.toString()
                        val base64_right_image=postDetail.base64_right_image.toString()
                        val base64_left_image=postDetail.base64_left_image.toString()
                        val base64_back_image=postDetail.base64_back_image.toString()

                        val decodedFrontImageString = Base64.decode(base64_front_image, Base64.DEFAULT)
                        val decodedFrontByte = BitmapFactory.decodeByteArray(decodedFrontImageString, 0, decodedFrontImageString.size)
                        val decodedRightImageString = Base64.decode(base64_right_image, Base64.DEFAULT)
                        val decodedRightByte = BitmapFactory.decodeByteArray(decodedRightImageString, 0, decodedRightImageString.size)
                        val decodedLeftImageString = Base64.decode(base64_left_image, Base64.DEFAULT)
                        val decodedLeftByte = BitmapFactory.decodeByteArray(decodedLeftImageString, 0, decodedLeftImageString.size)
                        val decodedBackImageString = Base64.decode(base64_back_image, Base64.DEFAULT)
                        val decodedBackByte = BitmapFactory.decodeByteArray(decodedBackImageString, 0, decodedBackImageString.size)

                        val images = listOf(getImageUri(this@Detail_New_Post,decodedFrontByte).toString(),getImageUri(this@Detail_New_Post,decodedRightByte).toString(),getImageUri(this@Detail_New_Post,decodedLeftByte).toString(),getImageUri(this@Detail_New_Post,decodedBackByte).toString())
                        sliderImage.setItems(images)
                        sliderImage.addTimerToSlide(3000)
                        sliderImage.removeTimerSlide()
                        sliderImage.getIndicator()
                    }
                } catch (e: JsonParseException) {
                    e.printStackTrace()
                }
            }
        })
    }

    fun Like_post(encode: String) {
        var url=ConsumeAPI.BASE_URL+"like/"
        val MEDIA_TYPE = MediaType.parse("application/json")
        val post = JSONObject()
        try{
            post.put("post", p)
            post.put("like_by",pk)
            post.put("record_status",1)

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

            }
        })

        }catch (e:Exception){
            e.printStackTrace()
        }
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
        if(loanTerm.isNullOrEmpty()) aLoanTerm=12 else aLoanTerm=loanTerm.toInt()*12

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
                     // Calculate ratios of height and width to requested height and
                     // width
                     val heightRatio:Int = Math.round(height.toFloat() /reqHeight.toFloat())
                     val widthRatio:Int = Math.round(width.toFloat() / reqWidth.toFloat())

                     // Choose the smallest ratio as inSampleSize value, this will
                     // guarantee
                     // a final image with both dimensions larger than or equal to
                     // the
                     // requested height and width.

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
}
