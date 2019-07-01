package com.example.lucky_app.Product_dicount

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.lucky_app.R
import com.google.android.material.tabs.TabLayout
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_user_post.*

class User_post : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_post)

        val img_user = findViewById<CircleImageView>(R.id.img_user)
        img_user.setImageResource(intent.getIntExtra("Image_user",R.drawable.thou))

        findViewById<TextView>(R.id.tv_back).setOnClickListener { finish() }

        configureTabLayout()
    }
    private fun configureTabLayout() {
        tab.addTab(tab.newTab().setText("Post"))
        tab.addTab(tab.newTab().setText("Contact"))

        val adapter = Tab_Adapter(supportFragmentManager, tab.tabCount)
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
    }
}
