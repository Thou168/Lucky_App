package com.example.lucky_app.Buy_Sell_Rent.Sell

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.lucky_app.R
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.custom.sliderimage.logic.SliderImage
import com.example.lucky_app.Startup.Item
import com.example.lucky_app.Startup.MyAdapter_list

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [fragment_rent_eletronics]interface.
 */
class fragment_sell_eletronics : PassSell2() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_eletronic, container, false)
//Slider
        val sliderImage = view.findViewById(R.id.slider_vehicles) as SliderImage
        val images = listOf("https://i.redd.it/glin0nwndo501.jpg", "https://i.redd.it/obx4zydshg601.jpg",
                            "https://i.redd.it/glin0nwndo501.jpg", "https://i.redd.it/obx4zydshg601.jpg")
        sliderImage.setItems(images)
        sliderImage.addTimerToSlide(3000)
        //  sliderImage.removeTimerSlide()
        sliderImage.getIndicator()
//Back
        val sell_back = BACK_SELL.intent.getStringExtra("Back")
        val back = view.findViewById<TextView>(R.id.tv_back)
        back.text = sell_back.toString()
        back.setOnClickListener { getActivity()?.finish() }

        val listview = view.findViewById<RecyclerView>(R.id.recyclerView)
        val item = ArrayList<Item>()
        item.addAll(Item.getPost_Type("Sell","Electronic"))
        //  listview.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        listview!!.layoutManager = GridLayoutManager(context,1)
        listview!!.adapter = MyAdapter_list(item,null)

        return view
    }
}
