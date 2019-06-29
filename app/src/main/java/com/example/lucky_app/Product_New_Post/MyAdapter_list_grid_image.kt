package com.example.lucky_app.Product_New_Post

import android.content.Intent
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


class MyAdapter_list_grid_image(private val itemList: ArrayList<Item>, val type: String?) : RecyclerView.Adapter<MyAdapter_list_grid_image.ViewHolder>() {

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

        val imageView = itemView.findViewById<ImageView>(R.id.image)
        val title = itemView.findViewById<TextView>(R.id.title)

        fun bindItems(item: Item) {
            imageView.setImageResource(item.image)
            title.setText(item.title)
            itemView.findViewById<LinearLayout>(R.id.linearLayout).setOnClickListener {
                val intent = Intent(itemView.context, Detail_New_Post::class.java)
                intent.putExtra("Image","https://i.imgur.com/Ggj883L.png")
                intent.putExtra("Image_user",item.img_user)
                intent.putExtra("Title",item.title)
                intent.putExtra("Price",item.price.toString())
                intent.putExtra("Name",item.name)
                intent.putExtra("ID",item.id)
                itemView.context.startActivity(intent)
            }
           // Glide.with(itemView.context).load(version.url).into(imageView)
        }
    }
}