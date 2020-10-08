package com.jeannypr.scientificstudy.Fee.activity;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Fee.adapter.ConsolidatedAdapter;
import com.jeannypr.scientificstudy.Fee.api.FeeService;
import com.jeannypr.scientificstudy.Fee.model.ConsolidatedDuesBean;
import com.jeannypr.scientificstudy.Fee.model.ConsolidatedDuesModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConsolidatedDuesActivity extends AppCompatActivity {
    private FeeService feeService;
    Context context;
    Context appContext;
    UserModel userData;
    List<ConsolidatedDuesModel> dataSet;
    ConsolidatedAdapter adapter;
    RecyclerView listContainer;
    private ProgressBar pb;

    private int selectedFeeCategory, fromInstallment, toInstallment, totalExpected, totalPaid, totalDue;
    String fromInstallmentTitle, toInstallmentTitle, feeCategoryTitle;
    LinearLayout totalExpectedContainer, totalPaidContainer, totalDueContainer, noRecord;
    TextView totalexpectedAmount, totalPaidAmount, totalDueAmount, txtFromInstallment, txtToInstallment, noRecordMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        appContext = getApplicationContext();
        setContentView(R.layout.activity_consolidated_dues);
        userData = UserPreference.getInstance(context).getUserData();
        feeService = new DataRepo<>(FeeService.class, context).getService();

        selectedFeeCategory = getIntent().getIntExtra("feeCategoryId", 0);
        fromInstallment = getIntent().getIntExtra("fromInstallmentId", -1);
        toInstallment = getIntent().getIntExtra("toInstallmentId", -1);

        fromInstallmentTitle = getIntent().getStringExtra("fromInstallmentTitle");
        toInstallmentTitle = getIntent().getStringExtra("toInstallmentTitle");
        feeCategoryTitle = getIntent().getStringExtra("feeCategoryTitle");

        Toolbar toolbar = (Toolbar) findViewById(R.id.consolidatedDuesToolbar);
        setSupportActionBar(toolbar);
        String title = feeCategoryTitle.equals("") ? "Consolidated Dues" : feeCategoryTitle;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        listContainer = findViewById(R.id.reyclerview_consolidated_dues_list);
        txtFromInstallment = findViewById(R.id.fromInstallment);
        txtToInstallment = findViewById(R.id.toInstallment);
        pb = findViewById(R.id.progressBar);
        noRecord = findViewById(R.id.noRecord);
        noRecordMsg = findViewById(R.id.noRecordMsg);

        txtFromInstallment.setText(fromInstallmentTitle);
        txtToInstallment.setText(toInstallmentTitle);

        dataSet = new ArrayList<>();
        adapter = new ConsolidatedAdapter(context, dataSet);
        listContainer.setAdapter(adapter);
        listContainer.setLayoutManager(new LinearLayoutManager(this));

        LoadData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    public void LoadData() {
        totalExpectedContainer = findViewById(R.id.totalExpectedContainer);
        totalexpectedAmount = findViewById(R.id.totalexpectedAmount);
        totalPaidContainer = findViewById(R.id.totalPaidContainer);
        totalPaidAmount = findViewById(R.id.totalPaidAmount);
        totalDueContainer = findViewById(R.id.totalDueContainer);
        totalDueAmount = findViewById(R.id.totalDueAmount);

        pb.setVisibility(View.VISIBLE);

        feeService.GetConsolidatedDues(userData.getAcademicyearId(), selectedFeeCategory,
                fromInstallment, toInstallment, userData.getSchoolId())
                .enqueue(new Callback<ConsolidatedDuesBean>() {
                    @Override
                    public void onResponse(Call<ConsolidatedDuesBean> call, Response<ConsolidatedDuesBean> response) {
                        ConsolidatedDuesBean res = response.body();

                        totalDue = 0;
                        totalExpected = 0;
                        totalPaid = 0;

                        if (res != null) {
                            if (res.rcode == Constants.Rcode.OK) {

                                for (ConsolidatedDuesModel datum : res.data) {
                                    dataSet.add(datum);
                                }
                                adapter.notifyDataSetChanged();

                                for (ConsolidatedDuesModel model : dataSet) {
                                    totalDue += model.getDue();
                                    totalExpected += model.getExpected();
                                    totalPaid += model.getPaid();
                                }
                                totalDueContainer.setVisibility(View.VISIBLE);
                                totalExpectedContainer.setVisibility(View.VISIBLE);
                                totalPaidContainer.setVisibility(View.VISIBLE);

                                String paid = String.valueOf(totalPaid);
                                String due = String.valueOf(totalDue);
                                String expected = String.valueOf(totalExpected);

                                totalPaidAmount.setText(paid);
                                totalDueAmount.setText(due);
                                totalexpectedAmount.setText(expected);


                            } else if (res.rcode == Constants.Rcode.NORECORDS) {
                                ShowNoRecordMsg();

                            } else {
                                ShowNoRecordMsg();
                                Toast.makeText(appContext, "Unable to load consolidated dues. Please try again.", Toast.LENGTH_LONG).show();
                            }
                        } else {
                           ShowNoRecordMsg();
                            Toast.makeText(appContext, "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
                        }
                        pb.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<ConsolidatedDuesBean> call, Throwable t) {
                        pb.setVisibility(View.GONE);
                        Toast.makeText(appContext, "Unable to load consolidated dues. Please try again.", Toast.LENGTH_LONG).show();
                    }

                });
    }

    private void ShowNoRecordMsg(){
        noRecord.setVisibility(View.VISIBLE);
        noRecordMsg.setText("No record found.");
    }

}
