package com.example.lucky_app.Activity

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.example.lucky_app.Edit_Account.Edit_account
import com.example.lucky_app.Edit_Account.Sheetviewupload
import com.example.lucky_app.Fragment.Tab_Adapter_Acc
import com.example.lucky_app.R
import com.example.lucky_app.Setting.Setting
import com.example.lucky_app.useraccount.UserInformationActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_acount.*
import java.io.IOException
import java.io.ByteArrayOutputStream
import java.util.*


class Account : AppCompatActivity(), Sheetviewupload.BottomSheetListener {

    private val GALLERY = 1
    private val CAMERA = 2
    private var type: String? = null

    private var uploadcover: Button? = null
    private var upload: ImageView? = null
    private var uploadprofile: ImageView? = null

    //upload image from gallery or camera, it implement from interface BottomSheetListener
    override fun camera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

    override fun gallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == CAMERA && resultCode == Activity.RESULT_OK)
        {
            val image = data!!.extras!!.get("data") as Bitmap
            if(type.equals("cover"))
            {
                imgCover!!.setImageBitmap(image)
            }
            else if(type.equals("profile"))
            {
                imgProfile!!.setImageBitmap(image)
            }
        }
        else if(requestCode == GALLERY && resultCode == Activity.RESULT_OK)
        {
            if(data != null)
            {
                val content = data!!.data
                try{
                    val upload = MediaStore.Images.Media.getBitmap(this.contentResolver,content)
                    if(type.equals("cover"))
                    {
                        imgCover!!.setImageBitmap(upload)
                    }
                    else if(type.equals("profile"))
                    {
                        imgProfile!!.setImageBitmap(upload)
                    }
                }
                catch(e: IOException)
                {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.lucky_app.R.layout.activity_account)

        val bnavigation = findViewById<BottomNavigationView>(com.example.lucky_app.R.id.bnaviga)
        bnavigation.menu.getItem(4).isChecked = true
        bnavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    val intent = Intent(this@Account,Home::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                }
                R.id.notification -> {
                    val intent = Intent(this@Account,Notification::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
                R.id.camera ->{
                    val intent = Intent(this@Account,Camera::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
                R.id.message -> {
                    val intent = Intent(this@Account,Message::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
                R.id.account ->{
//                    val intent = Intent(this@Account,Account::class.java)
//                    startActivity(intent)
                }

            }
            true
        }


        //press to show layout sheet_view_upload
        uploadcover = findViewById<Button>(R.id.btnUpload_Cover)
        uploadcover!!.setOnClickListener{
            type = "cover"
            val upload = Sheetviewupload()
            upload.show(supportFragmentManager,upload.tag)
        }
        upload = findViewById<ImageView>(R.id.imgProfile)
        upload!!.setOnClickListener{
            type = "profile"
            val upload = Sheetviewupload()
            upload.show(supportFragmentManager,upload.tag)
        }
        uploadprofile = findViewById<ImageView>(R.id.imgCover)
        uploadprofile!!.setOnClickListener{
            type = "cover"
            val upload = Sheetviewupload()
            upload.show(supportFragmentManager,upload.tag)
        }


        val setting = findViewById<ImageButton>(R.id.btn_setting)
        setting.setOnClickListener {
            val intent = Intent(this@Account , Setting::class.java)
            startActivity(intent)
            Toast.makeText(this@Account,"Setting", Toast.LENGTH_SHORT).show()
        }

        val account_edit = findViewById<ImageButton>(R.id.btn_edit)
        account_edit.setOnClickListener {
            Toast.makeText(this@Account,"Home Edit", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@Account, Edit_account::class.java)
            startActivity(intent)
        }
        val tab = findViewById<TabLayout>(R.id.tab)
        val pager = findViewById<ViewPager>(R.id.pager)

        tab.addTab(tab.newTab().setText(com.example.lucky_app.R.string.post))
        tab.addTab(tab.newTab().setText(com.example.lucky_app.R.string.like))
        val adapter = Tab_Adapter_Acc(supportFragmentManager,tab.tabCount)
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
