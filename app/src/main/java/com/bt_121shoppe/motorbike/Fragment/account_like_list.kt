package com.bt_121shoppe.motorbike.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.bt_121shoppe.motorbike.R
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bt_121shoppe.motorbike.Startup.Item
import com.bt_121shoppe.motorbike.Startup.MyAdapter_list

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [account_like_list]interface.
 */
class account_like_list: Fragment() {

    @SuppressLint("WrongConstant")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.user_post_list, container, false)

        val recyclrview = view.findViewById<RecyclerView>(R.id.recyclerView)

        val item = ArrayList<Item>()
        item.addAll(Item.getList())

        recyclrview.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        recyclrview.adapter = MyAdapter_list(item,null)

        return view
    }
}
