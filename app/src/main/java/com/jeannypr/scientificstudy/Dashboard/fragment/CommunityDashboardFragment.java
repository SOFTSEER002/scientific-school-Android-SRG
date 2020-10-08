package com.jeannypr.scientificstudy.Dashboard.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.jeannypr.scientificstudy.Article.adapter.BlogPagerAdapter;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;

import java.util.ArrayList;
import java.util.List;

public class CommunityDashboardFragment extends Fragment implements View.OnClickListener {
    Context context;
    View view;
    UserPreference userPref;
    ViewPager pager;
    TabLayout tabLayout;
    UserModel userdata;
    String moduleType;
    CommunityDashboardFragment.BlogVPagerAdapter adapter;

    BlogPagerAdapter pagerAdapter;


    private int[] mIconList = {
            R.drawable.ic_nterview,
            R.drawable.ic_journalism,
            R.drawable.ic_writings,
            R.drawable.ic_parenting
    };


    private String[] mTitle = new String[]{
            Constants.BlogTabs.INTERVIEW,
            Constants.BlogTabs.JOURNALISM,
            Constants.BlogTabs.WRITINGS,
            Constants.BlogTabs.PARENTING
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        //schoolService = new DataRepo<>(SchoolService.class, context).getService();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_community,
                container, false);

        context = getActivity();
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        pager = view.findViewById(R.id.viewPager);
        adapter = new CommunityDashboardFragment.BlogVPagerAdapter(getActivity().getSupportFragmentManager());


        tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);
        setupViewPager(pager,tabLayout);

        //SetUpViewPager();

        /*tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);
        int length = tabLayout.getTabCount();
        Log.w("length", "<<<length>> " + length);
        for (int i = 0; i < length; i++) {
            Objects.requireNonNull(tabLayout.getTabAt(i)).setCustomView(getTabView(i));

        }*/

        pager.setCurrentItem(0);



    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public View getTabView(int position) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.community_tab_layout, null);
        TextView title =  view.findViewById(R.id.title);
        ImageView icon =  view.findViewById(R.id.icon);
        ViewGroup layout =  view.findViewById(R.id.layout);
        icon.setImageResource(mIconList[position]);
        title.setText(this.getPageTitle(position));

        return view;
    }


    private String getPageTitle(int pos){
        return mTitle[pos];
    }

    private void setupViewPager(ViewPager viewPager, TabLayout tabLayout) {
        pagerAdapter = new BlogPagerAdapter(getActivity().getSupportFragmentManager());

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
      /*  tabLayout.getTabAt(0).setIcon(mIconList[0]);
        tabLayout.getTabAt(1).setIcon(mIconList[1]);
        tabLayout.getTabAt(2).setIcon(mIconList[2]);
        tabLayout.getTabAt(3).setIcon(mIconList[3]);*/

        int length = tabLayout.getTabCount();
        Log.w("length", "<<<length>> " + length);
        for (int i = 0; i < length; i++) {
            tabLayout.getTabAt(i).setCustomView(getTabView(i));

        }
    }
    /*private void SetUpViewPager() {



        *//*CommunityDashboardFragment.BlogVPagerAdapter adapter = new CommunityDashboardFragment.BlogVPagerAdapter(getActivity().getSupportFragmentManager());

        try {

            ParentingFragment interviewFrag = new ParentingFragment();
            Bundle paymentBundle = new Bundle();
            paymentBundle.putString("blogType", Constants.BlogTabs.INTERVIEW);
            interviewFrag.setArguments(paymentBundle);



            ParentingFragment jurnaalismFrag = new ParentingFragment();
            Bundle receiptBundle = new Bundle();
            receiptBundle.putString("blogType", Constants.BlogTabs.JOURNALISM);
            jurnaalismFrag.setArguments(receiptBundle);

            ParentingFragment writingFrag = new ParentingFragment();
            Bundle writingBundle = new Bundle();
            writingBundle.putString("blogType", Constants.BlogTabs.WRITINGS);
            writingFrag.setArguments(writingBundle);


            ParentingFragment parentingFrag = new ParentingFragment();
            Bundle parentingBundle = new Bundle();
            parentingBundle.putString("blogType", Constants.BlogTabs.PARENTING);
            parentingFrag.setArguments(parentingBundle);


            adapter.addFragment(interviewFrag, Constants.BlogTabs.INTERVIEW);
            adapter.addFragment(jurnaalismFrag, Constants.BlogTabs.JOURNALISM);
            adapter.addFragment(writingFrag, Constants.BlogTabs.WRITINGS);
            adapter.addFragment(parentingFrag, Constants.BlogTabs.PARENTING);*//*

            *//*switch (moduleType) {

                case Constants.Module.ACCOUNTS:

                    PaymentReceiptRegisterFragment paymentSummaryFrag = new PaymentReceiptRegisterFragment();
                    Bundle paymentBundle = new Bundle();
                    paymentBundle.putString("transactionType", Constants.InventoryReportType.PAYMENT);
                    paymentSummaryFrag.setArguments(paymentBundle);

                    PaymentReceiptRegisterFragment receiptSummaryFrag = new PaymentReceiptRegisterFragment();
                    Bundle receiptBundle = new Bundle();
                    receiptBundle.putString("transactionType", Constants.InventoryReportType.RECEIPT);
                    receiptSummaryFrag.setArguments(receiptBundle);

                    adapter.addFragment(paymentSummaryFrag, Constants.InventoryTabs.PAYMENT);
                    adapter.addFragment(receiptSummaryFrag, Constants.InventoryTabs.RECEIPT);

                    break;

                case Constants.Module.INVENTORY:

                    PurchaseSaleRegisterFragment purchaseSummaryFrag = new PurchaseSaleRegisterFragment();
                    Bundle purchaseBundle = new Bundle();
                    purchaseBundle.putString("transactionType", Constants.InventoryReportType.PURCHASE);
                    purchaseSummaryFrag.setArguments(purchaseBundle);

                    PurchaseSaleRegisterFragment saleSummaryFrag = new PurchaseSaleRegisterFragment();
                    Bundle saleBundle = new Bundle();
                    saleBundle.putString("transactionType", Constants.InventoryReportType.SALE);
                    saleSummaryFrag.setArguments(saleBundle);

                    adapter.addFragment(purchaseSummaryFrag, Constants.InventoryTabs.PURCHASE);
                    adapter.addFragment(saleSummaryFrag, Constants.InventoryTabs.SALE);

                    break;
            }

            InventoryReportsFragment paymnetReportsFrag = new InventoryReportsFragment();
            Bundle reportBundle = new Bundle();
            reportBundle.putString("transactionType", Constants.InventoryReportType.REPORT);
            reportBundle.putString("moduleType", moduleType);
            paymnetReportsFrag.setArguments(reportBundle);

            adapter.addFragment(paymnetReportsFrag, Constants.InventoryTabs.REPORT);*//*

            pager.setAdapter(adapter);



        } catch (Exception ex) {
            Log.e("Blog module", ex.getMessage());
        }

    }*/



    @Override
    public void onClick(View v) {

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //  inflater.inflate(R.menu.notification_menu, menu);


        super.onCreateOptionsMenu(menu, inflater);

    }


    class BlogVPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fmList = new ArrayList<>();
        private final List<String> fmTitle = new ArrayList<>();
        private Fragment mCurrentFragment;

        public BlogVPagerAdapter(FragmentManager fm) {
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

        public void removeFragment(int fragIndex, int titleIndex) {
            fmList.remove(fragIndex);
            fmTitle.remove(titleIndex);

            adapter.notifyDataSetChanged();
        }
    }


}