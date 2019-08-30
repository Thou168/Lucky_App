package com.bt_121shoppe.motorbike.Product_New_Post

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bt_121shoppe.motorbike.R
import java.io.ByteArrayOutputStream
import android.os.Build
import androidx.annotation.RequiresApi
//import javax.swing.text.StyleConstants.setIcon
import com.bt_121shoppe.motorbike.fragments.LoanItemAPI
import com.bumptech.glide.Glide
import kotlin.collections.ArrayList

class MyAdapter_history_loan(private val itemList: ArrayList<LoanItemAPI>, val type: String?) : RecyclerView.Adapter<MyAdapter_history_loan.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (type.equals("List")) {
            val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_history_loan, parent, false)
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
        val count_view = itemView.findViewById<TextView>(R.id.user_view)
        val time = itemView.findViewById<TextView>(R.id.location)
        val view =itemView.findViewById<TextView>(R.id.view)

        @RequiresApi(Build.VERSION_CODES.O)
        fun bindItems(item: LoanItemAPI) {
//            val decodedString = Base64.decode(item.img_user, Base64.DEFAULT)
//            val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
//            imageView.setImageBitmap(decodedByte)
            Glide.with(itemView.context).load(item.image).centerCrop().placeholder(R.drawable.no_image_available).thumbnail(0.1f).centerCrop().into(imageView)

            title.text = item.title
            cost.text = "$"+item.cost
            time.text = item.location_duration
            view.text =item.count_view
            var lang: String =count_view.text as String
            if (lang == "View:") {
                if (item.postType.equals("sell")) {
                    post_type.visibility = View.GONE
                    count_view.visibility = View.GONE
                    view.visibility = View.GONE
                } else if (item.postType.equals("buy")) {
                    post_type.visibility = View.GONE
                    count_view.visibility = View.GONE
                    view.visibility = View.GONE
                } else
                    post_type.visibility = View.GONE
                    count_view.visibility = View.GONE
                    view.visibility = View.GONE
            } else {
                if (item.postType.equals("sell")) {
                    post_type.visibility = View.GONE
                    count_view.visibility = View.GONE
                    view.visibility = View.GONE
                } else if (item.postType.equals("buy")) {
                    post_type.visibility = View.GONE
                    count_view.visibility = View.GONE
                    view.visibility = View.GONE
                } else
                    post_type.visibility = View.GONE
                    count_view.visibility = View.GONE
                    view.visibility = View.GONE
            }

//            itemView.findViewById<LinearLayout>(R.id.linearLayout).setOnClickListener {
//                val intent = Intent(itemView.context, LoanCreateActivity::class.java)
//                intent.putExtra("id",item.loanId)
//                intent.putExtra("post",item.id)
//                itemView.context.startActivity(intent)
//            }
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