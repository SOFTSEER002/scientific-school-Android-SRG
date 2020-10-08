package com.jeannypr.scientificstudy.Article.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.jeannypr.scientificstudy.Article.fragment.ParentingFragment;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Inventory.api.InventoryService;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.util.ArrayList;
import java.util.List;

//import android.app.FragmentManager;

public class BlogModuleActivity extends AppCompatActivity implements View.OnClickListener {
    private InventoryService service;
    ViewPager pager;
    Context context;
    TabLayout tabLayout;
    UserModel userdata;
    String moduleType;
    BlogModuleActivity.BlogVPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        setContentView(R.layout.activity_blog_module);

        //userdata = UserPreference.getInstance(context).getUserData();
        //service = new DataRepo<>(InventoryService.class, context).getService();

        //moduleType = getIntent().getStringExtra(Constants.MODULE_TYPE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, "Blog", "");

        pager = findViewById(R.id.viewPager);
        adapter = new BlogModuleActivity.BlogVPagerAdapter(getSupportFragmentManager());
        SetUpViewPager();

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);
    }

    @Override
    public void onResume() {
        super.onResume();
        /*if (getIntent().hasExtra("switchTab")) {
            String reportType = getIntent().getStringExtra("switchTab");
            moduleType = reportType;

            switch (reportType) {
                case Constants.InventoryReportType.PAYMENT:
                    tabLayout.getTabAt(0).select();
                    break;

                case Constants.InventoryReportType.RECEIPT:
                    tabLayout.getTabAt(1).select();
                    break;
            }
        }*/
    }

    private void SetUpViewPager() {
        BlogModuleActivity.BlogVPagerAdapter adapter = new BlogModuleActivity.BlogVPagerAdapter(getSupportFragmentManager());

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
            adapter.addFragment(parentingFrag, Constants.BlogTabs.PARENTING);

            /*switch (moduleType) {

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

            adapter.addFragment(paymnetReportsFrag, Constants.InventoryTabs.REPORT);*/

            pager.setAdapter(adapter);

        } catch (Exception ex) {
            Log.e("Blog module", ex.getMessage());
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
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