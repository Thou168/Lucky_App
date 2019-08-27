package com.bt_121shoppe.lucky_app.Product_New_Post

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings.Global.getString
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bt_121shoppe.lucky_app.Activity.Account
import com.bt_121shoppe.lucky_app.Api.ConsumeAPI
import com.bt_121shoppe.lucky_app.R
import com.bt_121shoppe.lucky_app.Startup.Unlike_api
import com.bt_121shoppe.lucky_app.utils.CommonFunction
import com.bumptech.glide.Glide
import okhttp3.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException

class MyAdapter_user_like(private val itemList: ArrayList<Unlike_api>, val type: String?) : RecyclerView.Adapter<MyAdapter_user_like.ViewHolder>() {

    internal var loadMoreListener: OnLoadMoreListener? = null
    internal var isLoading = false
    internal var isMoreDataAvailable = true
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (type.equals("List")) {
            val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_list2, parent, false)
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
        holder.bindItems(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val post_type = itemView.findViewById<ImageView>(R.id.post_type)
        val imageView = itemView.findViewById<ImageView>(R.id.image)
        val title = itemView.findViewById<TextView>(R.id.title)
        val cost = itemView.findViewById<TextView>(R.id.tv_price)
//        val location_duration=itemView.findViewById<TextView>(R.id.location)
        val show_view=itemView.findViewById<TextView>(R.id.user_view)
        val unlike = itemView.findViewById<ImageButton>(R.id.imgbtn_unlike)
        val tv_user_view = itemView.findViewById<TextView>(R.id.user_view1)

        fun bindItems(item: Unlike_api) {
            Glide.with(itemView.context).load(item.image).centerCrop().placeholder(R.drawable.no_image_available).thumbnail(0.1f).centerCrop().into(imageView)
            title.text = item.title
            cost.text = "$"+item.cost.toString()
//            location_duration.text=item.location_duration
            show_view.text=""+item.count_view

            var lang:String = tv_user_view.text as String
//            if (lang == "View:"){
//                if (item.postType.equals("sell")){
//                    post_type.setImageResource(R.drawable.sell)
//                }else if (item.postType.equals("buy")){
//                    post_type.setImageResource(R.drawable.buy)
//                }else
//                    post_type.setImageResource(R.drawable.rent)
//            }else{
//                if (item.postType.equals("sell")){
//                    post_type.setImageResource(R.drawable.sell_kh)
//                }else if (item.postType.equals("buy")){
//                    post_type.setImageResource(R.drawable.buy_kh)
//                }else
//                    post_type.setImageResource(R.drawable.rent_kh)
//            }

            Log.d("Like by id", item.like_id.toString())
            itemView.findViewById<LinearLayout>(R.id.linearLayout).setOnClickListener {
                val intent = Intent(itemView.context, Detail_New_Post::class.java)
                intent.putExtra("Price",item.cost)
                intent.putExtra("ID",item.id)
                itemView.context.startActivity(intent)
            }

            unlike.setOnClickListener {
                lateinit var sharedPref: SharedPreferences
                var name=""
                var pass=""
                var Encode=""
                var pk=0
                sharedPref= it.context.getSharedPreferences("Register", Context.MODE_PRIVATE)

                if (sharedPref.contains("token") || sharedPref.contains("id")){
                    name = sharedPref.getString("name", "")
                    pass = sharedPref.getString("pass", "")
                    Encode ="Basic "+ CommonFunction.getEncodedString(name,pass)
                }

                val builder = AlertDialog.Builder(it.context)
                builder.setTitle(R.string.title_unlike)
                        .setMessage(R.string.unlike_message)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes_loan) {
                            dialog, which ->
                            val URL_ENDCODE= ConsumeAPI.BASE_URL+"like/"+item.like_id+"/"
                            Log.d("Url", URL_ENDCODE)
                            val media = MediaType.parse("application/json")
                            val client = OkHttpClient()
                            val data = JSONObject()
                            try{
                                data.put("record_status",2)
                            }catch (e:IOException){
                                e.printStackTrace()
                            }

                            val body = RequestBody.create(media,data.toString())
                            val request = Request.Builder()
                                    .url(URL_ENDCODE)
                                    .put(body)
                                    .header("Accept", "application/json")
                                    .header("Content-Type", "application/json")
                                    .header("Authorization", Encode)
                                    .build()
                            client.newCall(request).enqueue(object :Callback{
                                override fun onFailure(call: Call, e: IOException) {
                                    val message = e.message.toString()
                                    Log.d("failure Response", message)

                                }

                                override fun onResponse(call: Call, response: Response) {
                                    val message = response.body()!!.string()
                                    Log.d("Response Unlike", message)
                                    val intent = Intent(it.context, Account::class.java)
                                    it.context.startActivity(intent)

                                }

                            })
                        }
                        .setNegativeButton(R.string.cancel) { dialog, which -> dialog.cancel() }
                val alert = builder.create()
                alert.show()

            }

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
    //
}