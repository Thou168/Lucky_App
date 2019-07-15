package com.bt_121shoppe.lucky_app.Startup

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bt_121shoppe.lucky_app.Product_dicount.Detail_Discount
import com.bt_121shoppe.lucky_app.R


class MyAdapter_search(private var itemList: ArrayList<Item>, val type: String?) :
        RecyclerView.Adapter<MyAdapter_search.ViewHolder>(), Filterable {

    private var SearchList: ArrayList<Item>? = null

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

    init {
        this.SearchList = itemList
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
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    SearchList = itemList
                } else {
                    val filteredList = ArrayList<Item>()
                    for (row in itemList) {
                        if (row.title!!.toLowerCase().contains(charString.toLowerCase()) ) {
                            filteredList.add(row)
                        }
                    }
                    SearchList = filteredList
                }
                val filterResults = Filter.FilterResults()
                filterResults.values = SearchList
                return filterResults
            }
            override fun publishResults(charSequence: CharSequence, filterResults: Filter.FilterResults) {
                SearchList = filterResults.values as ArrayList<Item>
                notifyDataSetChanged()
            }
        }
    }
}