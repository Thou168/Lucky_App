package com.example.lucky_app.Startup

import com.example.lucky_app.R

class Item(var id: Int,var img_user: Int,var image: Int,var price: Double,var name: String,var type: String?,var title: String?){

    companion object {
        fun getList(): ArrayList<Item> {
            val itemList = ArrayList<Item>()
            itemList.clear()
            itemList.add(Item(1,R.drawable.thean,R.drawable.image_suzuki,1300.0, "Thean","Discount","sell motor suzuki 2018"))
            itemList.add(Item(2,R.drawable.thou,R.drawable.honda_dream, 2000.0,"Thou","Discount","Honda Dream 2019"))
            itemList.add(Item(3,R.drawable.samang,R.drawable.image_honda_dream, 1500.0,"Samang",null,"Honda Dream 2007 good"))
            itemList.add(Item(1,R.drawable.thean,R.drawable.honda_dream, 2000.0,"Thean","Discount","Dream c125 sell 1200"))
            itemList.add(Item(3,R.drawable.samang,R.drawable.image_honda_dream, 2100.0,"Samang",null,"Sell 2100"))
            itemList.add(Item(2,R.drawable.thou,R.drawable.honda_dream, 1200.0,"Thou","Discount","sell motor dream"))
            itemList.add(Item(1,R.drawable.thean,R.drawable.image_suzuki, 3000.0,"Thean",null,"suzuki 2019 sell 1500"))
            itemList.add(Item(2,R.drawable.thou,R.drawable.honda_dream,5000.0, "Thou",null,"Honda Dream"))
            itemList.add(Item(3,R.drawable.samang,R.drawable.image_honda_dream, 1200.0,"Samang","Discount","sell 2000"))
            itemList.add(Item(3,R.drawable.thean,R.drawable.image_suzuki, 2300.0,"Samang",null,"i want to sell suzuki"))
            return itemList
        }
        fun getDiscount(): ArrayList<Item>{
            val discount = ArrayList<Item>()
            discount.clear()
            for (item in getList()){
                if (item.type.equals("Discount")){
                    discount.add(item)
                }
            }
            return discount
        }
        fun getPost(id: Int):ArrayList<Item>{
            val post = ArrayList<Item>()
            for (item in getList()){
                if (item.id == id){
                    post.add(item)
                }
            }
            return post
        }
    }
}