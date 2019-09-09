package com.bt_121shoppe.motorbike.Product_New_Post

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
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
import com.bt_121shoppe.motorbike.Activity.Item_API
import com.bt_121shoppe.motorbike.Api.ConsumeAPI
import com.bt_121shoppe.motorbike.Api.User
import com.bt_121shoppe.motorbike.R
import com.bt_121shoppe.motorbike.useraccount.User_post
import com.bt_121shoppe.motorbike.utils.CommomAPIFunction
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.JsonParseException
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.*
import java.io.ByteArrayOutputStream
import java.io.IOException

class MyAdapter_list_grid_image(private val itemList: ArrayList<Item_API>, val type: String?,val context: Context) : RecyclerView.Adapter<MyAdapter_list_grid_image.ViewHolder>() {

    internal var loadMoreListener: OnLoadMoreListener? = null
    internal var isLoading = false
    internal var isMoreDataAvailable = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (type.equals("List")) {
            val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
            Log.d("Type ",type.toString())
            return ViewHolder(layout)
        }else if (type.equals("Grid")){
            Log.d("Type ",type.toString())
            val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_grid, parent, false)
            return ViewHolder(layout)
        }else{
            Log.d("Type ",type.toString())
            val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
            return ViewHolder(layout)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(itemList[position],context)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val post_type = itemView.findViewById<ImageView>(R.id.post_type)
        val imageView = itemView.findViewById<ImageView>(R.id.image)
        val title = itemView.findViewById<TextView>(R.id.title)
        val cost = itemView.findViewById<TextView>(R.id.tv_price)
        val location_duration=itemView.findViewById<TextView>(R.id.location)
        val show_view= itemView.findViewById<TextView>(R.id.user_view)
        val tv_discount = itemView.findViewById<TextView>(R.id.tv_discount)
        val tv_user_view = itemView.findViewById<TextView>(R.id.user_view1)
        val img_user = itemView.findViewById<CircleImageView>(R.id.img_user)

        fun bindItems(item: Item_API,context: Context) {

            Glide.with(context).load(item.image).centerCrop().placeholder(R.drawable.no_image_available).thumbnail(0.1f).centerCrop().into(imageView)
            var price: Double = 0.0
            var discout1: Double = 0.0
            if (item.discount != 0.00){
                tv_discount.visibility = View.VISIBLE
                val dis_type = item.discount_type
                if (dis_type == "amount") {
                    price = item.cost - item.discount!!.toInt()
                }else if (dis_type == "percent"){
                    discout1 =  item.cost* (item.discount?.div(100))!!
                    price = item.cost - discout1
                }
                val st = "$"+item.cost.toString()
                val ms = SpannableString(st)
                val mst = StrikethroughSpan()
                ms.setSpan(mst,0,st.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                cost.text = "$"+price.toString()
                tv_discount.text = ms
             }else{
                tv_discount.text = price.toString()
                cost.text = "$"+item.cost.toString()
            }

            title.text = item.title
            location_duration.text=item.location_duration
            show_view.text=" "+item.count_view

            var lang:String = tv_user_view.text as String
            if(lang == "View:") {
                if (item.postType.equals("sell")) {
                    post_type.setImageResource(R.drawable.sell)
                } else if (item.postType.equals("buy")) {
                    post_type.setImageResource(R.drawable.buy)
                } else
                    post_type.setImageResource(R.drawable.rent)
            }else{
                if (item.postType.equals("sell")) {
                    post_type.setImageResource(R.drawable.sell_kh)
                } else if (item.postType.equals("buy")) {
                    post_type.setImageResource(R.drawable.buy_kh)
                } else
                    post_type.setImageResource(R.drawable.rent_kh)
            }
            itemView.findViewById<LinearLayout>(R.id.linearLayout).setOnClickListener {
                val intent = Intent(itemView.context, Detail_New_Post::class.java)
                  intent.putExtra("Discount",price)
                  intent.putExtra("Price",item.cost)
                  intent.putExtra("ID",item.id)
                itemView.context.startActivity(intent)
            }
            var user1 = User()
            var URL_ENDPOINT= ConsumeAPI.BASE_URL+"api/v1/users/"+item.userId
            var MEDIA_TYPE= MediaType.parse("application/json")
            var client= OkHttpClient()
            var request= Request.Builder()
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
                    try {
                        user1= gson.fromJson(mMessage, User::class.java)

                        CommomAPIFunction.getUserProfileFB(context,img_user,user1.username)


                    } catch (e: JsonParseException) {
                        e.printStackTrace()
                    }

                }
            })
        }
        fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
            val bytes = ByteArrayOutputStream()
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null)
            return Uri.parse(path)
        }
        fun BitMapToString(bitmap: Bitmap): String {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val arr = baos.toByteArray()
            return Base64.encodeToString(arr, Base64.DEFAULT)
        }
    }

    //
    internal class LoadHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun setMoreDataAvailable(moreDataAvailable: Boolean) {
        isMoreDataAvailable = moreDataAvailable
    }

    fun notifyDataChanged() {
        notifyDataSetChanged()
        isLoading = false
    }


    interface OnLoadMoreListener {
        fun onLoadMore()
    }

    fun setLoadMoreListener(loadMoreListener: OnLoadMoreListener) {
        this.loadMoreListener = loadMoreListener
    }
}