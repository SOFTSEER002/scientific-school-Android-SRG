package com.jeannypr.scientificstudy.Inventory.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Inventory.adapter.DayBookAdapter;
import com.jeannypr.scientificstudy.Inventory.api.InventoryService;
import com.jeannypr.scientificstudy.Inventory.model.DayBookBean;
import com.jeannypr.scientificstudy.Inventory.model.DayBookModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.ActivityDaybookReportBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DayBookActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    InventoryService inventoryService;
    UserModel userData;
    RecyclerView transcationList;
    ArrayList<DayBookModel> transcationModel;
    Calendar calendar;
    private SimpleDateFormat df, df2;
    private DayBookAdapter adapter;
    private TextView txtFromDate, txtTodate, txtRecordVal;
    ActivityDaybookReportBinding binding;
    private String toDate, fromDate, startDate, endDate;
    private int year_fromDate, month_fromDate, day_fromDate, totalRecords;
    ImageView fromDateIc, toDateIc;
    private ProgressBar pb;
    private LinearLayout noRecord;
    private TextView noRecordMsg, netBalance, txtTotalCredit, txtTotalDebit, totalRecordTxt;
    private FloatingActionButton fabBtn;
    ConstraintLayout subHeader;
    View netBalanceDiv;
    long sDateInMillisec, eDateInMillisec;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_daybook_report);
        context = this;

        userData = UserPreference.getInstance(context).getUserData();
        inventoryService = new DataRepo<>(InventoryService.class, context).getService();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, "Day Book ", "");


        df = new SimpleDateFormat("dd-MM-yyyy");
        df2 = new SimpleDateFormat("MM/dd/yyyy");

        calendar = Calendar.getInstance(TimeZone.getDefault());
        startDate = df2.format(calendar.getTime());
        endDate = df2.format(calendar.getTime());

        noRecord = findViewById(R.id.noRecord);
        noRecordMsg = findViewById(R.id.noRecordMsg);
        totalRecordTxt = findViewById(R.id.totalRecord);

        transcationList = findViewById(R.id.transcationList);
        txtFromDate = findViewById(R.id.fromTranscationDateVal);
        txtFromDate.setOnClickListener(this);
        fromDateIc = findViewById(R.id.fromTranscationDateIc);
        fromDateIc.setOnClickListener(this);

        txtTodate = findViewById(R.id.toTranscationDateVal);
        txtTodate.setOnClickListener(this);
        toDateIc = findViewById(R.id.toTranscationDateIc);
        toDateIc.setOnClickListener(this);

        fabBtn = findViewById(R.id.fabBtn);
        fabBtn.setOnClickListener(this);

        netBalance = findViewById(R.id.netBalance);
        txtTotalDebit = findViewById(R.id.txtTotalDebit);
        txtTotalCredit = findViewById(R.id.txtTotalCredit);
        txtRecordVal = findViewById(R.id.txtRecordVal);

        netBalanceDiv = findViewById(R.id.netBalanceDiv);

        subHeader = findViewById(R.id.subHeader);

        pb = findViewById(R.id.progressBar);

        txtFromDate.setText(df.format(calendar.getTime()));
        txtTodate.setText(df.format(calendar.getTime()));


        transcationModel = new ArrayList<>();
        adapter = new DayBookAdapter(this, transcationModel);
        transcationList.setAdapter(adapter);
        transcationList.setLayoutManager(new LinearLayoutManager(this));
        ShowTrancationRecord();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.fromTranscationDateIc:
            case R.id.fromTranscationDateVal:

                DatePickerDialog fromDateDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        year_fromDate = year;
                        month_fromDate = month;
                        day_fromDate = dayOfMonth;

                        calendar.set(year, month, dayOfMonth);
                        sDateInMillisec = calendar.getTimeInMillis();
                        fromDate = df.format(calendar.getTime());
                        startDate = df2.format(calendar.getTime());
                        txtFromDate.setText(fromDate);

                    }
                },
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                if (eDateInMillisec != 0) {
                    fromDateDialog.getDatePicker().setMaxDate(eDateInMillisec);
                }

                fromDateDialog.show();
                break;

            case R.id.toTranscationDateVal:
            case R.id.toTranscationDateIc:

                DatePickerDialog toDateDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        calendar.set(year, month, dayOfMonth);
                        eDateInMillisec = calendar.getTimeInMillis();
                        toDate = df.format(calendar.getTime());
                        endDate = df2.format(calendar.getTime());
                        txtTodate.setText(toDate);

                    }
                },
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

           /*     Date min = new Date();
                min.setDate(day_fromDate);
                min.setMonth(month_fromDate);
                min.setYear(year_fromDate);*/

                if (sDateInMillisec != 0) {
                    toDateDialog.getDatePicker().setMinDate(sDateInMillisec);
                }
                if (fromDate != null) {
                    toDateDialog.show();

                } else {
                    Toast.makeText(context, "Please select start date first.", Toast.LENGTH_SHORT).show();
                    break;
                }

                break;

            case R.id.fabBtn:
                ShowTrancationRecord();
                break;

        }
    }

    private void ShowTrancationRecord() {
        pb.setVisibility(View.VISIBLE);

        inventoryService.GetTransactionRecord(startDate, endDate, userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<DayBookBean>() {
            @Override
            public void onResponse(Call<DayBookBean> call, Response<DayBookBean> response) {
                DayBookBean resp = response.body();
                transcationModel.clear();

                if (resp != null) {
                    if (resp.data != null) {
                        totalRecords = resp.data.size();

                        if (totalRecords > 1) {
                            totalRecordTxt.setText("Total Records");
                        } else {
                            totalRecordTxt.setText("Total Record");
                        }

                        if (resp.rcode == Constants.Rcode.OK) {
                            float totalCredit = 0, totalDebit = 0, totalBalance = 0;
                            subHeader.setVisibility(View.VISIBLE);

                            for (DayBookModel collection : resp.data) {
                                if (collection.Credit != 0) {
                                    totalCredit = totalCredit + collection.Credit;

                                } else {
                                    totalDebit = totalDebit + collection.Debit;
                                }
                                transcationModel.add(collection);
                            }

                            totalBalance = totalCredit - totalDebit;
                            txtTotalCredit.setText((String.valueOf(totalCredit)) + " " + "(Cr.)");
                            txtTotalDebit.setText((String.valueOf(totalDebit)) + " " + "(Dr.)");

                            if (totalCredit > totalDebit) {
                                netBalance.setText((String.valueOf(totalBalance)) + " " + "(Cr.)");
                            } else {
                                netBalance.setText((String.valueOf(totalBalance)) + " " + "(Dr.)");
                            }

                            int width = netBalance.getWidth();
                            if (width > 0) {
                                netBalanceDiv.getLayoutParams().width = width;
                            } else {
                                netBalanceDiv.getLayoutParams().width = 250;
                            }

                            noRecord.setVisibility(View.GONE);

                        } else {
                            DisplayNoRecord();
                            totalRecords=0;
                        }
                    } else {
                        DisplayNoRecord();
                        totalRecords=0;
                    }
                } else {
                    DisplayNoRecord();
                    totalRecords=0;
                }
                txtRecordVal.setText(String.valueOf(totalRecords));
                adapter.notifyDataSetChanged();
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DayBookBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                Toast.makeText(context, "Date wise collections could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void DisplayNoRecord() {
        transcationModel.clear();
        subHeader.setVisibility(View.GONE);
        noRecord.setVisibility(View.VISIBLE);
        noRecordMsg.setText("No record found");
    }
}