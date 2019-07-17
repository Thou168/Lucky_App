package com.bt_121shoppe.lucky_app.Fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bt_121shoppe.lucky_app.Api.ConsumeAPI
import com.bt_121shoppe.lucky_app.Api.User
import com.bt_121shoppe.lucky_app.R
import com.google.gson.Gson
import com.google.gson.JsonParseException
import okhttp3.*
import java.io.IOException

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [contact_user]interface.
 */
class contact_user: Intent_data() {

    var user_id:Int=0
    var username:String=""
    var password:String=""
    var encode=""
    var phone: TextView? = null
    var email: TextView? = null

    @SuppressLint("WrongConstant")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.contact_user, container, false)

        val tvphone = ACTIVITY.intent.getStringExtra("Phone")
        phone = view.findViewById<TextView>(R.id.phone)
        phone!!.text = tvphone
        email = view.findViewById(R.id.email)
        if(ACTIVITY.intent.getStringExtra("Email") == null){
            email!!.text = "Null"
        }else
        email!!.text = ACTIVITY.intent.getStringExtra("Email")


//        var bundle :Bundle ?=ACTIVITY.intent.extras
//        user_id = ACTIVITY.intent.getStringExtra("ID").toInt()
//        val sharedPref: SharedPreferences = ACTIVITY.getSharedPreferences("Register", Context.MODE_PRIVATE)
//        username = sharedPref.getString("name", "")
//        password = sharedPref.getString("pass", "")
//        encode = "Basic "+com.bt_121shoppe.lucky_app.utils.CommonFunction.getEncodedString(username,password)
//        Log.d("ENCODE: ",encode)

        return view
    }
//    fun getUserProfile(){
//        var user1 = User()
//        var URL_ENDPOINT= ConsumeAPI.BASE_URL+"api/v1/users/"+user_id
//        var MEDIA_TYPE= MediaType.parse("application/json")
//        var client= OkHttpClient()
//        var request= Request.Builder()
//                .url(URL_ENDPOINT)
//                .header("Accept","application/json")
//                .header("Content-Type","application/json")
//                //.header("Authorization",encode)
//                .build()
//        client.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                val mMessage = e.message.toString()
//                Log.w("failure Response", mMessage)
//            }
//
//            @Throws(IOException::class)
//            override fun onResponse(call: Call, response: Response) {
//                val mMessage = response.body()!!.string()
//
//                val gson = Gson()
//                try {
//                    user1= gson.fromJson(mMessage, User::class.java)
//                    ACTIVITY.runOnUiThread {
//                        val profilepicture: String=if(user1.profile.profile_photo==null) " " else user1.profile.base64_profile_image
//                        val coverpicture: String= if(user1.profile.cover_photo==null) " " else user1.profile.base64_cover_photo_image
//                        Log.d("TAGGGGG",profilepicture)
//                        Log.d("TAGGGGG",coverpicture)
//                        //tvUsername!!.setText(user1.username)
//                        //Glide.with(this@Account).load(profilepicture).apply(RequestOptions().centerCrop().centerCrop().placeholder(R.drawable.default_profile_pic)).into(imgProfile)
//                        //Glide.with(this@Account).load(profilepicture).forImagePreview().into(imgCover)
//                        if(profilepicture==null){
//
//                        }else
//                        {
////                            val decodedString = Base64.decode(profilepicture, Base64.DEFAULT)
////                            var decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
////                            img_user!!.setImageBitmap(decodedByte)
//                        }
//
//                        if(coverpicture==null){
//
//                        }else
//                        {
//                            val decodedString = Base64.decode(coverpicture, Base64.DEFAULT)
//                            var decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
//                            //imgCover!!.setImageBitmap(decodedByte)
//                        }
//
//                    }
//
//                } catch (e: JsonParseException) {
//                    e.printStackTrace()
//                }
//
//            }
//        })
//    }
}
