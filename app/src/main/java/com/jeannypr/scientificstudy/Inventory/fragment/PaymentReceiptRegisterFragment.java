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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.DropDownModel;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Base.adapter.DropDownAdapter;
import com.jeannypr.scientificstudy.Inventory.activity.PaymentReceiptDetailActivity;
import com.jeannypr.scientificstudy.Inventory.activity.PaymentReceiptTransactionActivity;
import com.jeannypr.scientificstudy.Inventory.adapter.PaymentReceiptAdapter;
import com.jeannypr.scientificstudy.Inventory.api.InventoryService;
import com.jeannypr.scientificstudy.Inventory.model.PaymentReceiptSummaryBean;
import com.jeannypr.scientificstudy.Inventory.model.PaymentReceiptSummaryModel;
import com.jeannypr.scientificstudy.Inventory.model.PurchaseSummaryItemsModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import android.support.v4.app.FragmentTransaction;

public class PaymentReceiptRegisterFragment extends Fragment implements View.OnClickListener {
    private InventoryService service;
    private Context context;
    UserModel userdata;
    RecyclerView listContainer;
    private ProgressBar pb;
    private TextView txtFromDate, txtTodate, totalCollection, noRecordMsg;
    private Calendar calendar;
    private SimpleDateFormat df, df2;
    private String toDate, fromDate, reportType, filterName;
    FloatingActionButton createTransBtn;
    ImageView fromDateIc, toDateIc, filterBtn;
    private int year_fromDate, month_fromDate, day_fromDate, filterId;
    List<PurchaseSummaryItemsModel> items;
    List<PaymentReceiptSummaryModel> purchases;
    PaymentReceiptAdapter adapter;
    private LinearLayout noRecord;
    Spinner filter;
    DropDownAdapter filterAdapter;
    ArrayList<DropDownModel> filters;
    View divider, view;
    CommunicationWithActivity listner;
    long sDateInMillisec, eDateInMillisec;

    @Override
    public void onResume() {
        super.onResume();
        LoadData();
    }

    public PaymentReceiptRegisterFragment() {
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

        userdata = UserPreference.getInstance(context).getUserData();
        service = new DataRepo<>(InventoryService.class, context).getService();

        calendar = Calendar.getInstance();
        df = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        df2 = new SimpleDateFormat("MM/dd/yyyy",Locale.ENGLISH);

        fromDate = df2.format(calendar.getTime());
        toDate = df2.format(calendar.getTime());
        purchases = new ArrayList<PaymentReceiptSummaryModel>();
        items = new ArrayList<PurchaseSummaryItemsModel>();

        adapter = new PaymentReceiptAdapter(context, purchases, new PaymentReceiptAdapter.TransactionDetail() {
            @Override
            public void GetTransactionDetail(PaymentReceiptSummaryModel purchase) {
                Intent i = new Intent(context, PaymentReceiptDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("transactionDetail", purchase);
                i.putExtras(bundle);
                i.putExtra("transactionType", reportType);
                startActivity(i);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_payment_receipt_register, container, false);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.fromDateVal:
            case R.id.fromDateIc:

                DatePickerDialog fromDateDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                       /* year_fromDate = year;
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
                //TODO : open activity to create transaction
                //   listner.OnClick();
                SwithToTransaction();
                break;
        }
    }

    public void LoadData() {
        pb.setVisibility(View.VISIBLE);
        listContainer.setVisibility(View.GONE);

        service.GetPaymentReceiptSummary(fromDate, toDate, userdata.getSchoolId(), userdata.getAcademicyearId(), reportType).enqueue(new Callback<PaymentReceiptSummaryBean>() {
            @Override
            public void onResponse(Call<PaymentReceiptSummaryBean> call, Response<PaymentReceiptSummaryBean> response) {
                PaymentReceiptSummaryBean res = response.body();

                if (res != null) {
                    SetData(res);
                }
                pb.setVisibility(View.GONE);
                listContainer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<PaymentReceiptSummaryBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                listContainer.setVisibility(View.VISIBLE);
                Toast.makeText(context, "Payment/Receipts could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void SetData(PaymentReceiptSummaryBean res) {
        long total = 0;

        if (res.rcode == Constants.Rcode.OK) {
            noRecord.setVisibility(View.GONE);
            purchases.clear();

            for (PaymentReceiptSummaryModel purchase : res.dataSet) {
                purchases.add(purchase);

                try {
                    total += purchase.Amount;
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
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listner = (CommunicationWithActivity) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /* public void ReplaceFragment() {
        android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Fragment frag = new PaymentReceiptTransactionActivity();

        Bundle bundle = new Bundle();
        bundle.putString("transactionType", Constants.InventoryReportType.PAYMENT);
        frag.setArguments(bundle);
        transaction.replace(R.id.viewPager, frag);
        transaction.commit();
    }*/

    public void SwithToTransaction() {
        Intent intent = new Intent(context, PaymentReceiptTransactionActivity.class);
        intent.putExtra("transactionType", reportType);
        startActivity(intent);
    }

    public interface CommunicationWithActivity {
        void OnClick();
    }
}
