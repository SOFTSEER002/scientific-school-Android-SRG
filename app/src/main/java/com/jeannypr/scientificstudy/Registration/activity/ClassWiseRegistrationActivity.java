package com.jeannypr.scientificstudy.Registration.activity;

import android.content.Context;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.DropDownModel;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Base.adapter.DropDownAdapter;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Registration.adapter.ClassWiseRegistrationAdapter;
import com.jeannypr.scientificstudy.Registration.api.RegistrationService;
import com.jeannypr.scientificstudy.Registration.model.ClassWiseRegistrationBean;
import com.jeannypr.scientificstudy.Registration.model.ClassWiseRegistrationModel;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.ActivityClassWiseRegistrationBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClassWiseRegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    RegistrationService registrationService;
    UserModel userData;
    ArrayList<ClassWiseRegistrationModel> registrationList;
    ActivityClassWiseRegistrationBinding binding;
    private ClassWiseRegistrationAdapter adapter;
    private RecyclerView registrationContainer;
    private LinearLayout noRecord;
    TextView noRecordMsg, totalClassesTxt, totalRegTxt, totalAdmTxt, totalFeeTxt, analyticsTxt, sortTxt, searchTxt;
    private ProgressBar pb, pb_bs;
    ConstraintLayout bottomSheet;
    BottomSheetBehavior bottomSheetBehavior;
    ImageView icCross_bSheet, analyticsIc, searchIc, sortIc;
    AnyChartView chartView;
    SearchView searchView;
    ConstraintLayout analyticsCell, sortCell, searchCell, filterCell;

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_class_wise_registration);
        context = this;

        userData = UserPreference.getInstance(context).getUserData();
        registrationService = new DataRepo<>(RegistrationService.class, context).getService();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, "Class Wise Registration", "");

        noRecord = findViewById(R.id.noRecord);
        noRecordMsg = findViewById(R.id.noRecordMsg);
        pb = findViewById(R.id.progressBar);
        totalClassesTxt = findViewById(R.id.totalClasses);
        totalRegTxt = findViewById(R.id.totalReg);
        totalAdmTxt = findViewById(R.id.totalAdm);
        totalFeeTxt = findViewById(R.id.totalFee);
        registrationContainer = findViewById(R.id.registrationContainer);

        bottomSheet = findViewById(R.id.bottomSheet);
        pb_bs = findViewById(R.id.pb_bs);

        analyticsIc = findViewById(R.id.analyticsIc);
        /*analyticsIc.setOnClickListener(this);*/

        analyticsTxt = findViewById(R.id.analytics);
        sortTxt = findViewById(R.id.sort);
        sortIc = findViewById(R.id.sortIc);

        searchTxt = findViewById(R.id.search);
        searchIc = findViewById(R.id.searchIc);
        /*searchIc.setOnClickListener(this);*/

        icCross_bSheet = findViewById(R.id.icCross);
        icCross_bSheet.setOnClickListener(this);
        chartView = findViewById(R.id.chartView);

        searchView = findViewById(R.id.searchView);
        searchView.setOnClickListener(this);

        analyticsCell = findViewById(R.id.analyticsCell);
        analyticsCell.setOnClickListener(this);

        sortCell = findViewById(R.id.sortCell);
        sortCell.setOnClickListener(this);

        searchCell = findViewById(R.id.searchCell);
        searchCell.setOnClickListener(this);

        filterCell = findViewById(R.id.filterCell);
        filterCell.setOnClickListener(this);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        registrationList = new ArrayList<>();
        adapter = new ClassWiseRegistrationAdapter(this, registrationList);
        registrationContainer.setAdapter(adapter);
        registrationContainer.setLayoutManager(new LinearLayoutManager(this));

        ShowRegistrationRecord();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void ShowRegistrationRecord() {
        pb.setVisibility(View.VISIBLE);

        registrationService.GetAddmissionSummary(userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<ClassWiseRegistrationBean>() {
            @Override
            public void onResponse(Call<ClassWiseRegistrationBean> call, Response<ClassWiseRegistrationBean> response) {
                ClassWiseRegistrationBean resp = response.body();

                if (resp != null) {
                    long totalAdm = 0, totalReg = 0, totalFee = 0;

                    if (resp.rcode == Constants.Rcode.OK) {
                        if (resp.data != null) {
                            registrationList.clear();

                            int size = resp.data.size();
                            if (size > 0) {

                                for (ClassWiseRegistrationModel collection : resp.data) {
                                    registrationList.add(collection);

                                    bottomSheet.setVisibility(View.VISIBLE);
                                    bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                                    ArrayList<String> categories = new ArrayList<>();
                                    categories.add(Constants.AnalyticsCategoryRegistration.ADMISSION);
                                    categories.add(Constants.AnalyticsCategoryRegistration.REGISTRATION);
                                    InitializeBottomSheet(bottomSheet, bottomSheetBehavior, context, registrationList, categories);

                                    totalAdm += collection.TotalPermanentAdmission == -1 ? 0 : collection.TotalPermanentAdmission;
                                    totalFee += collection.TotalRegistrationFees == -1 ? 0 : collection.TotalRegistrationFees;
                                    totalReg += collection.TotalRegistration == -1 ? 0 : collection.TotalRegistration;
                                }

                            } else {
                                noRecord.setVisibility(View.VISIBLE);
                                noRecordMsg.setText(R.string.noRecordMsg);
                            }
                            adapter.notifyDataSetChanged();
                            totalAdmTxt.setText(String.valueOf(totalAdm));
                            totalClassesTxt.setText(String.valueOf(resp.data.size()));
                            totalFeeTxt.setText(String.valueOf(totalFee));
                            totalRegTxt.setText(String.valueOf(totalReg));
                        }

                    } else if (resp.rcode == Constants.Rcode.NORECORDS) {
                        noRecord.setVisibility(View.VISIBLE);
                        noRecordMsg.setText(R.string.noRecordMsg);

                    } else {
                        Toast.makeText(context, getResources().getString(R.string.noRecordMsg), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, getResources().getString(R.string.noRecordMsg), Toast.LENGTH_LONG).show();
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ClassWiseRegistrationBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                Toast.makeText(context, "Class registration report could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.analyticsCell:
               /* analyticsIc.setColorFilter(R.color.colorAccent);
                analyticsTxt.setTextColor(Color.parseColor("#5e5bca"));*/
                if (registrationList != null && registrationList.size() > 0) {
                    toggleBottomSheet();
                }
                break;
            case R.id.searchCell:
                /*searchIc.setColorFilter(R.color.colorAccent);
                searchTxt.setTextColor(Color.parseColor("#5e5bca"));*/

                searchView.setVisibility(View.VISIBLE);
                searchView.setIconifiedByDefault(false);
                analyticsCell.setVisibility(View.GONE);
                sortCell.setVisibility(View.GONE);
                searchCell.setVisibility(View.GONE);
                filterCell.setVisibility(View.GONE);
                break;

            case R.id.searchView:

                searchView.setIconifiedByDefault(true);
                searchView.setVisibility(View.GONE);
                analyticsCell.setVisibility(View.VISIBLE);
                sortCell.setVisibility(View.VISIBLE);
                searchCell.setVisibility(View.VISIBLE);
                filterCell.setVisibility(View.VISIBLE);
                break;

         /*   case R.id.analyticsIc:
            case R.id.icCross:
                // onclick action change color of icon and label to accent and vice versa on
                if (registrationList != null && registrationList.size() > 0) {
                    toggleBottomSheet();
                }
                break;
            case R.id.searchIc:

                searchView.setVisibility(View.VISIBLE);
                searchView.setIconifiedByDefault(false);
                sortIc.setVisibility(View.GONE);
                sortTxt.setVisibility(View.GONE);
                searchTxt.setVisibility(View.GONE);
                searchIc.setVisibility(View.GONE);
                analyticsIc.setVisibility(View.GONE);
                analyticsTxt.setVisibility(View.GONE);
                break;

            case R.id.searchView:

                searchView.setIconifiedByDefault(true);
                searchView.setVisibility(View.GONE);
                sortIc.setVisibility(View.VISIBLE);
                sortTxt.setVisibility(View.VISIBLE);
                searchTxt.setVisibility(View.VISIBLE);
                searchIc.setVisibility(View.VISIBLE);
                analyticsIc.setVisibility(View.VISIBLE);
                analyticsTxt.setVisibility(View.VISIBLE);
                break;*/
        }
    }

   /* public void InitializeBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        List<String> categories = new ArrayList<>();
        categories.add("Registration");
        categories.add("Admission");

        RecyclerView container_bs_left = findViewById(R.id.container_bs_left);
        BottomSheetLeftSectionAdapter leftSectionAdapter = new BottomSheetLeftSectionAdapter(context, categories);
        container_bs_left.setAdapter(leftSectionAdapter);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                Log.e("onStateChanged", "");
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
                Log.e("onSlide", "");
            }
        });
    }*/

    public void toggleBottomSheet() {
        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    public void InitializeBottomSheet(final ConstraintLayout bottomSheet, BottomSheetBehavior bottomSheetBehavior, Context mContext,
                                      final ArrayList<ClassWiseRegistrationModel> dataSet, ArrayList<String> categories) {

        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        /*List<String> categories = new ArrayList<>();
        categories.add("Registration");
        categories.add("Admission");
*/
        /*RecyclerView container_bs_left = bottomSheet.findViewById(R.id.container_bs_left);
        BottomSheetLeftSectionAdapter leftSectionAdapter = new BottomSheetLeftSectionAdapter(mContext, categories,
                new BottomSheetLeftSectionAdapter.ItemListner() {
                    @Override
                    public void onClickCategory(String category) {
                        ShowChart(bottomSheet, "Class wise admission", "Class", "Admission", dataSet);
                    }
                });
        container_bs_left.setAdapter(leftSectionAdapter);*/

        Spinner categorySpn = bottomSheet.findViewById(R.id.category_bs);
        final ArrayList<DropDownModel> categoryList = new ArrayList<>();
        DropDownModel defaultLbl = new DropDownModel();
        defaultLbl.setText("Choose category");
        defaultLbl.setId(0);
        categoryList.add(defaultLbl);

        int count = 1;
        for (String category : categories) {
            DropDownModel obj = new DropDownModel();
            obj.setText(category);
            obj.setId(count);
            categoryList.add(obj);
            count++;
        }

        DropDownAdapter categoryAdapter = new DropDownAdapter(mContext, R.layout.row_spinner, categoryList);
        categorySpn.setAdapter(categoryAdapter);
        categorySpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //    chartView.clear();
                AnyChartView chartView = findViewById(R.id.chartView);

                chartView.setProgressBar(pb_bs);
                chartView.setZoomEnabled(true);


                DropDownModel selectedCategory = categoryList.get(position);
                List<DataEntry> dataEntry = new ArrayList<>();

                switch (selectedCategory.getText()) {

                    case Constants.AnalyticsCategoryRegistration.ADMISSION:
                        dataEntry.clear();

                        for (ClassWiseRegistrationModel obj : registrationList) {
                            dataEntry.add(new ValueDataEntry(obj.ClassName, obj.TotalPermanentAdmission));
                        }
                        Utility.ShowChart(chartView, "Class wise admission", "Class", "Admission", dataEntry);
                        break;

                    case Constants.AnalyticsCategoryRegistration.REGISTRATION:
                        dataEntry.clear();

                        for (ClassWiseRegistrationModel obj : registrationList) {
                            dataEntry.add(new ValueDataEntry(obj.ClassName, obj.TotalRegistration));
                        }
                        Utility.ShowChart(chartView, "Class wise registration", "Class", "Registration", dataEntry);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                Log.e("onStateChanged", "");
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
                Log.e("onSlide", "");
            }
        });
    }
}