package com.bt_121shoppe.motorbike.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bt_121shoppe.motorbike.chats.ChatMainActivity
import com.bt_121shoppe.motorbike.Login_Register.UserAccountActivity
import com.bt_121shoppe.motorbike.R
import com.bt_121shoppe.motorbike.firebases.FBPostCommonFunction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject

class Message : AppCompatActivity() {

    private val FCM_API="https://fcm.googleapis.com/fcm/send"
    private val serverKey="key=AAAAc-OYK_o:APA91bGjnsEJ1fFNzxvfrTvyDU2J1DndOOsTnEtt8h9LlOgpYbSnoWGiWJ1O-8_b4fjEPDriIOgplV_NkT5xACGSzPcKjxjAIq2GjYdtqZwDkr2N3wOynCr9dUXQbMwQx3jCvlxw4qVG"
    private val contentType = "application/json"

    lateinit var msg:EditText
    lateinit var btn:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        val fuser=FirebaseAuth.getInstance().currentUser

        val sharedPref: SharedPreferences = getSharedPreferences("Register", Context.MODE_PRIVATE)
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/"+fuser!!.uid)

         msg=findViewById<EditText>(R.id.text_message)
         btn=findViewById<Button>(R.id.btnsend)

        btn.setOnClickListener {
            if (!TextUtils.isEmpty(msg.text)) {
                val topic = "/topics/"+fuser!!.uid //topic has to match what the receiver subscribed to

                val notification = JSONObject()
                val notifcationBody = JSONObject()

                try {
                    notifcationBody.put("title", "Register")
                    notifcationBody.put("message", msg.text)   //Enter your notification message
                    notifcationBody.put("to",fuser!!.uid)
                    notification.put("to", topic)
                    notification.put("data", notifcationBody)
                    FBPostCommonFunction.submitNofitication(fuser!!.uid,notifcationBody.toString())
                    Log.e("TAG", "try")
                } catch (e: JSONException) {
                    Log.e("TAG", "onCreate: " + e.message)
                }

                sendNotification(notification)
            }
        }

    }
    public val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(this.applicationContext)
    }

    public fun sendNotification(notification: JSONObject) {
        Log.e("TAG", "sendNotification")
        val jsonObjectRequest = object : JsonObjectRequest(FCM_API, notification,
                com.android.volley.Response.Listener<JSONObject> { response ->
                    Log.i("TAG", "onResponse: $response")
                    msg.setText("")
                },
                com.android.volley.Response.ErrorListener {
                    Toast.makeText(this, "Request error", Toast.LENGTH_LONG).show()
                    Log.i("TAG", "onErrorResponse: Didn't work")
                }) {

            override fun getHeaders(): Map<String, String> {
                val params = HashMap<String, String>()
                params["Authorization"] = serverKey
                params["Content-Type"] = contentType
                return params
            }
        }
        requestQueue.add(jsonObjectRequest)
    }
}
