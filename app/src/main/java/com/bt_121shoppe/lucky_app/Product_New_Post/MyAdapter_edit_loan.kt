package com.bt_121shoppe.lucky_app.Product_New_Post

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bt_121shoppe.lucky_app.R
import java.io.ByteArrayOutputStream
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
//import javax.swing.text.StyleConstants.setIcon
import androidx.appcompat.app.AlertDialog
import com.bt_121shoppe.lucky_app.Activity.*
import com.bt_121shoppe.lucky_app.loan.LoanCreateActivity
import com.bt_121shoppe.lucky_app.Api.ConsumeAPI
import com.bt_121shoppe.lucky_app.fragments.LoanItemAPI
import com.bt_121shoppe.lucky_app.fragments.Fragment_history
import com.bt_121shoppe.lucky_app.utils.CommonFunction
import com.bt_121shoppe.lucky_app.utils.CommonFunction.getEncodedString
import com.google.gson.JsonParseException
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.time.Instant
import kotlin.collections.ArrayList

//class MyAdapter_edit_loan(private val itemList: ArrayList<LoanItemAPI>, val type: String?) : RecyclerView.Adapter<MyAdapter_edit_loan.ViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        if (type.equals("List")) {
//            val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_loan, parent, false)
//            Log.d("Type ",type.toString())
//            return ViewHolder(layout)
//        }else if (type.equals("Grid")){
//            Log.d("Type ",type.toString())
//            val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_grid, parent, false)
//            return ViewHolder(layout)
//        }else{
//            Log.d("Type ",type.toString())
//            val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
//            return ViewHolder(layout)
//        }
//
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bindItems(itemList[position])
//    }
//
//    override fun getItemCount(): Int {
//        return itemList.size
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        val post_type = itemView.findViewById<ImageView>(R.id.post_type)
//        val imageView = itemView.findViewById<ImageView>(R.id.image)
//        val title = itemView.findViewById<TextView>(R.id.title)
//        val cost = itemView.findViewById<TextView>(R.id.tv_price)
//        val btn_cancel = itemView.findViewById<Button>(R.id.btndelete)
//        val btn_edit = itemView.findViewById<Button>(R.id.btnedit)
//        val count_view = itemView.findViewById<TextView>(R.id.count_view)
//
//        @RequiresApi(Build.VERSION_CODES.O)
//        fun bindItems(item: LoanItemAPI) {
////            imageView.setImageResource(item.image)
//
//            val decodedString = Base64.decode(item.img_user, Base64.DEFAULT)
//            val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
//            imageView.setImageBitmap(decodedByte)
////            Log.d("String = ",)
//            title.text = item.title
//            cost.text = item.cost.toString()
////            count_view.text = "view:"+item.count_view
//
//            if (item.postType.equals("sell")){
//                post_type.setImageResource(R.drawable.sell)
//            }else if (item.postType.equals("buy")){
//                post_type.setImageResource(R.drawable.buy)
//            }else
//                post_type.setImageResource(R.drawable.rent)
//
//            itemView.findViewById<LinearLayout>(R.id.linearLayout).setOnClickListener {
////                val intent = Intent(itemView.context, Detail_New_Post::class.java)
//////                intent.putExtra("Image",decodedByte)
//////                intent.putExtra("Image_user",decodedByte)
//////                intent.putExtra("Title",item.title)
////                  intent.putExtra("Price",item.cost)
//////                intent.putExtra("postt",1)
////////                intent.putExtra("Name",item.name)
////                  intent.putExtra("ID",item.id)
////                itemView.context.startActivity(intent)
//                val intent = Intent(itemView.context, LoanCreateActivity::class.java)
//                intent.putExtra("id",item.loanId)
//                intent.putExtra("post",item.id)
//                itemView.context.startActivity(intent)
//            }
//
//            btn_edit.setOnClickListener{
//
//                val intent = Intent(itemView.context, LoanCreateActivity::class.java)
//                intent.putExtra("id",item.loanId)
//                intent.putExtra("post",item.id)
//                itemView.context.startActivity(intent)
//            }
//
//            btn_cancel.setOnClickListener {
//                Toast.makeText(it.context, " Delete " + item.title + " " + item.loanId, Toast.LENGTH_SHORT).show()
//                lateinit var sharedPref: SharedPreferences
//                var name = ""
//                var pass = ""
//                var Encode = ""
//                var pk_loan= 12  //loan_state
//                var pk_record = 2   //record
//                var pk = 0
//                var encode = ""
//
//                sharedPref = it.context.getSharedPreferences("Register", Context.MODE_PRIVATE)
//                if (sharedPref.contains("token") || sharedPref.contains("id")) {
//                    name = sharedPref.getString("name", "")
//                    pass = sharedPref.getString("pass", "")
//
//                    encode ="Basic "+ CommonFunction.getEncodedString(name,pass)
//
//                    if (sharedPref.contains("token")) {
//                        pk = sharedPref.getInt("Pk", 0)
//                    } else if (sharedPref.contains("id")) {
//                        pk = sharedPref.getInt("id", 0)
//                    }
//                }
//
//                AlertDialog.Builder(it.context)
//                        .setTitle("Cancel loan")
//                        .setMessage("Are you sure you want to delete this loan?")
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .setPositiveButton(android.R.string.yes, DialogInterface.OnClickListener { dialog, whichButton ->
//
//                            val urlAPIEndpoint = ConsumeAPI.BASE_URL + "api/v1/loan/" + item.loanId.toInt()+"/"
//                            val media = MediaType.parse("application/json")
//                            val client = OkHttpClient()
////                            val auth = "Basic $encode"
//
//                            val request1 = Request.Builder()
//                                    .url(urlAPIEndpoint)
//                                    .header("Accept", "application/json")
//                                    .header("Content-Type", "application/json")
//                                    .header("Authorization", encode)
//                                    .build()
//                            client.newCall(request1).enqueue(object : Callback {
//                                override fun onFailure(call: Call, e: IOException) {
//                                    val mmessage = e.message.toString()
//                                    Log.w("failure Response", mmessage)
//                                }
//
//                                @Throws(IOException::class)
//                                override fun onResponse(call: Call, response: Response) {
//
//                                    val mMessage = response.body()!!.string()
//
//                                    Log.w("Respone data ", mMessage)
//
//                                    try {
//
//                                        val jsonObject = JSONObject(mMessage)
//
//                                            val loan_amount = jsonObject.getDouble("loan_amount")
//                                            val loan_interest_rate = jsonObject.getDouble("loan_interest_rate")
//                                            val loan_duration = jsonObject.getDouble("loan_duration")
//                                            val average_income = jsonObject.getDouble("average_income")
//                                            val average_expense = jsonObject.getDouble("average_expense")
//                                            val created_by = jsonObject.getInt("created_by")
//                                            val post = jsonObject.getInt("post")
//                                            val loan_to = jsonObject.getInt("loan_to")
//                                            val loan_purpose = jsonObject.getString("loan_purpose")
//                                            val username = jsonObject.getString("username")
//                                            val gender = jsonObject.getString("gender")
//                                            val age = jsonObject.getInt("age")
//                                            val job = jsonObject.getString("job")
//                                            val telephone = jsonObject.getString("telephone")
//                                            val address = jsonObject.getString("address")
//                                            val state_id = jsonObject.getBoolean("state_id")
//                                            val family_book = jsonObject.getBoolean("family_book")
//                                            val staff_id = jsonObject.getBoolean("staff_id")
//                                            val house_plant = jsonObject.getBoolean("house_plant")
//                                            val mfi = null
//                                            val created = null
//                                            val modified = Instant.now().toString()
//                                            val modified_by = jsonObject.getString("modified_by")
//                                            val received_date = null
//                                            val received_by = jsonObject.getString("received_by")
//                                            val rejected_date = null
//                                            val rejected_by = jsonObject.getString("rejected_by")
//                                            val rejected_comments = jsonObject.getString("rejected_comments")
//                                            val loan_status = jsonObject.getInt("loan_status")
//                                            val record_status = jsonObject.getInt("record_status")
//
//                                            try {
//                                                jsonObject.put("record_status", 2)
//                                                Log.d("Record_status", pk_record.toString())
//
//
//                                                jsonObject.put("loan_status", pk_loan)
//
//                                                jsonObject.put("loan_amount", loan_amount)
//                                                jsonObject.put("loan_interest_rate", loan_interest_rate)
//                                                jsonObject.put("loan_duration", loan_duration)
//                                                jsonObject.put("average_income", average_income)
//                                                jsonObject.put("average_expense", average_expense)
//                                                jsonObject.put("created_by", pk)
//                                                jsonObject.put("post", post)
//                                                jsonObject.put("loan_to",loan_to)
//                                                jsonObject.put("loan_purpose",loan_purpose)
//                                                jsonObject.put("username",username)
//                                                jsonObject.put("gender",gender)
//                                                jsonObject.put("age",age)
//                                                jsonObject.put("job",job)
//                                                jsonObject.put("telephone",telephone)
//                                                jsonObject.put("address",address)
//                                                jsonObject.put("state_id",state_id)
//                                                jsonObject.put("family_book",family_book)
//                                                jsonObject.put("staff_id",staff_id)
//                                                jsonObject.put("house_plant",house_plant)
//                                                jsonObject.put("mfi",mfi)
//                                                jsonObject.put("created",created)
//                                                jsonObject.put("modified",modified)
//                                                jsonObject.put("modified_by",pk)
//                                                jsonObject.put("received_date",received_date)
//                                                jsonObject.put("received_by",pk)
//                                                jsonObject.put("rejected_date",rejected_date)
//                                                jsonObject.put("rejected_by",pk)
//                                                jsonObject.put("rejected_commends",rejected_comments)
//
//                                            } catch (e: JSONException) {
//                                                e.printStackTrace()
//                                            }
//                                            Log.d("TAG", urlAPIEndpoint + " " )
//
//                                            val body = RequestBody.create(media, jsonObject.toString())
//                                            val request = Request.Builder()
//                                                    .url(urlAPIEndpoint)
//                                                    .put(body)
//                                                    .header("Accept", "application/json")
//                                                    .header("Content-Type", "application/json")
//                                                    .header("Authorization", encode)
//                                                    .build()
//                                            client.newCall(request).enqueue(object : Callback {
//                                                override fun onFailure(call: Call, e: IOException) {
//                                                    val message = e.message.toString()
//                                                    Log.d("failure Response", message)
//                                                }
//
//                                                @Throws(IOException::class)
//                                                override fun onResponse(call: Call, response: Response) {
//                                                    val message = response.body()!!.string()
//                                                    Log.d("Response Cancel", message)
//                                                    val intent = Intent(it.context, Account::class.java)
//                                                    it.context.startActivity(intent)
//
//                                                }
//                                            })
//                                    }
//                                    catch (e: JsonParseException) {
//                                        e.printStackTrace()
//                                    }
//
//                                }
//
//                            })
//                        })
//                        .setNegativeButton(android.R.string.no, null).show()
//            }
//            fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
//                val bytes = ByteArrayOutputStream()
//                inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
//                val path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null)
//                return Uri.parse(path)
//            }
//
//            fun BitMapToString(bitmap: Bitmap): String {
//                val baos = ByteArrayOutputStream()
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
//                val arr = baos.toByteArray()
//                return Base64.encodeToString(arr, Base64.DEFAULT)
//            }
//        }
//
//        //
//        internal class LoadHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
//
//    }
//}

class MyAdapter_edit_loan(private val itemList: ArrayList<LoanItemAPI>, val type: String?) : RecyclerView.Adapter<MyAdapter_edit_loan.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (type.equals("List")) {
            val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_loan, parent, false)
            Log.d("Type ",type.toString())
            return ViewHolder(layout)
        }else if (type.equals("Grid")){
            Log.d("Type ",type.toString())
            val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_grid, parent, false)
            return ViewHolder(layout)
        }else{
            Log.d("Type ",type.toString())
            val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
            return ViewHolder(layout)
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val post_type = itemView.findViewById<ImageView>(R.id.post_type)
        val imageView = itemView.findViewById<ImageView>(R.id.image)
        val title = itemView.findViewById<TextView>(R.id.title)
        val cost = itemView.findViewById<TextView>(R.id.tv_price)
        val btn_cancel = itemView.findViewById<Button>(R.id.btndelete)
        val btn_edit = itemView.findViewById<Button>(R.id.btnedit)
        val count_view = itemView.findViewById<TextView>(R.id.user_view)

        fun bindItems(item: LoanItemAPI) {
//            imageView.setImageResource(item.image)

            val decodedString = Base64.decode(item.img_user, Base64.DEFAULT)
            val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            imageView.setImageBitmap(decodedByte)
//            Log.d("String = ",)
            title.text = item.title
            cost.text = "$"+item.cost.toString()
            count_view.text ="View:"+item.count_view

            if (item.postType.equals("sell")){
                post_type.setImageResource(R.drawable.sell)
            }else if (item.postType.equals("buy")){
                post_type.setImageResource(R.drawable.buy)
            }else
                post_type.setImageResource(R.drawable.rent)

            itemView.findViewById<LinearLayout>(R.id.linearLayout).setOnClickListener {
                //                val intent = Intent(itemView.context, Detail_New_Post::class.java)
////                intent.putExtra("Image",decodedByte)
////                intent.putExtra("Image_user",decodedByte)
////                intent.putExtra("Title",item.title)
//                  intent.putExtra("Price",item.cost)
////                intent.putExtra("postt",1)
//////                intent.putExtra("Name",item.name)
//                  intent.putExtra("ID",item.id)
//                itemView.context.startActivity(intent)
                val intent = Intent(itemView.context, LoanCreateActivity::class.java)
                intent.putExtra("id",item.loanId)
                intent.putExtra("post",item.id)
                itemView.context.startActivity(intent)
            }

            btn_edit.setOnClickListener{

                val intent = Intent(itemView.context, LoanCreateActivity::class.java)
                intent.putExtra("id",item.loanId)
                intent.putExtra("post",item.id)
                itemView.context.startActivity(intent)
            }

            btn_cancel.setOnClickListener {
                Toast.makeText(it.context, " Delete " + item.title + " " + item.loanId, Toast.LENGTH_SHORT).show()
                lateinit var sharedPref: SharedPreferences
                var name = ""
                var pass = ""
                var Encode = ""
                var pk_loan= 12  //loan_state
                var pk_record = 2   //record
                var pk = 0
                var encode = ""

                sharedPref = it.context.getSharedPreferences("Register", Context.MODE_PRIVATE)
                if (sharedPref.contains("token") || sharedPref.contains("id")) {
                    name = sharedPref.getString("name", "")
                    pass = sharedPref.getString("pass", "")

                    encode ="Basic "+ CommonFunction.getEncodedString(name,pass)

                    if (sharedPref.contains("token")) {
                        pk = sharedPref.getInt("Pk", 0)
                    } else if (sharedPref.contains("id")) {
                        pk = sharedPref.getInt("id", 0)
                    }
                }

                AlertDialog.Builder(it.context)
                        .setTitle(R.string.cancel_loan)
                        .setMessage(R.string.delete_loan)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, DialogInterface.OnClickListener { dialog, whichButton ->

                            val urlAPIEndpoint = ConsumeAPI.BASE_URL + "api/v1/loan/" + item.loanId.toInt()+"/"
                            val media = MediaType.parse("application/json")
                            val client = OkHttpClient()
//                            val auth = "Basic $encode"

                            val request1 = Request.Builder()
                                    .url(urlAPIEndpoint)
                                    .header("Accept", "application/json")
                                    .header("Content-Type", "application/json")
                                    .header("Authorization", encode)
                                    .build()
                            client.newCall(request1).enqueue(object : Callback {
                                override fun onFailure(call: Call, e: IOException) {
                                    val mmessage = e.message.toString()
                                    Log.w("failure Response", mmessage)
                                }

                                @Throws(IOException::class)
                                override fun onResponse(call: Call, response: Response) {

                                    val mMessage = response.body()!!.string()

                                    Log.w("Respone data ", mMessage)

                                    try {

                                        val jsonObject = JSONObject(mMessage)

                                        val loan_amount = jsonObject.getDouble("loan_amount")
                                        val loan_interest_rate = jsonObject.getDouble("loan_interest_rate")
                                        val loan_duration = jsonObject.getDouble("loan_duration")
                                        val average_income = jsonObject.getDouble("average_income")
                                        val average_expense = jsonObject.getDouble("average_expense")
                                        val created_by = jsonObject.getInt("created_by")
                                        val post = jsonObject.getInt("post")
                                        val loan_to = jsonObject.getInt("loan_to")
                                        val loan_purpose = jsonObject.getString("loan_purpose")
                                        val username = jsonObject.getString("username")
                                        val gender = jsonObject.getString("gender")
                                        val age = jsonObject.getInt("age")
                                        val job = jsonObject.getString("job")
                                        val telephone = jsonObject.getString("telephone")
                                        val address = jsonObject.getString("address")
                                        val state_id = jsonObject.getBoolean("state_id")
                                        val family_book = jsonObject.getBoolean("family_book")
                                        val staff_id = jsonObject.getBoolean("staff_id")
                                        val house_plant = jsonObject.getBoolean("house_plant")
                                        val mfi = null
                                        val created = null
                                        val modified = Instant.now().toString()
                                        val modified_by = jsonObject.getString("modified_by")
                                        val received_date = null
                                        val received_by = jsonObject.getString("received_by")
                                        val rejected_date = null
                                        val rejected_by = jsonObject.getString("rejected_by")
                                        val rejected_comments = jsonObject.getString("rejected_comments")
                                        val loan_status = jsonObject.getInt("loan_status")
                                        val record_status = jsonObject.getInt("record_status")


                                        try {
                                            jsonObject.put("record_status", 2)
                                            jsonObject.put("loan_status", pk_loan)

                                            jsonObject.put("loan_amount", loan_amount)
                                            jsonObject.put("loan_interest_rate", loan_interest_rate)
                                            jsonObject.put("loan_duration", loan_duration)
                                            jsonObject.put("average_income", average_income)
                                            jsonObject.put("average_expense", average_expense)
                                            jsonObject.put("created_by", pk)
                                            jsonObject.put("post", post)
                                            jsonObject.put("loan_to",loan_to)
                                            jsonObject.put("loan_purpose",loan_purpose)
                                            jsonObject.put("username",username)
                                            jsonObject.put("gender",gender)
                                            jsonObject.put("age",age)
                                            jsonObject.put("job",job)
                                            jsonObject.put("telephone",telephone)
                                            jsonObject.put("address",address)
                                            jsonObject.put("state_id",state_id)
                                            jsonObject.put("family_book",family_book)
                                            jsonObject.put("staff_id",staff_id)
                                            jsonObject.put("house_plant",house_plant)
                                            jsonObject.put("mfi",mfi)
                                            jsonObject.put("created",created)
                                            jsonObject.put("modified",modified)
                                            jsonObject.put("modified_by",pk)
                                            jsonObject.put("received_date",received_date)
                                            jsonObject.put("received_by",pk)
                                            jsonObject.put("rejected_date",rejected_date)
                                            jsonObject.put("rejected_by",pk)
                                            jsonObject.put("rejected_commends",rejected_comments)

                                        } catch (e: JSONException) {
                                            e.printStackTrace()
                                        }
                                        Log.d("TAG", urlAPIEndpoint + " " )

                                        val body = RequestBody.create(media, jsonObject.toString())
                                        val request = Request.Builder()
                                                .url(urlAPIEndpoint)
                                                .put(body)
                                                .header("Accept", "application/json")
                                                .header("Content-Type", "application/json")
                                                .header("Authorization", encode)
                                                .build()
                                        client.newCall(request).enqueue(object : Callback {
                                            override fun onFailure(call: Call, e: IOException) {
                                                val message = e.message.toString()
                                                Log.d("failure Response", message)
                                            }

                                            @Throws(IOException::class)
                                            override fun onResponse(call: Call, response: Response) {
                                                val message = response.body()!!.string()
                                                Log.d("Response Cancel", message)
                                                val intent = Intent(it.context, Account::class.java)
                                                it.context.startActivity(intent)

                                            }
                                        })
                                    }
                                    catch (e: JsonParseException) {
                                        e.printStackTrace()
                                    }

                                }

                            })
                        })
                        .setNegativeButton(android.R.string.no, null).show()
            }
            fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
                val bytes = ByteArrayOutputStream()
                inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                val path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null)
                return Uri.parse(path)
            }

            fun BitMapToString(bitmap: Bitmap): String {
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
                val arr = baos.toByteArray()
                return Base64.encodeToString(arr, Base64.DEFAULT)
            }
        }

        //
        internal class LoadHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    }
}