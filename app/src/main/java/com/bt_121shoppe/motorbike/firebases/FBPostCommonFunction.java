package com.bt_121shoppe.motorbike.firebases;

import android.os.Build;
import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.time.Instant;
import java.util.HashMap;

public class FBPostCommonFunction {

//    public static void SubmitPost(String id,String title,String type,String coverUrl,String price,String discountAmount,String discountType,String location,String createdAt,int status,int createdby,String subTitle,String postCode,double eta1,double eta2, double eta3,double eta4,double machine1,double machine2,double machine3,double machine4,double other1){
    public static void SubmitPost(String id,String title,String type,String coverUrl,String price,String discountAmount,String discountType,String location,String createdAt,int status,int createdby,String subTitle,String postCode,String eta1,String eta2, String eta3,String eta4,String machine1,String machine2,String machine3,String machine4,String other1){

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
        hashMap.put("status",status);
        hashMap.put("createdBy",createdby);
        hashMap.put("subTitle",subTitle);
        hashMap.put("postCode",postCode);

        hashMap.put("used_eta1",eta1);
        hashMap.put("used_eta2",eta2);
        hashMap.put("used_eta3",eta3);
        hashMap.put("used_eta4",eta4);
        hashMap.put("used_machine1",machine1);
        hashMap.put("used_machine2",machine2);
        hashMap.put("used_machine3",machine3);
        hashMap.put("used_machine4",machine4);
        hashMap.put("used_other1",other1);
        reference.child(ConsumeAPI.FB_POST).child(id).setValue(hashMap);
    }

//    public static void modifiedPost(String id,String title,String coverUrl,String price,String discountAmount,String discountType,String location,String createdAt,String subTitle,double eta1,double eta2, double eta3,double eta4,double machine1,double machine2,double machine3,double machine4,double other1){
    public static void modifiedPost(String id,String title,String coverUrl,String price,String discountAmount,String discountType,String location,String createdAt,String subTitle,String eta1,String eta2, String eta3,String eta4,String machine1,String machine2,String machine3,String machine4,String other1){

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
        hashMap.put("subTitle",subTitle);

        hashMap.put("used_eta1",eta1);
        hashMap.put("used_eta2",eta2);
        hashMap.put("used_eta3",eta3);
        hashMap.put("used_eta4",eta4);
        hashMap.put("used_machine1",machine1);
        hashMap.put("used_machine2",machine2);
        hashMap.put("used_machine3",machine3);
        hashMap.put("used_machine4",machine4);
        hashMap.put("used_other1",other1);
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
