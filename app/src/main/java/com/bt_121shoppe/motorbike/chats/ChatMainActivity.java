package com.bt_121shoppe.motorbike.chats;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bt_121shoppe.motorbike.activities.Account;
import com.bt_121shoppe.motorbike.activities.Camera;
import com.bt_121shoppe.motorbike.activities.CheckGroup;
import com.bt_121shoppe.motorbike.activities.Dealerstore;
import com.bt_121shoppe.motorbike.activities.Home;
import com.bt_121shoppe.motorbike.activities.NotificationActivity;
import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.Api.User;
import com.bt_121shoppe.motorbike.Api.api.Active_user;
import com.bt_121shoppe.motorbike.Login_Register.UserAccountActivity;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.stores.StoreListActivity;
import com.bt_121shoppe.motorbike.utils.CommonFunction;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import com.bt_121shoppe.motorbike.Login_Register.LoginActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChatMainActivity extends AppCompatActivity {

    private final static String TAG=ChatMainActivity.class.getSimpleName();
    SharedPreferences preferences;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private FirebaseAnalytics firebaseAnalytics;
    private String uusername="";
    private String password="";
    private String encodeAuth="";
    private int pk=0;

    SharedPreferences prefer;
    private String name,pass,Encode;

    private CircleImageView profile_image;
    private TextView username;
    private  BottomNavigationView bnavigation,bnavigation1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);

        prefer = getSharedPreferences("Register",MODE_PRIVATE);
        name = prefer.getString("name","");
        pass = prefer.getString("pass","");
        Encode = CommonFunction.getEncodedString(name,pass);
        if (prefer.contains("token")) {
            pk = prefer.getInt("Pk",0);
        }else if (prefer.contains("id")) {
            pk = prefer.getInt("id", 0);
        }
//check active and deactive account by samang 2/09/19
        Active_user activeUser = new Active_user();
        String active;
        active = activeUser.isUserActive(pk,this);
        if (active.equals("false")){
            activeUser.clear_session(this);
        }
// end
        CheckGroup check = new CheckGroup();
        int g = check.getGroup(pk,this);
        if (g == 3){
            bnavigation1 = findViewById(R.id.bottom_nav);
            bnavigation1.setVisibility(View.VISIBLE);
            bnavigation1.getMenu().getItem(3).setChecked(true);
            bnavigation1.setOnNavigationItemSelectedListener(menuItem -> {
                switch (menuItem.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), Home.class));
                        break;
                    case R.id.notification:
                        if (prefer.contains("token")||prefer.contains("id")) {
                            startActivity(new Intent(getApplicationContext(), StoreListActivity.class));
                        }else {
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }
                        break;
                    case R.id.dealer:
                        if (prefer.contains("token")||prefer.contains("id")) {
                            startActivity(new Intent(getApplicationContext(), Dealerstore.class));
                        }else {
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }
                        break;
                    case R.id.message:
                        if (prefer.contains("token")||prefer.contains("id")) {
                            startActivity(new Intent(getApplicationContext(), ChatMainActivity.class));
                        }else {
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }
                        break;
                    case R.id.account :
                        if (prefer.contains("token")||prefer.contains("id")) {
                            startActivity(new Intent(getApplicationContext(), Account.class));
                        }else {
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }
                        break;
                }
                return false;
            });
        }else {
            bnavigation = findViewById(R.id.bnaviga);
            bnavigation.setVisibility(View.VISIBLE);
            bnavigation.getMenu().getItem(3).setChecked(true);
            bnavigation.setOnNavigationItemSelectedListener(menuItem -> {
                switch (menuItem.getItemId()){
                    case R.id.home:
                        Intent intent = new Intent(getApplicationContext(), Home.class);
                        startActivity(intent);
                        break;
                    case R.id.notification:
                        if (prefer.contains("token")||prefer.contains("id")) {
                            startActivity(new Intent(getApplicationContext(), StoreListActivity.class));
                        }else {
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }
                        break;
                    case R.id.camera:
                        if (prefer.contains("token")||prefer.contains("id")) {
                            startActivity(new Intent(getApplicationContext(), Camera.class));
                        }else {
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }
                        break;
                    case R.id.message:
                        if (prefer.contains("token")||prefer.contains("id")) {
                            startActivity(new Intent(getApplicationContext(), ChatMainActivity.class));
                        }else {
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }
                        break;
                    case R.id.account :
                        if (prefer.contains("token")||prefer.contains("id")) {
                            startActivity(new Intent(getApplicationContext(), Account.class));
                        }else {
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }
                        break;
                }
                return false;
            });
        }

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        profile_image=findViewById(R.id.profile_image);
        //username=findViewById(R.id.username);
        firebaseAnalytics=FirebaseAnalytics.getInstance(this);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser==null){
            Intent intent =new Intent(ChatMainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }else
            databaseReference= FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());

        preferences = getSharedPreferences("RegisterActivity",MODE_PRIVATE);
        uusername=preferences.getString("name","");
        password=preferences.getString("pass","");
        encodeAuth="Basic "+ CommonFunction.getEncodedString(uusername,password);
        if (preferences.contains("token")) {
            pk = preferences.getInt("Pk", 0);
        } else if (preferences.contains("id")) {
            pk = preferences.getInt("id", 0);
        }

        //username.setText(firebaseUser.getPhoneNumber());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                com.bt_121shoppe.motorbike.models.User user=dataSnapshot.getValue(com.bt_121shoppe.motorbike.models.User.class);
                //username.setText(user.getUsername());
                if(user.getImageURL().equals("default")){
                    profile_image.setImageResource(R.drawable.group_2293);
                }else{
                    Glide.with(ChatMainActivity.this).load(user.getImageURL()).into(profile_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //getUserProfile();
        //basicReadWrite();

        TabLayout tabLayout=findViewById(R.id.tab_layout);
        ViewPager viewPager=findViewById(R.id.view_pager);

        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(new ChatAllFragment(),getString(R.string.all_text));
//        viewPagerAdapter.addFragment(new ChatSellFragment(),getString(R.string.buy));
//        viewPagerAdapter.addFragment(new ChatSellFragment(),getString(R.string.sell));
//        viewPagerAdapter.addFragment(new ChatRentFragment(),getString(R.string.rent));

        //viewPagerAdapter.addFragment(new ChatsFragment(),"Chats");
        //viewPagerAdapter.addFragment(new UsersFragment(),"Users");
        //viewPagerAdapter.addFragment(new ProfileFragment(),"Profile");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }  // onCreate

    class ViewPagerAdapter extends FragmentPagerAdapter{

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragments=new ArrayList<>();
            this.titles=new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment,String title){
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

    private void status(String status){
        databaseReference=FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("status",status);

        databaseReference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }

    @Override
    protected void onStart() {
        super.onStart();
        CheckGroup check = new CheckGroup();
        int g = check.getGroup(pk,this);
        if (g == 3){
            bnavigation1.getMenu().getItem(3).setChecked(true);
        }else {
            bnavigation.getMenu().getItem(3).setChecked(true);
        }
    }

    private void getUserProfile() {
        final String url = String.format("%s%s%s/", ConsumeAPI.BASE_URL,"api/v1/users/",pk);
        MediaType MEDIA_TYPE=MediaType.parse("application/json");
        Log.d(TAG,"tt"+url);
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .header("Authorization",encodeAuth)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String respon = response.body().string();
                Gson gson = new Gson();
                try{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            User converJsonJava = new User();
                            converJsonJava = gson.fromJson(respon,User.class);
                            if(converJsonJava==null){
                                profile_image.setImageResource(R.drawable.group_2293);
                            }else {
                                if (converJsonJava.getProfile() == null) {
                                    profile_image.setImageResource(R.drawable.group_2293);
                                } else {
                                    //user1.profile.base64_profile_image
                                    byte[] decodedString = Base64.decode(converJsonJava.getProfile().getBase64_profile_image(), Base64.DEFAULT);
                                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                    profile_image.setImageBitmap(decodedByte);
                                }
                            }
                        }
                    });
                }catch (JsonParseException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                profile_image.setImageResource(R.drawable.group_2293);
            }
        });
    }

    public void basicReadWrite() {
        // [START write_message]
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");
        // [END write_message]

        // [START read_message]
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        // [END read_message]
    }

}
