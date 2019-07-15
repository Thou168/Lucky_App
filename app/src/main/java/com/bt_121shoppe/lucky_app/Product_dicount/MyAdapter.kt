package com.bt_121shoppe.lucky_app.Product_dicount

import android.content.Intent
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

        fun bindItems(item: Item_discount) {

            val dis = item.dis
            val cost1 = item.cost
            val price = cost1 - dis
            val st = cost1.toString()
            val ms = SpannableString(st)
            val mst = StrikethroughSpan()
            ms.setSpan(mst,0,st.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            discount.text = ms
//
//            textView.text = item.name
//            imageView.setImageResource(item.image)
//            img_user.setImageResource(item.img_user)
             title.text = item.title
             cost.text = price.toString()

            val decodedString = Base64.decode(item.img_user, Base64.DEFAULT)
            val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            imageView.setImageBitmap(decodedByte)

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
//                intent.putExtra("Title",item.title)
                intent.putExtra("Price",item.cost)
////                intent.putExtra("Name",item.name)
                intent.putExtra("ID",item.id)
                itemView.context.startActivity(intent)
            }
           // Glide.with(itemView.context).load(version.url).into(imageView)
        }
    }
}