package com.bt_121shoppe.motorbike.Fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.bt_121shoppe.motorbike.R
import androidx.recyclerview.widget.RecyclerView
import com.bt_121shoppe.motorbike.Activity.Item_API
import com.bt_121shoppe.motorbike.Api.ConsumeAPI
import com.bt_121shoppe.motorbike.Product_New_Post.MyAdapter_list_grid_image
import com.bt_121shoppe.motorbike.models.PostViewModel
import com.google.gson.Gson
import com.google.gson.JsonParseException
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class user_post_list: Passdata() {
    var user_id:Int=0
    var username:String=""
    var password:String=""
    var encode=""
    var recyclrview: RecyclerView? = null

    @SuppressLint("WrongConstant")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.user_post_list, container, false)
        recyclrview = view.findViewById(R.id.recyclerView)

        var bundle :Bundle ?= activity!!.intent.extras
        user_id = activity!!.intent.getStringExtra("ID").toInt()
        val sharedPref: SharedPreferences = activity!!.getSharedPreferences("RegisterActivity", Context.MODE_PRIVATE)
        username = sharedPref.getString("name", "")
        password = sharedPref.getString("pass", "")
        encode = "Basic "+com.bt_121shoppe.motorbike.utils.CommonFunction.getEncodedString(username,password)

        getUserPosts(container!!.context)
        return view
    }
    fun getUserPosts(context1:Context){
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
                    activity!!.runOnUiThread {
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
                                            activity!!.runOnUiThread {
                                                itemApi.add(Item_API(id,user_id,img_user,image,title,cost,condition,postType,ago.toString(),jsonCount.toString(),color,model,year,discount_type,discount,postsubtitle))
                                                recyclrview!!.adapter = MyAdapter_list_grid_image(itemApi, "List",context1)
                                                recyclrview!!.layoutManager = GridLayoutManager(context,1) as RecyclerView.LayoutManager?
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
}
