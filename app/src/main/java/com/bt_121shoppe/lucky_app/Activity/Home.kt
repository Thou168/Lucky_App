package com.bt_121shoppe.lucky_app.Activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.StrictMode
import android.provider.MediaStore
import android.provider.Settings
import android.text.format.DateUtils
import android.util.Log
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.custom.sliderimage.logic.SliderImage
import com.bt_121shoppe.lucky_app.Api.ConsumeAPI
import com.bt_121shoppe.lucky_app.Api.User
import com.bt_121shoppe.lucky_app.Buy_Sell_Rent.Buy.Buy
import com.bt_121shoppe.lucky_app.Buy_Sell_Rent.Rent.Rent
import com.bt_121shoppe.lucky_app.Buy_Sell_Rent.Sell.Sell
import com.bt_121shoppe.lucky_app.useraccount.Edit_account
import com.bt_121shoppe.lucky_app.Login_Register.UserAccount
import com.bt_121shoppe.lucky_app.Product_New_Post.MyAdapter_list_grid_image
import com.bt_121shoppe.lucky_app.Product_dicount.MyAdapter
import com.bt_121shoppe.lucky_app.R
import com.bt_121shoppe.lucky_app.Setting.AboutUsActivity
import com.bt_121shoppe.lucky_app.Setting.Setting
import com.bt_121shoppe.lucky_app.Setting.TermPrivacyActivity
import com.bt_121shoppe.lucky_app.Startup.Item
import com.bt_121shoppe.lucky_app.Startup.Search1
import com.bt_121shoppe.lucky_app.adapters.AllPostAdapter
import com.bt_121shoppe.lucky_app.chats.ChatMainActivity
import com.bt_121shoppe.lucky_app.classes.DividerItemDecoration
import com.bt_121shoppe.lucky_app.firebases.models.Sport
import com.bt_121shoppe.lucky_app.models.PostProduct
import com.bt_121shoppe.lucky_app.utils.CheckNetwork
import com.bt_121shoppe.lucky_app.utils.CommonFunction
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import de.hdodenhof.circleimageview.CircleImageView
import net.hockeyapp.android.CrashManager

import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Home : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    var recyclerView: RecyclerView? = null
    var list_item: ArrayList<Item_API>? = null
    var best_list: RecyclerView? = null
    var grid: ImageView? = null
    var list: ImageView? = null
    var image_list: ImageView? = null
    //    var click: String = "Khmer"
    lateinit var sharedPreferences: SharedPreferences
    val myPreferences = "mypref"
    val namekey = "Khmer"
    var english: ImageView? = null
    var khmer: ImageView? = null
    private var pk=0
    private var name=""
    private var pass=""
    private var Encode=""
    private var categoryId:String=""
    private var brandId:String=""
    private var yearId:String=""
    internal lateinit var locationManager: LocationManager
    private val REQUEST_LOCATION = 1
    var category: Spinner? = null
    var drawerLayout: DrawerLayout? = null
    private var listItems: ArrayList<String>?=null
    //    internal lateinit var ddBrand:Button
    internal lateinit var listItems1: Array<String?>
    internal lateinit var categoryIdItems: Array<Int?>
    internal lateinit var brandListItems: Array<String?>
    internal lateinit var brandIdListItems: Array<Int?>
    internal lateinit var yearListItems: Array<String?>
    internal lateinit var yearIdListItems: Array<Int?>
    private var bnavigation: BottomNavigationView? = null

    var progreessbar: ProgressBar? = null
    var txtno_found: TextView? = null
    var progreessbar1: ProgressBar? = null
    var txtno_found1: TextView? = null

    var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    lateinit var mHandler: Handler

    internal lateinit var mAllPostAdapter:AllPostAdapter
    internal lateinit var maLayoutManager:LinearLayoutManager
    internal lateinit var mAllPosts:ArrayList<PostProduct>
    private var itemCount=0
    internal val isLoading = false
    internal val isAPLoading = false

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

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locale()
        sharedPreferences = getSharedPreferences(myPreferences,Context.MODE_PRIVATE)
        setContentView(R.layout.activity_home)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        if(CheckNetwork.isIntenetAvailable(this@Home)){

        }else{
            Toast.makeText(this@Home,"No Internet connection",Toast.LENGTH_LONG).show();
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.title = " "
        setSupportActionBar(toolbar)
        progreessbar =findViewById(R.id.progress_bar)
        progreessbar!!.visibility = View.VISIBLE
        txtno_found =findViewById(R.id.text)
        progreessbar1 =findViewById(R.id.progress_bar1)
        progreessbar1!!.visibility = View.VISIBLE
        txtno_found1 =findViewById(R.id.text1)
        khmer = findViewById(R.id.khmer)
        english = findViewById(R.id.english)
        val prefer = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language = prefer.getString("My_Lang", "")
        val sharedPref: SharedPreferences = getSharedPreferences("Register", Context.MODE_PRIVATE)
        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout!!.addDrawerListener(toggle)
        toggle.syncState()
        if (sharedPref.contains("token") || sharedPref.contains("id")){
            navView.setVisibility(View.VISIBLE)
            drawerLayout!!.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            name = sharedPref.getString("name", "")
            pass = sharedPref.getString("pass", "")
            Encode = CommonFunction.getEncodedString(name,pass)
            if (sharedPref.contains("token")) {
                pk = sharedPref.getInt("Pk",0)
            } else if (sharedPref.contains("id")) {
                pk = sharedPref.getInt("id", 0)
            }
            english!!.setOnClickListener { language("en")
                recreate()
            }
            khmer!!.setOnClickListener { language("km")
                recreate()
            }
            if(language.equals("km")) {
                english!!.visibility = View.VISIBLE
                khmer!!.visibility = View.GONE
            }else{
                english!!.visibility = View.GONE
                khmer!!.visibility = View.VISIBLE
            }
            getUserProfile()
        }else{
            drawerLayout!!.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            if(language.equals("km")) {
                english!!.visibility = View.VISIBLE
                khmer!!.visibility = View.GONE
            }else{
                english!!.visibility = View.GONE
                khmer!!.visibility = View.VISIBLE
            }
            english!!.setOnClickListener { language("en")
                recreate()
            }
            khmer!!.setOnClickListener { language("km")
                recreate()
            }
        }
        requestStoragePermission(false)
        requestStoragePermission(true)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) run { buildAlertMessageNoGps() }
        navView.setNavigationItemSelectedListener(this)

        bnavigation = findViewById<BottomNavigationView>(R.id.bnaviga)
        bnavigation!!.menu.getItem(0).isChecked = true
        bnavigation!!.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    recreate()
                }
                R.id.notification -> {
                    if (sharedPref.contains("token") || sharedPref.contains("id")) {
                        val intent = Intent(this@Home, Notification::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }else{
                        val intent = Intent(this@Home, UserAccount::class.java)
                        intent.putExtra("noti","notifi")
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }
                }
                R.id.camera ->{
                    if (sharedPref.contains("token") || sharedPref.contains("id")) {
                        val intent = Intent(this@Home, Camera::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }else{
                        val intent = Intent(this@Home, UserAccount::class.java)
//                        intent.putExtra("came",0)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }
                }
                R.id.message -> {
                    if (sharedPref.contains("token") || sharedPref.contains("id")) {
                        val intent = Intent(this@Home,ChatMainActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
                    }else{
                        val intent = Intent(this@Home, UserAccount::class.java)
//                        intent.putExtra("mess",0)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }
                }
                R.id.account ->{
                    if (sharedPref.contains("token") || sharedPref.contains("id")) {
                        val intent = Intent(this@Home, Account::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }else{
                        val intent = Intent(this@Home, UserAccount::class.java)
//                        intent.putExtra("acc",0)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }
                }
            }
            true
        }
        //SliderImage
        val sliderImage = findViewById(R.id.slider) as SliderImage
        val images = listOf("https://i.redd.it/glin0nwndo501.jpg", "https://i.redd.it/obx4zydshg601.jpg",
                "https://i.redd.it/glin0nwndo501.jpg", "https://i.redd.it/obx4zydshg601.jpg")
        sliderImage.setItems(images)
        sliderImage.addTimerToSlide(3000)
        //  sliderImage.removeTimerSlide()
        sliderImage.getIndicator()
        //Buy sell and Rent
        val buy = findViewById<TextView>(R.id.buy)
        buy.setOnClickListener{
            val intent = Intent(this@Home, Buy::class.java)
            intent.putExtra("Title","Buy")
            startActivity(intent)
        }
        val sell = findViewById<TextView>(R.id.sell)
        sell.setOnClickListener {
            val intent = Intent(this@Home, Sell::class.java)
            intent.putExtra("Title","Sell")
            startActivity(intent)
        }
        val rent = findViewById<TextView>(R.id.rent)
        rent.setOnClickListener {
            val intent = Intent(this@Home, Rent::class.java)
            intent.putExtra("Title","Rent")
            startActivity(intent)
        }
        //Best Deal
        val version = ArrayList<Item>()
        version.addAll(Item.getType("Discount"))

        best_list = findViewById<RecyclerView>(R.id.horizontal)
        best_list!!.layoutManager = LinearLayoutManager(this@Home, LinearLayout.HORIZONTAL, false)
        getBest()

        mSwipeRefreshLayout = findViewById(R.id.refresh)
        mSwipeRefreshLayout!!.setOnRefreshListener(this)

        recyclerView = findViewById<RecyclerView>(R.id.list_new_post)
        //recyclerView!!.layoutManager = GridLayoutManager(this@Home,1)
        //Get()
        setUpAllPost()

        val search = findViewById<EditText>(R.id.search)
        search.setOnClickListener{
            val intent = Intent(this@Home, Search1::class.java)
            startActivity(intent)
        }
    }  // onCreate
    override fun onRefresh() {
        Handler().postDelayed({
            recreate()
            mSwipeRefreshLayout!!.setRefreshing(false)
        }, 1500)

    }
    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {

            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_profile -> {
                // Handle the camera action
                val intent = Intent(this@Home, Edit_account::class.java)
                startActivity(intent)
            }
            R.id.nav_post -> {
                val intent = Intent(this@Home, Account::class.java)
                startActivity(intent)
            }
            R.id.nav_like -> {
                val intent = Intent(this@Home,Account::class.java)
                intent.putExtra("Tab",1)
                startActivity(intent)
            }
            R.id.nav_loan -> {
                val intent = Intent(this@Home,Account::class.java)
                intent.putExtra("Tab",2)
                startActivity(intent)
            }
            R.id.nav_setting -> {
                val intent = Intent(this@Home, Setting::class.java)
                startActivity(intent)
            }
            R.id.nav_about -> {
                val intent = Intent(this@Home, AboutUsActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_privacy -> {
                val intent = Intent(this@Home, TermPrivacyActivity::class.java)
                startActivity(intent)
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true

    }

    override fun onResume() {
        super.onResume()
        checkForCrashes()
    }

    private fun checkForCrashes() {
        CrashManager.register(this)
    }


    fun getUserProfile(){
        var user1 = User()
        var URL_ENDPOINT=ConsumeAPI.BASE_URL+"api/v1/users/"+pk
        var MEDIA_TYPE=MediaType.parse("application/json")
        val encodeAuth="Basic $Encode"
        var client= OkHttpClient()
        var request=Request.Builder()
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

                val gson = Gson()
                try {
                    user1= gson.fromJson(mMessage, User::class.java)
                    runOnUiThread {

                        val fuser = FirebaseAuth.getInstance().currentUser
                        val reference = FirebaseDatabase.getInstance().getReference("users").child(fuser!!.uid)

                        reference.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                val ffuser = dataSnapshot.getValue(com.bt_121shoppe.lucky_app.models.User::class.java)
                                val drawer_username=findViewById<TextView>(R.id.drawer_username)
                                val imageView = findViewById<CircleImageView>(R.id.imageView)
                                if(user1.first_name.isNullOrEmpty())
                                    drawer_username.setText(ffuser!!.username)
                                else
                                    drawer_username.setText(user1.first_name)
                                if (ffuser!!.imageURL == "default") {
                                    imageView.setImageResource(R.drawable.user)
                                } else {
                                    Glide.with(this@Home).load(ffuser.imageURL).into(imageView)
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {

                            }
                        })
                    }

                } catch (e: JsonParseException) {
                    e.printStackTrace()
                }

            }
        })
    }


    private fun Get(): ArrayList<Item_API>{
        val itemApi = ArrayList<Item_API>()
        val url = ConsumeAPI.BASE_URL+"allposts/"
        val client = OkHttpClient()
        val request = Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val respon = response.body()!!.string()
                Log.d("Response", respon)
                try {
                    val jsonObject = JSONObject(respon)
                    val jsonArray = jsonObject.getJSONArray("results")
                    for (i in 0 until jsonArray.length()) {
                        val `object` = jsonArray.getJSONObject(i)
                        val title = `object`.getString("title")
                        val id = `object`.getInt("id")
                        val condition = `object`.getString("condition")
                        val cost = `object`.getDouble("cost")
                        val image = `object`.getString("front_image_path")
                        //val img_user = `object`.getString("right_image_base64")
                        val postType = `object`.getString("post_type")
                        val discount_type = `object`.getString("discount_type")
                        val discount = `object`.getDouble("discount")
                        var location_duration=""
                        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                        sdf.setTimeZone(TimeZone.getTimeZone("GMT"))
                        val time:Long = sdf.parse(`object`.getString("created")).getTime()
                        //val time:Long = sdf.parse(`object`.getString("approved_date")).getTime()
                        val now:Long = System.currentTimeMillis()
                        val ago:CharSequence = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)
                        Log.d("HOME POST",`object`.getString("created")+" "+`object`.getString("approved_date"))
                        //itemApi.add(Item_API(id,img_user,image,title,cost,condition,postType,ago.toString(),count_view.toString()))
                        runOnUiThread {
                            var cc=0
                            val URL_ENDPOINT1= ConsumeAPI.BASE_URL+"countview/?post="+id
                            var MEDIA_TYPE=MediaType.parse("application/json")
                            val client1= OkHttpClient()
                            val auth = "Basic $Encode"
                            val request1=Request.Builder()
                                    .url(URL_ENDPOINT1)
                                    .header("Accept","application/json")
                                    .header("Content-Type","application/json")
                                    .build()
                            client1.newCall(request1).enqueue(object : Callback{
                                override fun onFailure(call: Call, e: IOException) {
                                    val mMessage1 = e.message.toString()
                                    Log.w("failure Response", mMessage1)
                                }
                                @Throws(IOException::class)
                                override fun onResponse(call: Call, response: Response) {
                                    val mMessage1 = response.body()!!.string()
                                    val gson = Gson()
                                    val jsonObject= JSONObject(mMessage1)
                                    runOnUiThread {
                                        try {
                                            val jsonCount=jsonObject.getInt("count")
                                            if (jsonCount == 0 ){
                                                progreessbar1!!.visibility = View.GONE
                                                txtno_found1!!.visibility = View.VISIBLE
                                            }
                                            progreessbar1!!.visibility = View.GONE
                                            txtno_found1!!.visibility = View.GONE

                                            cc=jsonCount
                                            itemApi.add(Item_API(id,image,image,title,cost,condition,postType,ago.toString(),jsonCount.toString(),discount_type,discount))
                                            recyclerView!!.adapter = MyAdapter_list_grid_image(itemApi, "List",this@Home)
                                            //List Grid and Image
                                            list = findViewById(R.id.img_list)
                                            list!!.setOnClickListener {
                                                list!!.setImageResource(R.drawable.icon_list_c)
                                                image_list!!.setImageResource(R.drawable.icon_image)
                                                grid!!.setImageResource(R.drawable.icon_grid)
                                                recyclerView!!.adapter = MyAdapter_list_grid_image(itemApi, "List",this@Home)
                                                recyclerView!!.layoutManager = GridLayoutManager(this@Home,1)
                                            }
                                            grid = findViewById(R.id.grid)
                                            grid!!.setOnClickListener {
                                                grid!!.setImageResource(R.drawable.icon_grid_c)
                                                image_list!!.setImageResource(R.drawable.icon_image)
                                                list!!.setImageResource(R.drawable.icon_list)
                                                recyclerView!!.adapter = MyAdapter_list_grid_image(itemApi, "Grid",this@Home)
                                                recyclerView!!.layoutManager = GridLayoutManager(this@Home,2)
                                            }
                                            image_list = findViewById(R.id.btn_image)
                                            image_list!!.setOnClickListener {
                                                //                                                recyclerView!!.adapter = MyAdapter_list_grid_image(itemApi, "Image",this@Home)
                                                image_list!!.setImageResource(R.drawable.icon_image_c)
                                                grid!!.setImageResource(R.drawable.icon_grid)
                                                list!!.setImageResource(R.drawable.icon_list)
                                                recyclerView!!.adapter = MyAdapter_list_grid_image(itemApi, "Image",this@Home)
                                                recyclerView!!.layoutManager = GridLayoutManager(this@Home,1)
                                            }
                                        } catch (e: JsonParseException) {
                                            e.printStackTrace()
                                        }
                                    }
                                }
                            })
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
        return itemApi
    }

    private fun getBest(): ArrayList<Item_discount>{
        val itemApi = ArrayList<Item_discount>()
        val url = ConsumeAPI.BASE_URL+"bestdeal/"
        val client = OkHttpClient()
        val request = Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {

                val respon = response.body()!!.string()
                Log.d("Response", respon)
                val jsonObject = JSONObject(respon)
                val objectCount = jsonObject.getInt("count")
                try {
                    runOnUiThread {
                        val jsonArray = jsonObject.getJSONArray("results")
                        Log.d("count best", jsonArray.length().toString())
                        Log.d("Object count ", objectCount.toString())
                        if (objectCount == 0) {
                            progreessbar!!.visibility = View.GONE
                            txtno_found!!.visibility = View.VISIBLE
                        }
                        progreessbar!!.visibility = View.GONE
                        for (i in 0 until jsonArray.length()) {
                            var cc = 0
                            val `object` = jsonArray.getJSONObject(i)
                            val title = `object`.getString("title")
                            val id = `object`.getInt("id")
                            val condition = `object`.getString("condition")
                            val cost = `object`.getDouble("cost")
                            val discount = `object`.getDouble("discount")
                            val image = `object`.getString("front_image_path")
                            val postType = `object`.getString("post_type")
                            val discount_type = `object`.getString("discount_type")
                            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                            sdf.setTimeZone(TimeZone.getTimeZone("GMT"))
                            val timeap: Long = sdf.parse(`object`.getString("created")).time

                            val now: Long = System.currentTimeMillis()
                            val nowap: Long = System.currentTimeMillis()
                            val agoap: CharSequence = DateUtils.getRelativeTimeSpanString(timeap, nowap, DateUtils.MINUTE_IN_MILLIS)
                            val URL_ENDPOINT1 = ConsumeAPI.BASE_URL + "countview/?post=" + id
                            var MEDIA_TYPE = MediaType.parse("application/json")
                            val client1 = OkHttpClient()
                            val auth = "Basic $Encode"
                            val request1 = Request.Builder()
                                    .url(URL_ENDPOINT1)
                                    .header("Accept", "application/json")
                                    .header("Content-Type", "application/json")
                                    .build()
                            client1.newCall(request1).enqueue(object : Callback {
                                override fun onFailure(call: Call, e: IOException) {
                                    val mMessage1 = e.message.toString()
                                    Log.w("failure Response", mMessage1)
                                }

                                @Throws(IOException::class)
                                override fun onResponse(call: Call, response: Response) {
                                    val mMessage1 = response.body()!!.string()
                                    val gson = Gson()
                                    runOnUiThread {
                                        try {
                                            val jsonObject = JSONObject(mMessage1)
                                            Log.d("FFFFFF", " CCOUNT" + jsonObject)
                                            val jsonCount = jsonObject.getInt("count")
                                            Log.d("Item count view ", jsonCount.toString())
                                            cc = jsonCount
                                            itemApi.add(Item_discount(id, image, image, title, cost, discount, condition, postType, agoap.toString(), cc.toString(), discount_type))
                                            best_list!!.adapter = MyAdapter(itemApi)
                                            //List Grid and Image
                                        } catch (e: JsonParseException) {
                                            e.printStackTrace()
                                        }
                                    }
                                }
                            })
                        }
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
        return itemApi
    }
    fun countPostView(encode:String,postId:Int):Int{
        var count=0
        val URL_ENDPOINT= ConsumeAPI.BASE_URL+"countview/?post="+postId
        var MEDIA_TYPE=MediaType.parse("application/json")
        val client= OkHttpClient()
        val auth = "Basic $encode"
        val request=Request.Builder()
                .url(URL_ENDPOINT)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .header("Authorization",auth)
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
                    val jsonObject= JSONObject(mMessage)
                    val jsonCount=jsonObject.getInt("count")
                    runOnUiThread {
                        count=jsonCount
                    }

                } catch (e: JsonParseException) {
                    e.printStackTrace()
                }

            }
        })
        return count
    }
    private fun requestStoragePermission(isCamera: Boolean) {
        Dexter.withActivity(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            if (isCamera) {
                                //dispatchTakePictureIntent()
                            } else {
                                //dispatchGalleryIntent()
                            }
                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog()
                        }
                        if (ActivityCompat.checkSelfPermission(this@Home, Manifest.permission.ACCESS_FINE_LOCATION)
                                !== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this@Home, Manifest.permission.ACCESS_COARSE_LOCATION)
                                !== PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(this@Home, arrayOf<String>(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION)

                        }

                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                }).withErrorListener { error -> Toast.makeText(applicationContext, "Error occurred! ", Toast.LENGTH_SHORT).show() }
                .onSameThread()
                .check()
    }
    // navigating user to app settings
    private fun openSettings() {
        //Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        val intent = Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }
    private fun showSettingsDialog() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.permission))
        builder.setMessage(getString(R.string.setting_permission))
        builder.setPositiveButton(getString(R.string.go_setting)) { dialog, which ->
            dialog.cancel()
            openSettings()
        }
        builder.setNegativeButton(getString(R.string.cancel)) { dialog, which -> dialog.cancel() }
        builder.show()

    }

    private fun buildAlertMessageNoGps() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.requried_permission))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes_loan)) { dialog, which -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
                .setNegativeButton(getString(R.string.no_loan)) { dialog, which -> dialog.cancel() }
        val alert = builder.create()
        alert.show()
    }

    override fun onStart() {
        super.onStart()
        bnavigation!!.menu.getItem(0).isChecked = true
    }


    private fun setUpAllPost() {
        mAllPosts = java.util.ArrayList()
        maLayoutManager = GridLayoutManager(this, 1)
        maLayoutManager.setOrientation(RecyclerView.VERTICAL)
        //maLayoutManager.setExtraLayoutSpace(height);
        maLayoutManager.setAutoMeasureEnabled(true)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.setItemViewCacheSize(20)
        recyclerView!!.setDrawingCacheEnabled(true)
        recyclerView!!.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH)
        recyclerView!!.setRecycledViewPool(RecyclerView.RecycledViewPool())
        recyclerView!!.setLayoutManager(maLayoutManager)
        recyclerView!!.setNestedScrollingEnabled(false)
        recyclerView!!.setItemAnimator(DefaultItemAnimator())
        val dividerDrawable = ContextCompat.getDrawable(this, R.drawable.divider_drawable)
        recyclerView!!.addItemDecoration(DividerItemDecoration(dividerDrawable))
        mAllPostAdapter = AllPostAdapter(java.util.ArrayList(), "List")
        readAllPosts()
        progreessbar1!!.visibility = View.GONE
    }

    private fun readAllPosts() {
        val reference = FirebaseDatabase.getInstance().reference
        val myQuery = reference.child(ConsumeAPI.FB_POST).orderByChild("createdAt")

        myQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val mmPost = java.util.ArrayList<PostProduct>()
                for (snapshot in dataSnapshot.children) {
                    try {
                        var locationDT = ""
                        val obj = JSONObject(snapshot.value as Map<*, *>?)
                        val isProduction = obj.getBoolean("isProduction")
                        val status = obj.getInt("status")
                        if (isProduction == ConsumeAPI.IS_PRODUCTION && status == 4) {
                            itemCount++
                            val id = obj.getString("id")
                            val coverUrl = obj.getString("coverUrl")
                            val createdAt = obj.getString("createdAt")
                            val price = obj.getString("price")
                            val discountAmount = obj.getString("discountAmount")
                            val discountType = obj.getString("discountType")
                            var location = obj.getString("location")
                            val viewCount = obj.getInt("viewCount")
                            val title = obj.getString("title")
                            val type = obj.getString("type")

                            if (!location.isEmpty()) {
                                val lateLong = location.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                                location = CommonFunction.getAddressFromMap(this@Home, java.lang.Double.parseDouble(lateLong[0]), java.lang.Double.parseDouble(lateLong[1]))
                                locationDT = "$location - "
                            }
                            if (createdAt != "null") {
                                try {
                                    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                                    sdf.timeZone = TimeZone.getTimeZone("GMT")
                                    val time = sdf.parse(createdAt).time
                                    val now = System.currentTimeMillis()

                                    val ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)
                                    //Log.d(TAG,approvedDate+" "+ago.toString());
                                    locationDT = locationDT + ago.toString()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }

                            }

                            mAllPosts.add(PostProduct(Integer.parseInt(id), title, type, coverUrl, price, locationDT, viewCount, discountType, discountAmount))
                            mmPost.add(PostProduct(Integer.parseInt(id), title, type, coverUrl, price, locationDT, viewCount, discountType, discountAmount))
                            Log.d("HOME", "Result $locationDT")
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
                Collections.sort(mmPost) { s1, s2 -> Integer.compare(s2.postId, s1.postId) }
                mAllPostAdapter.addItems(mmPost)
                recyclerView!!.setAdapter(mAllPostAdapter)
                ViewCompat.setNestedScrollingEnabled(recyclerView!!, false)
                mAllPostAdapter.notifyDataSetChanged()

                list = findViewById(R.id.img_list)
                list!!.setOnClickListener {
                    list!!.setImageResource(R.drawable.icon_list_c)
                    image_list!!.setImageResource(R.drawable.icon_image)
                    grid!!.setImageResource(R.drawable.icon_grid)
                    recyclerView!!.adapter = AllPostAdapter(mmPost, "List")
                    recyclerView!!.layoutManager = GridLayoutManager(this@Home,1)
                }
                grid = findViewById(R.id.grid)
                grid!!.setOnClickListener {
                    grid!!.setImageResource(R.drawable.icon_grid_c)
                    image_list!!.setImageResource(R.drawable.icon_image)
                    list!!.setImageResource(R.drawable.icon_list)
                    recyclerView!!.adapter = AllPostAdapter(mmPost, "Grid")
                    recyclerView!!.layoutManager = GridLayoutManager(this@Home,2)
                }
                image_list = findViewById(R.id.btn_image)
                image_list!!.setOnClickListener {
                    image_list!!.setImageResource(R.drawable.icon_image_c)
                    grid!!.setImageResource(R.drawable.icon_grid)
                    list!!.setImageResource(R.drawable.icon_list)
                    recyclerView!!.adapter = AllPostAdapter(mmPost, "Image")
                    recyclerView!!.layoutManager = GridLayoutManager(this@Home,1)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

}