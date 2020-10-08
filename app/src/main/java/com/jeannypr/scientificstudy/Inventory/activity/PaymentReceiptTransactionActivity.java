package com.jeannypr.scientificstudy.Inventory.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.Bean;
import com.jeannypr.scientificstudy.Base.Model.DropDownModel;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Base.adapter.DropDownAdapter;
import com.jeannypr.scientificstudy.Inventory.api.InventoryService;
import com.jeannypr.scientificstudy.Inventory.model.CreditLedgerBean;
import com.jeannypr.scientificstudy.Inventory.model.CreditLedgerModel;
import com.jeannypr.scientificstudy.Inventory.model.CurrentBalanceBean;
import com.jeannypr.scientificstudy.Inventory.model.PaymentReceiptInputModel;
import com.jeannypr.scientificstudy.Inventory.model.PaymentReceiptItems;
import com.jeannypr.scientificstudy.Inventory.model.VoucharBean;
import com.jeannypr.scientificstudy.Inventory.model.VoucharModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentReceiptTransactionActivity extends AppCompatActivity implements View.OnClickListener {
    private InventoryService inventoryService;
    UserModel userData;
    private Context context;
    private String reportType;
    TextView transDate, txtLedger, txtReceiptNo, txtBalanceVal, txtBalancelbl, saveBtn, txtTotalLedgers;
    ImageView transDateIc, addLedgerArrowIc;
    private Calendar calendar;
    private SimpleDateFormat dfDMY, dfMDY;
    Spinner ledgerListSpn;
    ArrayList<DropDownModel> ledgerList;
    DropDownAdapter adapter;
    DropDownModel selectedLedger;
    int ledgerId;
    String ledgerName, paymentDate, narration;
    float currentBalance;
    private int selectedYear, selectedMonth, selectedDay, totalItems;
    EditText narrationEdt;
    ArrayList<PaymentReceiptItems> items;
    VoucharModel voucharDetail;
    ProgressBar pb;
    private static String TAG = PaymentReceiptTransactionActivity.class.getSimpleName();
    final private int TRANS_ITEMS_REQUEST_CODE = 100;
    float totalAmount = 0;
    ChipGroup chipGroup;
    HorizontalScrollView parent;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_receipt_trans);

        context = this;
        userData = UserPreference.getInstance(context).getUserData();
        inventoryService = new DataRepo<>(InventoryService.class, context).getService();
        ledgerList = new ArrayList<>();
        items = new ArrayList<>();
        voucharDetail = new VoucharModel();
        adapter = new DropDownAdapter(context, R.layout.row_spinner, ledgerList);

        dfDMY = new SimpleDateFormat("dd-MM-yyyy");
        dfMDY = new SimpleDateFormat("MM/dd/yyyy");
        calendar = Calendar.getInstance(TimeZone.getDefault());

        reportType = getIntent().getStringExtra("transactionType");

        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
        selectedMonth = calendar.get(Calendar.MONTH) + 1;
        selectedYear = calendar.get(Calendar.YEAR);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, reportType, "");

        InitializeViews();

    }

    private void InitializeViews() {
        pb = findViewById(R.id.pb);
        transDate = findViewById(R.id.transDate);
        transDateIc = findViewById(R.id.transDateIc);
        txtLedger = findViewById(R.id.addLedger);
        addLedgerArrowIc = findViewById(R.id.addLedgerArrowIc);
        txtReceiptNo = findViewById(R.id.receiptNo);
        narrationEdt = findViewById(R.id.narration);
        txtBalanceVal = findViewById(R.id.txtBalanceVal);
        txtBalancelbl = findViewById(R.id.txtBalancelbl);
        ledgerListSpn = findViewById(R.id.ledgerListSpn);
        saveBtn = findViewById(R.id.saveBtn);
        txtTotalLedgers = findViewById(R.id.totalLedgers);
        chipGroup = findViewById(R.id.chipGroup);
        parent = findViewById(R.id.chipScrollView);

        Date currentTime = calendar.getTime();
        transDate.setText(dfDMY.format(currentTime));
        paymentDate = dfMDY.format(currentTime);

        transDate.setOnClickListener(this);
        transDateIc.setOnClickListener(this);
        txtLedger.setOnClickListener(this);
        addLedgerArrowIc.setOnClickListener(this);
        saveBtn.setOnClickListener(this);

        ledgerListSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selectedLedger = adapter.getItem(position);

                if (selectedLedger != null) {
                    if (selectedLedger.getId() != 0) {
                        ledgerId = selectedLedger.getId();
                        ledgerName = selectedLedger.getText();

                        GetLedgerDetail();

                    } else {
                        ledgerId = 0;
                        ledgerName = "";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                ledgerId = 0;
                ledgerName = "";
            }
        });
        GetCreditLedgerList();
        GetVoucharDetail();
    }

    private void GetCreditLedgerList() {
        DropDownModel model = new DropDownModel();
        model.setId(0);
        model.setText("Select credit ledger");
        ledgerList.add(model);
        ledgerListSpn.setAdapter(adapter);

        inventoryService.GetCreditLedgerRecord(userData.getSchoolId()).enqueue(new Callback<CreditLedgerBean>() {
            @Override
            public void onResponse(Call<CreditLedgerBean> call, Response<CreditLedgerBean> response) {

                CreditLedgerBean resp = response.body();
                if (resp != null) {

                    if (resp.rcode == Constants.Rcode.OK) {
                        List<CreditLedgerModel> allLedgers = resp.data;

                        for (CreditLedgerModel ledger : allLedgers) {
                            DropDownModel dropDownModel = new DropDownModel();
                            dropDownModel.setId(ledger.Id);
                            dropDownModel.setText(ledger.Name);
                            ledgerList.add(dropDownModel);
                        }
                        adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(context, "Ledgers could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CreditLedgerBean> call, Throwable t) {
                Toast.makeText(context, "Something went wrong.Please try again.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void GetVoucharDetail() {
        String voucharType = "";

        switch (reportType) {
            case Constants.InventoryReportType.PAYMENT:
                voucharType = Constants.VoucharType.PAYMENT;
                break;

            case Constants.InventoryReportType.SALE:
                voucharType = Constants.VoucharType.SALE;
                break;

            case Constants.InventoryReportType.RECEIPT:
                voucharType = Constants.VoucharType.RECEIPT;
                break;

            case Constants.InventoryReportType.PURCHASE:
                voucharType = Constants.VoucharType.PURCHASE;
                break;
        }

        inventoryService.GetVoucharDetail(userData.getSchoolId(), voucharType,userData.getAcademicyearId()).enqueue(new Callback<VoucharBean>() {
            @Override
            public void onResponse(Call<VoucharBean> call, Response<VoucharBean> response) {
                VoucharBean resp = response.body();

                if (resp != null) {
                    if (resp.rcode == Constants.Rcode.OK) {

                        if (resp.data != null) {
                            voucharDetail = resp.data;

                            txtReceiptNo.setText(String.valueOf(voucharDetail.getVoucharNo()));
                            //txtReceiptNo.setText("40");
                        }
                    } else if (resp.rcode == Constants.Rcode.NORECORDS) {
                        Toast.makeText(context, "No Record Found", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(context, "Somthing went worng.Receipt no could not be loaded.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Somthing went worng. Please try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<VoucharBean> call, Throwable t) {
                Toast.makeText(context, "Receipt no could not be loaded. Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void GetLedgerDetail() {
        inventoryService.GetLedgerDetail(userData.getSchoolId(), ledgerId).enqueue(new Callback<CurrentBalanceBean>() {
            @Override
            public void onResponse(Call<CurrentBalanceBean> call, Response<CurrentBalanceBean> response) {
                CurrentBalanceBean resp = response.body();
                if (resp != null) {
                    if (resp.rcode == Constants.Rcode.OK) {

                        if (resp.data != null) {
                            currentBalance = resp.data.Credit - resp.data.Debit;
                        }

                        if (resp.data.Credit > resp.data.Debit) {
                            txtBalanceVal.setText(String.valueOf(currentBalance + " " + "Cr."));
                            txtBalanceVal.setVisibility(View.VISIBLE);
                            txtBalancelbl.setVisibility(View.VISIBLE);

                        } else if (resp.data.Credit == resp.data.Debit) {
                            txtBalanceVal.setVisibility(View.GONE);
                            txtBalancelbl.setVisibility(View.GONE);

                        } else {
                            txtBalanceVal.setText(String.valueOf(currentBalance + " " + "Dr."));
                            txtBalanceVal.setVisibility(View.VISIBLE);
                            txtBalancelbl.setVisibility(View.VISIBLE);
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
            }

            @Override
            public void onFailure(Call<CurrentBalanceBean> call, Throwable t) {
                Toast.makeText(context, "Current balance no could not be loaded. Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.transDateIc:
            case R.id.transDate:
                DatePickerDialog fromDateDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        selectedDay = dayOfMonth;
                        selectedMonth = monthOfYear + 1;
                        selectedYear = year;

                        calendar.set(year, monthOfYear, dayOfMonth);
                        transDate.setText(dfDMY.format(calendar.getTime()));
                        paymentDate = dfMDY.format(calendar.getTime());
                    }
                },
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                fromDateDialog.getDatePicker().setMaxDate(new Date().getTime());

                fromDateDialog.show();
                break;

            case R.id.addLedger:
            case R.id.addLedgerArrowIc:
                Intent itemsIntent = new Intent(context, PaymentReceiptItemsActivity.class);
                itemsIntent.putExtra("transactionType", reportType);
                itemsIntent.putParcelableArrayListExtra("items", items);
                startActivityForResult(itemsIntent, TRANS_ITEMS_REQUEST_CODE);
                break;

            case R.id.saveBtn:
                if (paymentDate == null || paymentDate.equals("")) {
                    Toast.makeText(context, "Please select transaction date.", Toast.LENGTH_SHORT).show();
                    break;
                }

                if (ledgerId == 0) {
                    Toast.makeText(context, "Please select ledger.", Toast.LENGTH_SHORT).show();
                    break;
                }

                narration = narrationEdt.getText().toString();
                if (narration.equals("")) {
                    Toast.makeText(context, "Please enter narration.", Toast.LENGTH_SHORT).show();
                    break;
                }

                if (totalItems < 1) {
                    Toast.makeText(context, "Please add item.", Toast.LENGTH_SHORT).show();
                    break;
                }

                SavePayment();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            return;
        }

        switch (requestCode) {
            case TRANS_ITEMS_REQUEST_CODE:
                items.clear();

                if (resultCode == RESULT_OK) {
                    items = data.getParcelableArrayListExtra("items");
                    totalAmount = data.getFloatExtra("totalAmount", 0);
                    totalItems = data.getIntExtra("totalItems", 0);

                    txtTotalLedgers.setText(String.valueOf(totalItems));
                }
                UpdateChips();
                break;
        }
    }

    private void UpdateChips() {
        if (items.size() > 0) {
            chipGroup.removeAllViews();

            for (final PaymentReceiptItems item : items) {

                final Chip chip = new Chip(context);
                chip.setText(item.LedgerName);
                chip.setCheckable(false);
                chip.setMinimumWidth(90);
                chip.setClickable(true);
                // chip.setTextAppearanceResource(R.style.ChipTextStyle_Selected);
                chip.setCloseIconVisible(true);
                chipGroup.addView(chip);

                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO: remove chips from UI n array
                        chipGroup.removeView(v);
                        items.remove(item);
                        totalItems = items.size();
                        txtTotalLedgers.setText(String.valueOf(totalItems));
                    }
                });

                chip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        //alert.setTitle(item.LedgerName);
                        final AlertDialog dialog = alert.create();

                        View view = LayoutInflater.from(context).inflate(R.layout.row_expanded_chip, parent, false);

                        final TextView ledgerName = view.findViewById(R.id.ledgerName);
                        final ImageView closeIc = view.findViewById(R.id.closeIc);
                        final TextView chipDesc = view.findViewById(R.id.chipDesc);
                        final TextView chipAmount = view.findViewById(R.id.chipAmount);

                        ledgerName.setAllCaps(true);
                        ledgerName.setText(item.LedgerName);
                        closeIc.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                            }
                        });

                       /* chipDesc.setText("Desc : " + item.getDescription());
                        chipAmount.setText("Amount(Rs) : " + String.valueOf(item.Amount));*/
                        chipDesc.setText(item.getDescription());
                        chipAmount.setText(String.valueOf(item.Amount));

                        dialog.setView(view);
                        dialog.show();

                       /* try {
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        } catch (Exception ex) {

                        }*/
                    }
                });
            }

            //TODO: create chips dynamically
        } else {
            //TODO: remove chips from UI n array
            chipGroup.removeAllViews();
        }
    }

    private void SavePayment() {
        pb.setVisibility(View.VISIBLE);

        PaymentReceiptInputModel input = new PaymentReceiptInputModel();
        input.AcademicYearId = userData.getAcademicyearId();
        input.SchoolId = userData.getSchoolId();
        input.CreatedBy = userData.getUserId();
        input.Items = new Gson().toJson(items);
        input.LedgerName = ledgerName;
        input.Note = narration;
        input.PaidAmount = totalAmount;
        input.ReceiptNo = voucharDetail.getReceiptNo();
        input.TransactionDate = paymentDate;
        input.VoucharNo = voucharDetail.VoucharNo;

        switch (reportType) {
            case Constants.InventoryReportType.PAYMENT:
                input.Mode = Constants.TransactionMode.PAYMENT;
                break;
            case Constants.InventoryReportType.RECEIPT:
                input.Mode = Constants.TransactionMode.RECEIPT;
                break;
        }

        inventoryService.SavePaymentReceipt(input).enqueue(new Callback<Bean>() {
            @Override
            public void onResponse(Call<Bean> call, Response<Bean> response) {
                Bean bean = response.body();
                if (bean != null) {
                    if (bean.rcode == Constants.Rcode.OK) {
                        Toast.makeText(context, "Successfully saved", Toast.LENGTH_SHORT).show();
                        pb.setVisibility(View.GONE);

                        //TODO: Switch to summary Activity
                       /* Intent summaryIntent = new Intent(context, InventoryModuleActivity.class);
                        summaryIntent.putExtra("switchTab", reportType);
                        startActivity(summaryIntent);
                        finish();*/

                    } else {
                        Toast.makeText(context, "Something went wrong.Please try again", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "No response");
                        pb.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<Bean> call, Throwable t) {
                Toast.makeText(context, "Something went wrong.Please try again", Toast.LENGTH_SHORT).show();
                Log.e(TAG, t.getMessage());
                pb.setVisibility(View.GONE);
            }
        });
    }
}