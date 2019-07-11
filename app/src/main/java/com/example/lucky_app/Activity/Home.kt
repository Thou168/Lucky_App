package com.example.lucky_app.Activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.custom.sliderimage.logic.SliderImage
import com.example.lucky_app.Buy_Sell_Rent.Buy.Buy
import com.example.lucky_app.Buy_Sell_Rent.Rent.Rent
import com.example.lucky_app.Buy_Sell_Rent.Sell.Sell
import com.example.lucky_app.Login_Register.UserAccount
import com.example.lucky_app.Product_New_Post.MyAdapter_list_grid_image
import com.example.lucky_app.Product_dicount.MyAdapter
import com.example.lucky_app.R
import com.example.lucky_app.Startup.Item
import com.example.lucky_app.Startup.Search1
import com.example.lucky_app.Startup.Your_Post
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class Home : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

    var recyclerView: RecyclerView? = null
    var list_item: ArrayList<Item_API>? = null
    var grid: ImageView? = null
    var list: ImageView? = null
    var image_list: ImageView? = null
//    var click: String = "Khmer"
    lateinit var sharedPreferences: SharedPreferences

    val myPreferences = "mypref"
    val namekey = "Khmer"

    var english: ImageView? = null
    var khmer: ImageView? = null

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
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.title = " "
        setSupportActionBar(toolbar)

        val sharedPref: SharedPreferences = getSharedPreferences("Register", Context.MODE_PRIVATE)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if (sharedPref.contains("token") || sharedPref.contains("id")){
            navView.setVisibility(View.VISIBLE)
        }else{
            navView.setVisibility(View.GONE)
        }

        val prefer = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language = prefer.getString("My_Lang", "")

        Log.d("khmer",language)

        khmer = findViewById(R.id.khmer)
        english = findViewById(R.id.english)
        if(language.equals("km")) {
            english!!.visibility = View.VISIBLE
            khmer!!.visibility = View.GONE
        }else{
            english!!.visibility = View.GONE
            khmer!!.visibility = View.VISIBLE
        }
        english!!.setOnClickListener {
            language("en")
            recreate()
        }
        khmer!!.setOnClickListener {
            language("km")
            recreate()
        }

        navView.setNavigationItemSelectedListener(this)
        val bnavigation = findViewById<BottomNavigationView>(R.id.bnaviga)
        bnavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
//                    val intent = Intent(this@Home,Home::class.java)
//                    startActivity(intent)
//                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
                }
                R.id.notification -> {
                    val intent = Intent(this@Home,Notification::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
                }
                R.id.camera ->{
                    if (sharedPref.contains("token") || sharedPref.contains("id")) {
                        val intent = Intent(this@Home, Camera::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }else{
                        val intent = Intent(this@Home, UserAccount::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }
                }
                R.id.message -> {val intent = Intent(this@Home,Message::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
                }
                R.id.account ->{
                    if (sharedPref.contains("token") || sharedPref.contains("id")) {
                        val intent = Intent(this@Home, Account::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }else{
                        val intent = Intent(this@Home, UserAccount::class.java)
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
            //      getActivity()!!.getSupportFragmentManager().beginTransaction().replace(R.id.content,fragment_buy_vehicle()).commit()
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
//RecyclerView
        val horizontal = findViewById<RecyclerView>(R.id.horizontal)
        val version = ArrayList<Item>()
        version.addAll(Item.getType("Discount"))
        horizontal.layoutManager = LinearLayoutManager(this@Home, LinearLayout.HORIZONTAL, false)
        horizontal.adapter = MyAdapter(version)

        val listview = findViewById<RecyclerView>(R.id.list_new_post)
//        val item = ArrayList<Item>()
//        item.addAll(Item.getList())
        val item = ArrayList<Item_API>()
        item.addAll(Get())
        Log.d("Item API: ",item.size.toString())
        //  listview.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        recyclerView = findViewById<RecyclerView>(R.id.list_new_post)
        recyclerView!!.layoutManager = GridLayoutManager(this@Home,1)
        //Get()
//        recyclerView!!.adapter = MyAdapter_list_grid_image(item, "List")
//List Grid and image
//        val list = findViewById<ImageView>(R.id.img_list)
//        list.setOnClickListener {
//            recyclerView!!.adapter = MyAdapter_list_grid_image(item, "List")
//            recyclerView!!.layoutManager = GridLayoutManager(this@Home,1)
//        }

//        val grid = findViewById<ImageView>(R.id.grid)
//        grid.setOnClickListener {
//            recyclerView!!.adapter = MyAdapter_list_grid_image(item, "Grid")
//            recyclerView!!.layoutManager = GridLayoutManager(this@Home,2)
//        }
//        val image = findViewById<ImageView>(R.id.btn_image)
//        image.setOnClickListener {
//            recyclerView!!.adapter = MyAdapter_list_grid_image(item, "Image")
//            recyclerView!!.layoutManager = GridLayoutManager(this@Home, 1)
//        }
//Dropdown
        val category = resources.getStringArray(R.array.category)
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, category)
        val sp_category = findViewById<MaterialBetterSpinner>(R.id.sp_category)
        sp_category.setAdapter(adapter)
        val items = ArrayList<Item>()
        sp_category.setOnItemClickListener { parent, view, position, id ->
            val selected = sp_category.getText()
            if (selected.equals("Motobike")){
                items.addAll(Item.getType("Motobike"))
            }else{
                items.addAll(Item.getType("Electronic"))
            }
            Toast.makeText(this@Home,selected,Toast.LENGTH_LONG).show()
        }

//Search
        val search = findViewById<EditText>(R.id.search)
        search.setOnClickListener{
           val intent = Intent(this@Home, Search1::class.java)
         //   intent.putExtra("items",Item.getList())
            startActivity(intent)
        }

        val brand = resources.getStringArray(R.array.brand)
        val adapter1 = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, brand)
        val sp_brand = findViewById<MaterialBetterSpinner>(R.id.sp_brand)
        sp_brand.setAdapter(adapter1)
        sp_brand.setOnItemClickListener { parent, view, position, id ->
            val selected = sp_brand.getText()
            Toast.makeText(this@Home,selected,Toast.LENGTH_LONG).show()
        }

        val year = resources.getStringArray(R.array.year)
        val adapter2 = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, year)
        val sp_year = findViewById<MaterialBetterSpinner>(R.id.sp_year)
        sp_year.setAdapter(adapter2)
        sp_year.setOnItemClickListener { parent, view, position, id ->
            val selected = sp_year.getText()
            Toast.makeText(this@Home,selected,Toast.LENGTH_LONG).show()
        }

    }
    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {

            super.onBackPressed()
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.account, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.action_language -> {
//                if(click.equals("Khmer")){
//                    item.setIcon(R.drawable.flag_khmer)
//                    val editor = sharedPreferences.edit()
//                    editor.putString(click,"English")
//                    editor.commit()
//
//                    language("km")
//                    recreate()
//                }else{
//                    item.setIcon(R.drawable.flag_english)
//
//
//                }
//
//
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_profile -> {
                // Handle the camera action
                val intent = Intent(this@Home,Account::class.java)
                startActivity(intent)
            }
            R.id.nav_post -> {
                val intent = Intent(this@Home, Your_Post::class.java)
                startActivity(intent)
            }
            R.id.nav_like -> {

            }
            R.id.nav_loan -> {

            }
            R.id.nav_setting -> {

            }
            R.id.nav_about -> {

            }
            R.id.nav_privacy -> {

            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun Get(): ArrayList<Item_API>{

        val itemApi = ArrayList<Item_API>()
        val url = "http://103.205.26.103:8000/allposts/"
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

                    Log.d("count",jsonArray.length().toString())

                    for (i in 0 until jsonArray.length()) {

                        val `object` = jsonArray.getJSONObject(i)

                        val title = `object`.getString("title")
                        val id = `object`.getInt("id")
                        val condition = `object`.getString("condition")
                        val cost = `object`.getDouble("cost")
                        val image = `object`.getString("base64_front_image")
                        val img_user = `object`.getString("base64_right_image")

//                        val buy = `object`.getString("buys")
//                        val sell = `object`.getString("sales")
                        val postType = `object`.getString("post_type")
//                        var postType: String? = null
//                        when {
//                            buy != "[]" -> postType = "Buy"
//                            sell != "[]" -> postType = "Sell"
//                            rent != "[]" -> postType = "Rent"
//                        }
//                        Log.d("PostType",postType)
                        itemApi.add(Item_API(id,img_user,image,title,cost,condition,postType))
                        runOnUiThread {
                            recyclerView!!.adapter = MyAdapter_list_grid_image(itemApi, "List")
//List Grid and Image
                            list = findViewById(R.id.img_list)
                            list!!.setOnClickListener {
                                recyclerView!!.adapter = MyAdapter_list_grid_image(itemApi, "List")
                                recyclerView!!.layoutManager = GridLayoutManager(this@Home,1)
                            }
                            grid = findViewById(R.id.grid)
                            grid!!.setOnClickListener {
                                recyclerView!!.adapter = MyAdapter_list_grid_image(itemApi, "Grid")
                                recyclerView!!.layoutManager = GridLayoutManager(this@Home,2)
                            }
                            image_list = findViewById(R.id.btn_image)
                            image_list!!.setOnClickListener {
                                recyclerView!!.adapter = MyAdapter_list_grid_image(itemApi, "Image")
                                recyclerView!!.layoutManager = GridLayoutManager(this@Home,1)
                            }
                        }

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        })
        return itemApi
    }
}
