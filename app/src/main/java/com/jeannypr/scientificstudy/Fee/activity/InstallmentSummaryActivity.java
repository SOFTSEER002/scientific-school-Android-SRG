package com.jeannypr.scientificstudy.Fee.activity;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Fee.adapter.InstallmentSummaryAdapter;
import com.jeannypr.scientificstudy.Fee.api.FeeService;
import com.jeannypr.scientificstudy.Fee.model.InstallmentSummaryBean;
import com.jeannypr.scientificstudy.Fee.model.InstallmentSummaryModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InstallmentSummaryActivity extends AppCompatActivity implements View.OnClickListener {

    private FeeService feeService;
    private Context context;
    UserModel userData;
    List<InstallmentSummaryModel> installmentSummaryModels;
    RecyclerView installmentSummary;
    InstallmentSummaryAdapter adapter;
    ProgressBar progressBar;
    TextView routeLbl, totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        setContentView(R.layout.activity_installment_summary);
        userData = UserPreference.getInstance(context).getUserData();
        feeService = new DataRepo<>(FeeService.class, context).getService();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, "Installment Summary", "");
        /*if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Installment Summary");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }*/

        installmentSummary = findViewById(R.id.installmentSummary);
        progressBar = findViewById(R.id.progressBar);
        // routeLbl = findViewById(R.id.routeLbl);
        totalAmount = findViewById(R.id.totalAmount);

        installmentSummaryModels = new ArrayList<>();
        adapter = new InstallmentSummaryAdapter(this, installmentSummaryModels);
        installmentSummary.setAdapter(adapter);

        loadData();
    }

    @Override
    public void onClick(View v) {

    }


    private void loadData() {
        // showLoader("Loading data. Please wait...");
        progressBar.setVisibility(View.VISIBLE);
        feeService.GetInstallmentSummary(userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<InstallmentSummaryBean>() {
            @Override
            public void onResponse(Call<InstallmentSummaryBean> call, Response<InstallmentSummaryBean> response) {
                InstallmentSummaryBean installmentSummaryBean = response.body();
                if (installmentSummaryBean != null) {

                    if (installmentSummaryBean.rcode == Constants.Rcode.OK) {
                        installmentSummaryModels.clear();
                        long total = 0;
                        for (InstallmentSummaryModel installment : installmentSummaryBean.data) {
                            installmentSummaryModels.add(installment);
                            try {
                                total += Integer.parseInt(installment.Amount);
                            } catch (NumberFormatException ex) {
                                ex.printStackTrace();
                            }
                        }

                        adapter.notifyDataSetChanged();
                        totalAmount.setText(" Installment Total(Rs): " + String.valueOf(total));

                    } else {
                        Toast.makeText(context, "Installment Summary could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }
//                hideLoader();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<InstallmentSummaryBean> call, Throwable t) {
                // hideLoader();
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, "Installment Summary could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
