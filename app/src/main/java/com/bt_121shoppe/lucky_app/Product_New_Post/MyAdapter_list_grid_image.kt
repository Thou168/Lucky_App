package com.bt_121shoppe.lucky_app.Product_New_Post

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.bt_121shoppe.lucky_app.Activity.Item_API
import com.bt_121shoppe.lucky_app.R
import java.io.ByteArrayOutputStream

class MyAdapter_list_grid_image(private val itemList: ArrayList<Item_API>, val type: String?) : RecyclerView.Adapter<MyAdapter_list_grid_image.ViewHolder>() {

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
        val location_duration=itemView.findViewById<TextView>(R.id.location)
        val show_view= itemView.findViewById<TextView>(R.id.user_view)
        val tv_discount = itemView.findViewById<TextView>(R.id.tv_discount)

//        var id:Int=0
        fun bindItems(item: Item_API) {
//            imageView.setImageResource(item.image)

            val options = BitmapFactory.Options()
            options.inSampleSize = 8
            val decodedString = Base64.decode(item.image, Base64.DEFAULT)
    /*
            val strPath = String(decodedString, StandardCharsets.UTF_8)
            Log.d("TAGGGGGGGGGGGGG",strPath)
            val decodedByte=BitmapFactory.decodeFile(strPath,options)
            */
            if (Integer.valueOf(android.os.Build.VERSION.SDK_INT) >= android.os.Build.VERSION_CODES.O_MR1) {
                //imageView.setImageBitmap(Bitmap.createScaledBitmap(decodedByte, 150, 150, false))
                val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                imageView.setImageBitmap(decodedByte)
            }else{

                val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                imageView.setImageBitmap(Bitmap.createScaledBitmap(decodedByte, 500, 500, false))
                Log.d("IMAGE SIZE", decodedByte.width.toString()+" "+decodedByte.height.toString())
            }
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP)

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
            show_view.text="View: "+item.count_view

            if (item.postType.equals("sell")){
                post_type.setImageResource(R.drawable.sell)
            }else if (item.postType.equals("buy")){
                post_type.setImageResource(R.drawable.buy)
            }else
                post_type.setImageResource(R.drawable.rent)

            itemView.findViewById<LinearLayout>(R.id.linearLayout).setOnClickListener {
                val intent = Intent(itemView.context, Detail_New_Post::class.java)
//                intent.putExtra("Image",decodedByte)
//                intent.putExtra("Image_user",decodedByte)
//                intent.putExtra("Title",item.title)
                  intent.putExtra("Discount",price)
                  intent.putExtra("Price",item.cost)
//                intent.putExtra("Name",item.name)
                    //intent.putExtra("postt",1)
                  intent.putExtra("ID",item.id)
                  Log.d("ID  :",item.id.toString())
                itemView.context.startActivity(intent)
            }
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