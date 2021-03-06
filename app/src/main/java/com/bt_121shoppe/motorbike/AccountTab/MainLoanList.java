package com.bt_121shoppe.motorbike.AccountTab;

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

import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.fragments.Loadbyuser;
import com.bt_121shoppe.motorbike.fragments.Loadbyuser_history;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainLoanList extends Fragment {
    public static final String TAG = "LoanFragement";
    LinearLayout mainLayout;
    TabLayout tabs;
    ViewPager vpNews;

    public MainLoanList(){}

    public static MainLoanList newInstance(){
        MainLoanList fragment=new MainLoanList();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainLayout = getView() != null ? (LinearLayout) getView() :(LinearLayout) inflater.inflate(R.layout.tab_loan_scrollable, container, false);
        tabs = (TabLayout) mainLayout.findViewById(R.id.tabScrollable);
        vpNews = (ViewPager) mainLayout.findViewById(R.id.vpNews);
        setUpPager();
        return mainLayout;
    }

    private void setUpPager() {
        NewsPagerAdapter adp = new NewsPagerAdapter(getChildFragmentManager());
        Loadbyuser n1 = new Loadbyuser();
        Loadbyuser_history n2 = new Loadbyuser_history();

        adp.addFrag(n1, getString(R.string.loan_active));
        adp.addFrag(n2, getString(R.string.history_loan));

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
