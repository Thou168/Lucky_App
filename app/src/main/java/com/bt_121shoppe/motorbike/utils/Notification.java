package com.bt_121shoppe.motorbike.utils;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.chats.ChatActivity;
import com.bt_121shoppe.motorbike.interfaces.APIService;
import com.bt_121shoppe.motorbike.models.NotificationDataViewModel;
import com.bt_121shoppe.motorbike.models.NotificationSenderViewModel;
import com.bt_121shoppe.motorbike.notifications.Client;
import com.bt_121shoppe.motorbike.notifications.MyResponse;
import com.bt_121shoppe.motorbike.notifications.Sender;
import com.bt_121shoppe.motorbike.notifications.Token;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Notification {

    private static final String TAG=Notification.class.getSimpleName();

    public static void sendNotification(String receiver){
        APIService apiService= Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        DatabaseReference tokens= FirebaseDatabase.getInstance().getReference("Tokens");
        Query query=tokens.orderByKey().equalTo(receiver);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e(TAG,dataSnapshot.toString());
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Token token=snapshot.getValue(Token.class);
                    NotificationDataViewModel data=new NotificationDataViewModel(R.mipmap.ic_launcher,receiver,"This is test notification.","Test Notify","Register", Instant.now().toString(),false);

                    NotificationSenderViewModel sender=new NotificationSenderViewModel(data,token.getToken());
                    apiService.sendNotification1(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    //Log.e(TAG,"NotificationActivity Response "+response);
                                    Notification.saveNotification(data);
                                    Log.e(TAG,"After send notify:"+response.body());

                                    if(response.code()==200){
                                        if(response.body().success!=1){
                                            //Toast.makeText(ChatActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private static void saveNotification(NotificationDataViewModel data){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("title",data.getTitle());
        hashMap.put("body",data.getBody());
        hashMap.put("userId",data.getUserId());
        hashMap.put("notifyType",data.getNotiType());
        hashMap.put("isSeen",data.isSeen());
        hashMap.put("datetime",data.getDatetime());
        reference.child("NotificationActivity").push().setValue(hashMap);
    }
}
