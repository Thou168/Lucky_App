package com.example.lucky_app.Startup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lucky_app.R
import java.util.ArrayList

class Search : AppCompatActivity() {

//    val lv: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val items = ArrayList<Item>()
        items.addAll(Item.getList())
        val lv = findViewById<RecyclerView>(R.id.myRecycler)
        lv.layoutManager = GridLayoutManager(this@Search,1)
        lv.adapter = MyAdapter_list(items,null)

        val sv = findViewById<SearchView>(R.id.mSearch)
        sv.isFocusable = true
        sv.isIconified = false
        sv.requestFocusFromTouch()

        sv.setOnQueryTextListener(object :  SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {

                return false
            }
            override fun onQueryTextSubmit(query: String): Boolean {
                // task HERE
                //on submit send entire query
//                lv.adapter.getFilter().filter(query)
                return false
            }
        })

    }
}
