package com.bt_121shoppe.motorbike.Product_dicount

import android.content.Intent
import android.graphics.BitmapFactory
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bt_121shoppe.motorbike.activities.Item_discount
import com.bt_121shoppe.motorbike.Api.ConsumeAPI
import com.bt_121shoppe.motorbike.Api.User
import com.bt_121shoppe.motorbike.Product_New_Post.Detail_New_Post
import com.bt_121shoppe.motorbike.R
import com.bt_121shoppe.motorbike.utils.CommomAPIFunction
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.JsonParseException
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.*
import java.io.IOException

class MyAdapter(private val itemList: ArrayList<Item_discount>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_discount, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val textView = itemView.findViewById<TextView>(R.id.name)
        val imageView = itemView.findViewById<ImageView>(R.id.image)
        val img_user = itemView.findViewById<CircleImageView>(R.id.img_user)
        val title = itemView.findViewById<TextView>(R.id.title)
        val discount = itemView.findViewById<TextView>(R.id.tv_discount)
        val cost = itemView.findViewById<TextView>(R.id.tv_price)
        val location = itemView.findViewById<TextView>(R.id.location)
//        val count_view = itemView.findViewById<TextView>(R.id.user_view)
        val count_view = itemView.findViewById<TextView>(R.id.view)


        fun bindItems(item: Item_discount) {
            val dis_type = item.discount_type
            var dis = item.dis
            val cost1 = item.cost
            var price: Double = 0.0
            var discout1: Double = 0.0
            if (dis_type == "amount") {
                price = cost1 - dis
            }else if (dis_type == "percent"){
                discout1 = cost1*(dis/100)
                price = cost1 - discout1
            }
            val st = "$"+cost1.toString()
            val ms = SpannableString(st)
            val mst = StrikethroughSpan()
            ms.setSpan(mst,0,st.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            discount.text = ms
            title.text = item.title
            cost.text = "$"+ price.toString()
            location.text = item.time
            count_view.text =item.countview
            Glide.with(itemView.context).load(item.image).placeholder(R.drawable.no_image_available).thumbnail(0.1f).into(imageView)

            itemView.findViewById<LinearLayout>(R.id.linearLayout).setOnClickListener {
                val intent = Intent(itemView.context, Detail_New_Post::class.java)
                intent.putExtra("Discount",price)
                intent.putExtra("Price",item.cost)
                intent.putExtra("ID",item.id)
                itemView.context.startActivity(intent)
            }
            var user1 = User()
            var URL_ENDPOINT= ConsumeAPI.BASE_URL+"api/v1/users/"+item.user_id
            var MEDIA_TYPE= MediaType.parse("application/json")
            var client= OkHttpClient()
            var request = Request.Builder()
                    .url(URL_ENDPOINT)
                    .header("Accept","application/json")
                    .header("Content-Type","application/json")
                    //.header("Authorization",encode)
                    .build()
            client.newCall(request).enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {
                    val mMessage = e.message.toString()
                    Log.w("failure Response", mMessage)
                }
                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    val mMessage = response.body()!!.string()
                    val gson = Gson()
                    try {
                        user1= gson.fromJson(mMessage, User::class.java)
                        val profilepicture: String=if(user1.profile.profile_photo==null) "" else user1.profile.base64_profile_image
                        val coverpicture: String= if(user1.profile.cover_photo==null) "" else user1.profile.base64_cover_photo_image
                        if(user1.getFirst_name().isEmpty())
                        {
//                            Username!!.setText(user1.getUsername())
                        }else {
//                            Username!!.setText(user1.getFirst_name())
                        }

                        CommomAPIFunction.getUserProfileFB(itemView.context,img_user,user1.username)

                        if(coverpicture.isNullOrEmpty()){

                        }else {
                            val decodedString = Base64.decode(coverpicture, Base64.DEFAULT)
                            var decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
//                                imgCover!!.setImageBitmap(decodedByte)
                        }

                    }catch (e: JsonParseException) { e.printStackTrace() }
                }
            })
        }
    }
}