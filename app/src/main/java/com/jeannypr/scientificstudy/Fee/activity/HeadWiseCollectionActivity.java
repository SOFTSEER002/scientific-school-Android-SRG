package com.jeannypr.scientificstudy.Fee.activity;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.ViewGroup;

import com.jeannypr.scientificstudy.Fee.fragment.DateWiseCollectonFragment;
import com.jeannypr.scientificstudy.Fee.fragment.HeadWiseCollectionFragment;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;


import java.util.ArrayList;
import java.util.List;

public class HeadWiseCollectionActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewPager pager;
    Context context;
    TabLayout tabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_module);
        context = this;

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, "Daily Collection Report", "");

        pager = findViewById(R.id.viewPager);
        SetUpViewPager();
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void SetUpViewPager() {
        HeadWiseCollectionActivity.HeadVPagerAdapter adapter = new HeadWiseCollectionActivity.HeadVPagerAdapter(getSupportFragmentManager());

        HeadWiseCollectionFragment headWiseCollectionFragment = new HeadWiseCollectionFragment();
        DateWiseCollectonFragment dateWiseCollectonFragment = new DateWiseCollectonFragment();


        adapter.addFragment(headWiseCollectionFragment, "Head Wise");
        adapter.addFragment(dateWiseCollectonFragment, "Student Wise");
        pager.setAdapter(adapter);
    }


    class HeadVPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fmList = new ArrayList<>();
        private final List<String> fmTitle = new ArrayList<>();
        private Fragment mCurrentFragment;

        public HeadVPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getCurrentFragment() {
            return mCurrentFragment;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            if (getCurrentFragment() != object) {
                mCurrentFragment = ((Fragment) object);
            }

            super.setPrimaryItem(container, position, object);
        }

        @Override
        public Fragment getItem(int position) {
            return fmList.get(position);
        }

        @Override
        public int getCount() {
            return fmList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fmTitle.get(position);
        }

        public void addFragment(Fragment fm, String title) {
            fmList.add(fm);
            fmTitle.add(title);
        }

    }


}
