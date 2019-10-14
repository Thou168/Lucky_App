package com.bt_121shoppe.motorbike.loan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.fragments.history_postbyuser;
import com.bt_121shoppe.motorbike.loan.child.AdapterNavigation.CustomViewPager;
import com.bt_121shoppe.motorbike.loan.child.AdapterNavigation.FragmentAdapter;
import com.bt_121shoppe.motorbike.loan.child.one;
import com.bt_121shoppe.motorbike.loan.child.three;
import com.bt_121shoppe.motorbike.loan.child.two;

public class Create_Load extends AppCompatActivity {

    Button next,prev;
    CustomViewPager viewPager;
    int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__load);

        viewPager = findViewById(R.id.viewPager);
        setupFm(getSupportFragmentManager(), viewPager); //Setup Fragment
//        next = findViewById(R.id.next);
//        prev = findViewById(R.id.back);

//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.viewPager,
//                    new one()).commit();
//        }
//        viewPager.setCurrentItem(count);

//        next.setOnClickListener(v -> {
//            count++;
//            viewPager.setCurrentItem(count,true);
//        });
//        prev.setOnClickListener(v -> {
//            count--;
//            viewPager.setCurrentItem(count,true);
//        });
    }
    public void setViewPager (int pager){
        viewPager.setCurrentItem(pager);
    }
    public static void setupFm(FragmentManager fragmentManager, ViewPager viewPager){
        FragmentAdapter Adapter = new FragmentAdapter(fragmentManager);
        //Add All Fragment To List
        Adapter.add(new one(), "null");
        Adapter.add(new two(), "null");
        Adapter.add(new three(), "null");
        viewPager.setAdapter(Adapter);
    }
}
