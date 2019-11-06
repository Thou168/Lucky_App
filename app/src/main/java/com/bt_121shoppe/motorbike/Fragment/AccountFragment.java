package com.bt_121shoppe.motorbike.Fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bt_121shoppe.motorbike.Activity.Account;
import com.bt_121shoppe.motorbike.Api.api.Active_user;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.model.UserResponseModel;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.models.User;
import com.bt_121shoppe.motorbike.utils.CommonFunction;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {
    private final static String TAG= AccountFragment.class.getSimpleName();
    SharedPreferences preferences;
    Context context;
    private FirebaseUser fuser;
    private DatabaseReference reference;
    Bundle bundle;

    private String username="",password="",encodeAuth="",fullname="";
    private int pk=0;
    private TextView tvUsername;
    private ImageView imgCover,imgProfile;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_account, container, false);

        imgCover = view.findViewById(R.id.imgCover);
        imgProfile=view.findViewById(R.id.imgProfile);
        tvUsername=view.findViewById(R.id.tvUsername);

        bundle=getArguments();




        context=container.getContext();
        preferences=container.getContext().getSharedPreferences("Register", Context.MODE_PRIVATE);
        username = preferences.getString("name","");
        password = preferences.getString("pass","");
        encodeAuth = "Basic "+ CommonFunction.getEncodedString(username,password);
        //Log.d("EncodeAuth",encodeAuth);
        if (preferences.contains("token")){
            pk = preferences.getInt("Pk",0);
        }else if (preferences.contains("id")){
            pk = preferences.getInt("id",0);
        }
        //check active and deactive account by samang 2/09/19
        Active_user activeUser = new Active_user();
        String active;
        active = activeUser.isUserActive(pk,context);
        if (active.equals("false")){
            activeUser.clear_session(context);
        }
        //profile and cover
        getUserProfile();
        //storageReference= FirebaseStorage.getInstance().getReference("uploads");
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("users").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);

                if(user.getImageURL().equals("default")){
                    Glide.with(context).load(R.drawable.square_logo).thumbnail(0.1f).into(imgProfile);
                }else{
                    Glide.with(context).load(user.getImageURL()).placeholder(R.drawable.square_logo).thumbnail(0.1f).into(imgProfile);
                }

                if(user.getCoverURL().equals("default")){
                    Glide.with(context).load(R.drawable.logo_121).thumbnail(0.1f).into(imgCover);
                }else{
                    Glide.with(context).load(user.getCoverURL()).placeholder(R.drawable.square_logo).thumbnail(0.1f).into(imgCover);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
    }

    private void getUserProfile(){
        //get username
        Service apiService= Client.getClient().create(Service.class);
        Call<UserResponseModel> call=apiService.getUserProfile(pk);
        call.enqueue(new Callback<UserResponseModel>() {
            @Override
            public void onResponse(Call<UserResponseModel> call, Response<UserResponseModel> response) {
                if(response.isSuccessful()){
                    if(response.body().getFirst_name()==null || response.body().getFirst_name().isEmpty()){
                        tvUsername.setText(response.body().getUsername());
                    }else{
                        tvUsername.setText(response.body().getFirst_name());
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResponseModel> call, Throwable t) {

            }
        });
    }
}
