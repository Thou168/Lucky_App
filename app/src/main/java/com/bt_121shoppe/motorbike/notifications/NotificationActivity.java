package com.bt_121shoppe.motorbike.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bt_121shoppe.motorbike.R;

public class NotificationActivity extends AppCompatActivity {

//    RecyclerView recyclerView;
//    ArrayList<Item>items;
//    MyAdapter_notification ap;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_notification);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel("MyNotifications","MyNotifications",
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
//        recyclerView = (RecyclerView)findViewById(R.id.layout_notification);
//
//        items.addAll(Item.Companion.getList());
//        Log.d("Item: ", String.valueOf(items.size()));
//        ap = new MyAdapter_notification(items,"List");
//        recyclerView.setAdapter(ap);


    }
}
