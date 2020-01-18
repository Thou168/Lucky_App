//package com.bt_121shoppe.motorbike.activities;
//
//import android.app.Activity;
//import android.app.FragmentTransaction;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.res.Configuration;
//import android.content.res.Resources;
//import android.os.Build;
//import android.os.Bundle;
//import android.util.Base64;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.RadioGroup;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AlertDialog;
//import androidx.fragment.app.Fragment;
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
//
//import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
//import com.bt_121shoppe.motorbike.Language.LocaleHapler;
//import com.bt_121shoppe.motorbike.R;
//import com.bt_121shoppe.motorbike.models.ChangepassModel;
//import com.google.android.material.textfield.TextInputEditText;
//import com.google.gson.Gson;
//import com.google.gson.JsonParseException;
//
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.util.Locale;
//
//import io.paperdb.Paper;
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.MediaType;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//
//import static android.content.Context.MODE_PRIVATE;
//import static com.bt_121shoppe.motorbike.utils.CommonFunction.getEncodedString;
//
//public class SettingActivityFragment extends Fragment {
//    private ImageView img_back;
//    TextInputEditText old_pass,new_pass,renew_pass;
//    Button reset_pass;
//    SharedPreferences prefer;
//    private String name,pass,Encode;
//    RadioGroup radio_groupnotification,radio_language;
//    private int index = 1;
//    private android.app.Fragment currentFragment;
//    private SwipeRefreshLayout mSwipeRefreshLayout;
//    private View view;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        view=inflater.inflate(R.layout.activity_account_setting_2_fragment, container, false);
//
//        old_pass=view.findViewById(R.id.old_pass);
//        new_pass=view.findViewById(R.id.new_pass);
//        renew_pass=view.findViewById(R.id.renew_pass);
//        reset_pass=view.findViewById(R.id.reset_pass);
//
//        prefer = getSharedPreferences("Register",MODE_PRIVATE);
//        name = prefer.getString("name","");
//        pass = prefer.getString("pass","");
//        Encode = getEncodedString(name,pass);
//        switch_paper();
//        Notification();
//        locale();
//
//        reset_pass.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (renew_pass.getText().toString().equals(new_pass.getText().toString())){
//                    Change_password(Encode);
//                }else {
//                    Toast.makeText(getContext(),"Wrong Password",Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });
//        return view;
//    }
//
//    //for notification
//    private void Notification(){
//        radio_groupnotification.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                View radioButton = group.findViewById(checkedId);
//                index=radio_groupnotification.indexOfChild(radioButton);
//                switch (index) {
//                    case 0:
//                        index=0;
//                        Toast.makeText(getContext(),"Enable",Toast.LENGTH_SHORT).show();
//                        break;
//                    case 1:
//                        index=1;
//                        Toast.makeText(getContext(),"Disable",Toast.LENGTH_SHORT).show();
//                        break;
//                }
//            }
//        });
//    }
//
//    private void updateView(String language){
//        language=language==null?"km":language;
//
//        Context context= LocaleHapler.setLocale(this,language);
//        Resources resources=context.getResources();
//        //title menu
//        if(resources != null) {
//
//        }
//        currentFragment = this.getFragmentManager().findFragmentById(R.id.frameLayout);
//        if(currentFragment!=null) {
//            FragmentTransaction ft = getFragmentManager().beginTransaction();
//            if (Build.VERSION.SDK_INT >= 26) {
//                ft.setReorderingAllowed(false);
//            }
//            ft.detach(currentFragment).attach(currentFragment).commit();
//        }
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        currentFragment = this.getFragmentManager().findFragmentById(R.id.frameLayout);
//        Log.e(TAG,"current Fragment onStart "+currentFragment);
//    }
//
//    @Override
//    public void onBackPressed() {
//        finish();
//    }
//
//    private void language(String lang){
//        Locale locale=new Locale(lang);
//        Locale.setDefault(locale);
//        Configuration confi=new Configuration();
//        confi.locale=locale;
//        getBaseContext().getResources().updateConfiguration(confi,getBaseContext().getResources().getDisplayMetrics());
//        SharedPreferences.Editor editor=getSharedPreferences("Settings",MODE_PRIVATE).edit();
//        editor.putString("My_Lang",lang);
//        editor.apply();
//    }
//
//    private void locale(){
//        SharedPreferences prefer=getSharedPreferences("Settings", Activity.MODE_PRIVATE);
//        String language=prefer.getString("My_Lang","");
//        language(language);
//    }
//
//    //change password
//    private void Change_password(String encode) {
//        MediaType MEDIA_TYPE = MediaType.parse("application/json");
//        String url = String.format("%s%s", ConsumeAPI.BASE_URL, "api/v1/changepassword/");
//        OkHttpClient client = new OkHttpClient();
//        JSONObject post = new JSONObject();
//        try{
//            post.put("old_password",old_pass.getText().toString());
//            post.put("new_password",renew_pass.getText().toString());
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        String auth = "Basic "+ encode;
//        RequestBody body = RequestBody.create(MEDIA_TYPE, post.toString());
//        Request request = new Request.Builder()
//                .url(url)
//                .put(body)
//                .header("Accept", "application/json")
//                .header("Content-Type", "application/json")
//                .header("Authorization",auth)
//                .build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String st = response.body().string();
//                Log.d("Result",st);
//                Gson gson = new Gson();
//                ChangepassModel model = new ChangepassModel();
//                try{
//                    model=gson.fromJson(st,ChangepassModel.class);
//                    if (model!=null){
//                        int statusCode = model.getStatus();
//                        if (statusCode==201){
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    AlertDialog alertDialog = new AlertDialog.Builder(SettingActivity.this).create();
//                                    alertDialog.setTitle(getString(R.string.title_change_password));
//                                    alertDialog.setMessage(getString(R.string.success_change_password));
//                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
//                                            new DialogInterface.OnClickListener() {
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    SharedPreferences.Editor editor = prefer.edit();
//                                                    editor.putString("name",name);
//                                                    editor.putString("pass",renew_pass.getText().toString());
//                                                    editor.commit();
////                                                    startActivity(new Intent(ChangePassword.this, Home.class));
//                                                    dialog.dismiss();
//                                                    finish();
//                                                }
//                                            });
//                                    alertDialog.show();
//                                }
//                            });
//                        }else {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(getApplicationContext(),st,Toast.LENGTH_LONG).show();
//                                }
//                            });
//                        }
//
//                    }else {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                AlertDialog alertDialog = new AlertDialog.Builder(SettingActivity.this).create();
//                                alertDialog.setTitle(getString(R.string.title_change_password));
//                                alertDialog.setMessage(getString(R.string.fail_change_password));
//                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
//                                        new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                dialog.dismiss();
//                                            }
//                                        });
//                                alertDialog.show();
//                            }
//                        });
//                    }
//                }catch (JsonParseException e){
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//
//    private String getEncodedString(String username, String password) {
//        final String userpass = username+":"+password;
//        return Base64.encodeToString(userpass.getBytes(),
//                Base64.NO_WRAP);
//    }
//}
