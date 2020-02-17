package com.bt_121shoppe.motorbike.firebases;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class FBPostCommonFunction {

//    public static void SubmitPost(String id,String title,String type,String coverUrl,String price,String discountAmount,String discountType,String location,String createdAt,int status,int createdby,String subTitle,String postCode,double eta1,double eta2, double eta3,double eta4,double machine1,double machine2,double machine3,double machine4,double other1){
    public static void SubmitPost(String id,String title,String type,String coverUrl,String price,String discountAmount,String discountType,String location,String createdAt,int status,int createdby,String subTitle,String postCode,String postColor){

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
        hashMap.put("color",postColor);

//        hashMap.put("used_eta1",eta1);
//        hashMap.put("used_eta2",eta2);
//        hashMap.put("used_eta3",eta3);
//        hashMap.put("used_eta4",eta4);
//        hashMap.put("used_machine1",machine1);
//        hashMap.put("used_machine2",machine2);
//        hashMap.put("used_machine3",machine3);
//        hashMap.put("used_machine4",machine4);
//        hashMap.put("used_other1",other1);
        reference.child(ConsumeAPI.FB_POST).child(id).setValue(hashMap);
    }

//    public static void modifiedPost(String id,String title,String coverUrl,String price,String discountAmount,String discountType,String location,String createdAt,String subTitle,double eta1,double eta2, double eta3,double eta4,double machine1,double machine2,double machine3,double machine4,double other1){
    public static void modifiedPost(String id,String title,String coverUrl,String price,String discountAmount,String discountType,String location,String createdAt,String subTitle,String eta1,String eta2, String eta3,String eta4,String machine1,String machine2,String machine3,String machine4,String other1,String postColor){

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
        hashMap.put("color",postColor);
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
        Log.e("FB","Post Delete in firebase id "+id);
//        DatabaseReference reference=FirebaseDatabase.getInstance().getReference(ConsumeAPI.FB_POST).child(id);
//        HashMap<String,Object> hashMap=new HashMap<>();
//        hashMap.put("status",2);
//        //reference.updateChildren(hashMap);
//        reference.setValue(hashMap);
        updatePostStatus(id,2);
    }
    public static void soldPost(String id){
        Log.e("FB","Post Sold in firebase id "+id);
//        DatabaseReference reference=FirebaseDatabase.getInstance().getReference(ConsumeAPI.FB_POST).child(id);
//        HashMap<String,Object> hashMap=new HashMap<>();
//        //hashMap.put("status",7);
//        reference.updateChildren(hashMap);
        updatePostStatus(id,7);
    }

    public static void submitNofitication(String to, String data){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("datetime","");
        hashMap.put("isSeen",false);
        hashMap.put("notifyType","Register");
        hashMap.put("data",data);
        hashMap.put("to",to);
        reference.child(ConsumeAPI.FB_Notification).push().setValue(hashMap);
    }
    public static void updatePostStatus(String id,int status){
        Log.e("FB","Post Sold in firebase id "+id);
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child(ConsumeAPI.FB_POST);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    try{

                        JSONObject obj=new JSONObject((Map)snapshot.getValue());
                        String fPostId=obj.getString("id");
                        if(fPostId.equals(id)){
                            String type=obj.getString("type");
                            String discountAmount=obj.getString("discountAmount");
                            String createdAt = obj.getString("createdAt");
                            int user_id = obj.getInt("createdBy");
                            String coverUrl = obj.getString("coverUrl");
                            String price = obj.getString("price");
                            String discountType = obj.getString("discountType");
                            int viewCount = obj.getInt("viewCount");
                            String title = obj.getString("subTitle");
                            String fcolor=obj.getString("color");
                            boolean isProduction=obj.getBoolean("isProduction");
                            String location=obj.getString("location");
                            String postCode=obj.getString("postCode");
                            String pCategory=obj.getString("title");

                            DatabaseReference reference1=FirebaseDatabase.getInstance().getReference(ConsumeAPI.FB_POST).child(id);
                            HashMap<String,Object> hashMap=new HashMap<>();
                            hashMap.put("isProduction", isProduction);
                            hashMap.put("id",id);
                            hashMap.put("title",pCategory);
                            hashMap.put("type",type);
                            hashMap.put("coverUrl",coverUrl);
                            hashMap.put("price",price);
                            hashMap.put("discountAmount",discountAmount);
                            hashMap.put("discountType",discountType);
                            hashMap.put("location",location);
                            hashMap.put("createdAt",createdAt);
                            hashMap.put("viewCount",viewCount);
                            hashMap.put("status",status);
                            hashMap.put("createdBy",user_id);
                            hashMap.put("subTitle",title);
                            hashMap.put("postCode",postCode);
                            hashMap.put("color",fcolor);
                            hashMap.put("status",status);
                            reference1.updateChildren(hashMap);

                            //Log.e("Firebase","Post ID "+fPostId+" "+id);
                        }

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase ","run faile");
            }
        });
    }

}
