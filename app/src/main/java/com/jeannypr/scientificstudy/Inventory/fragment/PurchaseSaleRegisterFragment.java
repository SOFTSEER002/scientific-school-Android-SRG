package com.jeannypr.scientificstudy.Inventory.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Inventory.activity.PurchaseSaleDetailActivity;
import com.jeannypr.scientificstudy.Inventory.activity.PurchaseSaleTransactionActivity;
import com.jeannypr.scientificstudy.Inventory.adapter.PurchaseSummaryAdapter;
import com.jeannypr.scientificstudy.Inventory.api.InventoryService;
import com.jeannypr.scientificstudy.Inventory.model.PurchaseSummaryBean;
import com.jeannypr.scientificstudy.Inventory.model.PurchaseSummaryItemsModel;
import com.jeannypr.scientificstudy.Inventory.model.PurchaseSummaryModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchaseSaleRegisterFragment extends Fragment implements View.OnClickListener {
    private InventoryService service;
    private Context context;
    UserModel userData;
    String reportType;
    private View view;
    RecyclerView listContainer;
    private ProgressBar pb;
    private TextView txtFromDate, txtTodate, totalCollection, noRecordMsg;
    private Calendar calendar;
    private SimpleDateFormat df, df2;
    private String toDate, fromDate;
    FloatingActionButton fabBtn;
    ImageView fromDateIc, toDateIc;
    List<PurchaseSummaryItemsModel> items;
    List<PurchaseSummaryModel> purchases;
    PurchaseSummaryAdapter adapter;
    private LinearLayout noRecord;
    private int year_fromDate, month_fromDate, day_fromDate;
    FloatingActionButton createTransBtn;
    ImageView filterBtn;
    long sDateInMillisec, eDateInMillisec;

    public PurchaseSaleRegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();
        Bundle bundle = getArguments();
        if (bundle != null) {
            reportType = bundle.getString("transactionType");
        }

        userData = UserPreference.getInstance(context).getUserData();
        service = new DataRepo<>(InventoryService.class, context).getService();

        calendar = Calendar.getInstance(TimeZone.getDefault());
        df = new SimpleDateFormat("dd-MM-yyyy");
        df2 = new SimpleDateFormat("MM/dd/yyyy");

        fromDate = df2.format(calendar.getTime());
        toDate = df2.format(calendar.getTime());
        purchases = new ArrayList<>();
        items = new ArrayList<>();

        adapter = new PurchaseSummaryAdapter(context, purchases, new PurchaseSummaryAdapter.TransactionDetail() {
            @Override
            public void GetTransactionDetail(PurchaseSummaryModel purchase) {
                Intent i = new Intent(context, PurchaseSaleDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("transactionDetail", purchase);
                i.putExtras(bundle);
                startActivity(i);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_purchase_sale_register, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listContainer = view.findViewById(R.id.purchaseSummary);
        noRecord = view.findViewById(R.id.noRecord);
        noRecordMsg = view.findViewById(R.id.noRecordMsg);
        listContainer.setAdapter(adapter);

        txtFromDate = view.findViewById(R.id.fromDateVal);
        txtFromDate.setOnClickListener(this);
        fromDateIc = view.findViewById(R.id.fromDateIc);
        fromDateIc.setOnClickListener(this);

        txtTodate = view.findViewById(R.id.toDateVal);
        txtTodate.setOnClickListener(this);
        toDateIc = view.findViewById(R.id.toDateIc);
        toDateIc.setOnClickListener(this);

        filterBtn = view.findViewById(R.id.filterBtn);
        filterBtn.setOnClickListener(this);

        createTransBtn = view.findViewById(R.id.createTransBtn);
        createTransBtn.setOnClickListener(this);
        createTransBtn.bringToFront();

        pb = view.findViewById(R.id.progressBar);
        totalCollection = view.findViewById(R.id.totalCollection);

        txtFromDate.setText(df.format(calendar.getTime()));
        txtTodate.setText(df.format(calendar.getTime()));

        LoadData();

    }

    public void LoadData() {
        listContainer.setVisibility(View.GONE);
        pb.setVisibility(View.VISIBLE);

        switch (reportType) {
            case Constants.InventoryReportType.PURCHASE:

                service.GetPurchaseSummary(fromDate, toDate, userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<PurchaseSummaryBean>() {
                    @Override
                    public void onResponse(Call<PurchaseSummaryBean> call, Response<PurchaseSummaryBean> response) {
                        PurchaseSummaryBean res = response.body();

                        if (res != null) {
                            SetData(res);
                        }
                        pb.setVisibility(View.GONE);
                        listContainer.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Call<PurchaseSummaryBean> call, Throwable t) {
                        pb.setVisibility(View.GONE);
                        listContainer.setVisibility(View.VISIBLE);
                        Toast.makeText(context, reportType + " Summary could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                    }
                });
                break;

            case Constants.InventoryReportType.SALE:

                service.GetSaleSummary(fromDate, toDate, userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<PurchaseSummaryBean>() {
                    @Override
                    public void onResponse(Call<PurchaseSummaryBean> call, Response<PurchaseSummaryBean> response) {
                        PurchaseSummaryBean res = response.body();

                        if (res != null) {
                            SetData(res);
                        }
                        pb.setVisibility(View.GONE);
                        listContainer.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Call<PurchaseSummaryBean> call, Throwable t) {
                        pb.setVisibility(View.GONE);
                        Toast.makeText(context, reportType + " Summary could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                    }
                });
                break;
        }


    }

    private void SetData(PurchaseSummaryBean res) {
        long total = 0;

        if (res.rcode == Constants.Rcode.OK) {
            noRecord.setVisibility(View.GONE);
            purchases.clear();

            for (PurchaseSummaryModel purchase : res.data) {
                purchases.add(purchase);

                try {
                    total += purchase.GraceTotal;
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                }
            }
            totalCollection.setText(String.valueOf(total));
            adapter.notifyDataSetChanged();

        } else if (res.rcode == Constants.Rcode.NORECORDS) {
            purchases.clear();
            totalCollection.setText(String.valueOf(total));
            adapter.notifyDataSetChanged();

            noRecord.setVisibility(View.VISIBLE);
            noRecordMsg.setText("No record found.");

        } else {
            Toast.makeText(context, "No record found.", Toast.LENGTH_LONG).show();
        }

        // listContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.fromDateVal:
            case R.id.fromDateIc:

                DatePickerDialog fromDateDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        /*year_fromDate = year;
                        month_fromDate = month;
                        day_fromDate = dayOfMonth;*/

                        calendar.set(year, month, dayOfMonth);
                        sDateInMillisec = calendar.getTimeInMillis();
                        fromDate = df2.format(calendar.getTime());
                        txtFromDate.setText(df.format(calendar.getTime()));
                    }
                },
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                if (eDateInMillisec != 0) {
                    fromDateDialog.getDatePicker().setMaxDate(eDateInMillisec);
                }

                fromDateDialog.show();
                break;

            case R.id.toDateVal:
            case R.id.toDateIc:

                DatePickerDialog toDateDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        calendar.set(year, month, dayOfMonth);
                        eDateInMillisec = calendar.getTimeInMillis();
                        toDate = df2.format(calendar.getTime());
                        txtTodate.setText(df.format(calendar.getTime()));

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

            case R.id.filterBtn:
                LoadData();
                break;

            case R.id.createTransBtn:
                SwithToTransaction();
                break;
        }
    }

    public void SwithToTransaction() {
        Intent intent = new Intent(context, PurchaseSaleTransactionActivity.class);
        intent.putExtra("transactionType", reportType);
        startActivity(intent);
    }
}
