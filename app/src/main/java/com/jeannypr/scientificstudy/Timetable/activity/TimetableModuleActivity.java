package com.jeannypr.scientificstudy.Timetable.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.ChildModel;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Timetable.Fragment.DayWiseTimetableFragment;
import com.jeannypr.scientificstudy.Timetable.api.TimetableService;
import com.jeannypr.scientificstudy.Timetable.model.DayWisePeriodsModel;
import com.jeannypr.scientificstudy.Timetable.model.PeriodModel;
import com.jeannypr.scientificstudy.Timetable.model.SchoolShiftsBean;
import com.jeannypr.scientificstudy.Timetable.model.SchoolShiftsModel;
import com.jeannypr.scientificstudy.Timetable.model.TimetableBean;
import com.jeannypr.scientificstudy.Timetable.model.TimetableModel;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimetableModuleActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TimetableService service;
    private Context context;
    UserPreference userPreference;
    UserModel userModel;
    TimetableModel timetableModel;
    List<DayWisePeriodsModel> dayWisePeriodsModel;
    List<PeriodModel> periodModel;
    private LinearLayout noRecord;
    private TextView noRecordMsg;
    ChildModel childModel;
    ProgressBar pb;
    int year, month, day, shiftId, weekday, totalshifts, staffId, classId;
    String teacherName, className, shiftName, timetableOf;
    CoordinatorLayout timetable;
    List<SchoolShiftsModel> schoolShiftsModel;
    Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable_module);
        context = this;

        service = new DataRepo<>(TimetableService.class, context).getService();
        userPreference = UserPreference.getInstance(this);

        timetableOf = getIntent().getStringExtra(Constants.Timetable_INTENT);

        userModel = userPreference.getUserData();
        if (userModel.getRoleTitle().equals(Constants.Role.PARENT)) {
            childModel = userPreference.getSelectedChild();
        }

        cal = Calendar.getInstance(TimeZone.getDefault());
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        shiftId = 0;
        totalshifts = 0;
        schoolShiftsModel = new ArrayList<>();
        dayWisePeriodsModel = new ArrayList<>();

        toolbar = findViewById(R.id.toolbar);
        timetable = findViewById(R.id.timetable);
        noRecord = findViewById(R.id.noRecord);
        noRecordMsg = findViewById(R.id.noRecordMsg);
        pb = findViewById(R.id.progressBar);

        setSupportActionBar(toolbar);
        SetHeaderTitleAndSubTitle();
        SetOnClickOnAppbarTitle();

        viewPager = findViewById(R.id.viewpager);

        if ((getIntent().hasExtra("teacherId"))) {
            staffId = getIntent().getIntExtra("teacherId", -1);
            shiftName = getIntent().getStringExtra("shiftName");
            teacherName = getIntent().getStringExtra("teacherName");
            shiftId = getIntent().getIntExtra("shiftId", -1);
            setupViewPager(viewPager);

        } else if (getIntent().hasExtra("classId")) {
            classId = getIntent().getIntExtra("classId", -1);
            className = getIntent().getStringExtra("className");
            shiftName = getIntent().getStringExtra("shiftName");
            shiftId = getIntent().getIntExtra("shiftId", -1);
            setupViewPager(viewPager);

        } else {
            staffId = userModel.getUserId();
            ChooseShift();
        }

        setSupportActionBar(toolbar);
        SetHeaderTitleAndSubTitle();
        SetOnClickOnAppbarTitle();

        //ChooseShift();
//        if (childModel.ShiftId == -1 || childModel.ShiftId == 0) {
//            ChooseShift();
//        } else {
//            shiftId = childModel.ShiftId;
//            setupViewPager(viewPager);
//        }


        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setupViewPager(final ViewPager viewPager) {
        pb.setVisibility(View.VISIBLE);
        final TimetableModuleActivity.ViewPagerAdapter adapter = new TimetableModuleActivity.ViewPagerAdapter(getSupportFragmentManager());
        GetTimetable(adapter);
        viewPager.setAdapter(adapter);
        // viewPager.setAdapter(adapter);

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

    public void GetTimetable(final TimetableModuleActivity.ViewPagerAdapter adapter) {


        String role = userModel.getRoleTitle().toLowerCase();

        if (role.equals((Constants.Role.PARENT).toLowerCase())) {

            service.GetClassTimetable(userModel.getSchoolId(), userModel.getAcademicyearId(), childModel.ClassId, shiftId, year, month + 1, day)
                    .enqueue(new Callback<TimetableBean>() {
                        @Override
                        public void onResponse(Call<TimetableBean> call, Response<TimetableBean> response) {
                            TimetableBean timetableBean = response.body();
                            if (timetableBean != null) {
                                ShowFragments(timetableBean, adapter);
                            }
                        }

                        @Override
                        public void onFailure(Call<TimetableBean> call, Throwable t) {
                            pb.setVisibility(View.GONE);
                            Toast.makeText(context, "Could not load timetable. Please try again", Toast.LENGTH_LONG).show();
                        }
                    });
        }/* else if (role.equals((Constants.Role.TEACHER).toLowerCase()) || role.equals((Constants.Role.ADMIN).toLowerCase())) {

        }*/ else if (role.equals((Constants.Role.TEACHER).toLowerCase())) {

/*
            service.GetTeacherTimetable(userModel.getSchoolId(), userModel.getAcademicyearId(), staffId, shiftId, year, month + 1, day)
                    .enqueue(new Callback<TimetableBean>() {
                        @Override
                        public void onResponse(Call<TimetableBean> call, Response<TimetableBean> response) {
                            TimetableBean timetableBean = response.body();
                            if (timetableBean != null) {
                                ShowFragments(timetableBean, adapter);
                            }
                        }

                        @Override
                        public void onFailure(Call<TimetableBean> call, Throwable t) {
                            pb.setVisibility(View.GONE);
                            Toast.makeText(context, "Could not load timetable. Please try again", Toast.LENGTH_LONG).show();
                        }
                    });
*/
            GetTeacherTimetable(adapter);

        } else if (role.equals((Constants.Role.ADMIN).toLowerCase())) {
            if (classId > 0) {
                service.GetClassTimetable(userModel.getSchoolId(), userModel.getAcademicyearId(), classId, shiftId, year, month + 1, day)
                        .enqueue(new Callback<TimetableBean>() {
                            @Override
                            public void onResponse(Call<TimetableBean> call, Response<TimetableBean> response) {
                                TimetableBean timetableBean = response.body();
                                if (timetableBean != null) {
                                    ShowFragments(timetableBean, adapter);
                                }
                            }

                            @Override
                            public void onFailure(Call<TimetableBean> call, Throwable t) {
                                pb.setVisibility(View.GONE);
                                Toast.makeText(context, "Could not load timetable. Please try again", Toast.LENGTH_LONG).show();
                            }
                        });
            } else {
                /*service.GetTeacherTimetable(userModel.getSchoolId(), userModel.getAcademicyearId(), staffId, shiftId, year, month + 1, day)
                        .enqueue(new Callback<TimetableBean>() {
                            @Override
                            public void onResponse(Call<TimetableBean> call, Response<TimetableBean> response) {
                                TimetableBean timetableBean = response.body();
                                if (timetableBean != null) {
                                    ShowFragments(timetableBean, adapter);
                                }
    }

                            @Override
                            public void onFailure(Call<TimetableBean> call, Throwable t) {
        pb.setVisibility(View.GONE);
                                Toast.makeText(context, "Could not load timetable. Please try again", Toast.LENGTH_LONG).show();
                            }
                        });*/
                GetTeacherTimetable(adapter);
            }
        }

    }


    private void GetTeacherTimetable(final TimetableModuleActivity.ViewPagerAdapter adapter) {
        service.GetTeacherTimetable(userModel.getSchoolId(), userModel.getAcademicyearId(), staffId, shiftId, year, month + 1, day)
                .enqueue(new Callback<TimetableBean>() {
                    @Override
                    public void onResponse(Call<TimetableBean> call, Response<TimetableBean> response) {
                        TimetableBean timetableBean = response.body();
                        if (timetableBean != null) {
                            ShowFragments(timetableBean, adapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<TimetableBean> call, Throwable t) {
                        pb.setVisibility(View.GONE);
                        Toast.makeText(context, "Could not load timetable. Please try again", Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void ShowFragments(TimetableBean timetableBean, TimetableModuleActivity.ViewPagerAdapter adapter) {
        pb.setVisibility(View.GONE);
        if (timetableBean.rcode == Constants.Rcode.OK) {
            if (timetableBean.data != null) {

                TimetableModel timetableModel = timetableBean.data;

                dayWisePeriodsModel.clear();
                dayWisePeriodsModel = timetableModel.classPeriodsList;

                if (dayWisePeriodsModel.size() > 0) {

                    for (DayWisePeriodsModel dayPeriods : dayWisePeriodsModel) {

                        DayWiseTimetableFragment fragment = new DayWiseTimetableFragment();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("periods", dayPeriods);
                        bundle.putString("timetableOf", timetableOf);
                        fragment.setArguments(bundle);
                        adapter.addFragment(fragment, dayPeriods.Day);
                    }

                    adapter.notifyDataSetChanged();
                    SetDefaultTab();

                } else {
                    noRecord.setVisibility(View.VISIBLE);
                    noRecordMsg.setText("No record found.");
                }
            }

        } else if (timetableBean.rcode == Constants.Rcode.NORECORDS) {
            noRecord.setVisibility(View.VISIBLE);
            noRecordMsg.setText("No record found.");

        } else {
            Toast.makeText(context, "Could not load timetable. Please try again", Toast.LENGTH_LONG).show();
        }
    }

    public void ChooseShift() {

        pb.setVisibility(View.VISIBLE);
        service.GetSchoolShifts(userModel.getSchoolId()).enqueue(new Callback<SchoolShiftsBean>() {
            @Override
            public void onResponse(final Call<SchoolShiftsBean> call, Response<SchoolShiftsBean> response) {
                SchoolShiftsBean schoolShiftsBean = response.body();

                if (schoolShiftsBean != null) {
                    if (schoolShiftsBean.rcode.equals(Constants.Rcode.OK)) {
                        schoolShiftsModel.clear();

                        if (schoolShiftsBean.data != null) {


                            schoolShiftsModel = schoolShiftsBean.data;
                            totalshifts = schoolShiftsModel.size();

                            if (totalshifts > 1) {
                                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                final AlertDialog alertDialog = builder.create();
                                /*alertDialog.setTitle("Choose Shift of " + childModel.Name + "...");*/
                                alertDialog.setTitle("Choose Shift...");

                                alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        SetHeaderTitleAndSubTitle();

                                        if (shiftId == 0) {
                                            SetSelectedShift(schoolShiftsModel.get(0));
                                            setupViewPager(viewPager);
                                        }
                                    }
                                });

                                View viewInflated = LayoutInflater.from(context).inflate(R.layout.shift_alert_dialog, timetable, false);
                                ViewGroup parent = viewInflated.findViewById(R.id.shifts);
                                final ArrayList<ConstraintLayout> shiftRows = new ArrayList<>();

                                int i = 0;
                                for (final SchoolShiftsModel shiftsModel : schoolShiftsModel) {
                                    View view = LayoutInflater.from(context).inflate(R.layout.row_shifts, parent, false);
                                    // Set up the input
                                    final TextView txtToTime = view.findViewById(R.id.to);
                                    final TextView txtFromTime = view.findViewById(R.id.from);
                                    final TextView txtShiftname = view.findViewById(R.id.shiftName);
                                    final ConstraintLayout shiftRow = view.findViewById(R.id.shiftRow);

                                    txtToTime.setText(shiftsModel.EndTime != null ? "To\n" + shiftsModel.EndTime : "");
                                    txtShiftname.setText(shiftsModel.ShiftName != null ? shiftsModel.ShiftName : "");
                                    txtFromTime.setText(shiftsModel.StartTime != null ? "From\n" + shiftsModel.StartTime : "");
                                    if (i == 0) {
                                        txtShiftname.setBackgroundColor(getResources().getColor(R.color.black5));
                                    }
                                    ++i;

                                    shiftRows.add(shiftRow);

                                    shiftRow.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            for (ConstraintLayout row : shiftRows) {
                                                row.setBackgroundColor(Color.parseColor("#f2f2f2"));
                                            }
                                            txtShiftname.setBackgroundColor(getResources().getColor(R.color.black5));
                                            // SetHeaderTitleAndSubTitle();

                                            SetSelectedShift(shiftsModel);
                                            alertDialog.cancel();
                                            setupViewPager(viewPager);

                                        }
                                    });
                                    parent.addView(view);
                                }

                                alertDialog.setView(viewInflated);
                                alertDialog.show();
                            } else if (schoolShiftsModel.size() == 0) {
                                noRecord.setVisibility(View.VISIBLE);
                                noRecordMsg.setText("No record found.");
                            }
                            // SchoolShiftsModel model = schoolShiftsModel.get(0);
                            else {
                                SetSelectedShift(schoolShiftsModel.get(0));

                                setupViewPager(viewPager);
                           /* shiftId = model.Id;
                            childModel.ShiftId = model.Id;
                            childModel.ShiftNamel = model.ShiftName;

                            userPreference.setSelectedChild(childModel);*/
                            }

                        }
                    } else if (schoolShiftsBean.rcode == Constants.Rcode.NORECORDS) {
                        noRecord.setVisibility(View.VISIBLE);
                        noRecordMsg.setText("No record found.");

                    } else {
                        Toast.makeText(context, "Could not load shifts. Please try again", Toast.LENGTH_LONG).show();
                    }
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<SchoolShiftsBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                Toast.makeText(context, "Could not load shifts. Please try again", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void SetSelectedShift(SchoolShiftsModel model) {
        shiftId = model.Id;

        switch (userModel.getRoleTitle()) {
            case Constants.Role.PARENT:

                childModel.ShiftId = model.Id;
                childModel.ShiftNamel = model.ShiftName;
                userPreference.setSelectedChild(childModel);
                break;

            case Constants.Role.TEACHER:

                userModel.setShiftId(model.Id);
                userModel.setShiftNamel(model.ShiftName);
                userPreference.setUserData(userModel);
                break;
        }
    }

    public void SetHeaderTitleAndSubTitle() {
        String subtitle = "";

        if (getSupportActionBar() != null) {

            switch (userModel.getRoleTitle()) {
                case Constants.Role.PARENT:
                    subtitle = childModel.ShiftNamel;
                    break;

                case Constants.Role.TEACHER:
                    subtitle = userModel.getShiftNamel();
                    break;

                case Constants.Role.ADMIN:
                    if (timetableOf.equals(Constants.TimetableOf.SELF)) {
                        subtitle = userModel.getShiftNamel();

                    } else if (timetableOf.equals(Constants.TimetableOf.STAFF)) {
                        if (teacherName != null && teacherName != "" && shiftName != null && !shiftName.equals("")) {
                            subtitle = teacherName + "(" + shiftName + ")";
                        }
                    } else if (timetableOf.equals(Constants.TimetableOf.STUDENT)) {
                        if (className != null && className != "" && shiftName != null && !shiftName.equals("")) {
                            subtitle = className + "(" + shiftName + ")";
                        }
                    }

                    break;
            }

            Utility.SetToolbar(context, "Timetable", subtitle);

          /*  getSupportActionBar().setTitle("Timetable");
            getSupportActionBar().setSubtitle(subtitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);*/
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //   if (totalshifts > 0) {
        //  getMenuInflater().inflate(R.menu.timetable_menu, menu);
        //}
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.changeShift:
                ChooseShift();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void SetOnClickOnAppbarTitle() {
        // if (totalshifts > 0) {
        /*findViewById(R.id.toolbar).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ChooseShift();
            }
        });*/
        //  }
    }

    public void SetDefaultTab() {
        final int index = GetDayOfWeek();

        viewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(index);
            }
        }, 10);
    }

    public int GetDayOfWeek() {
        switch (cal.get(Calendar.DAY_OF_WEEK)) {
            case 2:
                weekday = 0;
                break;
            case 3:
                weekday = 1;
                break;
            case 4:
                weekday = 2;
                break;
            case 5:
                weekday = 3;
                break;
            case 6:
                weekday = 4;
                break;
            case 7:
                weekday = 5;
                break;
            default:
                weekday = 0;
                break;
        }
        return weekday;
    }
}
