package com.jeannypr.scientificstudy.Fee.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.ClassBean;
import com.jeannypr.scientificstudy.Base.Model.ClassModel;
import com.jeannypr.scientificstudy.Base.Model.DropDownModel;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.BaseService;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Base.adapter.DropDownAdapter;
import com.jeannypr.scientificstudy.Fee.api.FeeService;
import com.jeannypr.scientificstudy.Fee.model.FeeInstallmentBean;
import com.jeannypr.scientificstudy.Fee.model.FeeInstallmentModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Student.api.StudentService;
import com.jeannypr.scientificstudy.Student.model.StudentBean;
import com.jeannypr.scientificstudy.Student.model.StudentModel;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeeDuesSelectInstallmentActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private Context context;
    BaseService baseService;
    StudentService studentService;
    FeeService feeService;
    UserModel userData;
    List<FeeInstallmentModel> installmentList;
    Spinner classesListSpn, studentListSpn, fromInstallmentSpn, toInstallmentSpn;
    ArrayList<DropDownModel> classes, fromInstallments, toInstallments, students;
    DropDownAdapter classAdapter, studentAdapter, fromInstallmentsAdapter, toInstallmentsAdapter;
    DropDownModel selectedClass, selectedStudent, selectedToInstallment, selectedFromInstallment;
    String className, studentName, fromInstallmentName, toInstallmentName, reportType;
    int classId, studentId, fromInstallmentId, toInstallmentId, fromInstallmetNo;
    RelativeLayout studentContainer, fromInstallment, toInstallment;
    TextView viewReportBtn;
    CheckBox checkVoucher, checkLateFee;
    boolean checked, checkedLateFee;
    ProgressBar pb;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_dues_select_installment);
        context = this;

        UserPreference userPref = UserPreference.getInstance(context);
        userData = userPref.getUserData();
        baseService = new DataRepo<>(BaseService.class, context).getService();
        studentService = new DataRepo<>(StudentService.class, context).getService();
        feeService = new DataRepo<>(FeeService.class, context).getService();

        reportType = getIntent().getStringExtra("reportType");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, reportType + " " + "Due Report", "");

        studentContainer = findViewById(R.id.studentContainer);
        fromInstallment = findViewById(R.id.fromInstallment);
        toInstallment = findViewById(R.id.toInstallment);
        classesListSpn = findViewById(R.id.classesListSpn);
        studentListSpn = findViewById(R.id.studentListSpn);
        fromInstallmentSpn = findViewById(R.id.fromInstallmentSpn);
        toInstallmentSpn = findViewById(R.id.toInstallmentSpn);
        pb = findViewById(R.id.progressBar);

        viewReportBtn = findViewById(R.id.viewReportBtn);
        viewReportBtn.setOnClickListener(this);

        checkVoucher = findViewById(R.id.checkVoucher);
        checkVoucher.setOnCheckedChangeListener(this);

        checkLateFee = findViewById(R.id.checkLateFee);
        checkLateFee.setOnCheckedChangeListener(this);

        classes = new ArrayList<>();
        classAdapter = new DropDownAdapter(context, R.layout.row_spinner, classes);
        classesListSpn.setAdapter(classAdapter);

        students = new ArrayList<>();
        studentAdapter = new DropDownAdapter(context, R.layout.row_spinner, students);
        studentListSpn.setAdapter(studentAdapter);

        fromInstallments = new ArrayList<>();
        fromInstallmentsAdapter = new DropDownAdapter(context, R.layout.row_spinner, fromInstallments);
        fromInstallmentSpn.setAdapter(fromInstallmentsAdapter);

        toInstallments = new ArrayList<>();
        toInstallmentsAdapter = new DropDownAdapter(context, R.layout.row_spinner, toInstallments);
        toInstallmentSpn.setAdapter(toInstallmentsAdapter);

        SetListner();
    }

    private void SetListner() {
        GetClasses();
        GetFeesInstallMent();

        classesListSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                selectedClass = classAdapter.getItem(position);
                if (selectedClass != null) {

                    if (selectedClass.getId() != 0) {
                        classId = selectedClass.getId();
                        className = selectedClass.getText();

                        switch (reportType) {
                            case Constants.DueReportTypes.CLASSWISE:

                                GetFeesInstallMent();
                                fromInstallment.setVisibility(View.VISIBLE);
                                break;

                            case Constants.DueReportTypes.STUDENTWISE:
                                students.clear();
                                studentAdapter.notifyDataSetChanged();
                                studentId = 0;
                                studentName = "";

                                GetStudents();
                                studentContainer.setVisibility(View.VISIBLE);
                                break;
                        }

                    } else {
                        classId = 0;
                        className = "";
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
                classId = 0;
                className = "";
            }
        });

        studentListSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                selectedStudent = studentAdapter.getItem(position);

                if (selectedStudent != null) {

                    if (selectedStudent.getId() != 0) {
                        studentId = selectedStudent.getId();
                        studentName = selectedStudent.getText();

                        fromInstallment.setVisibility(View.VISIBLE);
                    } else {
                        studentId = 0;
                        studentName = "";
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                studentId = 0;
                studentName = "";
            }
        });


        fromInstallmentSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                selectedFromInstallment = fromInstallmentsAdapter.getItem(position);

                fromInstallmetNo = 0;
                toInstallments.clear();

                DropDownModel defaultstate = new DropDownModel();
                defaultstate.setText(getString(R.string.default_to_installment));
                defaultstate.setId(0);
                toInstallments.add(defaultstate);

                if (selectedFromInstallment != null) {

                    if (selectedFromInstallment.getId() != 0) {
                        fromInstallmentId = selectedFromInstallment.getId();
                        fromInstallmentName = selectedFromInstallment.getText();
                        FeeInstallmentModel obj = (FeeInstallmentModel) selectedFromInstallment.getObject();
                        fromInstallmetNo = obj.getInstallmentNo();

                        GetFeesInstallMent();
                        toInstallment.setVisibility(View.VISIBLE);


                        //Get to installment list on the behalf of selected from installment
                        for (FeeInstallmentModel installment : installmentList) {

                            if (installment.getInstallmentNo() >= fromInstallmetNo) {

                                DropDownModel model = new DropDownModel();
                                model.setId(installment.getId());
                                model.setText(installment.getTitle());
                                toInstallments.add(model);
                            }
                        }
                    } else {
                        fromInstallmentId = 0;
                        fromInstallmentName = "";
                    }
                }

                toInstallmentsAdapter.notifyDataSetChanged();
                toInstallmentSpn.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
                fromInstallmentId = 0;
                fromInstallmentName = "";
            }
        });

        toInstallmentSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                selectedToInstallment = toInstallmentsAdapter.getItem(position);

                if (selectedToInstallment != null) {

                    if (selectedToInstallment.getId() != 0) {
                        toInstallmentId = selectedToInstallment.getId();
                        toInstallmentName = selectedToInstallment.getText();
                        viewReportBtn.setVisibility(View.VISIBLE);
                        checkVoucher.setVisibility(View.VISIBLE);

                        if (studentId != 0) {
                            checkLateFee.setVisibility(View.VISIBLE);
                        }

                    } else {
                        toInstallmentId = 0;
                        toInstallmentName = "";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
                toInstallmentId = 0;
                toInstallmentName = "";
            }
        });
    }

    public void GetClasses() {
        pb.setVisibility(View.VISIBLE);

        DropDownModel defaultOption = new DropDownModel();
        defaultOption.setText("Select Class");
        defaultOption.setId(0);
        classes.add(defaultOption);

        baseService.getClasses(userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<ClassBean>() {
            @Override
            public void onResponse(Call<ClassBean> call, Response<ClassBean> response) {
                ClassBean resp = response.body();
                if (resp != null) {

                    if (resp.rcode == Constants.Rcode.OK) {
                        List<ClassModel> allClasses = resp.data;

                        for (ClassModel cls : allClasses) {
                            DropDownModel dropDownModel = new DropDownModel();
                            dropDownModel.setId(cls.Id);
                            dropDownModel.setText(cls.Name);
                            classes.add(dropDownModel);
                        }
                        classAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, "Classes could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ClassBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                Toast.makeText(context, "Classes could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void GetStudents() {

        pb.setVisibility(View.VISIBLE);

        studentService.getStudents(classId).enqueue(new Callback<StudentBean>() {
            @Override
            public void onResponse(Call<StudentBean> call, Response<StudentBean> response) {

                StudentBean resp = response.body();
                if (resp != null) {
                    if (resp.rcode == Constants.Rcode.OK) {

                        if (resp.data != null) {
                            students.clear();

                            DropDownModel defaultOption = new DropDownModel();
                            defaultOption.setText("Select student");
                            defaultOption.setId(0);
                            students.add(defaultOption);

                            for (StudentModel studentModel : resp.data) {
                                DropDownModel dropDownModel = new DropDownModel();
                                dropDownModel.setId(studentModel.Id);
                                dropDownModel.setText(studentModel.Name);
                                students.add(dropDownModel);
                            }
                            studentAdapter.notifyDataSetChanged();
                        }
                    } else if (resp.rcode == Constants.Rcode.NORECORDS) {
                        Toast.makeText(context, "No record found", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "Student list could not be loaded .Try again.", Toast.LENGTH_SHORT).show();
                    }
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<StudentBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                Toast.makeText(context, "Student list could not be loaded .Try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void GetFeesInstallMent() {

        pb.setVisibility(View.VISIBLE);

        feeService.GetFeeInstallments(userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<FeeInstallmentBean>() {
            @Override
            public void onResponse(Call<FeeInstallmentBean> call, Response<FeeInstallmentBean> response) {
                FeeInstallmentBean res = response.body();

                if (res != null) {
                    if (res.rcode == Constants.Rcode.OK) {
                        if (res.data != null) {

                            fromInstallments.clear();
                            DropDownModel defaultOption = new DropDownModel();
                            defaultOption.setText("From Installment");
                            defaultOption.setId(0);
                            fromInstallments.add(defaultOption);

                            installmentList = res.data;

                            for (FeeInstallmentModel installment : installmentList) {
                                DropDownModel ddModel = new DropDownModel();
                                ddModel.setId(installment.getId());
                                ddModel.setText(installment.getTitle());
                                ddModel.setObject(installment);
                                fromInstallments.add(ddModel);
                            }

                            fromInstallmentsAdapter.notifyDataSetChanged();

                        } else {
                            Toast.makeText(context, getResources().getString(R.string.somethingWrongMsg), Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(context, getResources().getString(R.string.noRecordMsg), Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(context, getResources().getString(R.string.somethingWrongMsg), Toast.LENGTH_LONG).show();
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<FeeInstallmentBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                Toast.makeText(context, getString(R.string.fee_installment_failuer_msg), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.viewReportBtn:
                if (classId == 0) {
                    Toast.makeText(context, "Please select class.", Toast.LENGTH_LONG).show();
                    break;
                }
                if (fromInstallmentId == 0) {
                    Toast.makeText(context, "Please select from inastallment.", Toast.LENGTH_LONG).show();
                    break;
                }
                if (toInstallmentId == 0) {
                    Toast.makeText(context, "Please select to installment.", Toast.LENGTH_LONG).show();
                    break;
                }

                Intent viewIntent = new Intent(context, DueReportActivity.class);
                Bundle bundle = new Bundle();

                if (reportType.equals(Constants.DueReportTypes.STUDENTWISE)) {
                    if (studentId == 0) {
                        Toast.makeText(context, "Please select student.", Toast.LENGTH_LONG).show();
                        break;
                    }

                    bundle.putInt("studentid", studentId);
                    bundle.putString("studentName", studentName);
                    bundle.putBoolean("latefee", checkedLateFee);
                }

                bundle.putInt("classid", classId);
                bundle.putString("className", className);
                bundle.putInt("fromInstallment", fromInstallmentId);
                bundle.putInt("toInstallMent", toInstallmentId);
                bundle.putString("reportType", reportType);
                bundle.putString("fromInstallmentName", fromInstallmentName);
                bundle.putString("toInstallmentName", toInstallmentName);
                bundle.putBoolean("voucher", checked);
                viewIntent.putExtras(bundle);
                startActivity(viewIntent);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getId()) {
            case R.id.checkVoucher:
                checked = isChecked;
                break;

            case R.id.checkLateFee:
                checkedLateFee = isChecked;
                break;
        }
    }
}
