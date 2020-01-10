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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class BottomChooseYear extends BottomSheetDialogFragment implements View.OnClickListener {

    public static final String TAG = "ActionBottomDialog";
    private Button bt_ok;
    private TextView bt_clear;
    private ListView ls_year;

    private int[] yearIdListItems;
    private String[] yearListItems;
    SharedPreferences prefer;
    private String name,pass,Encode;
    private ItemClickListener mListener;
    private ArrayAdapter<String> adapter;

    public static BottomChooseYear newInstance() {
        return new BottomChooseYear();
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_year, container, false);
        return view;
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bt_clear = view.findViewById(R.id.bt_clear);
        ls_year = view.findViewById(R.id.year);

        prefer = getActivity().getSharedPreferences("Register",getActivity().MODE_PRIVATE);
        name = prefer.getString("name","");
        pass = prefer.getString("pass","");
        Encode = getEncodedString(name,pass);
        final String url = String.format("%s%s", ConsumeAPI.BASE_URL,"api/v1/years/");
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
                try{
                    JSONObject jsonObject = new JSONObject(respon);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    int count=0;
                    yearListItems=new String[jsonArray.length()];
                    yearIdListItems=new int[jsonArray.length()];
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    int id = object.getInt("id");
                                    String name = object.getString("year");
                                    Log.e("Year",""+id+name);
                                    yearListItems[i]=name;
                                    yearIdListItems[i]=id;
                                }
                                adapter = new ArrayAdapter<String>(getActivity(), R.layout.listitem, yearListItems);
                                ls_year.setAdapter(adapter);
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
        ls_year.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String s = ls_year.getItemAtPosition(i).toString();
                mListener.onClickItem(s);
                mListener.AddIDyear(yearIdListItems[i]);
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
        mListener.onClickItem(tvSelected.getSelectedItem().toString());
        dismiss();
    }

    public interface ItemClickListener {
        void onClickItem(String item);
        void AddIDyear(int id);
    }
    private String getEncodedString(String username, String password) {
        final String userpass = username+":"+password;
        return Base64.encodeToString(userpass.getBytes(),
                Base64.NO_WRAP);
    }

}
