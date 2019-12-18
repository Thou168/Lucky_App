package com.bt_121shoppe.motorbike.newversion.notifications;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bt_121shoppe.motorbike.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private SharedPreferences prefer;
    private String name,pass,basic_encode;
    private int pk=0;

    public static NotificationFragment newInstance() {
        return new NotificationFragment();
    } // Required empty public constructor


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_notification2, container, false);
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);

        prefer = getActivity().getSharedPreferences("Register", Context.MODE_PRIVATE);
        name = prefer.getString("name","");
        pass = prefer.getString("pass","");
        if (prefer.contains("token")) {
            pk = prefer.getInt("Pk", 0);
        } else if (prefer.contains("id")) {
            pk = prefer.getInt("id", 0);
        }

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public void onRefresh() {
        ImageView imglogo=requireActivity().findViewById(R.id.applogo);
        imglogo.setVisibility(View.GONE);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();
    }

}
