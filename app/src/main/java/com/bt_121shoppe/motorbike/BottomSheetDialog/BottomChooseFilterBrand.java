package com.bt_121shoppe.motorbike.BottomSheetDialog;

import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BottomChooseFilterBrand extends BottomSheetDialogFragment implements View.OnClickListener {

    public static final String TAG="ActionBottomDialog";
    private static final String ARG_CATEGORY="CATEGORY";
    private int categoryId=0;
    private Button btnOK;
    private TextView btnClear;
    private ListView lvBrand;
    private int[] brandIdListItems;
    private String[] brandListItem,brandListItemkh;
    private ArrayAdapter<String> adapter;
    private ItemClickListener mListener;
    private ProgressDialog pd;

    public static BottomChooseFilterBrand newInstance(int id){
        BottomChooseFilterBrand sheet=new BottomChooseFilterBrand();
        Bundle args=new Bundle();
        args.putInt(ARG_CATEGORY,id);
        sheet.setArguments(args);
        return sheet;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        return inflater.inflate(R.layout.bottom_sheet_filter_brand,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //btnOK=view.findViewById(R.id.bt_ok);
        btnClear=view.findViewById(R.id.bt_clear);
        lvBrand=view.findViewById(R.id.listview_brand);
        Bundle args=getArguments();
        if(args!=null)
        {
            categoryId=args.getInt(ARG_CATEGORY,0);
        }
        SharedPreferences preferences = getActivity().getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = preferences.getString("My_Lang", "");
        pd=new ProgressDialog(BottomChooseFilterBrand.this.getContext());
        pd.setMessage(getString(R.string.progress_waiting));
        pd.setCancelable(false);
        pd.show();

        String url = String.format("%s%s", ConsumeAPI.BASE_URL,"api/v1/brands/");
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(url)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Failure Error",e.getMessage());
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
                            int count=0,ccount=0;
                            if(categoryId==0){
                                count=jsonArray.length();
                                brandIdListItems=new int[count];
                                brandListItem=new String[count];
                                brandListItemkh=new String[count];
                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    int cate = object.getInt("category");
                                    int id = object.getInt("id");
                                    String name = object.getString("brand_name");
                                    String brand =object.getString("brand_name_as_kh");
                                    brandListItemkh[ccount]=brand;
                                    brandListItem[ccount]=name;
                                    brandIdListItems[ccount]=id;
                                    ccount++;
                                }
                            }
                            else{
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    int cate = object.getInt("category");
                                    if(categoryId==cate)
                                        count++;
                                }
                                brandIdListItems=new int[count];
                                brandListItem=new String[count];
                                brandListItemkh=new String[count];
                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    int cate = object.getInt("category");
                                    if (cate==categoryId){
                                        int id = object.getInt("id");
                                        String name = object.getString("brand_name");
                                        String brand =object.getString("brand_name_as_kh");
                                        brandListItemkh[ccount]=brand;
                                        brandListItem[ccount]=name;
                                        brandIdListItems[ccount]=id;
                                        ccount++;
                                    }
                                }
                            }

                            if (language.equals("en")) {
                                adapter = new ArrayAdapter<String>(getActivity(), R.layout.listitem, brandListItem);
                            }else if (language.equals("km")){
                                adapter = new ArrayAdapter<String>(getActivity(), R.layout.listitem, brandListItemkh);
                            }
                            lvBrand.setAdapter(adapter);
                            pd.dismiss();
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        //Start event listener
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        lvBrand.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mListener.onSetBrandName(lvBrand.getItemAtPosition(i).toString());
                mListener.onSetSelectedBrandIndex(brandIdListItems[i]);
                dismiss();
            }
        });
//        btnOK.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BottomChooseFilterBrand.ItemClickListener) {
            mListener = (BottomChooseFilterBrand.ItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement ItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        ListView tvSelected=(ListView) view;
        mListener.onSetBrandName(tvSelected.getSelectedItem().toString());
    }

    public interface ItemClickListener{
        void onSetBrandName(String item);
        void onSetSelectedBrandIndex(int id);
    }
}
