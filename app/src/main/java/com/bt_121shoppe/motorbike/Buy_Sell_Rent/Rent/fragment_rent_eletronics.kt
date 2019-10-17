package com.bt_121shoppe.motorbike.Buy_Sell_Rent.Sell

import android.content.Context
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.bt_121shoppe.motorbike.R
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.custom.sliderimage.logic.SliderImage
import com.bt_121shoppe.motorbike.Activity.Item_API
import com.bt_121shoppe.motorbike.Api.ConsumeAPI
import com.bt_121shoppe.motorbike.Product_New_Post.MyAdapter_list_grid_image
import com.bt_121shoppe.motorbike.models.PostViewModel
import com.bt_121shoppe.motorbike.utils.CommonFunction
import com.google.gson.Gson
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import com.google.gson.JsonParseException

class fragment_rent_eletronics : Fragment() {

    private var username: String? = null
    private var password: String? = null
    private var pk: Int? = null
    var encodeAuth=""
    var recycleView:RecyclerView?= null

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
        val toolbar: Toolbar =view.findViewById(R.id.toolbar)
        toolbar.setBackgroundColor(activity!!.getColor(R.color.logo_red))
        //Back
        val back = view.findViewById<TextView>(R.id.tv_back)
        back.setOnClickListener { getActivity()?.finish() }
        recycleView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val preferences = activity!!.getSharedPreferences("RegisterActivity", Context.MODE_PRIVATE)
        username=preferences.getString("name","")
        password=preferences.getString("pass","")
        encodeAuth="Basic "+ CommonFunction.getEncodedString(username, password)
        if (preferences.contains("token")) {
            pk = preferences.getInt("Pk", 0)
        } else if (preferences.contains("id")) {
            pk = preferences.getInt("id", 0)
        }
        Listelectronic_rent(container!!.context)
        return view
    }
    private fun Listelectronic_rent (context1:Context) {
        var item=ArrayList<Item_API>()
        var posts= PostViewModel()
        val url =  ConsumeAPI.BASE_URL+"relatedpost/?post_type=rent&category=1&modeling=&min_price=&max_price="
        var MEDIA_TYPE=MediaType.parse("application/json")
        val client = OkHttpClient()
        val request = Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
//                .header("Authorization",encodeAuth )
                .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val respon = response.body()!!.string()
                try {
                    activity!!.runOnUiThread{
                        val jsonObject = JSONObject(respon)
                        val jsonArray = jsonObject.getJSONArray("results")
                        for (i in 0 until jsonArray.length()) {
                            val `object` = jsonArray.getJSONObject(i)
                            val title = `object`.getString("title")
                            val id = `object`.getInt("id")
                            val user_id = `object`.getInt("user")
                            val condition = `object`.getString("condition")
                            val cost = `object`.getDouble("cost")
                            val image = `object`.getString("front_image_path")
                            val img_user = `object`.getString("right_image_path")
                            val postType = `object`.getString("post_type")
                            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                            sdf.setTimeZone(TimeZone.getTimeZone("GMT"))
                            val time:Long = sdf.parse(`object`.getString("created")).getTime()
                            val now:Long = System.currentTimeMillis()
                            val ago:CharSequence = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)
//                            val category = `object`.getInt("category")
                            val discount_type = `object`.getString("discount_type")
                            val discount = `object`.getDouble("discount")
                            val postsubtitle = `object`.getString("post_sub_title")
                            val URL_ENDPOINT1= ConsumeAPI.BASE_URL+"countview/?post="+id
                            var MEDIA_TYPE=MediaType.parse("application/json")
                            val client1= OkHttpClient()
                            //val auth = "Basic $encode"
                            val request1=Request.Builder()
                                    .url(URL_ENDPOINT1)
                                    .header("Accept","application/json")
                                    .header("Content-Type","application/json")
//                                    .header("Authorization",encodeAuth)
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
                                        Log.d("FRAGMENT 1",mMessage)
                                        val jsonObject= JSONObject(mMessage)
                                        val jsonCount=jsonObject.getInt("count")
                                        activity!!.runOnUiThread {
                                            item.add(Item_API(id, user_id,image, img_user, title, cost, condition, postType,ago.toString(),jsonCount.toString(),discount_type,discount,postsubtitle))
                                            Log.d("Item: ", item.size.toString())
                                            recycleView!!.layoutManager = GridLayoutManager(context, 1)
                                            recycleView!!.adapter = MyAdapter_list_grid_image(item, "List",context1)
                                        }

                                    } catch (e: JsonParseException) {
                                        e.printStackTrace()
                                    }

                                }
                            })
                        }

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        })
    }
}
