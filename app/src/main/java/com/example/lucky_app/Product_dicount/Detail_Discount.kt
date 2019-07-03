package com.example.lucky_app.Product_dicount

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.location.LocationListener
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.custom.sliderimage.logic.SliderImage
import com.example.lucky_app.R
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task

import de.hdodenhof.circleimageview.CircleImageView
import java.io.IOException
import java.util.*

class Detail_Discount : AppCompatActivity(), OnMapReadyCallback{
    internal lateinit var locationManager: LocationManager
    internal lateinit var txt_place: TextView
    private val REQUEST_LOCATION = 1

    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    private var mLocationPermissionGranted: Boolean = false
    private var mLastKnownLocation: Location? = null
    internal var locationListener: LocationListener? = null
    private lateinit var mGoogleMap: GoogleMap

    override fun onMapReady(googleMap: GoogleMap) {
        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        mGoogleMap = googleMap

        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {

            }

            override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {

            }

            override fun onProviderEnabled(s: String) {

            }

            override fun onProviderDisabled(s: String) {

            }
        }
        if (ActivityCompat.checkSelfPermission(this@Detail_Discount, Manifest.permission.ACCESS_FINE_LOCATION) !== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this@Detail_Discount, Manifest.permission.ACCESS_COARSE_LOCATION) !== PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@Detail_Discount, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION)
        } else {
            val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            if (location != null) {
                val geocoder = Geocoder(applicationContext, Locale.getDefault())
                try {
                    val listAddress = geocoder.getFromLocation(location.latitude, location.longitude, 3)
                    txt_place.text = txt_place.text.toString() + "" + listAddress[0].subThoroughfare
                    txt_place.text = txt_place.text.toString() + "," + listAddress[0].adminArea
                    txt_place.text = txt_place.text.toString() + "," + listAddress[0].countryCode

                } catch (e: IOException) {
                    e.printStackTrace()
                }

            } else {
                Toast.makeText(this, "Unble to trace your location", Toast.LENGTH_SHORT).show()
            }
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
        val lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
//        mGoogleMap.clear()
        val userLocation = LatLng(lastKnownLocation.latitude, lastKnownLocation.longitude)
        mGoogleMap.addMarker(MarkerOptions().position(userLocation).title("My location"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation))
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomBy(12f))
        mGoogleMap.moveCamera(CameraUpdateFactory.zoomBy(15f))
        mGoogleMap.isMyLocationEnabled = true
        //disable
        mGoogleMap.uiSettings.isScrollGesturesEnabled = false
        //mMap.getUiSettings().setZoomGesturesEnabled(false);
        getLocationPermission()

        getDeviceLocation()
        }

    private fun getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                val locationResult = mFusedLocationProviderClient!!.getLastLocation()
                locationResult.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        mLastKnownLocation = task.result
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(mLastKnownLocation!!.getLatitude(), mLastKnownLocation!!.getLongitude()), 15f))
                    } else {
                        mGoogleMap.uiSettings.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message)
        }

    }
    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.applicationContext,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) === PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_discount)

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION)
        txt_place = findViewById(R.id.txt_show_place) as TextView

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

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
