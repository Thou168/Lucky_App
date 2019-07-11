package com.example.lucky_app.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.lucky_app.R
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lucky_app.Activity.Item_API
import com.example.lucky_app.Api.ConsumeAPI
import com.example.lucky_app.Product_New_Post.MyAdapter_list_grid_image
import com.example.lucky_app.Product_New_Post.MyAdapter_user_post
import com.example.lucky_app.Product_dicount.Passtocontact
import com.example.lucky_app.Startup.Item
import com.example.lucky_app.Startup.MyAdapter_list
import com.example.lucky_app.models.PostViewModel
import com.example.lucky_app.utils.CommonFunction.getEncodedString
import com.google.gson.Gson
import com.google.gson.JsonParseException
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [FragmentA1]interface.
 */
class FragmentA1: Fragment() {

    private var username: String? = null
    private var password: String? = null
    private var pk: Int? = null
    var encodeAuth=""
    var recyclerView: RecyclerView? = null

    @SuppressLint("WrongConstant")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment, container, false)


//        val tvphone = ACTIVITY.intent.getStringExtra("Phone")
//        val phone = view.findViewById<TextView>(R.id.phone)
//        phone.text = tvphone
        recyclerView = view.findViewById(R.id.recycler_view)

        Log.d("Hello","lsdfjasjf")

        val preferences = activity!!.getSharedPreferences("Register", Context.MODE_PRIVATE)
        username=preferences.getString("name","")
        password=preferences.getString("pass","")
        encodeAuth="Basic "+ getEncodedString(username,password)
        if (preferences.contains("token")) {
            pk = preferences.getInt("Pk", 0)
        } else if (preferences.contains("id")) {
            pk = preferences.getInt("id", 0)
        }
        getMyPosts()

        return view
    }
    private fun getMyPosts(){
        val itemApi = ArrayList<Item_API>()
        var posts= PostViewModel()
        val URL_ENDPOINT= ConsumeAPI.BASE_URL+"postbyuser/"
        var MEDIA_TYPE= MediaType.parse("application/json")
        val client= OkHttpClient()
        val request= Request.Builder()
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

                    activity!!.runOnUiThread {
                    val jsonObject = JSONObject(mMessage)
                    Log.d("Run  :"," la"+jsonObject)
                    val jsonArray = jsonObject.getJSONArray("results")
                    val jsonCount= jsonObject.getInt("count")
                    for (i in 0 until jsonArray.length()) {

                        val `object` = jsonArray.getJSONObject(i)

                        val title = `object`.getString("title")
                        val id = `object`.getInt("id")
                        val condition = `object`.getString("condition")
                        val cost = `object`.getDouble("cost")
                        val image = `object`.getString("front_image_base64")
                        val img_user = `object`.getString("right_image_base64")
                        val postType = `object`.getString("post_type")

                        itemApi.add(Item_API(id,img_user,image,title,cost,condition,postType))
                        Log.d("Item: ",itemApi.size.toString())
//                        activity!!.runOnUiThread {
                            recyclerView!!.adapter = MyAdapter_user_post(itemApi, "List")
                            recyclerView!!.layoutManager = GridLayoutManager(context,1) as RecyclerView.LayoutManager?
                        }

                    }
//                    activity!!.runOnUiThread {
//                        if(jsonCount>0){
//                            for (i in 0 until jsonArray.length()) {
//                                val obj=jsonArray.getJSONObject(i)
//                                Log.e("TAG","T"+obj)
//                            }
//
//                        }
//
//                    }

                } catch (e: JsonParseException) {
                    e.printStackTrace()
                }

            }
        })
    }
}
