package com.jeannypr.scientificstudy.Fee.activity;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.ChildModel;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Fee.adapter.FeeInstallMentDetailAdapter;
import com.jeannypr.scientificstudy.Fee.api.FeeService;
import com.jeannypr.scientificstudy.Fee.model.FeeInstallmentDetailBean;
import com.jeannypr.scientificstudy.Fee.model.FeeInstallmentDetailModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeeInstallmentDetailActivity extends AppCompatActivity {
    private Context context;
    private int installmentId, paymentId;
    private UserPreference userPref;
    private UserModel userData;
    ChildModel selectedChild;
    FeeService feeService;
    LayoutInflater inflater;
    RecyclerView feeInstallmentDetailContainer;
    ArrayList<FeeInstallmentDetailModel> installmentDetailDataSet;
    ProgressBar pb;
    FeeInstallMentDetailAdapter adapter;
    private String installmentTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        setContentView(R.layout.activity_fee_detail);
        installmentId = getIntent().getIntExtra("installmentId", -1);
        paymentId = getIntent().getIntExtra("paymentId", -1);
        installmentTitle = getIntent().getStringExtra("installmentTitle");

        userPref = UserPreference.getInstance(context);
        userData = userPref.getUserData();
        selectedChild = userPref.getSelectedChild();
        feeService = new DataRepo<>(FeeService.class, context).getService();
        inflater = LayoutInflater.from(context);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, installmentTitle, "");
        /*if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(installmentTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }*/

        feeInstallmentDetailContainer = findViewById(R.id.feeInstallmentDetailContainer);
        pb = findViewById(R.id.progressBar);

        installmentDetailDataSet = new ArrayList<>();
        adapter = new FeeInstallMentDetailAdapter(context, installmentDetailDataSet);
        feeInstallmentDetailContainer.setAdapter(adapter);
        feeInstallmentDetailContainer.setLayoutManager(new LinearLayoutManager(this));


        pb.setVisibility(View.VISIBLE);

        feeService.GetFeeInstallmentDetail(paymentId, installmentId, userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<FeeInstallmentDetailBean>() {
            @Override
            public void onResponse(Call<FeeInstallmentDetailBean> call, Response<FeeInstallmentDetailBean> response) {
                FeeInstallmentDetailBean feeInstallmentDetailBean = response.body();

                if (feeInstallmentDetailBean != null) {
                    installmentDetailDataSet.clear();

                    if (feeInstallmentDetailBean.data != null) {
                        if (feeInstallmentDetailBean.rcode == Constants.Rcode.OK) {

                            for (FeeInstallmentDetailModel model : feeInstallmentDetailBean.data.paymentStructure.FeeType) {
                                installmentDetailDataSet.add(model);
                            }

                        } else if (feeInstallmentDetailBean.rcode == Constants.Rcode.NORECORDS) {
                            Toast.makeText(context, "No detail found...", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(context, "Fee installment's summary could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                        }
                    }

                } else {
                    Toast.makeText(context, "Fee installment's details could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                }
                adapter.notifyDataSetChanged();
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<FeeInstallmentDetailBean> call, Throwable t) {
                Toast.makeText(context, "Server error...", Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

   /* public void InflateUI() {
        for (final FeeInstallmentDetailModel feeInstallmentDetailModel : installmentDetailDataSet) {
            final ConstraintLayout view = (ConstraintLayout) inflater.inflate(R.layout.row_fee_installment_detail, feeInstallmentDetailContainer, false);

            final TextView feeType = view.findViewById(R.id.feeType);
            final TextView payableAmount = view.findViewById(R.id.payableAmount);
            final TextView paidAmount = view.findViewById(R.id.paidAmount);
            final TextView dueAmount = view.findViewById(R.id.dueAmount);
            final TextView discount = view.findViewById(R.id.discount);
            final ConstraintLayout payment = view.findViewById(R.id.payment);
            final ConstraintLayout row_Container = view.findViewById(R.id.row_Container);
            final TextView payAmount = view.findViewById(R.id.payAmount);

            // final ImageView installmentStatusImg = view.findViewById(R.id.installmentStatusImg);

            String totalPayable = feeInstallmentDetailModel.AmountPayable.toString();
            String totalPaid = feeInstallmentDetailModel.AmountPaid.toString();
            String totalDues = feeInstallmentDetailModel.AmountDue.toString();
            String totalDiscount = feeInstallmentDetailModel.Discount.toString();

            feeType.setText(feeInstallmentDetailModel.Title == null ? "" : feeInstallmentDetailModel.Title);
            payableAmount.setText(feeInstallmentDetailModel.AmountPayable == null ? "0" : totalPayable);
            paidAmount.setText(feeInstallmentDetailModel.AmountPaid == null ? "0" : totalPaid);
            dueAmount.setText(feeInstallmentDetailModel.AmountDue == null ? "0" : totalDues);
            discount.setText(feeInstallmentDetailModel.Discount == null ? "0" : totalDiscount);


            int due = feeInstallmentDetailModel.AmountDue;
            int paid = feeInstallmentDetailModel.AmountPaid;
            int payable = feeInstallmentDetailModel.AmountPayable;
            if (due == 0) {
                //installmentStatusImg.setImageResource(R.drawable.paid);
                row_Container.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.due_collection_green_bg));
                payment.setBackground(context.getResources().getDrawable(R.color.green_light));
                payAmount.setText("Paid");
                payAmount.setTextColor(context.getResources().getColor(R.color.mGreen));
            } else if (due < payable) {
                // installmentStatusImg.setImageResource(R.drawable.due);
                row_Container.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.daily_collection_red_bg));
                payment.setBackground(context.getResources().getDrawable(R.color.rea_light));
                payAmount.setText("Dues");
                payAmount.setTextColor(context.getResources().getColor(R.color.mRed));
            } else {
                // installmentStatusImg.setImageResource(R.drawable.overdue);
                row_Container.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.daily_collection_red_bg));
                payment.setBackground(context.getResources().getDrawable(R.color.rea_light));
                payAmount.setText("Overdue");
                payAmount.setTextColor(context.getResources().getColor(R.color.mRed));
            }
            feeInstallmentDetailContainer.addView(view);
        }
    }*/
}
