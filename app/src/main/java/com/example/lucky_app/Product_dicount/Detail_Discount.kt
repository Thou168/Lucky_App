package com.example.lucky_app.Product_dicount

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.custom.sliderimage.logic.SliderImage
import com.example.lucky_app.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

import de.hdodenhof.circleimageview.CircleImageView
import java.io.IOException
import java.util.*

class Detail_Discount : AppCompatActivity(), OnMapReadyCallback{
    private val TAG = Detail_Discount::class.java!!.getSimpleName()
    private lateinit var mMap: GoogleMap

    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    private var mLocationPermissionGranted: Boolean = false
    private var mLastKnownLocation: Location? = null

    internal lateinit var txt_place: TextView
    private val REQUEST_LOCATION = 1
    internal lateinit var locationManager: LocationManager
    internal lateinit var locationListener: LocationListener


    override fun onMapReady(p0: GoogleMap) {
        mMap = p0

        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationListener = object : LocationListener {
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }

            override fun onProviderEnabled(provider: String?) {
            }

            override fun onProviderDisabled(provider: String?) {
            }

            override fun onLocationChanged(location: Location) {
                val geocoder = Geocoder(applicationContext, Locale.getDefault())
                try {
                    val listAddress = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    //txt_place.text = txt_place.text.toString() + "" + listAddress[0].getAddressLine(0)
                    txt_place.text = listAddress[0].featureName + ", " + listAddress[0].adminArea + ", " + listAddress[0].countryName
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        if (ActivityCompat.checkSelfPermission(this@Detail_Discount, Manifest.permission.ACCESS_FINE_LOCATION) !== PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this@Detail_Discount, Manifest.permission.ACCESS_COARSE_LOCATION) !== PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@Detail_Discount, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION)
        } else {

        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
        val lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        mMap.clear()
        val userLocation = LatLng(lastKnownLocation.latitude, lastKnownLocation.longitude)
        mMap.addMarker(MarkerOptions().position(userLocation).title("Do you know? I`m here"))
        p0.moveCamera(CameraUpdateFactory.newLatLng(userLocation))
        mMap.animateCamera(CameraUpdateFactory.zoomBy(12f))
        mMap.moveCamera(CameraUpdateFactory.zoomBy(15f))
        mMap.isMyLocationEnabled = true
        //disable
        mMap.uiSettings.isScrollGesturesEnabled = false
        //mMap.getUiSettings().setZoomGesturesEnabled(false);
        getLocationPermission()

        getDeviceLocation()
    }
    private fun getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                val locationResult = mFusedLocationProviderClient!!.lastLocation
                locationResult.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        mLastKnownLocation = task.result
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(mLastKnownLocation!!.latitude, mLastKnownLocation!!.longitude), 15f))
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        mMap.uiSettings.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message)
        }

    }
    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.applicationContext,
                        Manifest.permission.ACCESS_FINE_LOCATION) === PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_discount)
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION)

        txt_place = findViewById(R.id.txt_show_place) as TextView
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        findViewById<TextView>(R.id.tv_back).setOnClickListener { finish() }

        val sliderImage = findViewById<SliderImage>(R.id.slider)
        val images = listOf(
                intent.getStringExtra("Image"),
                "https://i.redd.it/obx4zydshg601.jpg")
        sliderImage.setItems(images)
        sliderImage.addTimerToSlide(3000)
        sliderImage.removeTimerSlide()
        sliderImage.getIndicator()

        val id = intent.getIntExtra("ID", 0)

        val title = findViewById<TextView>(R.id.title)
        title.text = intent.getStringExtra("Title")
        val price = findViewById<TextView>(R.id.tv_price)
        price.text = intent.getStringExtra("Price")

        val discount = findViewById<TextView>(R.id.tv_discount)
        val st = intent.getStringExtra("Discount")
        val ms = SpannableString(st)
        val mst = StrikethroughSpan()
        ms.setSpan(mst, 0, st.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        discount.text = ms

        val name = findViewById<TextView>(R.id.tv_name)
        name.text = intent.getStringExtra("Name")
        val img_user = findViewById<CircleImageView>(R.id.cr_img)
        img_user.setImageResource(intent.getIntExtra("Image_user", R.drawable.thou))

        findViewById<CircleImageView>(R.id.cr_img).setOnClickListener {
            val intent = Intent(this@Detail_Discount, User_post::class.java)
            intent.putExtra("Image_user", getIntent().getIntExtra("Image_user", R.drawable.thean))
            intent.putExtra("ID", id)
            startActivity(intent)
        }

    }
}
