package com.example.lucky_app.Activity

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.content.SharedPreferences
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
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
import com.google.android.material.bottomnavigation.BottomNavigationView

class Home : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val sharedPref: SharedPreferences = getSharedPreferences("Register", Context.MODE_PRIVATE);
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

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
                    val intent = Intent(this@Home,Camera::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
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

        val horizontal = findViewById<RecyclerView>(R.id.horizontal)
        val version = ArrayList<Item>()
        version.addAll(Item.getType("Discount"))
        horizontal.layoutManager = LinearLayoutManager(this@Home, LinearLayout.HORIZONTAL, false)
        horizontal.adapter = MyAdapter(version)

        val listview = findViewById<RecyclerView>(R.id.list_new_post)
        val item = ArrayList<Item>()
        item.addAll(Item.getList())
        //  listview.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        recyclerView = findViewById<RecyclerView>(R.id.list_new_post)
        recyclerView!!.layoutManager = GridLayoutManager(this@Home,1)
        recyclerView!!.adapter = MyAdapter_list_grid_image(item, "List")
//List Grid and image
        val list = findViewById<ImageView>(R.id.img_list)
        list.setOnClickListener {
            recyclerView!!.adapter = MyAdapter_list_grid_image(item, "List")
            recyclerView!!.layoutManager = GridLayoutManager(this@Home,1)
        }

        val grid = findViewById<ImageView>(R.id.grid)
        grid.setOnClickListener {
            recyclerView!!.adapter = MyAdapter_list_grid_image(item, "Grid")
            recyclerView!!.layoutManager = GridLayoutManager(this@Home,2)
        }
        val image = findViewById<ImageView>(R.id.btn_image)
        image.setOnClickListener {
            recyclerView!!.adapter = MyAdapter_list_grid_image(item, "Image")
            recyclerView!!.layoutManager = GridLayoutManager(this@Home, 1)
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.account, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_profile -> {
                // Handle the camera action
                val intent = Intent(this@Home,Account::class.java)
                startActivity(intent)
            }
            R.id.nav_post -> {
                val intent = Intent(this@Home,Your_Post::class.java)
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
}
