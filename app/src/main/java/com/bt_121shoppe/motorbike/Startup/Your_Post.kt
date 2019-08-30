package com.bt_121shoppe.motorbike.Startup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bt_121shoppe.motorbike.R

class Your_Post : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_your__post)

        val back = findViewById<TextView>(R.id.tv_back)
        back.setOnClickListener { finish() }

        val listview = findViewById<RecyclerView>(R.id.recyclerView)
        val item = ArrayList<Item>()
        item.addAll(Item.getUser_Post(2))
        //  listview.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        listview.layoutManager = GridLayoutManager(this@Your_Post,1)
        listview.adapter = MyAdapter_list(item,null)
    }
}
