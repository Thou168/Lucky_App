package com.example.lucky_app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TableLayout
import android.widget.Toast
import com.example.lucky_app.R
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.lucky_app.Product_dicount.Tab_Adapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_user_post.*
import kotlinx.android.synthetic.main.fragment_acount.*
import kotlinx.android.synthetic.main.fragment_acount.pager
import kotlinx.android.synthetic.main.fragment_acount.tab

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [Fragment_account]interface.
 */
class Fragment_account : Fragment() {

    companion object {
        fun newInstance(): Fragment_account {
            var fragmentHome = Fragment_account()
            var args = Bundle()
            fragmentHome.arguments = args
            return fragmentHome
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_acount, container, false)


        val setting = view.findViewById<ImageButton>(R.id.btn_setting)
        setting.setOnClickListener {
            Toast.makeText(context,"Setting",Toast.LENGTH_SHORT).show()
        }

        val account_edit = view.findViewById<ImageButton>(R.id.btn_edit)
        account_edit.setOnClickListener {
            Toast.makeText(context,"Home Edit",Toast.LENGTH_SHORT).show()
        }
        val tab = view.findViewById<TabLayout>(R.id.tab)
        val pager = view.findViewById<ViewPager>(R.id.pager)

        tab.addTab(tab.newTab().setText("Post"))
        tab.addTab(tab.newTab().setText("Like"))
        val adapter = Tab_Adapter_Acc(childFragmentManager,tab.tabCount)
        pager.adapter = adapter
        pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tab))
        tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                pager.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {
            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        return view
    }

}
