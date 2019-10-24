package com.bt_121shoppe.motorbike.Product_New_Post

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bt_121shoppe.motorbike.Activity.Item_API
import com.bt_121shoppe.motorbike.R
import com.bumptech.glide.Glide
import java.io.ByteArrayOutputStream

class MyAdapter_post_history(private val itemList: ArrayList<Item_API>, val type: String?) : RecyclerView.Adapter<MyAdapter_post_history.ViewHolder>() {


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
        val post_type = itemView.findViewById<ImageView>(R.id.post_type)
        val imageView = itemView.findViewById<ImageView>(R.id.image)
        val title = itemView.findViewById<TextView>(R.id.title)
        val cost = itemView.findViewById<TextView>(R.id.tv_price)
        val location_duration=itemView.findViewById<TextView>(R.id.location)
        val show_view=itemView.findViewById<TextView>(R.id.user_view)
        val unlike = itemView.findViewById<ImageButton>(R.id.imgbtn_unlike)
        val tv_user_view = itemView.findViewById<TextView>(R.id.user_view1)

        fun bindItems(item: Item_API) {
            Glide.with(itemView.context).load(item.image).placeholder(R.drawable.no_image_available).thumbnail(0.1f).into(imageView)
            title.text = item.title
            cost.text = "$"+item.cost.toString()
            location_duration.text=item.location_duration
            show_view.text=""+item.count_view
            var lang:String = tv_user_view.text as String
            if (lang == "View:") {
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
                intent.putExtra("Price",item.cost)
                intent.putExtra("ID",item.id)
                itemView.context.startActivity(intent)
            }
            unlike.visibility = View.GONE
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