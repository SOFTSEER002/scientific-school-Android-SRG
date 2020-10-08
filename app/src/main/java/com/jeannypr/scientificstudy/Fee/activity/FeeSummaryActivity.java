package com.jeannypr.scientificstudy.Fee.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.ChildModel;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Fee.adapter.FeeSummaryAdapter;
import com.jeannypr.scientificstudy.Fee.api.FeeService;
import com.jeannypr.scientificstudy.Fee.model.FeeSummaryBean;
import com.jeannypr.scientificstudy.Fee.model.FeeSummaryModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeeSummaryActivity extends AppCompatActivity {
    private Context context;
    private UserPreference userPref;
    private UserModel userData;
    ChildModel selectedChild;
    FeeService feeService;
    ArrayList<FeeSummaryModel> feeSummaryDataSet;
    FeeSummaryAdapter adapter;
    LayoutInflater inflater;
    // Integer studentId;
    RecyclerView feeInstallmentsListContainer;
    LinearLayout noRecord;
    private ProgressBar pb;
    private int studentId;
    private String studentName;
    private TextView noRecordMsg;
    int installmentId, paymentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        studentId = getIntent().getIntExtra("studentId", -1);

        setContentView(R.layout.activity_fee_summary);

        userPref = UserPreference.getInstance(context);
        userData = userPref.getUserData();
        selectedChild = userPref.getSelectedChild();
        feeService = new DataRepo<>(FeeService.class, context).getService();
        inflater = LayoutInflater.from(context);
        studentName = selectedChild.Name;

        noRecord = findViewById(R.id.noRecord);
        noRecordMsg = findViewById(R.id.noRecordMsg);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, "Fee Summary", studentName);

        feeInstallmentsListContainer = findViewById(R.id.feeInstallmentsList);
        pb = findViewById(R.id.progressBar);

        feeSummaryDataSet = new ArrayList<>();
        adapter = new FeeSummaryAdapter(this, feeSummaryDataSet, new FeeSummaryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FeeSummaryModel model) {
                if (paymentId == 0) {
                    Toast.makeText(context, "There is no payment for this installment!", Toast.LENGTH_LONG).show();

                } else {
                    Intent feeDetailIntent = new Intent(context, FeeInstallmentDetailActivity.class);
                    feeDetailIntent.putExtra("installmentId", installmentId);
                    feeDetailIntent.putExtra("paymentId", paymentId);
                    feeDetailIntent.putExtra("installmentTitle", model.InstallmentTitle);
                    context.startActivity(feeDetailIntent);
                }
            }

            @Override
            public void showSearchMsg(Boolean isDataFound) {
                if (isDataFound) {
                    feeInstallmentsListContainer.setVisibility(View.VISIBLE);
                    findViewById(R.id.noRecordRow).setVisibility(View.GONE);

                } else {
                    feeInstallmentsListContainer.setVisibility(View.GONE);
                    findViewById(R.id.noRecordRow).setVisibility(View.VISIBLE);

                    ((ImageView) findViewById(R.id.noRecordIc)).setImageResource(R.drawable.ic_search);
                    ((TextView) findViewById(R.id.noRecordMsg)).setText(R.string.noResultFound);
                    ((TextView) findViewById(R.id.noRecordMsg2)).setText(R.string.noResultFoundDesc);
                }
            }
        });
        feeInstallmentsListContainer.setAdapter(adapter);
        feeInstallmentsListContainer.setLayoutManager(new LinearLayoutManager(this));

        pb.setVisibility(View.VISIBLE);
        feeService.GetFeeSummary(studentId, userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<FeeSummaryBean>() {
            @Override
            public void onResponse(Call<FeeSummaryBean> call, Response<FeeSummaryBean> response) {
                FeeSummaryBean feeSummaryBean = response.body();
                if (feeSummaryBean != null) {

                    if (feeSummaryBean.data != null) {
                        if (feeSummaryBean.rcode == Constants.Rcode.OK) {

                            for (FeeSummaryModel model : feeSummaryBean.data.installments) {
                                feeSummaryDataSet.add(model);

                                installmentId = model.FeeinstallmentId;
                                paymentId = model.Id;
                            }

                        } else if (feeSummaryBean.rcode == Constants.Rcode.NORECORDS) {
                            noRecord.setVisibility(View.VISIBLE);
                            noRecordMsg.setText("No record found");

                        } else {
                            Toast.makeText(context, "Fee installment's summary could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(context, "Fee installment's summary could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                }
                adapter.notifyDataSetChanged();

                if (getIntent().hasExtra(Constants.IS_FEE_PAID))
                    filterInstallments(getIntent().getBooleanExtra(Constants.IS_FEE_PAID, false));
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<FeeSummaryBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
            }
        });
    }


    private void filterInstallments(Boolean isFeePaid) {
//        if(!isFeePaid.equals(""))
        adapter.getFilter().filter(isFeePaid.toString());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
