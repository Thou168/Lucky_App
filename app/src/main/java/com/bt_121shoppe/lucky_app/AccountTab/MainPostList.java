package com.bt_121shoppe.lucky_app.AccountTab;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bt_121shoppe.lucky_app.R;
import com.bt_121shoppe.lucky_app.fragments.FragmentA1;
import com.bt_121shoppe.lucky_app.fragments.Fragment_history;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainPostList extends Fragment {
    public static final String TAG = "PostFragement";
    LinearLayout mainLayout;
    TabLayout tabs;
    ViewPager vpNews;

    public MainPostList(){}

    public static MainPostList newInstance(){
        MainPostList fragment=new MainPostList();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainLayout =getView() != null ? (LinearLayout) getView() : (LinearLayout) inflater.inflate(R.layout.tab_post_scrollable, container, false);
        tabs = (TabLayout) mainLayout.findViewById(R.id.tabScrollable);
        vpNews = (ViewPager) mainLayout.findViewById(R.id.vpNews);
        setUpPager();
        return mainLayout;
    }

    private void setUpPager() {
        NewsPagerAdapter adp = new NewsPagerAdapter(getChildFragmentManager());
        FragmentA1 n1 = new FragmentA1();
        Fragment_history n2 = new Fragment_history();

        adp.addFrag(n1, "Active ads");
        adp.addFrag(n2, "History");

        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        vpNews.setAdapter(adp);
        vpNews.setOffscreenPageLimit(12);
        tabs.setupWithViewPager(vpNews);
    }

    private class NewsPagerAdapter extends FragmentPagerAdapter {

        List<Fragment> fragList = new ArrayList<>();
        List<String> titleList = new ArrayList<>();

        public NewsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFrag(Fragment f, String title) {
            fragList.add(f);
            titleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragList.get(position);
        }

        @Override
        public int getCount() {
            return fragList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }
    }
}
