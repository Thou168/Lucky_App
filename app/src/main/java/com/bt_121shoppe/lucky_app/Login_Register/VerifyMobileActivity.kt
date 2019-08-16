package com.bt_121shoppe.lucky_app.Login_Register

import android.app.Notification
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bt_121shoppe.lucky_app.Activity.Home
import com.bt_121shoppe.lucky_app.Api.ConsumeAPI
import com.bt_121shoppe.lucky_app.Api.Convert_Json_Java
import com.bt_121shoppe.lucky_app.R
import com.bt_121shoppe.lucky_app.models.User
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.rengwuxian.materialedittext.MaterialEditText
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

class VerifyMobileActivity : AppCompatActivity() {

    internal lateinit var otp: MaterialEditText
    internal lateinit var login: Button
    internal lateinit var back:TextView
    internal lateinit var resend:TextView
    internal lateinit var tvphonenumber:TextView
    internal lateinit var no: String
    internal lateinit var password:String
    internal var authType:Int = 0
    private var mAuth: FirebaseAuth? = null
    private var mVerificationId: String? = null
    internal lateinit var prefer: SharedPreferences
    internal lateinit var mProgress: ProgressDialog
    internal lateinit var mResendToken:PhoneAuthProvider.ForceResendingToken
    internal lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_mobile)

        prefer = getSharedPreferences("Register", Context.MODE_PRIVATE)
        otp = findViewById<View>(R.id.otp) as MaterialEditText
        mAuth = FirebaseAuth.getInstance()
        authType=intent.getIntExtra("authType",0)
        no = intent.getStringExtra("phoneNumber")
        password=intent.getStringExtra("password")
        login = findViewById<View>(R.id.login) as Button
        back=findViewById<TextView>(R.id.tvBack_account)
        tvphonenumber=findViewById<TextView>(R.id.tv_phone_number)

        Log.d("Verify Code",no+" "+password)
        mProgress = ProgressDialog(this)
        mProgress.setMessage(getString(R.string.please_wait))
        mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        mProgress.setCancelable(false)
        mProgress.isIndeterminate = true

        reference = FirebaseDatabase.getInstance().reference

        sendVerificationCode(no)
        tvphonenumber.setText(no)
        back.setOnClickListener(View.OnClickListener { finish() })

        login.setOnClickListener(View.OnClickListener {
            val code = otp.text!!.toString().trim { it <= ' ' }
            if (code.isEmpty() || code.length < 6) {
                otp.error = "Enter valid code"
                return@OnClickListener
            }
            mProgress.show()
            verifyVerificationCode(code)
        })

        resend=findViewById<TextView>(R.id.tv_resend)
        resend.setOnClickListener(View.OnClickListener {
            resendVerificationCode("+855"+no,mResendToken)
        })
    }

    private fun sendVerificationCode(no: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+855$no",
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks
        )
    }
    private fun resendVerificationCode(phoneNumber:String, token:PhoneAuthProvider.ForceResendingToken) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token)            // ForceResendingToken from callbacks
    }

    private val mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
            val code = phoneAuthCredential.smsCode
            if (code != null) {
                otp.setText(code)
                verifyVerificationCode(code)
            }
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Toast.makeText(this@VerifyMobileActivity, e.message, Toast.LENGTH_LONG).show()
            Log.d("Verify ",e.message)
        }

        override fun onCodeSent(s: String?, forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(s, forceResendingToken)

            //storing the verification id that is sent to the user
            mVerificationId = s
            mResendToken = forceResendingToken
        }
    }

    private fun verifyVerificationCode(code: String?) {
        val credential = PhoneAuthProvider.getCredential(mVerificationId.toString(), code!!)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth!!.signInWithCredential(credential)
                .addOnCompleteListener(this@VerifyMobileActivity) { task ->
                    if (task.isSuccessful){
                        mProgress.dismiss()
                        val firebaseUser = mAuth!!.getCurrentUser()
                        val userId = firebaseUser!!.getUid()
                        if(authType==1){//register
                            val hashMap = HashMap<String, String>()
                            hashMap["id"] = userId
                            hashMap["username"] = no
                            hashMap["imageURL"] = "default"
                            hashMap["status"]="online"
                            hashMap["search"]=no.toLowerCase()
                            hashMap["coverURL"]="default"
                            hashMap["password"]=password
                            //Log.d("TAG", "" + hashMap)
                            reference.child("users").child(userId).setValue(hashMap)
                            registerRequest()
                        }else if(authType==2){ // login
                            setpassword(userId,password)
                            postLoginRequest(password)
                        }else if(authType==3){//register with facebook
                            val facebooktokenkey=intent.getStringExtra("facebooktokenkey")
                            val facebookid=intent.getStringExtra("facebookid")
                            val facebookname=intent.getStringExtra("facebookname")
                            val imageurl=intent.getStringExtra("imageurl")

                            val hashMap = HashMap<String, String>()
                            hashMap["id"] = userId
                            hashMap["username"] = no
                            hashMap["imageURL"] = imageurl
                            hashMap["status"]="online"
                            hashMap["search"]=no.toLowerCase()
                            hashMap["coverURL"]="default"
                            hashMap["password"]=password
                            //Log.d("TAG", "" + hashMap)
                            reference.child("users").child(userId).setValue(hashMap)
                            registerWithFBRequest(facebookname,facebookid)
                        }
                        else if(authType==4){ // login with fb
                            val reference = FirebaseDatabase.getInstance().getReference("users").child(userId)
                            reference.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    val user = dataSnapshot.getValue(User::class.java)
                                    val firebasePwd=user!!.password
                                    postLoginRequest(firebasePwd)
                                }

                                override fun onCancelled(databaseError: DatabaseError) {

                                }
                            })
                        }else if(authType==5){ // confirm to reset password
                            val reference = FirebaseDatabase.getInstance().getReference("users").child(userId)
                            reference.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    val user = dataSnapshot.getValue(User::class.java)
                                    val firebasePwd=user!!.password
                                    val intent = Intent(this@VerifyMobileActivity, ResetPasswordActivity::class.java)
                                    intent.putExtra("phonenumber",no)
                                    intent.putExtra("password",firebasePwd)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    startActivity(intent)
                                    finish()
                                }

                                override fun onCancelled(databaseError: DatabaseError) {

                                }
                            })
                        }
                    } else {
                        var message = "Someting is wrong, we will fix it soon....."
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            message = "Invalide code entered....."
                        }
                    }
                }
    }

    private fun registerRequest() {
        val MEDIA_TYPE = MediaType.parse("application/json")
        val url = String.format("%s%s", ConsumeAPI.BASE_URL, "api/v1/users/")
        val client = OkHttpClient()
        val postdata = JSONObject()
        val post_body = JSONObject()
        try {
            postdata.put("username", no)
            postdata.put("password", password)
            post_body.put("telephone", no)
            post_body.put("group",1)
            postdata.put("profile", post_body)
            postdata.put("groups", JSONArray("[\"1\"]"))
        } catch (e: JSONException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        val body = RequestBody.create(MEDIA_TYPE, postdata.toString())
        val request = Request.Builder()
                .url(url)
                .post(body)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build()

        client.newCall(request).enqueue(object : Callback {
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val mMessage = response.body()!!.string()
                Log.e("Register Verify", mMessage)
                convertUser(mMessage)
            }

            override fun onFailure(call: Call, e: IOException) {
                val mMessage = e.message.toString()
                Log.w("failure Response", mMessage)
                runOnUiThread { Toast.makeText(applicationContext, "failure Response:$mMessage", Toast.LENGTH_SHORT).show() }
                //call.cancel();
            }
        })
    }

    private fun registerWithFBRequest(username:String,facebookid:String){
        val MEDIA_TYPE = MediaType.parse("application/json")
        val url = String.format("%s%s", ConsumeAPI.BASE_URL, "api/v1/users/")
        val client = OkHttpClient()
        val postdata = JSONObject()
        val post_body = JSONObject()
        try {
            postdata.put("username", no)
            postdata.put("password", password)
            postdata.put("first_name",username)
            postdata.put("last_name",facebookid)
            post_body.put("telephone", no)
            post_body.put("group",1)
            postdata.put("profile", post_body)
            postdata.put("groups", JSONArray("[\"1\"]"))
        } catch (e: JSONException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        val body = RequestBody.create(MEDIA_TYPE, postdata.toString())
        val request = Request.Builder()
                .url(url)
                .post(body)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build()

        client.newCall(request).enqueue(object : Callback {
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val mMessage = response.body()!!.string()
                Log.e("Register Verify", mMessage)
                convertUser(mMessage)
            }

            override fun onFailure(call: Call, e: IOException) {
                val mMessage = e.message.toString()
                Log.w("failure Response", mMessage)
                runOnUiThread { Toast.makeText(applicationContext, "failure Response:$mMessage", Toast.LENGTH_SHORT).show() }
                //call.cancel();
            }
        })
    }

    private fun convertUser(mMessage: String) {
        val gson = Gson()
        var convertJsonJava = Convert_Json_Java()
        try {
            convertJsonJava = gson.fromJson(mMessage, Convert_Json_Java::class.java)
//            val gg = convertJsonJava.groups
//            val g = gg[0]
            val g=convertJsonJava.group
            //Log.d("Register Verify", convertJsonJava.username + "\t" + convertJsonJava.email + "\t" + convertJsonJava.id + "\t" + g + "\t" + convertJsonJava.status)
            val id = convertJsonJava.id

            runOnUiThread {
                if (id != 0) {
                    val editor = prefer.edit()
                    editor.putInt("id", id)
                    editor.putString("name", no)
                    editor.putString("pass", password)
                    editor.putString("groups", g.toString())
                    editor.commit()

                    Toast.makeText(applicationContext, "Register Success", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@VerifyMobileActivity, Home::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)

                    finish()

                } else {
                    //Toast.makeText(applicationContext, "register failure", Toast.LENGTH_SHORT).show()
                    val alertDialog = AlertDialog.Builder(this@VerifyMobileActivity).create()
                    //alertDialog.setTitle("Loan")
                    alertDialog.setMessage(convertJsonJava.username)
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok)
                    ) { dialog, which -> dialog.dismiss() }
                    alertDialog.show()
                }
            }
        } catch (e: JsonParseException) {
            e.printStackTrace()
            runOnUiThread {
                //Toast.makeText(applicationContext, "register failure", Toast.LENGTH_SHORT).show()
                val alertDialog = AlertDialog.Builder(this@VerifyMobileActivity).create()
                alertDialog.setTitle(getString(R.string.verify_code))
                alertDialog.setMessage(getString(R.string.verify_code_message))
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok)
                ) { dialog, which -> dialog.dismiss() }
                alertDialog.show()
            }

        }

    }

    private fun postLoginRequest(password: String) {
        val MEDIA_TYPE = MediaType.parse("application/json")
        val url = String.format("%s%s", ConsumeAPI.BASE_URL, "api/v1/rest-auth/login/")
        //   String url ="http://192.168.1.239:8000/rest-auth/login/";  // login
        val client = OkHttpClient()
        val postdata = JSONObject()
        try {
            postdata.put("username", no)
            postdata.put("password", password)

        } catch (e: JSONException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

        val body = RequestBody.create(MEDIA_TYPE, postdata.toString())
        val request = Request.Builder()
                .url(url)
                .post(body)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build()

        client.newCall(request).enqueue(object : Callback {

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val mMessage = response.body()!!.string()
                    //Log.e(TAG, mMessage)
                    converting(mMessage)
                } else {
                    mProgress.dismiss()
                    runOnUiThread { Toast.makeText(applicationContext, "Login failure", Toast.LENGTH_SHORT).show() }

                }
            }

            override fun onFailure(call: Call, e: IOException) {
                val mMessage = e.message.toString()
                Log.w("failure Response", mMessage)
                mProgress.dismiss()
                runOnUiThread { Toast.makeText(applicationContext, "failure Response:$mMessage", Toast.LENGTH_SHORT).show() }

            }
        })
    } // postRequest

    private fun converting(mMessage: String) {
        val gson = Gson()
        var convertJsonJava = Convert_Json_Java()
        try {
            convertJsonJava = gson.fromJson(mMessage, Convert_Json_Java::class.java)
            Log.d("Verify Account", convertJsonJava.username + "\t" + convertJsonJava.token + "\t" + convertJsonJava.status)
            val key = convertJsonJava.token
            val user = convertJsonJava.user
            val pk = user.pk

            runOnUiThread {
                if (key != null) {
                    val editor = prefer.edit()
                    editor.putString("token",key)
                    editor.putString("name", no)
                    editor.putString("pass", password)
                    editor.putInt("Pk",pk)
                    editor.commit()

                    //Toast.makeText(applicationContext, "Register Success", Toast.LENGTH_SHORT).show()

                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                    finish()
                } else {
                    mProgress.dismiss()
                    Toast.makeText(applicationContext, "Login failure", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: JsonParseException) {
            e.printStackTrace()

            runOnUiThread {
                Toast.makeText(applicationContext, "Login failure", Toast.LENGTH_SHORT).show()
                mProgress.dismiss()
            }
        }

    }

    private fun setpassword(userid:String, password: String) {
        reference = FirebaseDatabase.getInstance().getReference("users").child(userid)
        val hashMap = HashMap<String, Any>()
        hashMap["password"] = password
        hashMap["status"] = "online"
        reference.updateChildren(hashMap)
    }
}
