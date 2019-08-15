package com.bt_121shoppe.lucky_app.firebases;

import com.bt_121shoppe.lucky_app.Api.ConsumeAPI;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class FBPostCommonFunction {

    public static void SubmitPost(String id,String title,String type,String coverUrl,String price,String discountAmount,String discountType,String location,String createdAt){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("isProduction", ConsumeAPI.IS_PRODUCTION);
        hashMap.put("id",id);
        hashMap.put("title",title);
        hashMap.put("type",type);
        hashMap.put("coverUrl",coverUrl);
        hashMap.put("price",price);
        hashMap.put("discountAmount",discountAmount);
        hashMap.put("discountType",discountType);
        hashMap.put("location",location);
        hashMap.put("createdAt",createdAt);
        hashMap.put("viewCount",0);
        reference.child("posts").push().setValue(hashMap);
    }

}
