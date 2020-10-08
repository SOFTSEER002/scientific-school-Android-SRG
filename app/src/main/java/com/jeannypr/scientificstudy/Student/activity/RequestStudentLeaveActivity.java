package com.jeannypr.scientificstudy.Student.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.Bean;
import com.jeannypr.scientificstudy.Base.Model.ChildModel;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.leave.api.LeaveService;
import com.jeannypr.scientificstudy.leave.model.RequestLeaveInputModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestStudentLeaveActivity extends AppCompatActivity implements View.OnClickListener {
    Context context;
    Toolbar toolbar;
    LeaveService leaveService;
    TextView requestBtn, startDateTxt, endDateTxt, requestedLeaveTxt;
    EditText leaveReasonEdt;
    DatePickerDialog eDateDialog;
    DatePickerDialog sDateDialog;
    Calendar cal;
    int selectedSDate, selectedSMonth, selectedSYear;
    SimpleDateFormat dateFormatMDY, dateFormatDMY;
    long edateInMilisec, sdateInMilisec;
    String startDateMDY, startDateDMY, endDateMDY, endDateDMY;
    double totalRequestedLeaves;
    UserModel userModel;
    UserPreference userPref;
    ChildModel childModel;
    ProgressBar pb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_student_leave);
        context = this;

        userPref = UserPreference.getInstance(context);
        userModel = userPref.getUserData();
        childModel = userPref.getSelectedChild();
        leaveService = new DataRepo<>(LeaveService.class, context).getService();
        cal = Calendar.getInstance(java.util.TimeZone.getDefault());

        dateFormatDMY = new SimpleDateFormat("dd MMM, yyyy");
        dateFormatMDY = new SimpleDateFormat("MM/dd/yyyy");

        selectedSDate = cal.get(Calendar.DATE);
        selectedSMonth = cal.get(Calendar.MONTH);
        selectedSYear = cal.get(Calendar.YEAR);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, "Request Leave", "");

        requestBtn = findViewById(R.id.requestBtn);
        requestBtn.setOnClickListener(this);

        endDateTxt = findViewById(R.id.endDate);
        startDateTxt = findViewById(R.id.startDate);
        leaveReasonEdt = findViewById(R.id.leaveReason);
        requestedLeaveTxt = findViewById(R.id.requestedLeave);
        pb = findViewById(R.id.pb);

        startDateTxt.setOnClickListener(this);
        endDateTxt.setOnClickListener(this);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.requestBtn:

                if (startDateMDY == null || startDateMDY.equals("")) {
                    Toast.makeText(context, "Please select start date.", Toast.LENGTH_SHORT).show();
                    break;
                }

                if (endDateMDY == null || endDateMDY.equals("")) {
                    Toast.makeText(context, "Please select end date.", Toast.LENGTH_SHORT).show();
                    break;
                }

                if (leaveReasonEdt.getText().toString().equals("")) {
                    Toast.makeText(context, "Please enter note.", Toast.LENGTH_SHORT).show();
                    break;
                }

                RequestLeave();
                break;

            case R.id.startDate:

                sDateDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        cal.set(year, month, dayOfMonth);
                        sdateInMilisec = cal.getTimeInMillis();

                        startDateDMY = dateFormatDMY.format(cal.getTime());
                        startDateMDY = dateFormatMDY.format(cal.getTime());

                        startDateTxt.setText(startDateDMY);

                        if (endDateMDY != null) {
                            if (sdateInMilisec != 0 && edateInMilisec != 0) {
                                totalRequestedLeaves = Utility.CountDays(context, edateInMilisec, sdateInMilisec) + 1;


                                if (totalRequestedLeaves < 0) {
                                    eDateDialog.getDatePicker().updateDate(selectedSYear, selectedSMonth, selectedSDate);
                                    endDateTxt.setText(dateFormatDMY.format(cal.getTime()));

                                    endDateMDY = dateFormatMDY.format(cal.getTime());
                                    edateInMilisec = sdateInMilisec;
                                    requestedLeaveTxt.setText("Requested leaves: 1");

                                } else if (totalRequestedLeaves == 0) {
                                    eDateDialog.getDatePicker().updateDate(selectedSYear, selectedSMonth, selectedSDate);
                                    endDateTxt.setText(dateFormatDMY.format(cal.getTime()));

                                    endDateMDY = dateFormatMDY.format(cal.getTime());
                                    edateInMilisec = sdateInMilisec;
                                    requestedLeaveTxt.setText("Requested leaves: 1");

                                } else if (totalRequestedLeaves > 0) {
                                    requestedLeaveTxt.setText("Requested leaves: " + totalRequestedLeaves);
                                }
                            }
                        }

                    }
                },
                        cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH));
                sDateDialog.show();
                break;

            case R.id.endDate:
                eDateDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        cal.set(year, month, dayOfMonth);

                        edateInMilisec = cal.getTimeInMillis();
                        totalRequestedLeaves = Utility.CountDays(context, edateInMilisec, sdateInMilisec) + 1;
                        requestedLeaveTxt.setText("Requested leaves: " + String.valueOf(totalRequestedLeaves));

                        endDateDMY = dateFormatDMY.format(cal.getTime());
                        endDateMDY = dateFormatMDY.format(cal.getTime());

                        endDateTxt.setText(endDateDMY);
                    }
                },
                        cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH));

                if (sdateInMilisec != 0) {
                    eDateDialog.getDatePicker().setMinDate(sdateInMilisec);
                }

                if (startDateMDY != null) {
                    eDateDialog.show();

                } else {
                    Toast.makeText(context, "Please select start date first.", Toast.LENGTH_SHORT).show();
                    break;
                }

                break;
        }
    }

    private void RequestLeave() {
        pb.setVisibility(View.VISIBLE);

        RequestLeaveInputModel input = new RequestLeaveInputModel();
        input.SchoolId = userModel.getSchoolId();
        input.AcademicyearId = userModel.getAcademicyearId();
        input.AppliedBy = userModel.getUserId();
        input.StudentId = childModel.StudentId;
        input.ClassId = childModel.ClassId;
        input.FromDate = startDateMDY;
        input.ToDate = endDateMDY;
        input.Reason = leaveReasonEdt.getText().toString();

        leaveService.RequestStudentLeave(input).enqueue(new Callback<Bean>() {
            @Override
            public void onResponse(Call<Bean> call, Response<Bean> response) {
                if (response.body() != null) {
                    if (response.body().rcode == Constants.Rcode.OK) {
                        Toast.makeText(context, "Leave requested successfully.", Toast.LENGTH_SHORT).show();
                        //TODO: send notification to class teacher.
                        SwitchToListActivity();

                    } else {
                        Toast.makeText(context, "Failed to request leave. Please try again later.", Toast.LENGTH_SHORT).show();
                    }
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Bean> call, Throwable t) {
                Toast.makeText(context, "Something went wrong.Please try again later.", Toast.LENGTH_SHORT).show();
                Log.e("Request student leave:", t.getMessage());
                pb.setVisibility(View.GONE);
            }
        });
    }

    private void SwitchToListActivity() {
        Intent intent = new Intent(context, StudentLeaveHistoryActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
