package com.bt_121shoppe.motorbike.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bt_121shoppe.motorbike.R;

public class DetailNotification extends AppCompatActivity {

    String userId,title,message,date;
    TextView notiTitle,notiMessage,back,datetime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_notification);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        title = intent.getStringExtra("title");
        message = intent.getStringExtra("message");
        date = intent.getStringExtra("datetime");

        back = findViewById(R.id.tv_back);
        notiTitle = findViewById(R.id.title);
        notiMessage = findViewById(R.id.message);
        datetime = findViewById(R.id.date);
        notiTitle.setText(title);
        notiMessage.setText(message);
        if (date == null || date.isEmpty()){
            datetime.setText("NOV 27,2019");
        }else datetime.setText(date);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
