package com.example.lucky_app.Fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.example.lucky_app.R
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lucky_app.Activity.Item_API
import com.example.lucky_app.Api.ConsumeAPI
import com.example.lucky_app.Product_New_Post.MyAdapter_list_grid_image
import com.example.lucky_app.Product_New_Post.MyAdapter_user_post
import com.example.lucky_app.Startup.Item
import com.example.lucky_app.Startup.MyAdapter_list
import com.example.lucky_app.models.PostViewModel
import com.google.gson.Gson
import com.google.gson.JsonParseException
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [user_post_list]interface.
 */
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

//        val id = ACTIVITY.intent.getIntExtra("ID",0)
//        Log.d("ID in User_Post",id.toString())
//
//        val item = ArrayList<Item>()
//        item.clear()
//        item.addAll(Item.getUser_Post(id))
//
//        recyclrview.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
//        recyclrview.adapter = MyAdapter_list(item,null)

        var bundle :Bundle ?= activity!!.intent.extras
        user_id = activity!!.intent.getStringExtra("ID").toInt()
        val sharedPref: SharedPreferences = activity!!.getSharedPreferences("Register", Context.MODE_PRIVATE)
        username = sharedPref.getString("name", "")
        password = sharedPref.getString("pass", "")
        encode = "Basic "+com.example.lucky_app.utils.CommonFunction.getEncodedString(username,password)

        getUserPosts()
        return view
    }
    fun getUserPosts(){
        val itemApi = ArrayList<Item_API>()
        var posts= PostViewModel()
        val URL_ENDPOINT= ConsumeAPI.BASE_URL+"postbyuserfilter/?created_by="+user_id+"&approved_by=&rejected_by=&modified_by="
        var MEDIA_TYPE= MediaType.parse("application/json")
        val client= OkHttpClient()
        val request= Request.Builder()
                .url(URL_ENDPOINT)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .header("Authorization",encode)
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
                                val condition = obj.getString("condition")
                                val cost = obj.getDouble("cost")
                                val image = obj.getString("front_image_base64")
                                val img_user = obj.getString("right_image_base64")
                                val postType = obj.getString("post_type")

                                //var count_view=countPostView(encodeAuth,id)

                                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                                sdf.setTimeZone(TimeZone.getTimeZone("GMT"))
                                val time:Long = sdf.parse(obj.getString("created")).getTime()
                                val now:Long = System.currentTimeMillis()
                                val ago:CharSequence = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)

                                val URL_ENDPOINT1= ConsumeAPI.BASE_URL+"countview/?post="+id
                                var MEDIA_TYPE=MediaType.parse("application/json")
                                val client1= OkHttpClient()
                                //val auth = "Basic $encode"
                                val request1=Request.Builder()
                                        .url(URL_ENDPOINT1)
                                        .header("Accept","application/json")
                                        .header("Content-Type","application/json")
                                        .header("Authorization",encode)
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
                                                itemApi.add(Item_API(id,img_user,image,title,cost,condition,postType,ago.toString(),jsonCount.toString()))
                                                recyclrview!!.adapter = MyAdapter_list_grid_image(itemApi, "List")
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
