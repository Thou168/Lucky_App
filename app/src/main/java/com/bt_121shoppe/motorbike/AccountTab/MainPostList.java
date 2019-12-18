package com.bt_121shoppe.motorbike.AccountTab;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bt_121shoppe.motorbike.Language.LocaleHapler;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.fragments.Postbyuser;
import com.bt_121shoppe.motorbike.fragments.history_postbyuser;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class MainPostList extends Fragment {
    public static final String TAG = "PostFragement";
    LinearLayout mainLayout;
    TabLayout tabs;
    ViewPager vpNews;
    Context context;
    Resources resources;

    public MainPostList(){}

    public static MainPostList newInstance(){
        MainPostList fragment=new MainPostList();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.d(TAG, "onCreate: ");

        Paper.init(getContext());
        String language = Paper.book().read("language");
        //Log.d("44444444","MainPostList"+language);
        if (language == null)
            Paper.book().write("language","en");
        updateView(language);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainLayout =getView() != null ? (LinearLayout) getView() : (LinearLayout) inflater.inflate(R.layout.tab_post_scrollable, container, false);
        tabs = (TabLayout) mainLayout.findViewById(R.id.tabScrollable);
        vpNews = (ViewPager) mainLayout.findViewById(R.id.vpNews);
        setUpPager();
        return mainLayout;
    }

    private void updateView(String language) {
        context = LocaleHapler.setLocale(getContext(),language);
        resources = context.getResources();

//        NewsPagerAdapter adp = new NewsPagerAdapter(getChildFragmentManager());
//        Postbyuser n1 = new Postbyuser();
//        history_postbyuser n2 = new history_postbyuser();
//        adp.addFrag(n1, resources.getString(R.string.active));
//        adp.addFrag(n2, resources.getString(R.string.history));
//
//        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
//        vpNews.setAdapter(adp);
//        vpNews.setOffscreenPageLimit(12);
//        tabs.setupWithViewPager(vpNews);
    }

    private void setUpPager() {
        NewsPagerAdapter adp = new NewsPagerAdapter(getChildFragmentManager());
        Postbyuser n1 = new Postbyuser();
        history_postbyuser n2 = new history_postbyuser();
        adp.addFrag(n1, resources.getString(R.string.active));
        adp.addFrag(n2, resources.getString(R.string.history));

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
