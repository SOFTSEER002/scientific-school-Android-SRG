package com.jeannypr.scientificstudy.Fee.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Fee.adapter.MonthWiseCollectionAdapter;
import com.jeannypr.scientificstudy.Fee.api.FeeService;
import com.jeannypr.scientificstudy.Fee.model.DateWiseCollectionBean;
import com.jeannypr.scientificstudy.Fee.model.DateWiseCollectionModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MonthWiseCollectionActivity extends AppCompatActivity implements View.OnClickListener {
    private FeeService feeService;
    private Context context;
    UserModel userData;
    private List<DateWiseCollectionModel> dateWiseCollectionModels;
    private MonthWiseCollectionAdapter adapter;
    private RecyclerView listContainer;
    private ProgressBar pb;
    private TextView txtDatepickerSpinner, totalCollection, datePicker, noRecordMsg;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    Calendar calendar;
    private int selectedYear, selectedMonth, day;
    //RelativeLayout dateSection_monthwiseFeeCollection;
    ConstraintLayout dateSectionRow;
    SimpleDateFormat df;
    LinearLayout noRecord;
    //FloatingActionButton monthwiseCollectionBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        setContentView(R.layout.activity_monthwise_collection);
        userData = UserPreference.getInstance(context).getUserData();
        feeService = new DataRepo<>(FeeService.class, context).getService();

        calendar = Calendar.getInstance(TimeZone.getDefault());


        selectedMonth = calendar.get(Calendar.MONTH) + 1;
        selectedYear = calendar.get(Calendar.YEAR);

        df = new SimpleDateFormat("MMMM, yyyy", Locale.ENGLISH);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Utility.SetToolbar(context, "Month Wise Collections", "");

     /*   if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Month Wise Collections");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }*/

        listContainer = findViewById(R.id.monthwise_collection_list);
        datePicker = findViewById(R.id.datePicker);
        dateSectionRow = findViewById(R.id.dateSectionRow);
        dateSectionRow.setOnClickListener(this);

        //txtDatepickerSpinner = findViewById(R.id.datepickerSpinner);
        // dateSection_monthwiseFeeCollection = findViewById(R.id.dateSection_monthwiseFeeCollection);
        totalCollection = findViewById(R.id.totalCollection);
        pb = findViewById(R.id.progressBar);

        noRecord = findViewById(R.id.noRecord);
        noRecordMsg = findViewById(R.id.noRecordMsg);


        datePicker.setText(df.format(calendar.getTime()));
        // dateSection_monthwiseFeeCollection.setOnClickListener(this);
        //   monthwiseCollectionBtn.setOnClickListener(this);

        dateWiseCollectionModels = new ArrayList<>();
        adapter = new MonthWiseCollectionAdapter(this, dateWiseCollectionModels);
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
            case R.id.dateSectionRow:
                DatePickerDialog dialog = new DatePickerDialog(MonthWiseCollectionActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.YEAR, year);
                        selectedMonth = monthOfYear + 1;
                        selectedYear = year;
                        datePicker.setText(df.format(calendar.getTime()));
                        loadData();
                    }
                }, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getDatePicker().setMaxDate(new Date().getTime());
                ((ViewGroup) dialog.getDatePicker()).findViewById(getResources().getIdentifier("day", "id", "android")).setVisibility(View.GONE);


                //TODO : set current month and year as max date
                dialog.show();
                break;

        }
    }

    private void loadData() {
        pb.setVisibility(View.VISIBLE);

        feeService.GetMonthWiseColection(userData.getSchoolId(), userData.getAcademicyearId(), selectedMonth, selectedYear).enqueue(new Callback<DateWiseCollectionBean>() {
            @Override
            public void onResponse(Call<DateWiseCollectionBean> call, Response<DateWiseCollectionBean> response) {
                DateWiseCollectionBean dateWiseCollectionBean = response.body();
                long total = 0;
                if (dateWiseCollectionBean != null) {

                    noRecord.setVisibility(View.GONE);

                    int size = dateWiseCollectionBean.data.size();
                    if (size == 0) {
                        noRecord.setVisibility(View.VISIBLE);
                        noRecordMsg.setText(R.string.noRecordMsg);
                    }

                    if (dateWiseCollectionBean.rcode == Constants.Rcode.OK) {
                        dateWiseCollectionModels.clear();
                        for (DateWiseCollectionModel collection : dateWiseCollectionBean.data) {
                            dateWiseCollectionModels.add(collection);


                            try {
                                total += Integer.parseInt(collection.Amount);
                            } catch (NumberFormatException ex) {
                                ex.printStackTrace();
                            }
                        }
                        adapter.notifyDataSetChanged();
                        totalCollection.setText("Total Collection : Rs. " + String.valueOf(total));

                    } else if (dateWiseCollectionBean.rcode == Constants.Rcode.NORECORDS) {

                        dateWiseCollectionModels.clear();
                        totalCollection.setText("Total Collection : Rs. " + String.valueOf(total));
                        adapter.notifyDataSetChanged();
                        noRecord.setVisibility(View.VISIBLE);
                        noRecordMsg.setText(R.string.noRecordMsg);
                        //Toast.makeText(context, "No Collection found.", Toast.LENGTH_LONG).show();

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
