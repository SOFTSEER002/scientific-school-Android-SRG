package com.jeannypr.scientificstudy.Classwork.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
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
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Classwork.api.CwHwService;
import com.jeannypr.scientificstudy.Classwork.fragment.HWInstructionFragment;
import com.jeannypr.scientificstudy.Classwork.fragment.HWResponseFragment;
import com.jeannypr.scientificstudy.Classwork.fragment.HWStudentWorkFragment;
import com.jeannypr.scientificstudy.Classwork.model.ActivityDetailModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.leave.fragment.RequestLeaveFragment;

import java.util.ArrayList;
import java.util.List;

public class HWDetailActivity extends AppCompatActivity implements RequestLeaveFragment.CommunicationWithActivity {
    Toolbar toolbar;
    ViewPager pager;
    Context context;
    TabLayout tabLayout;
    UserModel userModel;
    private int activityId, activityTypeId;
    private String className, activityType, subjectName;
    CwHwService classworkService;
    ActivityDetailModel hwDetailModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hw_detail);
        context = this;
        userModel = UserPreference.getInstance(context).getUserData();
        classworkService = new DataRepo<>(CwHwService.class, context).getService();

        getIntentData();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, activityType, subjectName);

        pager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);
        SetUpViewPager();

    }

    //get intent data
    private void getIntentData() {
        activityId = getIntent().getIntExtra("activityId", -1); // in case of foreground, intent will be received from myFirebaseMessaging
        className = getIntent().getStringExtra("className");
        subjectName = getIntent().getStringExtra("subjectName");
        activityTypeId = getIntent().getIntExtra("activityTypeId", -1);
        activityType = Constants.DiaryTypeName.Homework;
    }


    private void setFragmentData(ActivityDetailModel hwDetailModel) {
        Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewPager + ":" + pager.getCurrentItem());
        if (page != null) {

            switch (pager.getCurrentItem()) {
                case 0:
                    HWInstructionFragment instructionFragment = (HWInstructionFragment) page;
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(Constants.HW_DETAIL, hwDetailModel);
                    instructionFragment.setArguments(bundle);
                    break;
            }
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (userModel.getRoleTitle().equals(Constants.Role.ADMIN) || userModel.getRoleTitle().equals(Constants.Role.TEACHER))
            getMenuInflater().inflate(R.menu.cw_hw_detail_menu, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void SetUpViewPager() {
        HWDetailActivity.HWPagerAdapter adapter = new HWDetailActivity.HWPagerAdapter(getSupportFragmentManager());

        Bundle bundle = new Bundle();
        bundle.putInt(Constants.ACTIVITY_ID, activityId);
        bundle.putString(Constants.ACTIVITY_TYPE, activityType);
        bundle.putInt(Constants.ACTIVITY_TYPE_ID, activityTypeId);

        HWInstructionFragment instructionFragment = new HWInstructionFragment();
        instructionFragment.setArguments(bundle);

        HWResponseFragment responseFragment = new HWResponseFragment();
        responseFragment.setArguments(bundle);

        HWStudentWorkFragment studentFragment = new HWStudentWorkFragment();
        studentFragment.setArguments(bundle);

        adapter.addFragment(instructionFragment, Constants.HWDetailTab.INSTRUCTION);

        if (!userModel.getRoleTitle().equals(Constants.Role.PARENT))
            adapter.addFragment(studentFragment, Constants.HWDetailTab.STUDENT_WORK);
        else adapter.addFragment(responseFragment, Constants.HWDetailTab.RESPONSE);
       /* else {
            tabLayout.setVisibility(View.GONE);
        }*/
        pager.setAdapter(adapter);
    }

    @Override
    public void RefreshHistoyFrag() {
//        SetUpViewPager();
    }

    class HWPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fmList = new ArrayList<>();
        private final List<String> fmTitle = new ArrayList<>();
        private Fragment mCurrentFragment;

        public HWPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getCurrentFragment() {
            return mCurrentFragment;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            if (getCurrentFragment() != object)
                mCurrentFragment = ((Fragment) object);

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
