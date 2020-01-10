package com.bt_121shoppe.motorbike.BottomSheetDialog;

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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BottomChooseModel extends BottomSheetDialogFragment implements View.OnClickListener {

    public static final String TAG = "ActionBottomDialog";
    private Button bt_ok;
    private TextView bt_clear;
    private ListView ls_model;
    private int model,id_bran;

    private int[] modelIdListItems;
    private String[] modelListItems,modelListItemkh;
    SharedPreferences prefer;
    private String name,pass,Encode;
    private ItemClickListener mListener;
    private ArrayAdapter<String> adapter;

    public static BottomChooseModel newInstance() {
        return new BottomChooseModel();
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_model, container, false);
        return view;
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bt_clear = view.findViewById(R.id.bt_clear);
        ls_model = view.findViewById(R.id.model);

        prefer = getActivity().getSharedPreferences("Register",getActivity().MODE_PRIVATE);
        name = prefer.getString("name","");
        pass = prefer.getString("pass","");
        Encode = getEncodedString(name,pass);
        final String url = String.format("%s%s", ConsumeAPI.BASE_URL,"api/v1/models/");
        OkHttpClient client = new OkHttpClient();
        String auth = "Basic "+ Encode;
        Request request = new Request.Builder()
                .url(url)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .header("Authorization",auth)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String respon = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            model=0;
                            JSONObject jsonObject = new JSONObject(respon);
                            JSONArray jsonArray = jsonObject.getJSONArray("results");
                            int count=0,ccount=0;
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                int Brand = object.getInt("brand");
                                if(Brand!=id_bran)
                                    count++;
                            }
                            modelListItems=new String[count];
                            modelIdListItems=new int[count];
                            modelListItemkh=new String[count];
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                int id = object.getInt("id");
                                int Brand = object.getInt("brand");
                                if (Brand!=id_bran) {
                                    String name = object.getString("modeling_name");
                                    String model=object.getString("modeling_name_kh");
                                    modelListItemkh[ccount]=model;
                                    modelListItems[ccount]=name;
                                    modelIdListItems[ccount]=id;
                                    ccount++;
                                }
                            }
                            adapter = new ArrayAdapter<String>(getActivity(), R.layout.listitem, modelListItems);
                            ls_model.setAdapter(adapter);
                        }catch (JSONException e){
                            e.printStackTrace();
                            Log.d("Exception",e.getMessage());
                        }
                    }
                });
            }
        });
        ls_model.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String s = ls_model.getItemAtPosition(i).toString();
                mListener.onClickItemModel(s);
                mListener.AddModelID(modelIdListItems[i]);
                dismiss();
            }
        });


        bt_clear.setOnClickListener(new View.OnClickListener() {
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
        mListener.onClickItemModel(tvSelected.getSelectedItem().toString());
        dismiss();
    }

    public interface ItemClickListener {
        void onClickItemModel(String item);
        void AddModelID(int id);
    }
    private String getEncodedString(String username, String password) {
        final String userpass = username+":"+password;
        return Base64.encodeToString(userpass.getBytes(),
                Base64.NO_WRAP);
    }

}
