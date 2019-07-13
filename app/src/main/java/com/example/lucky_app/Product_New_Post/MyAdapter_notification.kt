package com.example.lucky_app.Product_New_Post

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.lucky_app.R
import com.example.lucky_app.Startup.Item
import org.w3c.dom.Text
import java.io.ByteArrayOutputStream

class MyAdapter_notification(private val itemList: ArrayList<Item>, val type: String?) : RecyclerView.Adapter<MyAdapter_notification.ViewHolder>() {

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
        var id:Int=0
        fun bindItems(item: Item) {
            imageView.setImageResource(item.image)

//            val options = BitmapFactory.Options()
//            options.inSampleSize = 8
//            val decodedString = Base64.decode(item.img_user, Base64.DEFAULT)
//            val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
//            imageView.setImageBitmap(decodedByte)
////            Log.d("String = ",)
//            title.text = item.title
//            cost.text = item.cost.toString()

//            if (item.postType.equals("sell")){
//                post_type.setImageResource(R.drawable.sell)
//            }else if (item.postType.equals("buy")){
//                post_type.setImageResource(R.drawable.buy)
//            }else
//                post_type.setImageResource(R.drawable.rent)

//            itemView.findViewById<LinearLayout>(R.id.linearLayout).setOnClickListener {
//                val intent = Intent(itemView.context, Detail_New_Post::class.java)
////                intent.putExtra("Image",decodedByte)
////                intent.putExtra("Image_user",decodedByte)
////                intent.putExtra("Title",item.title)
//                  intent.putExtra("Price",item.cost)
//////                intent.putExtra("Name",item.name)
//                intent.putExtra("ID",item.id)
//                itemView.context.startActivity(intent)
//            }
           // Glide.with(itemView.context).load(version.url).into(imageView)
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