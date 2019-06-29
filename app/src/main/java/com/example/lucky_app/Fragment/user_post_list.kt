package com.example.lucky_app.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.lucky_app.R
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lucky_app.Startup.Item
import com.example.lucky_app.Startup.MyAdapter_list

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [user_post_list]interface.
 */
class user_post_list: Passdata() {

    @SuppressLint("WrongConstant")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.user_post_list, container, false)

        val recyclrview = view.findViewById<RecyclerView>(R.id.recyclerView)

        val id = ACTIVITY.intent.getIntExtra("ID",0)
        Log.d("ID in User_Post",id.toString())

        val item = ArrayList<Item>()
        item.clear()
        item.addAll(Item.getPost(id))

        recyclrview.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        recyclrview.adapter = MyAdapter_list(item,null)

        return view
    }
}
