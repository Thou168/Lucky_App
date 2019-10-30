package com.bt_121shoppe.motorbike.useraccount

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.*
import androidx.recyclerview.widget.RecyclerView
import com.bt_121shoppe.motorbike.Activity.Item_API
import com.bt_121shoppe.motorbike.Api.ConsumeAPI
import com.bt_121shoppe.motorbike.Api.User
import com.bt_121shoppe.motorbike.R
import com.bt_121shoppe.motorbike.models.PostViewModel
import com.bt_121shoppe.motorbike.utils.CommomAPIFunction
import com.bt_121shoppe.motorbike.Product_New_Post.MyAdapter_list_grid_image
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.google.gson.JsonParseException
import de.hdodenhof.circleimageview.CircleImageView

import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.security.AccessController.getContext
import java.text.SimpleDateFormat
import java.util.*

class User_post : AppCompatActivity() , OnMapReadyCallback{

    var user_id:Int=0
    var username:String=""
    var password:String=""
    var encode=""
    private lateinit var tabLayout: TabLayout
    private lateinit var img_user:CircleImageView
    private lateinit var Username:TextView

    private lateinit var tvBack:TextView
    private lateinit var tvBack_post:TextView
    private lateinit var title:TextView
    private lateinit var recyclrview:RecyclerView
    private lateinit  var tvPhone: TextView
    private lateinit  var tvEmail:TextView
    private lateinit  var tvAddress:TextView
    private val REQUEST_LOCATION = 1
    internal lateinit var locationManager: LocationManager
    internal var latitude: Double = 0.toDouble()
    internal var longtitude:Double = 0.toDouble()
    private lateinit var  mapView: MapView
    private lateinit var  mMap: GoogleMap
    private lateinit var  latlng: String
    private lateinit var linearLayout: LinearLayout
    private var haha:Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_post)
        locale()
        var bundle :Bundle ?=intent.extras
        user_id = intent.getStringExtra("ID").toInt()
        val sharedPref: SharedPreferences = getSharedPreferences("Register", Context.MODE_PRIVATE)
        username = sharedPref.getString("name", "")
        password = sharedPref.getString("pass", "")
        encode = "Basic "+com.bt_121shoppe.motorbike.utils.CommonFunction.getEncodedString(username,password)

        tvBack =  findViewById<TextView>(R.id.tv_back)
        tvBack.setOnClickListener {
            haha = false
            finish()
        }
        tvBack_post = findViewById(R.id.tv_back_post)
        tvBack_post.setOnClickListener {
            tvBack_post.visibility = View.GONE
            tvBack.visibility = View.VISIBLE
            title.setText(R.string.post)
            recyclrview.visibility = View.VISIBLE
            linearLayout.visibility = View.GONE
            haha = true
        }


        Username = findViewById(R.id.username)
        img_user = findViewById(R.id.img_user)
        //        tabLayout = findViewById(R.id.tab)
        title  = findViewById(R.id.title_userpost)
        recyclrview = findViewById(R.id.recycler_user_post)
        linearLayout = findViewById(R.id.contact_user2)
        tvPhone = findViewById(R.id.phone)
        tvEmail = findViewById(R.id.email)
        tvAddress = findViewById(R.id.address_contact)

        val phone = intent.getExtras()!!.getString("Phone")
        val email = intent.getExtras()!!.getString("Email")
        val addr_shop  = intent.getExtras()!!.getString("map_address")
        val addr_post  = intent.extras.getString("map_post")  // use for user detail when user no address shop

        Log.d("Late","1111------"+addr_shop+"and"+addr_post)

        var addr =""
        if (addr_shop.isEmpty()){
            addr = addr_post
        }else{
            addr = addr_shop
        }

//        configureTabLayout()
        getUserProfile()
        getUserPost_Data(this)
        getUserInformation(phone,email,addr)
        img_user.setOnClickListener {
            title.setText(R.string.contact)
            recyclrview.visibility = View.GONE
            linearLayout.visibility = View.VISIBLE
            tvBack_post.visibility = View.VISIBLE
            tvBack.visibility = View.GONE

        }
        //getUserPosts()
    }

//    override fun onBackPressed() {
//        if (haha){
//            tvBack_post.visibility = View.GONE
//            tvBack.visibility = View.VISIBLE
//            title.setText(R.string.post)
//            recyclrview.visibility = View.VISIBLE
//            linearLayout.visibility = View.GONE
//        }else{
//            finish()
//        }
//    }
    //    private fun configureTabLayout() {
//        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.post)))
//        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.contact)))
//        val adapter = Tab_Adapter(supportFragmentManager, tabLayout.tabCount)
//        pager.adapter = adapter
//        pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
//        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tablayout: TabLayout.Tab) {
//                pager.currentItem = tablayout.position
//            }
//            override fun onTabUnselected(tab: TabLayout.Tab) {
//            }
//            override fun onTabReselected(tab: TabLayout.Tab) {
//
//            }
//        })
//    }

//    override fun onBackPressed() {
//        tvBack_post.visibility = View.GONE
//        tvBack.visibility = View.VISIBLE
//        title.setText(R.string.post)
//        recyclrview.visibility = View.VISIBLE
//        linearLayout.visibility = View.GONE
//        super.onBackPressed()
//    }

    fun getUserProfile(){
        var user1 = User()
        var URL_ENDPOINT= ConsumeAPI.BASE_URL+"api/v1/users/"+user_id
        var MEDIA_TYPE= MediaType.parse("application/json")
        var client= OkHttpClient()
        var request= Request.Builder()
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

            @SuppressLint("ResourceType")
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val mMessage = response.body()!!.string()
                val gson = Gson()
                try {
                    user1= gson.fromJson(mMessage, User::class.java)
                    runOnUiThread {

                            val profilepicture: String=if(user1.profile.profile_photo==null) "" else user1.profile.base64_profile_image
//                            val coverpicture: String= if(user1.profile.cover_photo==null) "" else user1.profile.base64_cover_photo_image
                            if(user1.getFirst_name().isEmpty())
                            {
                                Username!!.setText(user1.getUsername())

                            }else {
                                Username.setText(user1.getFirst_name())
                            }
                        CommomAPIFunction.getUserProfileFB(this@User_post,img_user,user1.username)

//                            if(coverpicture.isNullOrEmpty()){
//
//                            }else {
//                                val decodedString = Base64.decode(coverpicture, Base64.DEFAULT)
//                                var decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
////                                imgCover!!.setImageBitmap(decodedByte)
//                            }

                    }

                } catch (e: JsonParseException) {
                    e.printStackTrace()
                }
            }
        })
    }

    fun getUserPost_Data(context1:Context){
        val itemApi = ArrayList<Item_API>()
        var posts= PostViewModel()
        val URL_ENDPOINT= ConsumeAPI.BASE_URL+"postbyuserfilter/?created_by="+user_id+"&approved_by=&rejected_by=&modified_by="
        var MEDIA_TYPE= MediaType.parse("application/json")
        val client= OkHttpClient()
        val request= Request.Builder()
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
                Log.d("TAH","TT"+mMessage)

                try {
                    val jsonObject= JSONObject(mMessage)
                    val jsonArray=jsonObject.getJSONArray("results")
                    val jsonCount=jsonObject.getInt("count")

                    runOnUiThread {
                        if(jsonCount>0){
                            for (i in 0 until jsonArray.length()) {
                                val obj =jsonArray.getJSONObject(i)
                                val title = obj.getString("title")
                                val id = obj.getInt("id")
                                val user_id = obj.getInt("user")
                                val condition = obj.getString("condition")
                                val cost = obj.getDouble("cost")
                                val image = obj.getString("front_image_path")
                                val img_user = obj.getString("right_image_path")
                                val postType = obj.getString("post_type")
                                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                                sdf.setTimeZone(TimeZone.getTimeZone("GMT"))
                                val time:Long = sdf.parse(obj.getString("created")).getTime()
                                val now:Long = System.currentTimeMillis()
                                val ago:CharSequence = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)
                                val discount_type = obj.getString("discount_type")
                                val discount = obj.getDouble("discount")
                                val postsubtitle = obj.getString("post_sub_title")
                                val color = obj.getString("color")
                                val model = obj.getInt("modeling")
                                val year = obj.getInt("year")

                                val URL_ENDPOINT1= ConsumeAPI.BASE_URL+"countview/?post="+id
                                var MEDIA_TYPE=MediaType.parse("application/json")
                                val client1= OkHttpClient()
                                //val auth = "Basic $encode"
                                val request1=Request.Builder()
                                        .url(URL_ENDPOINT1)
                                        .header("Accept","application/json")
                                        .header("Content-Type","application/json")
                                        //.header("Authorization",encode)
                                        .build()
                                client1.newCall(request1).enqueue(object : Callback{
                                    override fun onFailure(call: Call, e: IOException) {
                                        val mMessage = e.message.toString()
                                        Log.w("failure Response", mMessage)
                                    }
                                    @Throws(IOException::class)
                                    override fun onResponse(call: Call, response: Response) {
                                        val mMessage = response.body()!!.string()
                                        val gson = Gson()
                                        try {
                                            val jsonObject= JSONObject(mMessage)
                                            val jsonCount=jsonObject.getInt("count")
                                            runOnUiThread {
                                                itemApi.add(Item_API(id,user_id,img_user,image,title,cost,condition,postType,ago.toString(),jsonCount.toString(),color,model,year,discount_type,discount,postsubtitle))
                                                recyclrview!!.adapter = MyAdapter_list_grid_image(itemApi, "image",context1)
                                                recyclrview!!.layoutManager = GridLayoutManager(this@User_post,1) as RecyclerView.LayoutManager?
                                                val dividerItemDecoration = DividerItemDecoration(this@User_post,DividerItemDecoration.VERTICAL)
                                                dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this@User_post, R.drawable.divider_drawable)!!)
                                                recyclrview.addItemDecoration(dividerItemDecoration)
                                            }

                                        } catch (e: JsonParseException) {
                                            e.printStackTrace()
                                        }

                                    }
                                })

                                //
                            }
                        }

                    }

                } catch (e: JsonParseException) {
                    e.printStackTrace()
                }

            }
        })
    }

    fun getUserInformation(phone: String?, email: String?, addr: String?) {
        val splitPhone = phone!!.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        if (splitPhone.size == 1) {
            tvPhone.setText(splitPhone[0].toString())
        } else if (splitPhone.size == 2) {
            tvPhone.setText(splitPhone[0].toString() + " / " + splitPhone[1].toString())
        } else if (splitPhone.size == 3) {
            tvPhone.setText(splitPhone[0].toString() + " / " + splitPhone[1].toString() + " / " + splitPhone[2].toString())
        }

        if (email == null || email.isEmpty()) {
            tvEmail.text = "Null"
        } else {
            tvEmail.setText(email)
        }

        if (addr!!.isEmpty()) {
            Toast.makeText(this, "Unble to Trace your location", Toast.LENGTH_SHORT).show()
        } else {
            val splitAddr = addr.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            latitude = java.lang.Double.valueOf(splitAddr[0])
            longtitude = java.lang.Double.valueOf(splitAddr[1])
            Log.d("Late and Long",""+latitude + "," + longtitude)
            get_location(latitude, longtitude)
            val mapFragment = supportFragmentManager.findFragmentById(R.id.map_contact) as SupportMapFragment?
            mapFragment?.getMapAsync(this)

        }

    }

//    fun getUserPosts(){
//        var posts= PostViewModel()
//        val URL_ENDPOINT=ConsumeAPI.BASE_URL+"postbyuserfilter/?created_by="+user_id+"&approved_by=&rejected_by=&modified_by="
//        var MEDIA_TYPE=MediaType.parse("application/json")
//        val client= OkHttpClient()
//        val request=Request.Builder()
//                .url(URL_ENDPOINT)
//                .header("Accept","application/json")
//                .header("Content-Type","application/json")
//                //.header("Authorization",encode)
//                .build()
//        client.newCall(request).enqueue(object : Callback{
//            override fun onFailure(call: Call, e: IOException) {
//                val mMessage = e.message.toString()
//                Log.w("failure Response", mMessage)
//            }
//            @Throws(IOException::class)
//            override fun onResponse(call: Call, response: Response) {
//                val mMessage = response.body()!!.string()
//                val gson = Gson()
//                Log.d("TAH","TT"+mMessage)
//
//                try {
//                    val jsonObject= JSONObject(mMessage)
//                    val jsonArray=jsonObject.getJSONArray("results")
//                    val jsonCount=jsonObject.getInt("count")
//                    runOnUiThread {
//                        if(jsonCount>0){
//                            for (i in 0 until jsonArray.length()) {
//                                val obj=jsonArray.getJSONObject(i)
//                                Log.e("TAG","T"+obj)
//                            }
//
//                        }
//
//                    }
//
//                } catch (e: JsonParseException) {
//                    e.printStackTrace()
//                }
//
//            }
//        })
//    }



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

                    tvAddress!!.text = road

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
}
