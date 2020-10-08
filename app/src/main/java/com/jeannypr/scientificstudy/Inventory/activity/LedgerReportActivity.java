package com.jeannypr.scientificstudy.Inventory.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.DropDownModel;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Base.adapter.DropDownAdapter;
import com.jeannypr.scientificstudy.Inventory.adapter.LedgerReportAdapter;
import com.jeannypr.scientificstudy.Inventory.api.InventoryService;
import com.jeannypr.scientificstudy.Inventory.model.AllLedgerBean;
import com.jeannypr.scientificstudy.Inventory.model.AllLedgerModel;
import com.jeannypr.scientificstudy.Inventory.model.LedgerReportBean;
import com.jeannypr.scientificstudy.Inventory.model.LedgerReportModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.ActivityLedgerReportBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LedgerReportActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    InventoryService service;
    UserModel userData;
    Spinner ddlledgerList;
    ArrayList<DropDownModel> ledegrsList;
    DropDownAdapter adapter;
    DropDownModel selectedledger;
    String ledgerName;
    int ledgerId;
    Calendar calendar;
    private SimpleDateFormat df, df2;
    private LedgerReportAdapter ledgerAdapter;
    RecyclerView collectionledgerList;
    ArrayList<LedgerReportModel> ledgerModel;
    private TextView txtFromDate, txtTodate, txtTotalCredit, txtTotalDebit;
    private String toDate, fromDate, startDate, endDate;
    private int year_fromDate, month_fromDate, day_fromDate;
    ImageView fromDateIc, toDateIc;
    private ProgressBar pb;
    private LinearLayout noRecord;
    private TextView noRecordMsg;
    ConstraintLayout subHeader;
    ActivityLedgerReportBinding binding;
    FloatingActionButton fabBtn;
    long sDateInMillisec, eDateInMillisec;


    @Override

    public void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ledger_report);
        context = this;

        userData = UserPreference.getInstance(context).getUserData();
        service = new DataRepo<>(InventoryService.class, context).getService();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, "Ledger Report", "");

        df = new SimpleDateFormat("dd-MM-yyyy");
        df2 = new SimpleDateFormat("MM/dd/yyyy");

        calendar = Calendar.getInstance(TimeZone.getDefault());

        startDate = df2.format(calendar.getTime());
        endDate = df2.format(calendar.getTime());


        collectionledgerList = findViewById(R.id.collectionledgerList);
        noRecord = findViewById(R.id.noRecord);
        noRecordMsg = findViewById(R.id.noRecordMsg);

        txtFromDate = findViewById(R.id.fromLedgerDateVal);
        txtFromDate.setOnClickListener(this);
        fromDateIc = findViewById(R.id.fromDateIc);
        fromDateIc.setOnClickListener(this);

        txtTodate = findViewById(R.id.toDateLedgerVal);
        txtTodate.setOnClickListener(this);
        toDateIc = findViewById(R.id.toDateIc);
        toDateIc.setOnClickListener(this);

        fabBtn = findViewById(R.id.fabBtn);
        fabBtn.setOnClickListener(this);
        txtTotalDebit = findViewById(R.id.txtTotalDebit);
        txtTotalCredit = findViewById(R.id.txtTotalCredit);

        txtFromDate.setText(df.format(calendar.getTime()));
        txtTodate.setText(df.format(calendar.getTime()));

        pb = findViewById(R.id.progressBar);
        subHeader = findViewById(R.id.subHeader);

        ledegrsList = new ArrayList<>();
        adapter = new DropDownAdapter(context, R.layout.row_spinner, ledegrsList);
        ddlledgerList = findViewById(R.id.ddlledgerList);


        ledgerModel = new ArrayList<>();
        ledgerAdapter = new LedgerReportAdapter(this, ledgerModel);
        collectionledgerList.setAdapter(ledgerAdapter);
        collectionledgerList.setLayoutManager(new LinearLayoutManager(this));


        DropDownModel model = new DropDownModel();
        model.setId(0);
        model.setText("Select Ledger");
        ledegrsList.add(model);
        ddlledgerList.setAdapter(adapter);


        ddlledgerList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selectedledger = adapter.getItem(position);
                if (selectedledger != null) {

                    if (selectedledger.getId() != 0) {
                        ledgerId = selectedledger.getId();
                        ledgerName = selectedledger.getText();
                        ShowLedegrRecord();

                    } else {
                        ledgerId = 0;
                        ledgerName = "";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        GetLedgerList();
        ShowLedegrRecord();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void GetLedgerList() {
        service.GetAllLedgerDetail(userData.getSchoolId()).enqueue(new Callback<AllLedgerBean>() {
            @Override
            public void onResponse(Call<AllLedgerBean> call, Response<AllLedgerBean> response) {

                AllLedgerBean resp = response.body();
                if (resp != null) {

                    if (resp.rcode == Constants.Rcode.OK) {
                        List<AllLedgerModel> allLedgers = resp.data;

                        for (AllLedgerModel ledger : allLedgers) {
                            DropDownModel dropDownModel = new DropDownModel();
                            dropDownModel.setId(ledger.Id);
                            dropDownModel.setText(ledger.LedegrName);
                            ledegrsList.add(dropDownModel);
                        }
                        adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(context, "Ledgers could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<AllLedgerBean> call, Throwable t) {
                Toast.makeText(context, "Something went wrong.Please try again.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.fromDateIc:
            case R.id.fromLedgerDateVal:

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

            case R.id.toDateLedgerVal:
            case R.id.toDateIc:

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

               /* Date min = new Date();
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
                ShowLedegrRecord();
                break;

        }
    }

    private void ShowLedegrRecord() {
        pb.setVisibility(View.VISIBLE);

        service.GetLedgerReport(startDate, endDate, userData.getSchoolId(), userData.getAcademicyearId(), ledgerId).enqueue(new Callback<LedgerReportBean>() {
            @Override
            public void onResponse(Call<LedgerReportBean> call, Response<LedgerReportBean> response) {
                LedgerReportBean resp = response.body();

                if (resp != null) {

                    if (resp.rcode == Constants.Rcode.OK) {
                        float totalCredit = 0, totalDebit = 0;

                        if (resp.data != null) {
                            ledgerModel.clear();
                            subHeader.setVisibility(View.VISIBLE);

                            for (LedgerReportModel collection : resp.data) {

                                if (collection.Credit != 0) {
                                    totalCredit = totalCredit + collection.Credit;
                                } else {
                                    totalDebit = totalDebit + collection.Debit;
                                }

                                ledgerModel.add(collection);
                            }

                            txtTotalCredit.setText("Rs. " + (String.valueOf(totalCredit)) + " " + "(Cr.)");
                            txtTotalDebit.setText("Rs. " + (String.valueOf(totalDebit)) + " " + "(Dr.)");

                            ledgerAdapter.notifyDataSetChanged();
                            noRecord.setVisibility(View.GONE);
                        }

                    } else if (resp.rcode == Constants.Rcode.NORECORDS) {
                        ledgerModel.clear();
                        subHeader.setVisibility(View.GONE);
                        noRecord.setVisibility(View.VISIBLE);
                        noRecordMsg.setText("No record found");

                    } else {
                        Toast.makeText(context, "No record found.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "Something went wrong.Please try again.", Toast.LENGTH_LONG).show();
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<LedgerReportBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                Toast.makeText(context, "Ledger report could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
