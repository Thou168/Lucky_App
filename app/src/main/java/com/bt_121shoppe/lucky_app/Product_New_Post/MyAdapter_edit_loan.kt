package com.bt_121shoppe.lucky_app.Product_New_Post

import android.content.Context
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
//import javax.swing.text.StyleConstants.setIcon
import androidx.appcompat.app.AlertDialog
import com.bt_121shoppe.lucky_app.Activity.*
import com.bt_121shoppe.lucky_app.loan.LoanCreateActivity
import com.bt_121shoppe.lucky_app.Api.ConsumeAPI
import com.bt_121shoppe.lucky_app.fragments.LoanItemAPI
import com.bt_121shoppe.lucky_app.utils.CommonFunction
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.time.Instant
import kotlin.collections.ArrayList


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
        val btn_delete = itemView.findViewById<Button>(R.id.btndelete)
        val btn_edit = itemView.findViewById<Button>(R.id.btnedit)

        fun bindItems(item: LoanItemAPI) {
//            imageView.setImageResource(item.image)

            val decodedString = Base64.decode(item.img_user, Base64.DEFAULT)
            val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            imageView.setImageBitmap(decodedByte)
//            Log.d("String = ",)
            title.text = item.title
            cost.text = item.cost.toString()

            if (item.postType.equals("sell")){
                post_type.setImageResource(R.drawable.sell)
            }else if (item.postType.equals("buy")){
                post_type.setImageResource(R.drawable.buy)
            }else
                post_type.setImageResource(R.drawable.rent)

            itemView.findViewById<LinearLayout>(R.id.linearLayout).setOnClickListener {
                val intent = Intent(itemView.context, Detail_New_Post::class.java)
//                intent.putExtra("Image",decodedByte)
//                intent.putExtra("Image_user",decodedByte)
//                intent.putExtra("Title",item.title)
                  intent.putExtra("Price",item.cost)
//                intent.putExtra("postt",1)
////                intent.putExtra("Name",item.name)
                  intent.putExtra("ID",item.id)
                itemView.context.startActivity(intent)
            }

            btn_edit.setOnClickListener{

                val intent = Intent(itemView.context, LoanCreateActivity::class.java)
                intent.putExtra("id",item.loanId)
                intent.putExtra("post",item.id)
                itemView.context.startActivity(intent)
            }

            btn_delete.setOnClickListener {
                //Toast.makeText(it.context,"Hello"+item.title,Toast.LENGTH_SHORT).show()
                lateinit var sharedPref: SharedPreferences
                var name=""
                var pass=""
                var Encode=""
                var pk=0

                sharedPref= it.context.getSharedPreferences("Register", Context.MODE_PRIVATE)
                if (sharedPref.contains("token") || sharedPref.contains("id")){
                    name = sharedPref.getString("name", "")
                    pass = sharedPref.getString("pass", "")
                    Encode ="Basic "+ CommonFunction.getEncodedString(name,pass)
                    if (sharedPref.contains("token")) {
                        pk = sharedPref.getInt("Pk", 0)
                    } else if (sharedPref.contains("id")) {
                        pk = sharedPref.getInt("id", 0)
                    }
                }
                //Log.d("Delete Post",name+" "+pass+" "+Encode)
                //Toast.makeText(it.context,"Click Newal",Toast.LENGTH_SHORT).show()

                val items = arrayOf<CharSequence>("This product has been sold", "Suspend this ads", "Delete to post new ads", "Cancel")
                val builder = AlertDialog.Builder(it.context)
                builder.setItems(items) { dialog, ite ->
                    if (items[ite] == "Cancel") {
                        dialog.dismiss()
                    }else{
                        val reason=items[ite].toString()
                        val URL_ENDCODE=ConsumeAPI.BASE_URL+"api/v1/renewaldelete/"+item.id.toInt()+"/"
                        val media = MediaType.parse("application/json")
                        val client = OkHttpClient()
                        val data = JSONObject()
                        try{
                            //data.put("id",60)
                            data.put("status",2)
                            //ata.put("description","Test")
                            data.put("modified", Instant.now().toString())
                            data.put("modified_by", pk)
                            data.put("rejected_comments",reason)
                        }catch (e: JSONException){
                            e.printStackTrace()
                        }
                        Log.d("TAG",URL_ENDCODE+" "+data)
                        val body = RequestBody.create(media, data.toString())
                        val request = Request.Builder()
                                .url(URL_ENDCODE)
                                .put(body)
                                .header("Accept", "application/json")
                                .header("Content-Type", "application/json")
                                .header("Authorization", Encode)
                                .build()

                        client.newCall(request).enqueue(object : Callback {
                            override fun onFailure(call: Call, e: IOException) {
                                val message = e.message.toString()
                                Log.d("failure Response", message)
                            }

                            @Throws(IOException::class)
                            override fun onResponse(call: Call, response: Response) {
                                val message = response.body()!!.string()
                                Log.d("Response Renewal", message)
                                val intent = Intent(it.context, Account::class.java)
                                it.context.startActivity(intent)
                            }
                        })
                    }
                }
                builder.show()
            }

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