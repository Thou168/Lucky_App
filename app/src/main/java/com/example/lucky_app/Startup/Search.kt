package com.example.lucky_app.Startup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lucky_app.R
import kotlinx.android.synthetic.main.activity_search.*
import java.util.ArrayList

class Search : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
//    private var dogsList: MutableList<Item>? = null
    private var mAdapter: MyAdapter_search? = null
    private var searchView: SearchView? = null

//    val lv: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        back.setOnClickListener { finish() }


    searchView = findViewById(R.id.mSearch)
    val items = ArrayList<Item>()
        items.addAll(Item.getList())
    recyclerView = findViewById(R.id.myRecycler)
    recyclerView!!.layoutManager = GridLayoutManager(this@Search,1)
    mAdapter = MyAdapter_search( items, null)
    recyclerView!!.adapter = mAdapter


//        val items = ArrayList<Item>()
//        items.addAll(Item.getList())
//        val lv = findViewById<RecyclerView>(R.id.myRecycler)
//        lv.layoutManager = GridLayoutManager(this@Search,1)
//        lv.adapter = MyAdapter_search(items,null)
//
//        val sv = findViewById<SearchView>(R.id.mSearch)
//        sv.isFocusable = true
//        sv.isIconified = false
//        sv.requestFocusFromTouch()
//
        searchView!!.setOnQueryTextListener(object :  SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                mAdapter!!.filter.filter(newText)
                Toast.makeText(this@Search,"hello",Toast.LENGTH_SHORT).show()
                return false
            }
            override fun onQueryTextSubmit(query: String): Boolean {
                // task HERE
                //on submit send entire query
                Toast.makeText(this@Search,"he74567llo",Toast.LENGTH_SHORT).show()
                mAdapter!!.filter.filter(query)
                return false
            }
        })

    }
}
