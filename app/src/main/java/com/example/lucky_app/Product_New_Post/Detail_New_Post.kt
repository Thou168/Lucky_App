package com.example.lucky_app.Product_New_Post

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.lucky_app.Product_dicount.Detail_Discount
import com.example.lucky_app.R
import com.example.lucky_app.loan.LoanCreateActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.GoogleMap
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class Detail_New_Post : AppCompatActivity(){//, OnMapReadyCallback{
    private val TAG = Detail_Discount::class.java.simpleName
    private lateinit var mMap: GoogleMap

    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    private var mLocationPermissionGranted: Boolean = false
    private var mLastKnownLocation: Location? = null
    private var pk=0
    private var name=""
    private var pass=""
    private var Encode=""
    private var p=0



    internal lateinit var txt_detail_new: TextView
//    /*
//    private val REQUEST_LOCATION = 1
//    internal lateinit var locationManager: LocationManager
//    internal lateinit var locationListener: LocationListener
//    override fun onMapReady(p0: GoogleMap) {
//        mMap = p0
//
//        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//
//        locationListener = object : LocationListener {
//            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
//            }
//
//            override fun onProviderEnabled(provider: String?) {
//            }
//
//            override fun onProviderDisabled(provider: String?) {
//            }
//
//            override fun onLocationChanged(location: Location) {
//                val geocoder = Geocoder(applicationContext, Locale.getDefault())
//                try {
//                    val listAddress = geocoder.getFromLocation(location.latitude, location.longitude, 1)
//                    //txt_place.text = txt_place.text.toString() + "" + listAddress[0].getAddressLine(0)
//                    txt_detail_new.text = listAddress[0].featureName + ", "+ listAddress[0].thoroughfare+", " + listAddress[0].adminArea + ", " + listAddress[0].countryName
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//            }
//        }
//
//        if (ActivityCompat.checkSelfPermission(this@Detail_New_Post, Manifest.permission.ACCESS_FINE_LOCATION) !== PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(this@Detail_New_Post, Manifest.permission.ACCESS_COARSE_LOCATION) !== PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this@Detail_New_Post, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION)
//        } else {
//
//        }
//
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
//        val lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
//        mMap.clear()
//        val userLocation = LatLng(lastKnownLocation.latitude, lastKnownLocation.longitude)
//        mMap.addMarker(MarkerOptions().position(userLocation).title("I`m here"))
//        p0.moveCamera(CameraUpdateFactory.newLatLng(userLocation))
//        mMap.animateCamera(CameraUpdateFactory.zoomBy(12f))
//        mMap.moveCamera(CameraUpdateFactory.zoomBy(15f))
//        mMap.isMyLocationEnabled = true
//        //disable
//        mMap.uiSettings.isScrollGesturesEnabled = false
//        //mMap.getUiSettings().setZoomGesturesEnabled(false);
//        getLocationPermission()
//
//        getDeviceLocation()
//    }
//
//    private fun getDeviceLocation() {
//        try {
//            if (mLocationPermissionGranted) {
//                val locationResult = mFusedLocationProviderClient!!.lastLocation
//                locationResult.addOnCompleteListener(this) { task ->
//                    if (task.isSuccessful) {
//                        mLastKnownLocation = task.result
//                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(mLastKnownLocation!!.latitude, mLastKnownLocation!!.longitude), 15f))
//                    } else {
//                        Log.d(TAG, "Current location is null. Using defaults.")
//                        Log.e(TAG, "Exception: %s", task.exception)
//                        mMap.uiSettings.isMyLocationButtonEnabled = false
//                    }
//                }
//            }
//        } catch (e: SecurityException) {
//            Log.e("Exception: %s", e.message)
//        }
//
//    }
//    private fun getLocationPermission() {
//        if (ContextCompat.checkSelfPermission(this.applicationContext,
//                        Manifest.permission.ACCESS_FINE_LOCATION) === PackageManager.PERMISSION_GRANTED) {
//            mLocationPermissionGranted = true
//        } else {
//            ActivityCompat.requestPermissions(this,
//                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
//        }
//    }
//    */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_new_post)

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
//        val sliderImage = findViewById<SliderImage>(R.id.slider)
//        val images = listOf(R.drawable.fan,R.drawable.motorcycle)
//        sliderImage.setItems(images)
//        sliderImage.addTimerToSlide(3000)
//        sliderImage.removeTimerSlide()
//        sliderImage.getIndicator()

//        val id = intent.getIntExtra("ID",0)
//        val phone = findViewById<TextView>(R.id.tv_phone)
//
//        val title = findViewById<TextView>(R.id.title)
//        title.text = intent.getStringExtra("Title")
        val price = findViewById<TextView>(R.id.tv_price)
        price.text = intent.getDoubleExtra("Price",0.0).toString()

        val brand = findViewById<TextView>(R.id.tv_brand)
        brand.text = intent.getIntExtra("ID",0).toString()

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


    }  // oncreate

    fun Like_post(encode: String) {
        var url="http://103.205.26.103:8000/like/"
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
                Log.d("Error",call.toString())
            }
        })

        }catch (e:Exception){
            e.printStackTrace()
        }
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
}
