package com.bt_121shoppe.motorbike.firebases;

import android.os.Build;
import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.time.Instant;
import java.util.HashMap;

public class FBPostCommonFunction {

    public static void SubmitPost(String id,String title,String title_new,String type,String coverUrl,String price,String discountAmount,String discountType,String location,String createdAt,int status,int createdby){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("isProduction", ConsumeAPI.IS_PRODUCTION);
        hashMap.put("id",id);
        hashMap.put("title",title);
        hashMap.put("new_title",title_new);
        hashMap.put("type",type);
        hashMap.put("coverUrl",coverUrl);
        hashMap.put("price",price);
        hashMap.put("discountAmount",discountAmount);
        hashMap.put("discountType",discountType);
        hashMap.put("location",location);
        hashMap.put("createdAt",createdAt);
        hashMap.put("viewCount",0);
        hashMap.put("status",status);
        hashMap.put("createdBy",createdby);
        reference.child(ConsumeAPI.FB_POST).child(id).setValue(hashMap);
    }

    public static void modifiedPost(String id,String title,String coverUrl,String price,String discountAmount,String discountType,String location,String createdAt){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference(ConsumeAPI.FB_POST).child(id);
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("title",title);
        hashMap.put("coverUrl",coverUrl);
        hashMap.put("discountAmount",discountAmount);
        hashMap.put("discountType",discountType);
        hashMap.put("location",location);
        hashMap.put("createdAt",createdAt);
        hashMap.put("price",price);
        hashMap.put("status",3);
        reference.updateChildren(hashMap);
    }

    public static void addCountView(String postId,int viewCount){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference(ConsumeAPI.FB_POST).child(postId);
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("viewCount",viewCount);
        reference.updateChildren(hashMap);
    }

    public static void renewalPost(String id){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference(ConsumeAPI.FB_POST).child(id);
        HashMap<String,Object> hashMap=new HashMap<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            hashMap.put("createdAt",Instant.now().toString());
        }
        reference.updateChildren(hashMap);
    }
    public static void deletePost(String id){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference(ConsumeAPI.FB_POST).child(id);
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("status",2);
        reference.updateChildren(hashMap);
    }
}
