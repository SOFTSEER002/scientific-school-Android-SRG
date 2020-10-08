package com.jeannypr.scientificstudy.Fee.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.jeannypr.scientificstudy.Base.Constants;

import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Fee.adapter.DiscountStudentDetailAdapter;
import com.jeannypr.scientificstudy.Fee.api.FeeService;
import com.jeannypr.scientificstudy.Fee.model.AllowedDiscountBean;
import com.jeannypr.scientificstudy.Fee.model.DiscountPermissionBean;
import com.jeannypr.scientificstudy.Fee.model.DiscountPermissionModel;
import com.jeannypr.scientificstudy.Fee.model.DiscountStudentDetailBean;
import com.jeannypr.scientificstudy.Fee.model.FeeSummaryModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllowDiscountActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    TextView classnameLbl, noRecordMsg, helpText, nameLbl;
    Button allowBtn, checkBtn;
    EditText addNumber;
    ProgressBar pb;
    RecyclerView listContainer;
    private UserPreference userPreference;
    String addmissionNumber, studetnName, className;
    FeeService feeService;
    UserModel userModel;
    boolean isPermissinDiscount;
    private DiscountStudentDetailAdapter adapter;
    private ArrayList<FeeSummaryModel> feeSummaryModels;
    int studentId, classId, secretKey;
    LinearLayout noRecord;
    ConstraintLayout parent;

    @Override

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allow_discount);
        context = this;

        feeService = new DataRepo<>(FeeService.class, this).getService();
        userPreference = UserPreference.getInstance(context);
        userModel = userPreference.getUserData();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, "Allow Discount", "");

        checkBtn = findViewById(R.id.checkBtn);
        addNumber = findViewById(R.id.addmissionNo);
        pb = findViewById(R.id.progressBar);

        addNumber = findViewById(R.id.addmissionNo);
        noRecord = findViewById(R.id.noRecord);
        noRecordMsg = findViewById(R.id.noRecordMsg);
        helpText = findViewById(R.id.helpText);
        classnameLbl = findViewById(R.id.classnameLbl);
        nameLbl = findViewById(R.id.nameLbl);


        allowBtn = findViewById(R.id.allowdiscountBtn);
        allowBtn.setOnClickListener(this);

        checkBtn.setOnClickListener(this);
        listContainer = findViewById(R.id.list);


        feeSummaryModels = new ArrayList<FeeSummaryModel>();
        adapter = new DiscountStudentDetailAdapter(this, feeSummaryModels);
        listContainer.setAdapter(adapter);
        listContainer.setLayoutManager(new LinearLayoutManager(this));

        GiveDiscount();

    }

    private void GiveDiscount() {
        pb.setVisibility(View.VISIBLE);
        feeService.GetPermissionDiscount(userModel.getUserId(), userModel.getSchoolId()).enqueue(new Callback<AllowedDiscountBean>() {
            @Override
            public void onResponse(Call<AllowedDiscountBean> call, Response<AllowedDiscountBean> response) {
                AllowedDiscountBean resp = response.body();

                if (resp != null) {
                    if (resp.data != null) {
                        if (resp.rcode == Constants.Rcode.OK) {
                            isPermissinDiscount = resp.data.isCanGiveDiscount();
                            //isPermissinDiscount = false;
                            IsPermissionDiscount();
                        }
                    }
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<AllowedDiscountBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
            }
        });
    }

    private void GetStudentDeatil() {
        pb.setVisibility(View.VISIBLE);

        feeService.GetStudentFeeDeatil(addmissionNumber, userModel.getSchoolId(), userModel.getAcademicyearId()).enqueue(new Callback<DiscountStudentDetailBean>() {
            @Override
            public void onResponse(Call<DiscountStudentDetailBean> call, Response<DiscountStudentDetailBean> response) {
                DiscountStudentDetailBean resp = response.body();

                feeSummaryModels.clear();
                listContainer.setVisibility(View.VISIBLE);
                nameLbl.setVisibility(View.VISIBLE);
                classnameLbl.setVisibility(View.VISIBLE);

                if (resp != null) {
                    if (resp.data != null) {
                        if (resp.rcode == Constants.Rcode.OK) {

                            studentId = resp.data.StudentDetail.studentId;
                            classId = resp.data.StudentDetail.ClassId;
                            studetnName = resp.data.StudentDetail.StudentName;
                            className = resp.data.StudentDetail.ClassName;

                            nameLbl.setText(studetnName.substring(0, 1).toUpperCase() + studetnName.substring(1).toLowerCase());
                            classnameLbl.setText("Class: " + className);

                            int size = resp.data.FeeStructure.Installments.size();

                            if (size == 0) {
                                Toast.makeText(context, "The student does not have the fee structure defined!", Toast.LENGTH_SHORT).show();
                            } else {

                                for (FeeSummaryModel model : resp.data.FeeStructure.Installments) {

                                    feeSummaryModels.add(model);
                                }
                                allowBtn.setVisibility(View.VISIBLE);
                            }
                            adapter.notifyDataSetChanged();
                        }


                    } else {
                        noRecord.setVisibility(View.VISIBLE);
                        noRecordMsg.setText("This admission number does not exist.");
                        nameLbl.setVisibility(View.GONE);
                        classnameLbl.setVisibility(View.GONE);
                    }

                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DiscountStudentDetailBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                Toast.makeText(context, "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void SavePermissionDiscount(final DiscountPermissionModel input) {

        input.setClassId(classId);
        input.setStudentId(studentId);
        input.setUserId(userModel.getUserId());
        input.setSchoolId(userModel.getSchoolId());
        input.setAcademicYearId(userModel.getAcademicyearId());

        pb.setVisibility(View.VISIBLE);
        feeService.SaveDiscountPermission(input).enqueue(new Callback<DiscountPermissionBean>() {
            @Override
            public void onResponse(Call<DiscountPermissionBean> call, Response<DiscountPermissionBean> response) {

                DiscountPermissionBean resp = response.body();
                if (resp != null) {
                    if (resp.rcode == Constants.Rcode.OK) {
                        if (resp.data != null) {
                            secretKey = resp.data.SecretKey;

                            ShowPopup();

                        } else {
                            Toast.makeText(context, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(context, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }

                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DiscountPermissionBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                Toast.makeText(context, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void IsPermissionDiscount() {
        if (!isPermissinDiscount) {

            final AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle("School admin has not given you permission for this")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onSupportNavigateUp();
                            dialog.dismiss();
                        }
                    })
                    .show();
        } else {
            addNumber.setVisibility(View.VISIBLE);
            checkBtn.setVisibility(View.VISIBLE);
            helpText.setVisibility(View.VISIBLE);
        }
    }

    private void ShowPopup() {
        ConstraintLayout view = (ConstraintLayout) getLayoutInflater().inflate(R.layout.row_discount_otp, parent, false);
        final TextView sName = view.findViewById(R.id.studentName);
        final TextView clName = view.findViewById(R.id.clasName);
        final TextView addmissionNo = view.findViewById(R.id.addmissionNo);
        final TextView meassgeLbl = view.findViewById(R.id.meassgeLbl);
        final TextView otpCode = view.findViewById(R.id.otpCode);
        final TextView alerMessage = view.findViewById(R.id.alerMessage);

        listContainer.setVisibility(View.GONE);
        allowBtn.setVisibility(View.GONE);
        helpText.setVisibility(View.GONE);
        nameLbl.setVisibility(View.GONE);
        classnameLbl.setVisibility(View.GONE);

        otpCode.setText("OTP: " + String.valueOf(secretKey));
        addmissionNo.setText("Admission no: " + addmissionNumber);
        sName.setText(studetnName);
        clName.setText("Class: " + className);

        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setView(view)
                .show();


        Button positiveBtn = view.findViewById(R.id.positiveBtn);
        Button negativeBtn = view.findViewById(R.id.negativeBtn);
        negativeBtn.setVisibility(View.GONE);
        positiveBtn.setText("OK");

        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameLbl.setVisibility(View.GONE);
                classnameLbl.setVisibility(View.GONE);
                listContainer.setVisibility(View.GONE);
                allowBtn.setVisibility(View.GONE);
                helpText.setVisibility(View.VISIBLE);
                addNumber.setText("");
                dialog.dismiss();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.checkBtn:
                allowBtn.setVisibility(View.GONE);
                addmissionNumber = addNumber.getText().toString().trim();

                if (addmissionNumber != null && !addmissionNumber.equals("")) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                    listContainer.setVisibility(View.GONE);
                    noRecord.setVisibility(View.GONE);

                    GetStudentDeatil();

                } else {
                    Toast.makeText(context, "Please enter admission number", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.allowdiscountBtn:
                DiscountPermissionModel model = new DiscountPermissionModel();
                SavePermissionDiscount(model);
        }
    }
}
