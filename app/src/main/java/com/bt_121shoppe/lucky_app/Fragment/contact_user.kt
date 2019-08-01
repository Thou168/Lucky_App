package com.bt_121shoppe.lucky_app.Fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.bt_121shoppe.lucky_app.Api.ConsumeAPI
import com.bt_121shoppe.lucky_app.Api.User
import com.bt_121shoppe.lucky_app.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_detail_discount.*
import java.io.IOException

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [contact_user]interface.
 */
class contact_user: Fragment(){
    private lateinit var mMap: GoogleMap
    private val REQUEST_LOCATION = 1
    internal lateinit var locationManager: LocationManager
    internal var latitude: Double = 0.toDouble()
    internal var longtitude:Double = 0.toDouble()

    var user_id:Int=0
    var username:String=""
    var password:String=""
    var encode=""
    var phone: TextView? = null
    var email: TextView? = null
    var address:   TextView? = null
    internal lateinit var mapFragment: SupportMapFragment

    @SuppressLint("WrongConstant")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.contact_user, container, false)

        val tvphone = activity!!.intent.getStringExtra("Name")
        phone = view.findViewById<TextView>(R.id.phone)
        phone!!.text = tvphone
        email = view.findViewById(R.id.email)
        if(activity!!.intent.getStringExtra("Email") == null){
            email!!.text = "Null"
        }else
        email!!.text = activity!!.intent.getStringExtra("Email")

        address = view.findViewById(R.id.address_contact)
        val addr = activity!!.intent.getStringExtra("map")
        Log.d("AAAAAA",addr)
        Toast.makeText(activity,addr,Toast.LENGTH_LONG).show()
        if(addr.isEmpty()){
            Toast.makeText(context, "Unble to Trace your location", Toast.LENGTH_SHORT).show()
        }else{
            val splitAddr = addr.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            latitude = java.lang.Double.valueOf(splitAddr[0])
            longtitude = java.lang.Double.valueOf(splitAddr[1])
            get_location(latitude,longtitude)

//             mapFragment = (activity!!.supportFragmentManager.findFragmentById(R.id.map_contact) as SupportMapFragment?)!!
      //     mapFragment?.getMapAsync(OnMapReadyCallback { this@contact_user.onMapReady(it) })
      //      mapFragment?.getMapAsync (this)


            mapFragment = activity!!.getSupportFragmentManager().findFragmentById(R.id.map_contact) as SupportMapFragment
            mapFragment.getMapAsync(OnMapReadyCallback(){
                if (it != null) {
                    mMap = it
                    Toast.makeText(activity,"whaaaaa"+addr,Toast.LENGTH_LONG).show()
                }
                Toast.makeText(activity,"whatthe fuck"+addr,Toast.LENGTH_LONG).show()
                val current_location = LatLng(latitude, longtitude)
                mMap.animateCamera(CameraUpdateFactory.zoomIn())
                mMap.animateCamera(CameraUpdateFactory.zoomTo(5f), 2000, null)
                val cameraPosition = CameraPosition.Builder()
                        .target(current_location)
                        .zoom(18f)
                        .bearing(90f)
                        .tilt(30f)
                        .build()
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                mMap.addMarker(MarkerOptions().position(LatLng(latitude, longtitude)))
            })



        }

        return view
    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val mapFragment = activity!!.supportFragmentManager.findFragmentById(R.id.map_contact) as SupportMapFragment?
//        Log.d("Testing","afasddfddfddfdfsdfsdfdfddsfdfdfsdfdfsdfddfd"+latitude+","+longtitude)
//        if(mapFragment!=null){
//            mapFragment.onCreate(null)
//            mapFragment.onResume()
//            mapFragment.getMapAsync(this)
//        }
//    }

    private fun get_location(latitude:Double, longtitude:Double) {

        locationManager = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation(latitude, longtitude)
        }
    }

    private fun buildAlertMessageNoGps() {
        val builder = context?.let { AlertDialog.Builder(it) }
        builder!!.setMessage("Please Turn On your PGS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, which -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
                .setNegativeButton("No") { dialog, which -> dialog.cancel() }
        val alert = builder.create()
        alert.show()
    }

    private fun getLocation(latitude: Double,longtitude: Double) {
        if (context?.let { ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) } != PackageManager.PERMISSION_GRANTED && context?.let { ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_COARSE_LOCATION) } != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION)
        } else {
            val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            if (location != null) {

                try {
                    val geocoder = Geocoder(context)
                    var addressList: List<Address>? = null
                    addressList = geocoder.getFromLocation(latitude, longtitude, 1)

                    val road = addressList!![0].getAddressLine(0)

                    address!!.text = road
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            } else {
                Toast.makeText(context, "Unble to Trace your location", Toast.LENGTH_SHORT).show()
            }
        }
    }

//     override fun onMapReady(googleMap: GoogleMap?) {
//
//        if (googleMap != null) {
//            mMap = googleMap
//        }
//        val current_location = LatLng(latitude, longtitude)
//        mMap.animateCamera(CameraUpdateFactory.zoomIn())
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(5f), 2000, null)
//        val cameraPosition = CameraPosition.Builder()
//                .target(current_location)
//                .zoom(18f)
//                .bearing(90f)
//                .tilt(30f)
//                .build()
//        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
//        mMap.addMarker(MarkerOptions().position(LatLng(latitude, longtitude)))
//
//    }

}
