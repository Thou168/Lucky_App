package com.bt_121shoppe.lucky_app.AccountTab;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bt_121shoppe.lucky_app.R;

public class MainLikeList extends Fragment {

    public static final String TAG = "LikeFragement";
    LinearLayout main;

    public MainLikeList(){}

    public static MainLikeList newInstance(){
        MainLikeList fragment=new MainLikeList();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main = (LinearLayout) inflater.inflate(R.layout.main_extra, container, false);
        return main;
    }
}
