package com.example.lucky_app.Product_dicount

import android.content.Intent
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
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
import com.example.lucky_app.useraccount.User_post
import de.hdodenhof.circleimageview.CircleImageView

class MyAdapter(private val itemList: ArrayList<Item>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

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
        val price = itemView.findViewById<TextView>(R.id.tv_price)

        fun bindItems(item: Item) {
            val st = "$1200"
            val ms = SpannableString(st)
            val mst = StrikethroughSpan()
            ms.setSpan(mst,0,st.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            discount.text = ms

            textView.text = item.name
            imageView.setImageResource(item.image)
            img_user.setImageResource(item.img_user)
            title.setText(item.title)
            price.text = item.price.toString()


            itemView.findViewById<LinearLayout>(R.id.user).setOnClickListener {
                val intent = Intent(imageView.context, User_post::class.java)
                intent.putExtra("Image_user",item.img_user)
                intent.putExtra("ID",item.id)
                imageView.context.startActivity(intent)
            }
            itemView.findViewById<LinearLayout>(R.id.linearLayout).setOnClickListener {
                Toast.makeText(itemView.context,"Click item "+item.id,Toast.LENGTH_SHORT).show()
                val intent = Intent(itemView.context,Detail_Discount::class.java)
                intent.putExtra("Image","https://i.imgur.com/Ggj883L.png")
                intent.putExtra("Image_user",item.img_user)
                intent.putExtra("Title",item.title)
                intent.putExtra("Price",item.price.toString())
                intent.putExtra("Discount",st)
                intent.putExtra("Name",item.name)
                intent.putExtra("ID",item.id)
                itemView.context.startActivity(intent)
            }
           // Glide.with(itemView.context).load(version.url).into(imageView)
        }
    }
}