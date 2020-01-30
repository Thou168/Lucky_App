package com.bt_121shoppe.motorbike.Product_New_Post

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
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
import com.bt_121shoppe.motorbike.Activity.Detail_new_post_java
import com.bt_121shoppe.motorbike.Api.ConsumeAPI
import com.bt_121shoppe.motorbike.Api.User
import com.bt_121shoppe.motorbike.R
import com.bt_121shoppe.motorbike.activities.Item_API
import com.bt_121shoppe.motorbike.utils.CommomAPIFunction
import com.bt_121shoppe.motorbike.utils.CommonFunction
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
            val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_list_for_search, parent, false)
            Log.d("Type ",type.toString())
            return ViewHolder(layout)
        }else if (type.equals("Grid")){
            Log.d("Type ",type.toString())
            val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_grid, parent, false)
            return ViewHolder(layout)
        } else if (type.equals("image")){
            Log.d("Type ",type.toString())
            val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_list3, parent, false)
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
//        val post_type = itemView.findViewById<ImageView>(R.id.post_type)
        val post_type = itemView.findViewById<TextView>(R.id.post_type)
        val imageView = itemView.findViewById<ImageView>(R.id.image)
        val title = itemView.findViewById<TextView>(R.id.title)
        val cost = itemView.findViewById<TextView>(R.id.tv_price)
        val location_duration=itemView.findViewById<TextView>(R.id.location)
        val show_view= itemView.findViewById<TextView>(R.id.user_view)
        val tv_discount = itemView.findViewById<TextView>(R.id.tv_discount)
        val tv_user_view = itemView.findViewById<TextView>(R.id.user_view1)
        val img_user = itemView.findViewById<CircleImageView>(R.id.img_user)
        val lang = itemView.findViewById<TextView>(R.id.user_view1)
        var tvColor1 = itemView.findViewById<TextView>(R.id.tv_color1)
        var tvColor2 = itemView.findViewById<TextView>(R.id.tv_color2)
        val category = itemView.findViewById<TextView>(R.id.cate);

        fun bindItems(item: Item_API,context: Context) {

            Glide.with(context).load(item.image).centerCrop().placeholder(R.drawable.no_image_available).thumbnail(0.1f).into(imageView)
            var price: Double = 0.00
            var discount: Double = 0.00
            var language:String
            if (item.discount == 0.00){
                tv_discount.visibility = View.GONE
//                tv_discount.text = price.toString()
                cost.text = "$" + item.cost.toString()
             }else {
                tv_discount.visibility = View.VISIBLE
                price = item.cost - item.discount!!.toInt()
                discount =  item.cost* (item.discount?.div(100))!!
                price = item.cost - discount
                cost.text = "$$price"

                val st = "$"+item.cost.toString()
                val ms = SpannableString(st)
                val mst = StrikethroughSpan()
                ms.setSpan(mst,0,st.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                tv_discount.text = ms
            }
            language=lang.text.toString()
            var strPostTitle:String = ""
            if (item.postsubtitle.isNotEmpty()){
                if (language.equals("View:")){
                    strPostTitle = item.postsubtitle.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
                }else{
                    strPostTitle = item.postsubtitle.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                }
            } else{
//                val postTitle=CommonFunction.generatePostSubTitle(item.modeling,item.year,item.color)
//                if (language.equals("View:"))
//                    strPostTitle = postTitle.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0]
//                else
//                    strPostTitle = postTitle.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1]
            }
            title.text = strPostTitle
//            if (strPostTitle.length > 36) {
//                strPostTitle = strPostTitle.substring(0, 36) + "..."
//                title.setText(strPostTitle)
//            } else {
//                title.setText(strPostTitle)
//            }
            //location_duration.text=item.location_duration
            show_view.text=" "+item.count_view

            //category
            category.visibility = View.VISIBLE
            if (item.category==1){
                category.setText(R.string.electronic)
            }else{
                category.setText(R.string.motor)
            }

            //            GradientDrawable backgroundGradient = (GradientDrawable)tvColor1.getBackground();
//            backgroundGradient.setColor(itemView.getContext().getResources().getColor(R.color.logo_green));
            val splitColor: Array<String> = item.color.split(",").toTypedArray()

            val shape = GradientDrawable()
            shape.shape = GradientDrawable.OVAL
            shape.setColor(Color.parseColor(CommonFunction.getColorHexbyColorName(splitColor[0])))
            tvColor1.background = shape
            tvColor2.visibility = View.GONE
            if (splitColor.size > 1) {
                tvColor2.visibility = View.VISIBLE
                val shape1 = GradientDrawable()
                shape1.shape = GradientDrawable.OVAL
                shape1.setColor(Color.parseColor(CommonFunction.getColorHexbyColorName(splitColor[1])))
                tvColor2.background = shape
            }

            var lang:String = tv_user_view.text as String
            if(lang == "View:") {
                if (item.postType.equals("sell")) {
//                    post_type.setImageResource(R.drawable.sell)
//                    Glide.with(itemView.getContext()).load(R.drawable.sell).thumbnail(0.1f).into(typeView);
                    post_type.setText(R.string.sell)
                    post_type.setBackgroundResource(R.drawable.roundimage)
                } else if (item.postType.equals("rent")) {
//                    post_type.setImageResource(R.drawable.buy)
//                    Glide.with(itemView.getContext()).load(R.drawable.sell).thumbnail(0.1f).into(typeView);
                    post_type.setText(R.string.ren)
                    post_type.setBackgroundResource(R.drawable.roundimage_rent)
                }
//                } else
//                    post_type.setImageResource(R.drawable.rent)
            }else {
                if (item.postType.equals("sell")) {
//                    post_type.setImageResource(R.drawable.sell_kh)
//                    Glide.with(itemView.getContext()).load(R.drawable.sell).thumbnail(0.1f).into(typeView);
                    post_type.setText(R.string.sell)
                    post_type.setBackgroundResource(R.drawable.roundimage)
                } else if (item.postType.equals("rent")) {
//                    post_type.setImageResource(R.drawable.buy_kh)
//                    Glide.with(itemView.getContext()).load(R.drawable.sell).thumbnail(0.1f).into(typeView);
                    post_type.setText(R.string.ren)
                    post_type.setBackgroundResource(R.drawable.roundimage_rent)
                }
//                } else
//                    post_type.setImageResource(R.drawable.rent_kh)
            }

            itemView.findViewById<LinearLayout>(R.id.linearLayout).setOnClickListener {
                val intent = Intent(itemView.context, Detail_new_post_java::class.java)
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