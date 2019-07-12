package com.example.lucky_app.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.lucky_app.fragments.FragmentA;
import com.example.lucky_app.fragments.FragmentA1;
import com.example.lucky_app.fragments.FragmentB;
import com.example.lucky_app.fragments.FragmentB1;
import com.example.lucky_app.fragments.FragmentC;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment=null;
        if(position==0){
            fragment=new FragmentA1();
        }
        else if(position==1){
            fragment=new FragmentB1();
        }
        else if(position==2){
            fragment=new FragmentB1();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = "Post";
        }
        else if (position == 1) {
            title = "Like";
        }
        else if (position == 2) {
            title = "Load";
        }
        return title;
    }
}
