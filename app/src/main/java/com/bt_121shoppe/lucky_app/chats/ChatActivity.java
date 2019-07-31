package com.bt_121shoppe.lucky_app.chats;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bt_121shoppe.lucky_app.R;
import com.bumptech.glide.Glide;

public class ChatActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tvusername,tvactive,tvposttitle,tvpostprice;
    private ImageView imageView;
    private RelativeLayout rlUsername,rlPost;
    private Bundle bundle;

    private int postId,userPk;
    private String postUsername,postTitle,postPrice,postImage,postUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        TextView btnBack=(TextView) findViewById(R.id.tvBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar=(Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        rlUsername=(RelativeLayout) findViewById(R.id.rlusername);
        rlPost=(RelativeLayout) findViewById(R.id.rlPost);
        tvusername=(TextView) findViewById(R.id.toolbar_user_title);
        tvactive=(TextView) findViewById(R.id.tv_active_duration);
        tvposttitle=(TextView) findViewById(R.id.tv_post_title);
        tvpostprice=(TextView) findViewById(R.id.tv_price);
        imageView=(ImageView) findViewById(R.id.image_view);

        bundle=getIntent().getExtras();
        if(bundle!=null){
            postId=bundle.getInt("postId",0);
            postTitle=bundle.getString("postTitle");
            postPrice=bundle.getString("postPrice");
            postImage=bundle.getString("postImage");
            postUsername=bundle.getString("postUsername");
            postUserId=bundle.getString("postUserId");
            userPk=bundle.getInt("postUserPk");

            tvusername.setText(postUsername);
            tvposttitle.setText(postTitle);
            tvpostprice.setText("$ "+postPrice);

            if(postImage.isEmpty()){
                Glide.with(this).load("https://www.straitstimes.com/sites/default/files/styles/article_pictrure_780x520_/public/articles/2018/10/22/ST_20181022_NANVEL_4360142.jpg?itok=ZB5zgW7e&timestamp=1540134011").into(imageView);
            }
            else{
                byte[] decodedString1 = Base64.decode(postImage, Base64.DEFAULT);
                Bitmap bitmapImage = BitmapFactory.decodeByteArray(decodedString1, 0, decodedString1.length);
                imageView.setImageBitmap(bitmapImage);
            }
        }

        rlUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        rlPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.chat_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menuAbout:
                Toast.makeText(this, "You clicked about", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menuSettings:
                Toast.makeText(this, "You clicked settings", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menuLogout:
                Toast.makeText(this, "You clicked logout", Toast.LENGTH_SHORT).show();
                break;

        }
        return true;
    }
}
