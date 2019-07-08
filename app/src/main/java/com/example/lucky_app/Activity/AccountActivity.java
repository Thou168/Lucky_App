package com.example.lucky_app.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lucky_app.Edit_Account.Sheetviewupload;
import com.example.lucky_app.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AccountActivity extends AppCompatActivity implements Sheetviewupload.BottomSheetListener {

    private static final int GALARY=1;
    private static final int CAMERA=2;

    private Button btuploadcover;
    private ImageView ivcoverphoto;
    private ImageView ivprofilephoto;
    private TextView tvusername;

    private Bitmap bitmap;
    private String imageType="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        ivcoverphoto=(ImageView)findViewById(R.id.imgCover);
        ivprofilephoto=(ImageView)findViewById(R.id.imgProfile);
        btuploadcover=(Button)findViewById(R.id.btnUpload_Cover);
        tvusername=(TextView)findViewById(R.id.tvUsername);

        BottomNavigationView bnavigation=(BottomNavigationView)findViewById(R.id.bnaviga);
        bnavigation.getMenu().getItem(4).isChecked();
        bnavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id=menuItem.getItemId();

                return true;
            }
        });
    }

    @Override
    public void gallery() {
        Intent intent=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,GALARY);
    }

    @Override
    public void camera() {
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CAMERA && resultCode== Activity.RESULT_OK){
            Uri filePath=data.getData();
            try{
                bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                if(imageType.equals("profile")){
                    ivprofilephoto.setImageBitmap(bitmap);
                }
                else if(imageType.equals("cover")){
                    ivcoverphoto.setImageBitmap(bitmap);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else if(requestCode==GALARY && resultCode==Activity.RESULT_OK){
            Uri filePath=data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                if(imageType.equals("profile")){
                    ivprofilephoto.setImageBitmap(bitmap);
                }
                else if(imageType.equals("cover")){
                    ivcoverphoto.setImageBitmap(bitmap);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
