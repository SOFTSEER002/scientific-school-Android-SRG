package com.jeannypr.scientificstudy.leave.activity;

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
import android.view.Menu;
import android.view.ViewGroup;

import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.leave.fragment.HistoryLeaveFragment;
import com.jeannypr.scientificstudy.leave.fragment.ReportsFragment;
import com.jeannypr.scientificstudy.leave.fragment.RequestLeaveFragment;

import java.util.ArrayList;
import java.util.List;

public class LeaveModuleActivity extends AppCompatActivity implements RequestLeaveFragment.CommunicationWithActivity {
    Toolbar toolbar;
    ViewPager pager;
    Context context;
    TabLayout tabLayout;
    UserPreference pref;
    UserModel userModel;
    boolean isApprover;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_module);
        context = this;

        isApprover = getIntent().getBooleanExtra("isApprover", false);
        pref = UserPreference.getInstance(context);
        userModel = pref.getUserData();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, "Leave", "");

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
        LeaveModuleActivity.LeaveVPagerAdapter adapter = new LeaveModuleActivity.LeaveVPagerAdapter(getSupportFragmentManager());

        Bundle bundle = new Bundle();
        bundle.putBoolean("isApprover", isApprover);

        HistoryLeaveFragment historyLeaveFrag = new HistoryLeaveFragment();
        historyLeaveFrag.setArguments(bundle);

        RequestLeaveFragment requestLeaveFrag = new RequestLeaveFragment();
        requestLeaveFrag.setArguments(bundle);

        ReportsFragment reportsFragment = new ReportsFragment();
        reportsFragment.setArguments(bundle);

        adapter.addFragment(historyLeaveFrag, "History");
        adapter.addFragment(requestLeaveFrag, "Request");

        //TODO: uncomment to show report tab
       /* if (isApprover) {
            adapter.addFragment(reportsFragment, "Report");
        }*/

        pager.setAdapter(adapter);
    }

    @Override
    public void RefreshHistoyFrag() {
        SetUpViewPager();
    }

    class LeaveVPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fmList = new ArrayList<>();
        private final List<String> fmTitle = new ArrayList<>();
        private Fragment mCurrentFragment;

        public LeaveVPagerAdapter(FragmentManager fm) {
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
            //fragmentListner = listner;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.leave_menu, menu);

        return true;
    }
}
