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

public class BottomChooseCategory extends BottomSheetDialogFragment implements View.OnClickListener {

    public static final String TAG = "ActionBottomDialog";
    private Button bt_ok;
    private TextView bt_clear;
    private ListView ls_type;

    private int[] categoryIdListItems;
    private String[] categoryListItems,categoryListItemkh;
    SharedPreferences prefer;
    private String name,pass,Encode;
    private ItemClickListener mListener;
    private ArrayAdapter<String> adapter;


    public static BottomChooseCategory newInstance() {
        return new BottomChooseCategory();
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_category, container, false);
        return view;
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bt_clear = view.findViewById(R.id.bt_clear);
        ls_type = view.findViewById(R.id.category);

        prefer = getActivity().getSharedPreferences("Register",getActivity().MODE_PRIVATE);
        name = prefer.getString("name","");
        pass = prefer.getString("pass","");
        Encode = getEncodedString(name,pass);
        final String url = String.format("%s%s", ConsumeAPI.BASE_URL,"api/v1/categories/");
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
                            JSONObject jsonObject = new JSONObject(respon);
                            JSONArray jsonArray = jsonObject.getJSONArray("results");
                            categoryListItems=new String[jsonArray.length()];
                            categoryListItemkh=new String[jsonArray.length()];
                            categoryIdListItems=new int[jsonArray.length()];
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                int id = object.getInt("id");
                                String name = object.getString("cat_name");
                                String category=object.getString("cat_name_kh");
                                categoryListItemkh[i]=category;
                                categoryListItems[i]=name;
                                categoryIdListItems[i]=id;
                                Log.e("category",""+id+","+name);
                            }
                            adapter = new ArrayAdapter<String>(getActivity(), R.layout.listitem, categoryListItems);
                            ls_type.setAdapter(adapter);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        ls_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String s = ls_type.getItemAtPosition(i).toString();
                mListener.onCLickItemType(s);
                mListener.AddIdCategory(categoryIdListItems[i]);
                dismiss();
            }
        });

        bt_ok = view.findViewById(R.id.bt_ok);

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
        mListener.onCLickItemType(tvSelected.getSelectedItem().toString());
        dismiss();
    }

    public interface ItemClickListener {
        void onCLickItemType(String item);
        void AddIdCategory(int id);
    }
    private String getEncodedString(String username, String password) {
        final String userpass = username+":"+password;
        return Base64.encodeToString(userpass.getBytes(),
                Base64.NO_WRAP);
    }

}
