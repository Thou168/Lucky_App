package com.bt_121shoppe.motorbike.newversion.homes;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bt_121shoppe.motorbike.Api.api.AllResponse;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.model.Slider;
import com.bt_121shoppe.motorbike.R;
import com.custom.sliderimage.logic.SliderImage;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private android.app.Fragment currentFragment;

    private List<Slider> mImages=new ArrayList<>();
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        StrictMode.ThreadPolicy policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        return inflater.inflate(R.layout.fragment_home2, container, false);
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        Toolbar toolbar=getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("");
        /* start implementation slider navigation */
        SliderImage mSliderImages=view.findViewById(R.id.slider);
        List<String> mImages1 =new ArrayList<>();
//        mImages.add("http://cambo-report.com/storage/0MX5fa6STYIdLNYePG9x1rQHKPYWQSxazY8rRI1S.jpeg");
//        mImages.add("https://i.ytimg.com/vi/iAkUDrdAmUU/maxresdefault.jpg");
//        mImages.add("https://www.tracker.co.uk/application/files/thumbnails/hero_banner_small/6015/4867/2711/motorbike-banner.jpg");
//        mImages.add("https://www.coxmotorgroup.com/images/cmg/new-motorcycles/new-motorcycle-banner.jpg");
//        mSliderImages.setItems(mImages);
//        mSliderImages.addTimerToSlide(3000);
//        mSliderImages.getIndicator();

        Service apiService = Client.getClient().create(Service.class);
        retrofit2.Call<AllResponse> call = apiService.getSliderImage();
        call.enqueue(new retrofit2.Callback<AllResponse>() {
            @Override
            public void onResponse(retrofit2.Call<AllResponse> call, retrofit2.Response<AllResponse> response) {
                mImages = response.body().getresults();
                if (!response.isSuccessful()){
                    Log.d("211111111111111212", String.valueOf(response.code()));
                }
                for (int i=0;i<mImages.size();i++){
                    mImages1.add(mImages.get(i).getImg());
                }
                mSliderImages.setItems(mImages1);
                mSliderImages.addTimerToSlide(3000);
                mSliderImages.getIndicator();
            }

            @Override
            public void onFailure(retrofit2.Call<AllResponse> call, Throwable t) {

            }
        });

        /* end implementation slider navigation */
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        currentFragment=new com.bt_121shoppe.motorbike.homes.HomeFragment();
        if(savedInstanceState==null){
            com.bt_121shoppe.motorbike.homes.HomeFragment details=new com.bt_121shoppe.motorbike.homes.HomeFragment();
            getActivity().getFragmentManager().beginTransaction().add(R.id.frameLayout,details).commit();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

}
