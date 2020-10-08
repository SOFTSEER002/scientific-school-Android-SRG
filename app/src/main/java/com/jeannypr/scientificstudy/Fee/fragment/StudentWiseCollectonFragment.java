package com.jeannypr.scientificstudy.Fee.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
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

public class StudentWiseCollectonFragment extends Fragment implements View.OnClickListener {
    private FeeService feeService;
    private Context context;
    UserModel userData;
    List<DateWiseCollectionModel> dateWiseCollectionModel;
    DateWiseCollectionAdapter adapter;
    RecyclerView mList;
    private ProgressBar pb;
    private TextView txtFromDate, txtTodate, totalCollection, noRecordMsg;
    private Calendar calendar;
    private SimpleDateFormat df, df2;
    private FloatingActionButton fabBtn;
    private String toDate, fromDate, startDate, endDate;
    private int year_fromDate, month_fromDate, day_fromDate;
    // ImageView fromDateIc, toDateIc;
    long sDateInMillisec, eDateInMillisec;
    View view;
    LinearLayout noRecord;
    long todayDate;
    //  private OnFragmentInteractionListener mListener;

    public StudentWiseCollectonFragment() {
        // Required empty public constructor
    }

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return view = inflater.inflate(R.layout.fragment_date_wise_collecton, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userData = UserPreference.getInstance(context).getUserData();
        feeService = new DataRepo<>(FeeService.class, context).getService();

        df = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        df2 = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);

        calendar = Calendar.getInstance(TimeZone.getDefault());
        startDate = df2.format(calendar.getTime());
        endDate = df2.format(calendar.getTime());
        todayDate = calendar.getTimeInMillis();

        totalCollection = view.findViewById(R.id.totalCollection);
        mList = view.findViewById(R.id.list);
        txtFromDate = view.findViewById(R.id.fromDate);
        txtFromDate.setOnClickListener(this);
       /* fromDateIc = view.findViewById(R.id.fromDateIc_datewiseFeeCollection);
        fromDateIc.setOnClickListener(this);*/

        txtTodate = view.findViewById(R.id.toDate);
        txtTodate.setOnClickListener(this);

        noRecord = view.findViewById(R.id.noRecord);
        noRecordMsg = view.findViewById(R.id.noRecordMsg);

      /*  toDateIc = view.findViewById(R.id.toDateIc_datewiseFeeCollection);
        toDateIc.setOnClickListener(this);*/

       /* fabBtn = view.findViewById(R.id.fabBtn);
        fabBtn.bringToFront();
        fabBtn.setOnClickListener(this);*/

        pb = view.findViewById(R.id.progressBar);

        txtFromDate.setText(df.format(calendar.getTime()));
        txtTodate.setText(df.format(calendar.getTime()));

        dateWiseCollectionModel = new ArrayList<>();
        adapter = new DateWiseCollectionAdapter(context, dateWiseCollectionModel);
        mList.setAdapter(adapter);
        mList.setLayoutManager(new LinearLayoutManager(context));

        loadData();
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
                        noRecord.setVisibility(View.GONE);
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
                        totalCollection.setText("Total Collection : Rs. " + String.valueOf(total));
                        adapter.notifyDataSetChanged();

                    } else if (dateWiseCollectionBean.rcode == Constants.Rcode.NORECORDS) {
                        dateWiseCollectionModel.clear();
                        totalCollection.setText("Total Collection : Rs. " + String.valueOf(total));
                        adapter.notifyDataSetChanged();
                        noRecord.setVisibility(View.VISIBLE);
                        noRecordMsg.setText(R.string.noRecordMsg);
                        // Toast.makeText(context, "No Collection found.", Toast.LENGTH_LONG).show();

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            //case R.id.fromDateIc_datewiseFeeCollection:
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
                        loadData();
                    }
                },
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                fromDateDialog.getDatePicker().setMaxDate(todayDate);

                if (eDateInMillisec != 0) {
                    fromDateDialog.getDatePicker().setMaxDate(eDateInMillisec);
                }

                fromDateDialog.show();
                break;

            case R.id.toDate:
                // case R.id.toDateIc_datewiseFeeCollection:

                DatePickerDialog toDateDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        calendar.set(year, month, dayOfMonth);
                        eDateInMillisec = calendar.getTimeInMillis();
                        toDate = df.format(calendar.getTime());
                        endDate = df2.format(calendar.getTime());
                        txtTodate.setText(toDate);
                        loadData();

                    }
                },
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                toDateDialog.getDatePicker().setMaxDate(todayDate);

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

           /* case R.id.fabBtn:
               // loadData();*/
        }
    }
}