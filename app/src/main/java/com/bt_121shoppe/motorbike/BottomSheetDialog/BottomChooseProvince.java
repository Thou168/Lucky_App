package com.bt_121shoppe.motorbike.BottomSheetDialog;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.Login_Register.LoginActivity;
import com.bt_121shoppe.motorbike.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BottomChooseProvince extends BottomSheetDialogFragment implements View.OnClickListener{
    public static final String TAG="ActionBottomDialog";
    private TextView btnClear;
    private ListView ls_province;
    private int[] provinceIdListItems;
    private String[] provinceListItems,provinceListItems_kh;
    SharedPreferences preferences;
    private String name,pass,Encode;
    private ItemClickListener mListener;
    private ArrayAdapter<String> adapter;

    public static BottomChooseProvince newInstance(){
        return new BottomChooseProvince();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_province, container, false);
        return view;
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnClear = view.findViewById(R.id.bt_clear);
        ls_province = view.findViewById(R.id.province);

        SharedPreferences prefer = getActivity().getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefer.getString("My_Lang", "");

        preferences = getActivity().getSharedPreferences("Register",getActivity().MODE_PRIVATE);
        name = preferences.getString("name","");
        pass = preferences.getString("pass","");
        Encode = getEncodedString(name,pass);
        final String url = String.format("%s%s", ConsumeAPI.BASE_URL,"api/v1/provinces/");
        OkHttpClient client = new OkHttpClient();
        String auth = "Basic "+ Encode;
        Request request = new Request.Builder()
                .url(url)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                //.header("Authorization",auth)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.e("TAG","Province list "+response.body().string());
                String respon = response.body().string();
                try{
                    JSONObject jsonObject = new JSONObject(respon);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    int count=0;
                    provinceListItems=new String[jsonArray.length()+1];
                    provinceIdListItems=new int[jsonArray.length()+1];
                    provinceListItems_kh=new String[jsonArray.length()+1];
                    provinceIdListItems[0]=0;
                    provinceListItems[0]="All";
                    provinceListItems_kh[0]="ទាំងអស់";
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    int id = object.getInt("id");
                                    String name = object.getString("province");
                                    String name_kh = object.getString("province_kh");
                                    //Log.e("Year",""+id+name);
                                    provinceListItems[i+1]=name;
                                    provinceListItems_kh[i+1]=name_kh;
                                    provinceIdListItems[i+1]=id;
                                }
                                if (language.equals("en")) {
                                    adapter = new ArrayAdapter<String>(getActivity(), R.layout.listitem, provinceListItems);
                                }else {
                                    adapter = new ArrayAdapter<String>(getActivity(), R.layout.listitem, provinceListItems_kh);
                                }
                                ls_province.setAdapter(adapter);
                            }catch (JSONException e){
                                e.printStackTrace();
                            }

                        }
                    });

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
        ls_province.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String s = ls_province.getItemAtPosition(i).toString();
                mListener.onClickProvinceItem(s);
                mListener.AddIDProvince(provinceIdListItems[i]);
                dismiss();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ItemClickListener) {
            mListener = (ItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement ItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override public void onClick(View view) {
        ListView tvSelected = (ListView) view;
        mListener.onClickProvinceItem(tvSelected.getSelectedItem().toString());
        dismiss();
    }

    public interface ItemClickListener {
        void onClickProvinceItem(String item);
        void AddIDProvince(int id);
    }
    private String getEncodedString(String username, String password) {
        final String userpass = username+":"+password;
        return Base64.encodeToString(userpass.getBytes(),
                Base64.NO_WRAP);
    }
}
