package com.example.lucky_app.Fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.lucky_app.R
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.custom.sliderimage.logic.SliderImage
import com.example.lucky_app.Buy_Sell_Rent.Buy.Buy
import com.example.lucky_app.Buy_Sell_Rent.Rent.Rent
import com.example.lucky_app.Buy_Sell_Rent.Sell.Sell
import com.example.lucky_app.Startup.Item
import com.example.lucky_app.Product_dicount.MyAdapter
import com.example.lucky_app.Product_New_Post.MyAdapter_list_grid_image
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.fragment_home1.*

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [Fragment_home]interface.
 */
class Fragment_home: Fragment(){

    private var gridLayoutManager: GridLayoutManager? = null
    private var content: FrameLayout? = null
    var recyclerView: RecyclerView? = null

    companion object {
        fun newInstance(): Fragment_home {
            val fragmentHome = Fragment_home()
            val args = Bundle()
            fragmentHome.arguments = args
            return fragmentHome
        }

    }

    @SuppressLint("WrongConstant")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
//SliderImage
        val sliderImage = view.findViewById(R.id.slider) as SliderImage
        val images = listOf("https://i.redd.it/glin0nwndo501.jpg", "https://i.redd.it/obx4zydshg601.jpg",
                            "https://i.redd.it/glin0nwndo501.jpg", "https://i.redd.it/obx4zydshg601.jpg")
        sliderImage.setItems(images)
        sliderImage.addTimerToSlide(3000)
        //  sliderImage.removeTimerSlide()
        sliderImage.getIndicator()
//Buy sell and Rent
        val buy = view.findViewById<ImageButton>(R.id.buy)
        buy.setOnClickListener{
//      getActivity()!!.getSupportFragmentManager().beginTransaction().replace(R.id.content,fragment_buy_vehicle()).commit()
            val intent = Intent(context, Buy::class.java)
            intent.putExtra("Title","Buy")
            startActivity(intent)
        }
        val sell = view.findViewById<ImageButton>(R.id.sell)
        sell.setOnClickListener {
            val intent = Intent(context,Sell::class.java)
            intent.putExtra("Title","Sell")
            startActivity(intent)
        }
        val rent = view.findViewById<ImageButton>(R.id.rent)
        rent.setOnClickListener {
            val intent = Intent(context, Rent::class.java)
            intent.putExtra("Title","Rent")
            startActivity(intent)
        }

        val horizontal = view.findViewById<RecyclerView>(R.id.horizontal)
        val version = ArrayList<Item>()
        version.addAll(Item.getDiscount())
        horizontal.layoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL, false)
        horizontal.adapter = MyAdapter(version)

        val listview = view.findViewById<RecyclerView>(R.id.list_new_post)
        val item = ArrayList<Item>()
        item.addAll(Item.getList())
      //  listview.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        recyclerView = view.findViewById<RecyclerView>(R.id.list_new_post)
        recyclerView!!.layoutManager = GridLayoutManager(context,1)
        recyclerView!!.adapter = MyAdapter_list_grid_image(item, "List")
//List Grid and image
        val list = view.findViewById<ImageButton>(R.id.img_list)
        list.setOnClickListener {
            recyclerView!!.adapter = MyAdapter_list_grid_image(item, "List")
            recyclerView!!.layoutManager = GridLayoutManager(context,1)
        }

        val grid = view.findViewById<ImageButton>(R.id.grid)
        grid.setOnClickListener {
            recyclerView!!.adapter = MyAdapter_list_grid_image(item, "Grid")
            recyclerView!!.layoutManager = GridLayoutManager(context,2)
        }
        val image = view.findViewById<ImageButton>(R.id.btn_image)
        image.setOnClickListener {
            recyclerView!!.adapter = MyAdapter_list_grid_image(item, "Image")
            recyclerView!!.layoutManager = GridLayoutManager(context,1)
//DrawerLayout
//            val drawerLayout: DrawerLayout = view.findViewById(R.id.drawer_layout)
//            val navView: NavigationView = view.findViewById(R.id.nav_view)
//            val toggle = ActionBarDrawerToggle(
//                    activity, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
//            drawerLayout.addDrawerListener(toggle)


        }
        return view
    }


}



