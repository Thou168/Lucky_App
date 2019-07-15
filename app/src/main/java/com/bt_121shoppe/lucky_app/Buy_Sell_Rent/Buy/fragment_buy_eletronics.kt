package com.bt_121shoppe.lucky_app.Buy_Sell_Rent.Buy

import android.content.Context
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bt_121shoppe.lucky_app.R
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.custom.sliderimage.logic.SliderImage
import com.bt_121shoppe.lucky_app.Activity.Item_API
import com.bt_121shoppe.lucky_app.Api.ConsumeAPI
import com.bt_121shoppe.lucky_app.Product_New_Post.MyAdapter_list_grid_image
import com.bt_121shoppe.lucky_app.models.PostViewModel
import com.bt_121shoppe.lucky_app.utils.CommonFunction
import com.google.gson.Gson
import com.google.gson.JsonParseException
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [fragment_buy_eletronics]interface.
 */
class fragment_buy_eletronics : Fragment() {

    private var username: String? = null
    private var password: String? = null
    private var pk: Int? = null
    var encodeAuth=""
    var recycleview:RecyclerView?= null

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
        val back = view.findViewById<TextView>(R.id.tv_back)
        back.setOnClickListener { getActivity()?.finish() }

        recycleview = view.findViewById<RecyclerView>(R.id.recyclerView)
//        val item = ArrayList<Item>()
//        item.addAll(Item.getPost_Type("Buy","Electronic"))
//        //  listview.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
//        listview!!.layoutManager = GridLayoutManager(context,1)
//        listview!!.adapter = MyAdapter_list(item,null)

        val preferences = activity!!.getSharedPreferences("Register", Context.MODE_PRIVATE)
        username=preferences.getString("name","")
        password=preferences.getString("pass","")
        encodeAuth="Basic "+ CommonFunction.getEncodedString(username, password)
        if (preferences.contains("token")) {
            pk = preferences.getInt("Pk", 0)
        } else if (preferences.contains("id")) {
            pk = preferences.getInt("id", 0)
        }

        Listelectronic_buy()

        return view
    }
    private fun Listelectronic_buy () {

        var item=ArrayList<Item_API>()
        var posts= PostViewModel()
        val url =  "http://103.205.26.103:8000/relatedpost/?post_type=buy&category=1&modeling=&min_price=&max_price="
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
                            val condition = `object`.getString("condition")
                            val cost = `object`.getDouble("cost")
                            val image = `object`.getString("front_image_base64")
                            val img_user = `object`.getString("right_image_base64")
                            val postType = `object`.getString("post_type")
                            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                            sdf.setTimeZone(TimeZone.getTimeZone("GMT"))
                            val time:Long = sdf.parse(`object`.getString("created")).getTime()
                            val now:Long = System.currentTimeMillis()
                            val ago:CharSequence = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)
//                            val category = `object`.getInt("category")

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
                                            item.add(Item_API(id, image, img_user, title, cost, condition, postType,ago.toString(),jsonCount.toString()))
                                            Log.d("Item: ", item.size.toString())
                                            recycleview!!.layoutManager = GridLayoutManager(context, 1)
                                            recycleview!!.adapter = MyAdapter_list_grid_image(item, "List")
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
