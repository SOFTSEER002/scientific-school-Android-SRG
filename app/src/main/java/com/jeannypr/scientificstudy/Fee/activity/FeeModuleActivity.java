package com.jeannypr.scientificstudy.Fee.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Fee.api.FeeService;
import com.jeannypr.scientificstudy.Fee.model.FeeBean;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeeModuleActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    UserModel userData;
    FeeService feeService;
    Calendar cal;
    int monthId;
    ProgressBar pb;
    String monthName;
    SimpleDateFormat dateFormat;
    ImageView dateIc, classIc, monthIc, installmentIc, classDueIc, studentDueIc;
    TextView dateLbl, classLbl, installmentLbl, monthLbl, classDueLbl, studentDueLbl, todaysCollectionAmount,
            monthsCollectionAmount, yearlyCollectionAmount, monthsCollectionLbl, yearlyCollectionLbl, discountVal, dueValue;

    ConstraintLayout DateWiseReportRow, classWiseReportRow, monthWiseReportRow, installmentWiseReportRow, classWiseDueReportRow, studentWiseDueReportRow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        setContentView(R.layout.activity_fee_module);
        userData = UserPreference.getInstance(context).getUserData();
        feeService = new DataRepo<>(FeeService.class, context).getService();

        cal = Calendar.getInstance();
        monthId = cal.get(Calendar.MONTH) + 1;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, "Fee", "");

        DateWiseReportRow = findViewById(R.id.DateWiseReportRow);
        DateWiseReportRow.setOnClickListener(this);

        classWiseReportRow = findViewById(R.id.classWiseReportRow);
        classWiseReportRow.setOnClickListener(this);

        monthWiseReportRow = findViewById(R.id.monthWiseReportRow);
        monthWiseReportRow.setOnClickListener(this);

        installmentWiseReportRow = findViewById(R.id.installmentWiseReportRow);
        installmentWiseReportRow.setOnClickListener(this);

        classWiseDueReportRow = findViewById(R.id.classWiseDueReportRow);
        classWiseDueReportRow.setOnClickListener(this);

        studentWiseDueReportRow = findViewById(R.id.studentWiseDueReportRow);
        studentWiseDueReportRow.setOnClickListener(this);

        todaysCollectionAmount = findViewById(R.id.todaysCollectionAmount);
        monthsCollectionAmount = findViewById(R.id.monthsCollectionAmount);
        yearlyCollectionAmount = findViewById(R.id.yearlyCollectionAmount);
        monthsCollectionLbl = findViewById(R.id.monthsCollectionLbl);
        yearlyCollectionLbl = findViewById(R.id.yearlyCollectionLbl);
        dueValue = findViewById(R.id.dueValue);
        discountVal = findViewById(R.id.discountVal);

        pb = findViewById(R.id.progressBar);

        dateFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);
        try {
            Date dateObj = new SimpleDateFormat("MM").parse(String.valueOf(monthId));
            monthName = dateFormat.format(dateObj);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        monthsCollectionLbl.setText(monthName + "'s" + " " + "collection");
        yearlyCollectionLbl.setText(userData.getAcademicYearName() + "'s" + " " + "collection");

        GetTotalCollection();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetTotalCollection();
    }

  /*  private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }*/


    private void GetTotalCollection() {
        pb.setVisibility(View.VISIBLE);
        feeService.GetFeeCollection(userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<FeeBean>() {
            @Override
            public void onResponse(Call<FeeBean> call, Response<FeeBean> response) {

                FeeBean resp = response.body();
                if (resp != null) {
                    if (resp.rcode == Constants.Rcode.OK) {

                        if (resp.data != null) {
                            todaysCollectionAmount.setText(("Rs") + " " + String.valueOf(resp.data.TodayCollection));
                            monthsCollectionAmount.setText(("Rs") + " " + String.valueOf(resp.data.MonthCollection));
                            yearlyCollectionAmount.setText(("Rs") + " " + String.valueOf(resp.data.YearCollection));

                       /*     String name = getColoredSpanned("Discount:", "#ffffff");
                            String discount = getColoredSpanned(String.valueOf(resp.data.TodayDiscount), "#f8ff00");
                            discountVal.setText(Html.fromHtml(name + " " + "<b>" + discount + "</b>"));
                            String dueName = getColoredSpanned("Due:", "#ffffff");
                            String Value = getColoredSpanned(String.valueOf(resp.data.TodayDue), "#f8ff00");
                            dueValue.setText(Html.fromHtml(dueName + " " + "<b>" + Value + "</b>"));*/

                            discountVal.setText(String.valueOf(resp.data.TodayDiscount));
                            dueValue.setText(String.valueOf(resp.data.TodayDue));


                        }
                    } else if (resp.rcode == Constants.Rcode.NORECORDS) {
                        Toast.makeText(context, "No Record Found", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(context, "No Record Found", Toast.LENGTH_SHORT).show();
                    }

                } else

                {
                    Toast.makeText(context, "Somthing went worng. Please try again", Toast.LENGTH_LONG).show();
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<FeeBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                Toast.makeText(context, "Total collection no could not be loaded. Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.classWiseReportRow:
                Intent classWiseCollectionModuleIntent = new Intent(this, ClassWiseCollectionActivity.class);
                startActivity(classWiseCollectionModuleIntent);
                break;

            case R.id.DateWiseReportRow:
                /*Intent dateWiseCollectionIntent = new Intent(this, DateWiseCollectionActivity.class);*/
                Intent dateWiseCollectionIntent = new Intent(this, DailyCollectionActivity.class);
                startActivity(dateWiseCollectionIntent);
                break;

            case R.id.monthWiseReportRow:
                Intent monthWiseCollectionIntent = new Intent(this, MonthWiseCollectionActivity.class);
                startActivity(monthWiseCollectionIntent);
                break;

            case R.id.installmentWiseReportRow:
                Intent installmentIntent = new Intent(this, InstallmentSummaryActivity.class);
                startActivity(installmentIntent);
                break;

            case R.id.studentWiseDueReportRow:
                Intent studentDuesIntent = new Intent(this, FeeDuesSelectInstallmentActivity.class);
                studentDuesIntent.putExtra("reportType", Constants.DueReportTypes.STUDENTWISE);
                startActivity(studentDuesIntent);
                break;

            case R.id.classWiseDueReportRow:
                Intent classIntent = new Intent(this, FeeDuesSelectInstallmentActivity.class);
                classIntent.putExtra("reportType", Constants.DueReportTypes.CLASSWISE);
                startActivity(classIntent);
                break;

        /*    case R.id.classIc:
            case R.id.classLbl:
                Intent classWiseCollectionModuleIntent = new Intent(this, ClassWiseCollectionActivity.class);
                startActivity(classWiseCollectionModuleIntent);
                break;

            case R.id.dateIc:
            case R.id.dateLbl:
                Intent dateWiseCollectionIntent = new Intent(this, DateWiseCollectionActivity.class);
                startActivity(dateWiseCollectionIntent);
                break;

            case R.id.monthIc:
            case R.id.monthLbl:
                Intent monthWiseCollectionIntent = new Intent(this, MonthWiseCollectionActivity.class);
                startActivity(monthWiseCollectionIntent);
                break;

           *//* case R.id.consolidatedDuesBtn:
                Intent consolidatedDuesIntent = new Intent(this, SelectInstallmentActivity.class);
                startActivity(consolidatedDuesIntent);
                break;*//*
            case R.id.installmentIc:
            case R.id.installmentLbl:
                Intent installmentIntent = new Intent(this, InstallmentSummaryActivity.class);
                startActivity(installmentIntent);
                break;

            case R.id.studentDueIc:
            case R.id.studentDueLbl:
                Intent studentDuesIntent = new Intent(this, FeeDuesSelectInstallmentActivity.class);
                studentDuesIntent.putExtra("reportType", Constants.DueReportTypes.STUDENTWISE);
                startActivity(studentDuesIntent);
                break;

            case R.id.classDueIc:
            case R.id.classDueLbl:
                Intent classIntent = new Intent(this, FeeDuesSelectInstallmentActivity.class);
                classIntent.putExtra("reportType", Constants.DueReportTypes.CLASSWISE);
                startActivity(classIntent);
                break;*/
        }
    }
}