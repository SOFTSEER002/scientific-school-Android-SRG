package com.jeannypr.scientificstudy.Notification.activity;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.ApiConstants;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Database.table.TransportNotificationModel;
import com.jeannypr.scientificstudy.Notification.ViewModel.TransportNotificationVM;
import com.jeannypr.scientificstudy.Notification.fragment.AllNotificationsFragment;
import com.jeannypr.scientificstudy.Notification.fragment.TransportNotificationFragment;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Timetable.api.TimetableService;
import com.jeannypr.scientificstudy.Transport.api.TransportService;
import com.jeannypr.scientificstudy.Transport.model.GetNotificationBean;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsActivity extends AppCompatActivity {

    private TimetableService service;
    private Context context;
    UserPreference userPreference;
    UserModel userModel;
    ProgressBar pb;
    CoordinatorLayout timetable;
    TransportNotificationVM vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable_module);
        context = this;

//        vm = new TransportNotificationVM(this);
        vm = ViewModelProviders.of(this).get(TransportNotificationVM.class);
        service = new DataRepo<>(TimetableService.class, context).getService();
        userPreference = UserPreference.getInstance(this);
        userModel = userPreference.getUserData();
        GetAllNotificationsFromServer();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, "Notifications", "");

        timetable = findViewById(R.id.timetable);
        pb = findViewById(R.id.progressBar);
        ViewPager viewPager = findViewById(R.id.viewpager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupViewPager(viewPager);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setupViewPager(final ViewPager viewPager) {
        //     pb.setVisibility(View.VISIBLE);
        final NotificationsActivity.ViewPagerAdapter adapter = new NotificationsActivity.ViewPagerAdapter(getSupportFragmentManager());
        TransportNotificationFragment transportFrag = new TransportNotificationFragment();
        AllNotificationsFragment allNotificationsFrag = new AllNotificationsFragment();

        adapter.addFragment(allNotificationsFrag, "ALL");
        adapter.addFragment(transportFrag, "TRANSPORT");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void GetAllNotificationsFromServer() {
        TransportService service = new DataRepo<>(TransportService.class, context, ApiConstants.CHAT_BASE_URL).getService();
        service.GetAllNotifications(UserPreference.getInstance(context).getSchoolCode(), userModel.getUserId(),
                0, 10).enqueue(new Callback<GetNotificationBean>() {

            @Override
            public void onResponse(Call<GetNotificationBean> call, Response<GetNotificationBean> response) {
                if (response.body() != null) {

                    GetNotificationBean bean = response.body();
                    if (bean != null && bean.data != null) {

                        for (TransportNotificationModel model : bean.data) {

                            Date objDate = null;
                            try {
                                objDate = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z").parse(model.getNotificationDate());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            String formattedTime = Utility.GetFormattedTimeHMS(objDate);
                            String formattedDate = Utility.GetFormattedDateMDY(objDate, Constants.DATE_FORMAT_MDY);
                            model.setNotificationDate(formattedDate);
                            model.setNotificationTime(formattedTime);

                            if (!model.getReceiverIds().equals("")) {
                                String ids = model.getReceiverIds();
                                model.setReceiverIds("," + ids + ","); // comma is concatenated as per "where" condition in select query.
                            }
                            if (vm != null)
                                vm.insert(model);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<GetNotificationBean> call, Throwable t) {
                Log.e("Exception: ", t.getMessage());
            }
        });
    }
}