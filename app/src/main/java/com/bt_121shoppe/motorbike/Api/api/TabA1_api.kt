package com.bt_121shoppe.motorbike.Api.api

class TabA1_api(var id: Int, var img_user: String, var image: String, var title: String, var cost: Double,
                var condition: String, var postType: String?, var location_duration:String, var count_view:String,
                var discount_type:String?, var discount:Double?,var status_id: String){
//    companion object {
//        fun getItem():ArrayList<Item_API> {
//            val itemApi = ArrayList<Item_API>()
//
//            val url = "http://103.205.26.103:8000/allposts/"
//            val client = OkHttpClient()
//            val request = Request.Builder()
//                    .url(url)
//                    .header("Accept", "application/json")
//                    .header("Content-Type", "application/json")
//                    .build()
//
//            client.newCall(request).enqueue(object : Callback {
//                override fun onFailure(call: Call, e: IOException) {}
//
//                @Throws(IOException::class)
//                override fun onResponse(call: Call, response: Response) {
//
//                    val respon = response.body()!!.string()
//                    Log.d("Response", respon)
//                    try {
//                        val jsonObject = JSONObject(respon)
//                        val jsonArray = jsonObject.getJSONArray("results")
//
//                        for (i in 0 until jsonArray.length()) {
//                            val `object` = jsonArray.getJSONObject(i)
//
//                            val title = `object`.getString("title")
//                            val id = `object`.getInt("id")
//                            val condition = `object`.getString("condition")
//                            val imag = `object`.getString("base64_front_image")
//                            val imguser = `object`.getString("base64_right_image")
//
//                            itemApi.add(Item_API(id,imguser,imag,title,condition))
//                            Log.d("Count I ",i.toString())
//
//                        }
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                    }
//
//                }
//            })
//            Log.d("Hello",itemApi.size.toString())
//            return itemApi
//        }
//    }

}

