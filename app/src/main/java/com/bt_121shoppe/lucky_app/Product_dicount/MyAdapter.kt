package com.bt_121shoppe.lucky_app.Product_dicount

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bt_121shoppe.lucky_app.Activity.Item_discount
import com.bt_121shoppe.lucky_app.Product_New_Post.Detail_New_Post
import com.bt_121shoppe.lucky_app.R
import de.hdodenhof.circleimageview.CircleImageView

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
//
//            textView.text = item.name
//            imageView.setImageResource(item.image)
//            img_user.setImageResource(item.img_user)
             title.text = item.title
             cost.text = "$"+ price.toString()
             location.text = item.time
//             count_view.text ="View:"+item.countview
            count_view.text =item.countview

            val decodedString = Base64.decode(item.image, Base64.DEFAULT)

            if (Integer.valueOf(android.os.Build.VERSION.SDK_INT) >= android.os.Build.VERSION_CODES.O_MR1) {
                //imageView.setImageBitmap(Bitmap.createScaledBitmap(decodedByte, 150, 150, false))
                val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                imageView.setImageBitmap(decodedByte)
            }else {

                val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                imageView.setImageBitmap(Bitmap.createScaledBitmap(decodedByte, 500, 500, false))
            }
            itemView.findViewById<LinearLayout>(R.id.user).setOnClickListener {
//                val intent = Intent(imageView.context,User_post::class.java)
//                intent.putExtra("Image_user",item.img_user)
//                intent.putExtra("ID",item.id)
//                imageView.context.startActivity(intent)
            }
            itemView.findViewById<LinearLayout>(R.id.linearLayout).setOnClickListener {
                val intent = Intent(itemView.context, Detail_New_Post::class.java)
//                intent.putExtra("Image",decodedByte)
//                intent.putExtra("Image_user",decodedByte)
                intent.putExtra("Discount",price)
                intent.putExtra("Price",item.cost)
////                intent.putExtra("Name",item.name)
                intent.putExtra("ID",item.id)
                itemView.context.startActivity(intent)
            }
           // Glide.with(itemView.context).load(version.url).into(imageView)
        }
    }
}