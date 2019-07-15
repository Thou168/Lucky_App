package com.bt_121shoppe.lucky_app.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bt_121shoppe.lucky_app.R
import com.bt_121shoppe.lucky_app.Startup.Item


/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [account_user_list]interface.
 */
class account_user_list: Fragment() {

    var strings = arrayOf("1", "2", "3", "4", "5", "6", "7","1", "2", "3", "4", "5", "6", "7","1", "2", "3", "4", "5", "6", "7","1", "2", "3", "4", "5", "6", "7")

    @SuppressLint("WrongConstant")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(com.bt_121shoppe.lucky_app.R.layout.user_post_list, container, false)

        val recyclrview = view.findViewById<RecyclerView>(R.id.recyclerView)

        val item = ArrayList<Item>()
        item.addAll(Item.getUser_Post(2))

        recyclrview.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        recyclrview.adapter = MyAdapter_user(item,null)
        //recyclrview.isNestedScrollingEnabled=false
        return view

    }

    inner class SimpleRVAdapter(private val dataSource: Array<String>) : RecyclerView.Adapter<SimpleViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
            val view = TextView(parent.context)
            return SimpleViewHolder(view)
        }

        override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
            holder.textView.text = dataSource[position]
        }

        override fun getItemCount(): Int {
            return dataSource.size
        }
    }
    class SimpleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView

        init {
            textView = itemView as TextView
        }
    }
}
