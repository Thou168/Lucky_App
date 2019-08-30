package com.bt_121shoppe.motorbike.Startup

import com.bt_121shoppe.motorbike.R

class Item(var id: Int,var img_user: Int,var image: Int,var price: Double,var name: String,var category: String,var post_type: String,var type: String?,var title: String?){

    companion object {
        fun getList(): ArrayList<Item> {
            val itemList = ArrayList<Item>()
            itemList.clear()
            itemList.add(Item(1,R.drawable.thean,R.drawable.image_suzuki,1300.0, "Thean","Motobike","Sell","Discount","sell motor suzuki 2018"))
            itemList.add(Item(2,R.drawable.thou,R.drawable.honda_dream, 2000.0,"Thou","Motobike","Rent","Discount","Honda Dream 2019"))
            itemList.add(Item(3,R.drawable.samang,R.drawable.image_honda_dream, 1500.0,"Samang","Motobike","But",null,"Honda Dream 2007 good"))
            itemList.add(Item(1,R.drawable.thean,R.drawable.honda_dream, 2000.0,"Thean","Motobike","Sell","Discount","Dream c125 sell 1200"))
            itemList.add(Item(3,R.drawable.samang,R.drawable.image_honda_dream, 2100.0,"Samang","Motobike","Rent",null,"Sell 2100"))
            itemList.add(Item(2,R.drawable.thou,R.drawable.honda_dream, 1200.0,"Thou","Motobike","Sell","Discount","sell motor dream"))
            itemList.add(Item(1,R.drawable.thean,R.drawable.image_suzuki, 3000.0,"Thean","Motobike","Buy",null,"suzuki 2019 sell 1500"))
            itemList.add(Item(2,R.drawable.thou,R.drawable.honda_dream,5000.0, "Thou","Motobike","Buy",null,"Honda Dream"))
            itemList.add(Item(3,R.drawable.samang,R.drawable.image_honda_dream, 1200.0,"Samang","Motobike","Sell","Discount","sell 2000"))
            itemList.add(Item(3,R.drawable.thean,R.drawable.image_suzuki, 2300.0,"Samang","Motobike","Rent",null,"i want to sell suzuki"))
            itemList.add(Item(1,R.drawable.thean,R.drawable.camera, 3000.0,"Thean","Electronic","Buy",null,"camera 2019 buy 1500"))
            itemList.add(Item(2,R.drawable.thou,R.drawable.fan,5000.0, "Thou","Electronic","Buy",null,"fan new 100%"))
            itemList.add(Item(3,R.drawable.samang,R.drawable.fan1, 1200.0,"Samang","Electronic","Sell",null,"fan sell 2000"))
            itemList.add(Item(2,R.drawable.thou,R.drawable.camera1, 2300.0,"Thou","Electronic","Rent",null,"i want to rent camera"))

            return itemList
        }
        fun getType(type: String): ArrayList<Item>{
            val post_type = ArrayList<Item>()
            post_type.clear()
            for (item in getList()){
                if (item.type.equals(type)){
                    post_type.add(item)
                }
            }
            return post_type
        }
        fun getPost_Type(post_type: String,category: String): ArrayList<Item>{
            val items = ArrayList<Item>()
            items.clear()
            for (item in getList()){
                if (item.post_type.equals(post_type)&&item.category.equals(category)){
                    items.add(item)
                }
            }
            return items
        }
        fun getUser_Post(id: Int):ArrayList<Item>{
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