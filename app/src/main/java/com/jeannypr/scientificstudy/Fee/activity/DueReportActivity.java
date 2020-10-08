package com.jeannypr.scientificstudy.Fee.activity;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import com.google.android.material.button.MaterialButton;
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
import com.jeannypr.scientificstudy.Base.activity.BaseActivity;
import com.jeannypr.scientificstudy.Fee.adapter.ClassWiseDuesReportAdapter;
import com.jeannypr.scientificstudy.Fee.adapter.StudentWiseDueFeeReportAdapter;
import com.jeannypr.scientificstudy.Fee.adapter.StudentWiseDueVoucherAdapter;
import com.jeannypr.scientificstudy.Fee.api.FeeService;
import com.jeannypr.scientificstudy.Fee.model.ClassWiseDuesBean;
import com.jeannypr.scientificstudy.Fee.model.ClassWiseDuesModel;
import com.jeannypr.scientificstudy.Fee.model.FeeDueModel;
import com.jeannypr.scientificstudy.Fee.model.StudentWiseDueBean;
import com.jeannypr.scientificstudy.Fee.model.VoucherDueModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Student.activity.StudentProfileActivity;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.ActivityClassWiseDueReportBinding;
import com.jeannypr.scientificstudy.databinding.ActivityDueReportBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DueReportActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    ActivityDueReportBinding studentWiseBinding;
    ActivityClassWiseDueReportBinding classWiseBinding;
    String reportType;
    FeeService feeService;
    UserModel userData;
    ArrayList<ClassWiseDuesModel> dueFeeData;
    ArrayList<FeeDueModel> feeDuesList;
    ArrayList<VoucherDueModel> voucherDueList;
    RecyclerView feeDue, voucherDue;
    ClassWiseDuesReportAdapter adapter;
    StudentWiseDueFeeReportAdapter feeDuesAdapter;
    StudentWiseDueVoucherAdapter voucherAdapter;
    ProgressBar pb;
    int classId, fromInstallmentId, toInstallmentId, feeGroupId, feeCategoryId, studentId;
    String className, fromInstallmentName, toInstallmentName, studentName;
    boolean voucher, lateFee;
    LinearLayout noRecord;
    TextView noRecordMsg, totalFeeAmount, totalFeeItems, fromInstallmentLbl, toInstallmentLabl, totalVouchers, totalVoucherAmount, voucherDuesLbl;
    ConstraintLayout voucherDuesHeader, feeDuesHeader, parent;
    MaterialButton notificationBtn, chatBtn, smsBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            reportType = bundle.getString("reportType");
            className = bundle.getString("className");
            toInstallmentName = bundle.getString("toInstallmentName");
            fromInstallmentName = bundle.getString("fromInstallmentName");
            voucher = bundle.getBoolean("voucher", voucher);
            lateFee = bundle.getBoolean("latefee", lateFee);
            classId = bundle.getInt("classid", 0);
            fromInstallmentId = bundle.getInt("fromInstallment", 0);
            toInstallmentId = bundle.getInt("toInstallMent", 0);

            if (reportType.equals(Constants.DueReportTypes.STUDENTWISE)) {
                studentId = bundle.getInt("studentid", 0);
                studentName = bundle.getString("studentName");
            }
        }

        switch (reportType) {

            case Constants.DueReportTypes.CLASSWISE:
                classWiseBinding = DataBindingUtil.setContentView(this, R.layout.activity_class_wise_due_report);
                SetToolbar(className);
                break;

            case Constants.DueReportTypes.STUDENTWISE:
                studentWiseBinding = DataBindingUtil.setContentView(this, R.layout.activity_due_report);
                SetToolbar(className + " (" + studentName + ")");
                break;
        }

        userData = UserPreference.getInstance(context).getUserData();
        feeService = new DataRepo<>(FeeService.class, context).getService();

        parent = findViewById(R.id.parent);
        pb = findViewById(R.id.progressBar);
        noRecord = findViewById(R.id.noRecord);
        noRecordMsg = findViewById(R.id.noRecordMsg);

        totalFeeAmount = findViewById(R.id.totalFeeAmount);
        totalFeeItems = findViewById(R.id.totalFeeItems);
        totalVoucherAmount = findViewById(R.id.totalVoucherDue);
        totalVouchers = findViewById(R.id.totalVouchers);

        //toInstallmentLabl = findViewById(R.id.toInstallmentLabl);
        fromInstallmentLbl = findViewById(R.id.fromInstallmentLbl);
        fromInstallmentLbl.setText("From: " + fromInstallmentName + ", To:  " + toInstallmentName);
        // toInstallmentLabl.setText("  To  " + toInstallmentName);

        voucherDuesHeader = findViewById(R.id.voucherDuesHeader);
        feeDuesHeader = findViewById(R.id.feeDuesHeader);

        feeDue = findViewById(R.id.feeDue);
        feeDue.setLayoutManager(new LinearLayoutManager(this));
        voucherDue = findViewById(R.id.voucherDue);

        ShowDueFeeRecord();
        if (reportType.equals(Constants.DueReportTypes.STUDENTWISE)) {
            if (!voucher) {
                ConstraintSet set = new ConstraintSet();
                set.clone(parent);
                set.connect(feeDue.getId(), ConstraintSet.BOTTOM, parent.getId(), ConstraintSet.BOTTOM);
                set.applyTo(parent);
            } else {
                voucherDue.setVisibility(View.VISIBLE);
                voucherDuesHeader.setVisibility(View.VISIBLE);
            }
        }

       /* notificationBtn.setOnClickListener(this);
        chatBtn.setOnClickListener(this);
        smsBtn.setOnClickListener(this);*/

    }

    private void SetToolbar(String subTitle) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, reportType + " " + "Due Report", subTitle);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void ShowDueFeeRecord() {
        switch (reportType) {

            case Constants.DueReportTypes.CLASSWISE:

                dueFeeData = new ArrayList<>();
                Boolean canSeeContactNo = UserPreference.getInstance(context).getSchoolData().getCanSeeContactNumber();
                if(userData.getRoleTitle().equals(Constants.Role.ADMIN))canSeeContactNo = true;

                adapter = new ClassWiseDuesReportAdapter(this, dueFeeData,canSeeContactNo, new ClassWiseDuesReportAdapter.OnItemClickListener() {
                    @Override
                    public void onStudentClick(ClassWiseDuesModel studentModel) {
                        Intent studentProfileIntent = new Intent(context, StudentProfileActivity.class);
                        if (studentModel != null) {
                            // studentProfileIntent.putExtra("studentId", studentModel.id);
                            studentProfileIntent.putExtra("classId", classId);
                            studentProfileIntent.putExtra("studentName", studentModel.getStudentName());
                            studentProfileIntent.putExtra("className", className);
                            studentProfileIntent.putExtra("imgPath", "");
                            studentProfileIntent.putExtra("admNo", studentModel.getAdmissionNumber());
                            studentProfileIntent.putExtra("parentUserId", studentModel.getParentUserId());
                        }
                        startActivity(studentProfileIntent);
                    }
                });
                feeDue.setAdapter(adapter);

                pb.setVisibility(View.VISIBLE);

                feeService.GetClassWiseDueFeeDetail(userData.getSchoolId(), userData.getAcademicyearId(), classId, fromInstallmentId,
                        toInstallmentId, feeCategoryId, voucher, lateFee, feeGroupId)
                        .enqueue(new Callback<ClassWiseDuesBean>() {
                            @Override
                            public void onResponse(Call<ClassWiseDuesBean> call, Response<ClassWiseDuesBean> response) {
                                ClassWiseDuesBean resp = response.body();

                                if (resp != null) {

                                    if (resp.rcode == Constants.Rcode.OK) {

                                        feeDuesHeader.setVisibility(View.VISIBLE);

                                        if (resp.data != null) {
                                            int size = resp.data.size();
                                            totalFeeItems.setText("Total Count - " + String.valueOf(size));

                                            dueFeeData.clear();

                                            long total = 0;
                                            for (ClassWiseDuesModel collection : resp.data) {
                                                dueFeeData.add(collection);

                                                try {
                                                    total += collection.getAmount();
                                                } catch (NumberFormatException ex) {
                                                    ex.printStackTrace();
                                                }

                                            }
                                            totalFeeAmount.setText("Rs. " + (String.valueOf(total)));
                                            adapter.notifyDataSetChanged();
                                        }

                                    } else if (resp.rcode == Constants.Rcode.NORECORDS) {
                                        feeDuesHeader.setVisibility(View.GONE);
                                        noRecord.setVisibility(View.VISIBLE);
                                        noRecordMsg.setText("No record found");

                                    } else {
                                        Toast.makeText(context, "No record found.", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(context, "Class wise due report could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                                }
                                pb.setVisibility(View.GONE);
                            }

                            @Override
                            public void onFailure(Call<ClassWiseDuesBean> call, Throwable t) {
                                pb.setVisibility(View.GONE);
                                Toast.makeText(context, "Class wise due report could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                            }
                        });
                break;

            case Constants.DueReportTypes.STUDENTWISE:

              /*  if (!voucher) {
                    voucherDuesHeader.setVisibility(View.GONE);
                    voucherDue.setVisibility(View.GONE);
                }*/

                feeDuesList = new ArrayList<>();
                feeDuesAdapter = new StudentWiseDueFeeReportAdapter(this, feeDuesList);
                feeDue.setAdapter(feeDuesAdapter);

                voucherDueList = new ArrayList<>();
                voucherAdapter = new StudentWiseDueVoucherAdapter(this, voucherDueList);
                voucherDue.setAdapter(voucherAdapter);
                voucherDue.setLayoutManager(new LinearLayoutManager(this));

                pb.setVisibility(View.VISIBLE);
                feeService.GetStudentWiseDueFeeDetail(studentId, userData.getSchoolId(), userData.getAcademicyearId(), classId, fromInstallmentId, toInstallmentId,
                        feeCategoryId, voucher, lateFee, feeGroupId)
                        .enqueue(new Callback<StudentWiseDueBean>() {
                            @Override
                            public void onResponse(Call<StudentWiseDueBean> call, Response<StudentWiseDueBean> response) {
                                StudentWiseDueBean resp = response.body();

                                if (resp != null) {
                                    if (resp.rcode == Constants.Rcode.OK) {
                                        if (resp.data != null) {
                                            feeDuesList.clear();
                                            voucherDueList.clear();

                                            int feeItem = 0, voucherItem = 0;
                                            long totalFeeDue = 0;
                                            long totalVoucherDue = 0;

                                            feeItem = resp.data.feeDue.size();
                                            voucherItem = resp.data.voucherDue.size();


                                            if (feeItem > 0) {
                                                for (FeeDueModel feeDueModel : resp.data.feeDue) {
                                                    feeDuesList.add(feeDueModel);

                                                    try {
                                                        totalFeeDue += feeDueModel.getTotalAmountDue();
                                                    } catch (NumberFormatException ex) {
                                                        ex.printStackTrace();
                                                    }
                                                }
                                                feeDuesAdapter.notifyDataSetChanged();
                                                totalFeeItems.setText("Installment Due -  " + String.valueOf(feeItem));

                                            } else {
                                                //feeDuesHeader.setVisibility(View.GONE);
                                                //feeDue.setVisibility(View.GONE);
                                                noRecord.setVisibility(View.VISIBLE);
                                                noRecordMsg.setText("No record for fee dues");
                                            }

                                            if (voucherItem > 0) {
                                                for (VoucherDueModel vouchar : resp.data.voucherDue) {
                                                    voucherDueList.add(vouchar);
                                                    try {
                                                        totalVoucherDue += vouchar.getTotalAmountDue();
                                                    } catch (NumberFormatException ex) {
                                                        ex.printStackTrace();
                                                    }

                                                }
                                                voucherAdapter.notifyDataSetChanged();
                                                totalVouchers.setText("Voucher Due -  " + String.valueOf(voucherItem));

                                            } else {
                                                totalVouchers.setText("Voucher Due - ");
                                               /* voucherDuesHeader.setVisibility(View.GONE);
                                                voucherDue.setVisibility(View.GONE);*/
                                                // Toast.makeText(context, "No record found.", Toast.LENGTH_LONG).show();
                                            }

                                            totalFeeAmount.setText("Rs. " + String.valueOf(totalFeeDue));
                                            totalVoucherAmount.setText("Rs. " + String.valueOf(totalVoucherDue));

                                        }

                                    } else if (resp.rcode == Constants.Rcode.NORECORDS) {

                                        noRecord.setVisibility(View.VISIBLE);
                                        noRecordMsg.setText("No record found");

                                    } else {
                                        Toast.makeText(context, "No record found", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(context, "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
                                }
                                pb.setVisibility(View.GONE);
                            }

                            @Override
                            public void onFailure(Call<StudentWiseDueBean> call, Throwable t) {
                                pb.setVisibility(View.GONE);
                                Toast.makeText(context, "Student wise due report could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                            }
                        });
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.notificationBtn:
                sendNotificationToNotifyDues();
                break;

            case R.id.chatBtn:
                sendMessageToNotifyDues();
                break;

            case R.id.smsBtn:
                sendSMSToNotifyDues();
                break;
        }
    }

    private void sendSMSToNotifyDues() {
    }

    private void sendMessageToNotifyDues() {
        /*
          On click chat button,
         call api to send message, dont redirect to chat screen.
         */

    }

    private void sendNotificationToNotifyDues() {
    }
}
