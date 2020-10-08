package com.jeannypr.scientificstudy.Fee.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Fee.adapter.DateWiseCollectionAdapter;
import com.jeannypr.scientificstudy.Fee.api.FeeService;
import com.jeannypr.scientificstudy.Fee.model.DateWiseCollectionBean;
import com.jeannypr.scientificstudy.Fee.model.DateWiseCollectionModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DateWiseCollectionActivity extends AppCompatActivity implements View.OnClickListener {
    private FeeService feeService;
    private Context context;
    UserModel userData;
    List<DateWiseCollectionModel> dateWiseCollectionModel;
    DateWiseCollectionAdapter adapter;
    RecyclerView listContainer;
    private ProgressBar pb;
    private TextView txtFromDate, txtTodate, totalCollection;
    private Calendar calendar;
    private SimpleDateFormat df, df2;
    private FloatingActionButton fabBtn;
    private String toDate, fromDate, startDate, endDate;
    private int year_fromDate, month_fromDate, day_fromDate;
    //  ImageView fromDateIc, toDateIc;
    long sDateInMillisec, eDateInMillisec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        setContentView(R.layout.activity_datewise_collection);

        totalCollection = findViewById(R.id.totalCollection);
        userData = UserPreference.getInstance(context).getUserData();
        feeService = new DataRepo<>(FeeService.class, context).getService();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Date Wise Collection");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        df = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        df2 = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);

        calendar = Calendar.getInstance(TimeZone.getDefault());
        startDate = df2.format(calendar.getTime());
        endDate = df2.format(calendar.getTime());


        listContainer = findViewById(R.id.reyclerview_datewise_collection_list);
        txtFromDate = findViewById(R.id.fromDate);
        txtFromDate.setOnClickListener(this);
      /*  fromDateIc = findViewById(R.id.fromDateIc_datewiseFeeCollection);
        fromDateIc.setOnClickListener(this);*/

        txtTodate = findViewById(R.id.toDate);
        txtTodate.setOnClickListener(this);
       /* toDateIc = findViewById(R.id.toDateIc_datewiseFeeCollection);
        toDateIc.setOnClickListener(this);*/

        fabBtn = findViewById(R.id.fabBtn);
        fabBtn.bringToFront();
        fabBtn.setOnClickListener(this);

        pb = findViewById(R.id.progressBar);

        txtFromDate.setText(df.format(calendar.getTime()));
        txtTodate.setText(df.format(calendar.getTime()));

        dateWiseCollectionModel = new ArrayList<>();
        adapter = new DateWiseCollectionAdapter(this, dateWiseCollectionModel);
        listContainer.setAdapter(adapter);
        listContainer.setLayoutManager(new LinearLayoutManager(this));

        loadData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            //  case R.id.fromDateIc_datewiseFeeCollection:
            case R.id.fromDate:

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

            case R.id.toDate:
                //  case R.id.toDateIc_datewiseFeeCollection:

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
//                long eDate;
//                try {
//                     eDate = Long.parseLong(fromDate);
//                } catch (NumberFormatException e) {
//                    System.out.println("NumberFormatException: " + e.getMessage());
//                }
         /*       Date min = new Date();
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
                loadData();
        }
    }

    private void loadData() {
        pb.setVisibility(View.VISIBLE);

        feeService.GetDateWiseColection(startDate, endDate, userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<DateWiseCollectionBean>() {
            @Override
            public void onResponse(Call<DateWiseCollectionBean> call, Response<DateWiseCollectionBean> response) {
                DateWiseCollectionBean dateWiseCollectionBean = response.body();
                long total = 0;

                if (dateWiseCollectionBean != null) {
                    if (dateWiseCollectionBean.rcode == Constants.Rcode.OK) {
                        //  listContainer.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        dateWiseCollectionModel.clear();

                        for (DateWiseCollectionModel collection : dateWiseCollectionBean.data) {
                            dateWiseCollectionModel.add(collection);

                            try {
                                total += Integer.parseInt(collection.Amount);
                            } catch (NumberFormatException ex) {
                                ex.printStackTrace();
                            }
                        }
                        totalCollection.setText("Rs. " + String.valueOf(total));
                        adapter.notifyDataSetChanged();

                    } else if (dateWiseCollectionBean.rcode == Constants.Rcode.NORECORDS) {
                        dateWiseCollectionModel.clear();
                        totalCollection.setText("Rs. " + String.valueOf(total));
                        adapter.notifyDataSetChanged();
                        Toast.makeText(context, "No Collection found.", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(context, "No Collection found.", Toast.LENGTH_LONG).show();
                    }
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DateWiseCollectionBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                Toast.makeText(context, "Date wise collections could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }
}