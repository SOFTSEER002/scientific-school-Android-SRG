package com.jeannypr.scientificstudy.Article.adapter;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.jeannypr.scientificstudy.Article.fragment.ParentingFragment;
import com.jeannypr.scientificstudy.Base.Constants;

import java.util.ArrayList;
import java.util.List;

public class BlogPagerAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    //private List<AcademicClassListModel> academicListModels

    private String[] titles = new String[]{Constants.BlogTabs.INTERVIEW, Constants.BlogTabs.JOURNALISM, Constants.BlogTabs.WRITINGS, Constants.BlogTabs.PARENTING};

    public BlogPagerAdapter(FragmentManager manager) {
        super(manager);
    }

   /* public AcademicPagerAdapter(FragmentManager manager, String[] mTitle) {
        super(manager);
        this.mTitle = mTitle;
    }*/

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        bundle.putString("subId", "hello");
        bundle.putInt("type", position);
        fragment = new ParentingFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    public void addFragment(String title) {
        mFragmentTitleList.add(title);
    }


    @Override
    public int getItemPosition(Object object) {
        // this method will be called for every fragment in the ViewPager
        if (object instanceof FragmentStatePagerAdapter) {
            return POSITION_UNCHANGED; // don't force a reload
        } else {
            // POSITION_NONE means something like: this fragment is no longer valid
            // triggering the ViewPager to re-build the instance of this fragment.
            return POSITION_NONE;
        }
    }



    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
