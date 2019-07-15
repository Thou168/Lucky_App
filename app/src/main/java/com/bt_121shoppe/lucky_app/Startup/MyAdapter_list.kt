package com.bt_121shoppe.lucky_app.Startup

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bt_121shoppe.lucky_app.Product_dicount.Detail_Discount
import com.bt_121shoppe.lucky_app.R


class MyAdapter_list(private val itemList: ArrayList<Item>,val type: String?) : RecyclerView.Adapter<MyAdapter_list.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
            return ViewHolder(layout)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(itemList[position])

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageView = itemView.findViewById<ImageView>(R.id.image)
        val title = itemView.findViewById<TextView>(R.id.title)
        val post_type = itemView.findViewById<ImageView>(R.id.post_type)

        fun bindItems(item: Item) {
            imageView.setImageResource(item.image)
            title.setText(item.title)

            if (item.post_type.equals("Sell")){
                post_type.setImageResource(R.drawable.sell)
            }else if (item.post_type.equals("Buy")){
                post_type.setImageResource(R.drawable.buy)
            }else
                post_type.setImageResource(R.drawable.rent)

            itemView.findViewById<LinearLayout>(R.id.linearLayout).setOnClickListener {
                val intent = Intent(itemView.context,Detail_Discount::class.java)
                intent.putExtra("Image","https://i.imgur.com/Ggj883L.png")
                intent.putExtra("Image_user",item.img_user)
                intent.putExtra("Title",item.title)
                intent.putExtra("Price",item.price.toString())
                intent.putExtra("Discount","500")
                intent.putExtra("Name",item.name)
                intent.putExtra("ID",item.id)
                itemView.context.startActivity(intent)
                Toast.makeText(itemView.context," click "+item.name,Toast.LENGTH_SHORT).show()
            }
           // Glide.with(itemView.context).load(version.url).into(imageView)
        }
    }
}