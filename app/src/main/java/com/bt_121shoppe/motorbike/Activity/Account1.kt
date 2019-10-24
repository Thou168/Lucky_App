package com.bt_121shoppe.motorbike.Activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bt_121shoppe.motorbike.AccountTab.MainLoanList
import com.bt_121shoppe.motorbike.AccountTab.MainPostList
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bt_121shoppe.motorbike.Api.ConsumeAPI
import com.bt_121shoppe.motorbike.Api.User
import com.bt_121shoppe.motorbike.useraccount.EditAccountActivity
import com.bt_121shoppe.motorbike.Login_Register.UserAccountActivity
import com.bt_121shoppe.motorbike.R
import com.bt_121shoppe.motorbike.Setting.Setting
import com.bt_121shoppe.motorbike.adapters.ViewPagerAdapter
import com.bt_121shoppe.motorbike.chats.ChatMainActivity
import com.bt_121shoppe.motorbike.fragments.FragmentB1
import com.bt_121shoppe.motorbike.models.PostViewModel
import com.bt_121shoppe.motorbike.utils.FileCompressor
import com.bt_121shoppe.motorbike.utils.ImageUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.fragment_acount.imgCover
import kotlinx.android.synthetic.main.fragment_acount.imgProfile
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class Account1 : AppCompatActivity(){//}, Sheetviewupload.BottomSheetListener {

    private val REQUEST_TAKE_PHOTO=1
    private val REQUEST_GALLARY_PHOTO=2
    private val GALLERY = 1
    private val CAMERA = 2
    private var type: String? = null
    private var uploadcover: Button? = null
    private var upload: ImageView? = null
    private var uploadprofile: ImageView? = null
    private var tvUsername: TextView?= null
    private var PRIVATE_MODE = 0
    var username=""
    var password=""
    var encodeAuth=""
    var API_ENDPOINT=""
    var pk=0
    var prefs: SharedPreferences?=null
    private var user:User?=null
    var mPhotoFile: File?=null
    var mCompressor: FileCompressor?=null
    var bitmapImage:Bitmap?=null

    internal lateinit var mainPager: ViewPager
    internal lateinit var tabs: TabLayout
    internal lateinit var tabLayout: TabLayout
    internal lateinit var viewPager: ViewPager
    internal lateinit var viewPagerAdapter: ViewPagerAdapter

    private lateinit var storageReference: StorageReference
    private val IMAGE_REQUEST=1
    private lateinit var imageUri:Uri
    //private lateinit var uploadTask
    internal var fuser: FirebaseUser? = null
    internal lateinit var reference: DatabaseReference
    lateinit var mkm:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_tab_layout)

        val preferences = getSharedPreferences("RegisterActivity", Context.MODE_PRIVATE)
        //username=preferences.getString("name","")
        //password=preferences.getString("pass","")
        encodeAuth="Basic "+ getEncodedString(username,password)
        //Log.d("Hello",password)
        if (preferences.contains("token")) {
            pk = preferences.getInt("Pk", 0)
        } else if (preferences.contains("id")) {
            pk = preferences.getInt("id", 0)
        }
        //Log.d("Account", "Breand pk "+ username)
        if(pk==0){
            Log.d("Account", "Breand pk "+ pk)
            val intent= Intent(this@Account1, UserAccountActivity::class.java)
            startActivity(intent)
            finish()
        }

        val bnavigation = findViewById<BottomNavigationView>(com.bt_121shoppe.motorbike.R.id.bnaviga)
        bnavigation.menu.getItem(4).isChecked = true
        bnavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    val intent = Intent(this@Account1,Home::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                }
                R.id.notification -> {
                    if (preferences.contains("token") || preferences.contains("id")) {
                        val intent = Intent(this@Account1, NotificationActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }else{
                        val intent = Intent(this@Account1, UserAccountActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }
                }
                R.id.camera ->{
                    if (preferences.contains("token") || preferences.contains("id")) {
                        val intent = Intent(this@Account1, Camera::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }else{
                        val intent = Intent(this@Account1, UserAccountActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }
                }
                R.id.message -> {
                    if (preferences.contains("token") || preferences.contains("id")) {
                        val intent = Intent(this@Account1, ChatMainActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }else{
                        val intent = Intent(this@Account1, UserAccountActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }
                }
                R.id.account ->{
                    if (preferences.contains("token") || preferences.contains("id")) {
                        val intent = Intent(this@Account1, Account::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }else{
                        val intent = Intent(this@Account1, UserAccountActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }
                }

            }
            true
        }

        /*
        storageReference=FirebaseStorage.getInstance().getReference("uploads")
        fuser=FirebaseAuth.getInstance().currentUser
        reference=FirebaseDatabase.getInstance().getReference("users").child(fuser!!.uid)

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(com.bt_121shoppe.motorbike.models.Breand::class.java)
                //username.setText(user!!.username)
                if (user!!.imageURL == "default") {
                    //image_profile.setImageResource(R.drawable.user)
                    imgProfile!!.setImageResource(R.drawable.user)
                } else {
                    Glide.with(this@Account).load(user.imageURL).into(imgProfile)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

        */

        //press to show layout sheet_view_upload
        uploadcover = findViewById<Button>(R.id.btnUpload_Cover)
        uploadcover!!.setOnClickListener{
            type = "cover"
            selectImage()
            //val upload = Sheetviewupload()
            //upload.show(supportFragmentManager,upload.tag)
        }
        upload = findViewById<ImageView>(R.id.imgProfile)
        upload!!.setOnClickListener{
            type = "profile"
            //openImage()
            selectImage()

            //val upload = Sheetviewupload()
            //upload.show(supportFragmentManager,upload.tag)
        }

        uploadprofile =   findViewById<ImageView>(R.id.imgCover)
        uploadprofile!!.setOnClickListener{
            type = "cover"
            selectImage()
            //val upload = Sheetviewupload()
            //upload.show(supportFragmentManager,upload.tag)
        }

        mainPager = findViewById<View>(R.id.pagerMain) as ViewPager
        tabs = findViewById<View>(R.id.tabs) as TabLayout

        val setting = findViewById<ImageButton>(R.id.btn_setting)
        setting.setOnClickListener {
            val intent = Intent(this@Account1 , Setting::class.java)
            startActivity(intent)
            Toast.makeText(this@Account1,"Setting", Toast.LENGTH_SHORT).show()
        }

        val account_edit = findViewById<ImageButton>(R.id.btn_edit)
        account_edit.setOnClickListener {
            val intent = Intent(this@Account1, EditAccountActivity::class.java)
            startActivity(intent)
        }
        /*
        val tab = findViewById<TabLayout>(R.id.tab)
        val pager = findViewById<ViewPager>(R.id.pager)

        tab.addTab(tab.newTab().setText(com.example.lucky_app.R.string.post))
        tab.addTab(tab.newTab().setText(com.example.lucky_app.R.string.like))
        val adapter = Tab_Adapter_Acc(supportFragmentManager,tab.tabCount)

        pager.adapter = adapter
        pager.isNestedScrollingEnabled=false
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
        */

        /*
        viewPager = findViewById<View>(R.id.viewPager) as ViewPager
        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPager.adapter = viewPagerAdapter
        tabLayout = findViewById<View>(R.id.tabs) as TabLayout
        tabLayout.setupWithViewPager(viewPager)
           */

        setUpPager()
        tvUsername=findViewById<TextView>(R.id.tvUsername)
        getUserProfile()
        getMyPosts()
        mCompressor = FileCompressor(this)


    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK){
            if(requestCode==REQUEST_TAKE_PHOTO){
                try{
                    mPhotoFile=mCompressor!!.compressToFile(mPhotoFile)
                    bitmapImage=BitmapFactory.decodeFile(mPhotoFile!!.getPath())
                }catch (e:IOException) {
                    e.printStackTrace()
                }
                if(type.equals("profile")) {
                    var URL_ENDPOINT = ConsumeAPI.BASE_URL + "api/v1/users/" + pk + "/profilephoto/"
                    var client = OkHttpClient()
                    val profile = JSONObject()
                    val profile_body = JSONObject()
                    try {
                        profile_body.put("profile_photo", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmapImage)))
                        profile.put("profile", profile_body)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    val body = RequestBody.create(ConsumeAPI.MEDIA_TYPE, profile.toString())
                    val request = Request.Builder()
                            .url(URL_ENDPOINT)
                            .put(body)
                            .header("Accept", "application/json")
                            .header("Content-Type", "application/json")
                            .header("Authorization", encodeAuth)
                            .build()
                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            val result = e.message.toString()
                            Log.d("Profile", "Fail:$result")
                        }

                        @Throws(IOException::class)
                        override fun onResponse(call: Call, response: Response) {
                            val result = response.body()!!.string()
                            runOnUiThread {
                                Glide.with(this@Account1).load(mPhotoFile).apply(RequestOptions().placeholder(R.drawable.default_profile_pic)).into(imgProfile)
                            }
                            Log.d("Response", result)
                        }
                    })
                }else if(type.equals("cover")){
                    var URL_ENDPOINT = ConsumeAPI.BASE_URL + "api/v1/users/" + pk + "/coverphoto/"
                    var client = OkHttpClient()
                    val profile = JSONObject()
                    val profile_body = JSONObject()
                    try {
                        profile_body.put("cover_photo", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmapImage)))
                        profile.put("profile", profile_body)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    val body = RequestBody.create(ConsumeAPI.MEDIA_TYPE, profile.toString())
                    val request = Request.Builder()
                            .url(URL_ENDPOINT)
                            .put(body)
                            .header("Accept", "application/json")
                            .header("Content-Type", "application/json")
                            .header("Authorization", encodeAuth)
                            .build()
                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            val result = e.message.toString()
                            Log.d("Profile", "Fail:$result")
                        }

                        @Throws(IOException::class)
                        override fun onResponse(call: Call, response: Response) {
                            val result = response.body()!!.string()
                            runOnUiThread {
                                Glide.with(this@Account1).load(mPhotoFile).apply(RequestOptions().placeholder(R.drawable.default_profile_pic)).into(imgCover)
                            }
                            Log.d("Response", result)
                        }
                    })
                }

            }
            else if(requestCode==REQUEST_GALLARY_PHOTO){
                val selectedImage = data!!.getData()
                try {
                    mPhotoFile = mCompressor!!.compressToFile(File(getRealPathFromUri(selectedImage)))
                    bitmapImage=BitmapFactory.decodeFile(mPhotoFile!!.getPath())
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                if(type.equals("profile")) {
                    var URL_ENDPOINT = ConsumeAPI.BASE_URL + "api/v1/users/" + pk + "/profilephoto/"
                    var client = OkHttpClient()
                    val profile = JSONObject()
                    val profile_body = JSONObject()
                    try {
                        profile_body.put("profile_photo", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmapImage)))
                        profile.put("profile", profile_body)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    val body = RequestBody.create(ConsumeAPI.MEDIA_TYPE, profile.toString())
                    val request = Request.Builder()
                            .url(URL_ENDPOINT)
                            .put(body)
                            .header("Accept", "application/json")
                            .header("Content-Type", "application/json")
                            .header("Authorization", encodeAuth)
                            .build()
                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            val result = e.message.toString()
                            Log.d("Profile", "Fail:$result")
                        }

                        @Throws(IOException::class)
                        override fun onResponse(call: Call, response: Response) {
                            val result = response.body()!!.string()
                            runOnUiThread {
                                Glide.with(this@Account1).load(mPhotoFile).apply(RequestOptions().centerCrop().centerCrop().placeholder(R.drawable.default_profile_pic)).into(imgProfile)
                            }
                            Log.d("Response", result)
                        }
                    })
                }else if(type.equals("cover")){
                    var URL_ENDPOINT = ConsumeAPI.BASE_URL + "api/v1/users/" + pk + "/coverphoto/"
                    var client = OkHttpClient()
                    val profile = JSONObject()
                    val profile_body = JSONObject()
                    try {
                        profile_body.put("cover_photo", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmapImage)))
                        profile.put("profile", profile_body)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    val body = RequestBody.create(ConsumeAPI.MEDIA_TYPE, profile.toString())
                    val request = Request.Builder()
                            .url(URL_ENDPOINT)
                            .put(body)
                            .header("Accept", "application/json")
                            .header("Content-Type", "application/json")
                            .header("Authorization", encodeAuth)
                            .build()
                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            val result = e.message.toString()
                            Log.d("Profile", "Fail:$result")
                        }

                        @Throws(IOException::class)
                        override fun onResponse(call: Call, response: Response) {
                            val result = response.body()!!.string()
                            runOnUiThread {
                                Glide.with(this@Account1).load(mPhotoFile).apply(RequestOptions().centerCrop().centerCrop().placeholder(R.drawable.default_profile_pic)).into(imgCover)
                            }
                            Log.d("Response", result)
                        }
                    })
                }
                //Glide.with(this@Account).load(mPhotoFile).apply(RequestOptions().centerCrop().centerCrop().placeholder(R.drawable.default_profile_pic)).into(imgProfile)
            }

        }

        /*
        if(requestCode == CAMERA && resultCode == Activity.RESULT_OK)
        {
            val image = data!!.extras!!.get("data") as Bitmap
            if(type.equals("cover"))
            {
                imgCover!!.setImageBitmap(image)
            }
            else
            {
                imgProfile!!.setImageBitmap(image)
            }
        }
        else if(requestCode == GALLERY && resultCode == Activity.RESULT_OK)
        {
            if(data != null)
            {
                val content = data.data
                try{
                    val upload = MediaStore.Images.Media.getBitmap(this.contentResolver,content)
                    if(type.equals("cover"))
                    {
                        imgCover!!.setImageBitmap(upload)
                    }
                    else
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
        */
    }

    /*
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data

            if (uploadTask != null && uploadTask.isInProgress()) {
                Toast.makeText(this@Account, "Upload in progress.", Toast.LENGTH_LONG).show()
            } else {
                uploadImage()
            }
        }
    }
    */

    fun getEncodedString(username: String,password:String):String{
        val userpass = "$username:$password"
        return Base64.encodeToString(userpass.toByteArray(),
                Base64.NO_WRAP)
    }

    fun getUserProfile(){
        var user1 = User()
        //Log.d("Thou",pk.toString())
        //Log.d("Hello",encodeAuth)
        var URL_ENDPOINT=ConsumeAPI.BASE_URL+"api/v1/users/"+pk
        var MEDIA_TYPE=MediaType.parse("application/json")
        var client= OkHttpClient()
        var request=Request.Builder()
                .url(URL_ENDPOINT)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .header("Authorization",encodeAuth)
                .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                val mMessage = e.message.toString()
                Log.w("failure Response", mMessage)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val mMessage = response.body()!!.string()

                val gson = Gson()
                try {
                    Log.d("Breand",mMessage)
                    user1= gson.fromJson(mMessage, User::class.java)
                    runOnUiThread {
                        if(user1.profile!=null) {
                            val profilepicture: String = if (user1.profile.profile_photo == null) "" else user1.profile.base64_profile_image
                            val coverpicture: String = if (user1.profile.cover_photo == null) "" else user1.profile.base64_cover_photo_image
                            if(user1.getFirst_name().isEmpty())
                            {
                                tvUsername!!.setText(user1.getUsername())
                            }else{
                                tvUsername!!.setText(user1.getFirst_name())
                            }

                            //Glide.with(this@Account).load(profilepicture).apply(RequestOptions().centerCrop().centerCrop().placeholder(R.drawable.default_profile_pic)).into(imgProfile)
                            //Glide.with(this@Account).load(profilepicture).forImagePreview().into(imgCover)

                            if (profilepicture.isNullOrEmpty()) {
                                imgProfile!!.setImageResource(R.drawable.user)
                                //Log.d("Profile", "TTTTTTTTTTTTTTTTTTTTTTTTTTTTTT")
                            } else {
                                val decodedString = Base64.decode(profilepicture, Base64.DEFAULT)
                                var decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                                imgProfile!!.setImageBitmap(decodedByte)
                            }

                            if (coverpicture == null) {
                                //9Glide.with(this@Account).load("https://utmsi.utexas.edu/components/com_easyblog/themes/wireframe/images/placeholder-image.png").into(imgProfile)
                            } else {
                                val decodedString = Base64.decode(coverpicture, Base64.DEFAULT)
                                var decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                                imgCover!!.setImageBitmap(decodedByte)
                            }

                        }
                    }

                } catch (e: JsonParseException) {
                    e.printStackTrace()
                }

            }
        })
    }

    fun getMyPosts(){
        var posts=PostViewModel()
        val URL_ENDPOINT=ConsumeAPI.BASE_URL+"postbyuser/"
        var MEDIA_TYPE=MediaType.parse("application/json")
        val client= OkHttpClient()
        val request=Request.Builder()
                .url(URL_ENDPOINT)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .header("Authorization",encodeAuth)
                .build()

        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                val mMessage = e.message.toString()
                Log.w("failure Response", mMessage)
            }
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val mMessage = response.body()!!.string()
                Log.w(" Response123", mMessage)
                val gson = Gson()
                val jsonObject=JSONObject(mMessage)
                try {

//                    val detail:String=jsonObject.getString("detail").toString()
//                    if(detail.isNullOrEmpty()) {
                        val jsonArray = jsonObject.getJSONArray("results")
                        val jsonCount = jsonObject.getInt("count")
                        runOnUiThread {
                            if (jsonCount > 0) {
                                for (i in 0 until jsonArray.length()) {
                                    val obj = jsonArray.getJSONObject(i)
                                    Log.e("TAG", "T" + obj)
                                }

                            }

                        }
//                    }
                } catch (e: JsonParseException) {
                    e.printStackTrace()
                }

            }
        })
    }

    private fun selectImage() {
        val items = arrayOf<CharSequence>("Take Photo", "Choose from Library", "Cancel")
        val builder = androidx.appcompat.app.AlertDialog.Builder(this@Account1)
        builder.setItems(items) {
            dialog, item ->
            if (items[item] == "Take Photo") {
                requestStoragePermission(true)
            } else if (items[item] == "Choose from Library") {
                requestStoragePermission(false)
            } else if (items[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun requestStoragePermission(isCamera: Boolean) {
        Dexter.withActivity(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            if (isCamera) {
                                dispatchTakePictureIntent()
                            } else {
                                dispatchGalleryIntent()
                            }
                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                }).withErrorListener { error -> Toast.makeText(applicationContext, "Error occurred! ", Toast.LENGTH_SHORT).show() }
                .onSameThread()
                .check()
    }
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
                ex.printStackTrace()
                // Error occurred while creating the File
            }

            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(this,
                        this.packageName + ".provider",
                        photoFile)
                //BuildConfig.APPLICATION_ID
                mPhotoFile = photoFile
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                //startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
            }
        }
    }
    private fun dispatchGalleryIntent() {
        val pickPhoto = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(pickPhoto, REQUEST_GALLARY_PHOTO)
    }

    // navigating user to app settings
    private fun openSettings() {
        //Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        val intent = Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }
    private fun showSettingsDialog() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.permission))
        builder.setMessage(getString(R.string.setting_permission))
        builder.setPositiveButton(getString(R.string.go_setting)) { dialog, which ->
            dialog.cancel()
            openSettings()
        }
        builder.setNegativeButton(getString(R.string.cancel)) { dialog, which -> dialog.cancel() }
        builder.show()

    }
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        val mFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(mFileName, ".jpg", storageDir)
    }
    fun getRealPathFromUri(contentUri: Uri): String {
        var cursor: Cursor? = null
        try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = contentResolver.query(contentUri, proj, null, null, null)
            assert(cursor != null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(column_index)
        } finally {
            cursor?.close()
        }
    }

    private fun setUpPager() {
        val posts = MainPostList.newInstance()
        val likes = FragmentB1()
        val loans = MainLoanList.newInstance()
        val adapter = MainPager(supportFragmentManager)

        adapter.addFragment(posts, getString(R.string.tab_post))
        adapter.addFragment(likes, getString(R.string.tab_like))
        adapter.addFragment(loans, getString(R.string.tab_loan))

        mainPager.setAdapter(adapter)
        tabs.setupWithViewPager(mainPager)

        tabs.getTabAt(intent.getIntExtra("Tab",0))!!.select()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        
    }

    private inner class MainPager(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        internal var mFragments: MutableList<Fragment> = ArrayList()
        internal var mFragmentsTitle: MutableList<String> = ArrayList()

        fun addFragment(f: Fragment, s: String) {
            mFragments.add(f)
            mFragmentsTitle.add(s)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            Log.d("mylog", mFragmentsTitle[position])
            return mFragmentsTitle[position]
        }

        override fun getItem(position: Int): Fragment {
            return mFragments[position]
        }

        override fun getCount(): Int {
            return mFragments.size
        }
    }


    private fun openImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, IMAGE_REQUEST)
    }

    private fun getFileExtenstion(uri: Uri): String? {
        val contentResolver = this@Account1!!.getContentResolver()
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }

    /*
    private fun uploadImage() {
        val pd = ProgressDialog(this@Account)
        pd.setMessage("Uploading")
        pd.show()

        if (imageUri != null) {
            val fileReference = storageReference.child(System.currentTimeMillis().toString()
                    + "." + getFileExtenstion(imageUri))

            uploadTask = fileReference.putFile(imageUri)

            uploadTask = uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation fileReference.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    val mUri = downloadUri!!.toString()

                    reference = FirebaseDatabase.getInstance().getReference("users").child(fuser!!.uid)
                    val map = HashMap<String, Any>()
                    map["imageURL"] = mUri
                    reference.updateChildren(map)
                    pd.dismiss()
                } else {
                    // Handle failures
                    // ...
                }
            }
            /*
            uploadTask.continueWithTask(Continuation { task ->
                if (!task.isSuccessful) {
                    throw task.getException()!!
                }
                fileReference.downloadUrl
            }).addOnCompleteListener(object : OnCompleteListener<Uri> {
                override fun onComplete(task: Task<Uri>) {
                    if (task.isSuccessful) {
                        val downloadUri = task.result as Uri?
                        val mUri = downloadUri!!.toString()

                        reference = FirebaseDatabase.getInstance().getReference("users").child(fuser!!.uid)
                        val map = HashMap<String, Any>()
                        map["imageURL"] = mUri
                        reference.updateChildren(map)
                        pd.dismiss()
                    } else {
                        Toast.makeText(this@Account, "Failed", Toast.LENGTH_LONG).show()
                    }
                }

            }).addOnFailureListener { e:Exception ->
                Toast.makeText(this@Account, e.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
            */
        } else {
            Toast.makeText(this@Account, "No image selected.", Toast.LENGTH_LONG).show()
        }
    }
    */

}
