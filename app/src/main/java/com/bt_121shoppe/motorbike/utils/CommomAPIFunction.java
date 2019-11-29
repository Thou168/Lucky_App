package com.bt_121shoppe.motorbike.utils;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.models.User;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

public class CommomAPIFunction {
    public static String TAG=CommomAPIFunction.class.getSimpleName();
    public static int getPostView(int mId){
        String result="";
        try{
            result=CommonFunction.doGetRequest(ConsumeAPI.BASE_URL+"countview/?post="+mId);
        }catch (IOException e){
            e.printStackTrace();
        }
        try{
            JSONObject object=new JSONObject(result);
            return object.getInt("count");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return 0;
    }
    public static void getUserProfileFB(Context context, de.hdodenhof.circleimageview.CircleImageView imageView, String username){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
                @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    //Log.d(TAG,"Firebase User "+snapshot);

                    User user=snapshot.getValue(User.class);
                    if(user.getUsername().equals(username)){
                        if(user.getImageURL().equals("default"))
                        {
                            Glide.with(context.getApplicationContext()).load(R.drawable.logo121final).thumbnail(0.1f).into(imageView);
                        }else{
                            Glide.with(context.getApplicationContext()).load(user.getImageURL()).placeholder(R.drawable.logo121final).thumbnail(0.1f).into(imageView);
                        }
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
