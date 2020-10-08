package com.jeannypr.scientificstudy.Inventory.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.DropDownModel;
import com.jeannypr.scientificstudy.Base.Model.UserGuidanceDetail;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Base.adapter.DropDownAdapter;
import com.jeannypr.scientificstudy.Inventory.adapter.PaymentReceiptItemAdapter;
import com.jeannypr.scientificstudy.Inventory.api.InventoryService;
import com.jeannypr.scientificstudy.Inventory.model.AccountGroupBean;
import com.jeannypr.scientificstudy.Inventory.model.AccountGroupModel;
import com.jeannypr.scientificstudy.Inventory.model.AddLedgerBean;
import com.jeannypr.scientificstudy.Inventory.model.AddLedgerInputModel;
import com.jeannypr.scientificstudy.Inventory.model.AllLedgerBean;
import com.jeannypr.scientificstudy.Inventory.model.AllLedgerModel;
import com.jeannypr.scientificstudy.Inventory.model.CurrentBalanceBean;
import com.jeannypr.scientificstudy.Inventory.model.CurrentBalanceModel;
import com.jeannypr.scientificstudy.Inventory.model.PaymentReceiptItems;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.FloatingActionButton.MovableFloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentReceiptItemsActivity extends AppCompatActivity implements View.OnClickListener {
    InventoryService service;
    UserModel userData;
    String transactionType;
    Context context;
    ArrayList<PaymentReceiptItems> items;
    RecyclerView rowContainer;
    ArrayList<AllLedgerModel> ledgers;
    MovableFloatingActionButton addItemBtn;
    TextView txtTotalItems, continueBtn, txtTotalAmount;
    int totalItems, accountGroupId;
    String ledgerName, email, mobile, accountGroupName;
    float totalAmount;
    ProgressBar pb;
    PaymentReceiptItemAdapter adapter;
    ConstraintLayout parent, parent1;
    private static String TAG = PaymentReceiptItemsActivity.class.getSimpleName();
    private static final int ADD = 100;
    private static final int EDIT = 101;
    UserPreference pref;
    ArrayList<AccountGroupModel> accountGroupList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_receipt_items);
        context = this;

        pref = UserPreference.getInstance(context);
        userData = pref.getUserData();
        service = new DataRepo<>(InventoryService.class, context).getService();
        ledgers = new ArrayList<>();
        items = new ArrayList<>();
        adapter = new PaymentReceiptItemAdapter(context, items);

        transactionType = getIntent().getStringExtra("transactionType");
        totalItems = items.size();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, transactionType + " Items", "");

        parent = findViewById(R.id.parent);
        rowContainer = findViewById(R.id.rowContainer);
        rowContainer.setAdapter(adapter);
        addItemBtn = findViewById(R.id.addItemBtn);
        txtTotalItems = findViewById(R.id.totalItems);
        pb = findViewById(R.id.pb);
        continueBtn = findViewById(R.id.continueBtn);
        txtTotalAmount = findViewById(R.id.totalAmount);

        continueBtn.setOnClickListener(this);
        addItemBtn.setOnClickListener(this);

        pb.setVisibility(View.VISIBLE);
        GetAllLedgers();
        GetAccountGroupLedgerList();

        UserGuidanceDetail detail = pref.GetUserGuidanceDetail();
        if (detail != null) {
            if (!detail.isAdd_btn_inv_items()) {
                Utility.ShowTapTargetView(context, addItemBtn, "Add Item Button", "Click it to add new item.", 0, R.color.white);
                detail.setAdd_btn_inv_items(true);
                pref.SetUserGuidanceDetail(detail);
            }
        }
    }

    private void GetAllLedgers() {

        pb.setVisibility(View.VISIBLE);

        service.GetAllLedgerDetail(userData.getSchoolId()).enqueue(new Callback<AllLedgerBean>() {
            @Override
            public void onResponse(Call<AllLedgerBean> call, Response<AllLedgerBean> response) {
                AllLedgerBean resp = response.body();

                ledgers.clear();
                if (resp != null && resp.data != null) {

                    if (resp.rcode == Constants.Rcode.OK) {
                        ledgers = resp.data;
                        InflateItemRow();

                    } else {
                        Toast.makeText(context, "Ledger could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<AllLedgerBean> call, Throwable t) {
                Toast.makeText(context, "Ledger could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                pb.setVisibility(View.GONE);
            }
        });
    }

    private CurrentBalanceModel GetLedgerDetail(int ledgerId) {
        final CurrentBalanceModel[] ledgerDetail = {new CurrentBalanceModel()};

        service.GetLedgerDetail(userData.getSchoolId(), ledgerId).enqueue(new Callback<CurrentBalanceBean>() {
            @Override
            public void onResponse(Call<CurrentBalanceBean> call, Response<CurrentBalanceBean> response) {

                CurrentBalanceBean resp = response.body();
                if (resp != null) {
                    if (resp.rcode == Constants.Rcode.OK) {

                        if (resp.data != null) {
                            ledgerDetail[0] = resp.data;

                        }
                    } else if (resp.rcode == Constants.Rcode.NORECORDS) {
                        Toast.makeText(context, "No Record Found", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "No Record Found", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(context, "Somthing went worng. Please try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CurrentBalanceBean> call, Throwable t) {
                Toast.makeText(context, "Current balance no could not be loaded. Please try again", Toast.LENGTH_SHORT).show();
            }
        });

        return ledgerDetail[0];
    }

    @Override
    public boolean onSupportNavigateUp() {
        //pass data to previous screen
     /*   Intent transIntent = new Intent(context, PaymentReceiptTransactionActivity.class);
        transIntent.putExtra("totalAmount", totalAmount);
        transIntent.putExtra("totalItems", totalItems);

        if (totalItems > 0) {
            transIntent.putParcelableArrayListExtra("items", items);
            setResult(RESULT_OK, transIntent);
        } else {
            setResult(RESULT_FIRST_USER, transIntent);
        }
        finish();*/

        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.addItemBtn:
                if (ledgers.size() < 1) {
                    break;
                }

                PaymentReceiptItems item = new PaymentReceiptItems();
                GetInputFromUser(0, ADD, item);
                break;

            case R.id.continueBtn:
                Intent transIntent = new Intent(context, PaymentReceiptTransactionActivity.class);
                transIntent.putExtra("totalAmount", totalAmount);
                transIntent.putExtra("totalItems", totalItems);

                if (totalItems > 0) {
                    transIntent.putParcelableArrayListExtra("items", items);
                    setResult(RESULT_OK, transIntent);
                } else {
                    setResult(RESULT_FIRST_USER, transIntent);
                }
                finish();
                break;

            case R.id.addCreditLedgerIc:
                GetInputLedgerFromUser(ADD);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void InflateItemRow() {
       /* totalItems = items.size();
        if (totalItems > 0) {*/
           /* int index = 1;
            for (PaymentReceiptItems item : items) {
                item.RowIndex = index;
                AddNewRow(item);
                index++;
            }*/
        items.clear();
        ArrayList<PaymentReceiptItems> temp = getIntent().getParcelableArrayListExtra("items");
        for (PaymentReceiptItems transactionItems : temp) {
            items.add(transactionItems);
        }
        adapter.notifyDataSetChanged();
        //     }
        UpdateSubheader();
    }

    private void GetInputFromUser(final int adapterPosition, final int action, final PaymentReceiptItems item) {

        final ArrayList<DropDownModel> ledgersList = new ArrayList<>();
        final DropDownModel[] model = {new DropDownModel()};
        model[0].setId(0);
        model[0].setText("Select Ledger");
        ledgersList.add(model[0]);

        for (AllLedgerModel ledger : ledgers) {
            DropDownModel dropDownModel = new DropDownModel();
            dropDownModel.setId(ledger.Id);
            dropDownModel.setText(ledger.LedegrName);
            ledgersList.add(dropDownModel);
        }
        final DropDownAdapter ledgerAdapter = new DropDownAdapter(context, R.layout.row_spinner, ledgersList);
       /* final AutoCompleteDropDownAdapter ledgerAdapter = new AutoCompleteDropDownAdapter(context, R.layout.row_spinner, ledgersList,
                new AutoCompleteDropDownAdapter.OnSelectListner() {
                    @Override
                    public void OnSelect(DropDownModel selectedLedger) {
                        final int[] ledgerId = new int[1];
                        final String[] ledgerName = new String[1];
                        ledgerId[0] = 0;
                        ledgerName[0] = "";
                        final CurrentBalanceModel[] ledgerDetail = {new CurrentBalanceModel()};

                        if (selectedLedger.getId() != 0) {

                            ledgerId[0] = selectedLedger.getId();
                            ledgerName[0] = selectedLedger.getText();

                            ledgerDetail[0] = GetLedgerDetail(ledgerId[0]);

                            float balance = ledgerDetail[0].getDebit() - ledgerDetail[0].getCredit();
                            if (balance != 0) {
                                String type = "";

                                if (ledgerDetail[0].Debit > ledgerDetail[0].Credit) {
                                    type = " Dr.";
                                } else if (ledgerDetail[0].Credit > ledgerDetail[0].Debit) {
                                    type = " Cr.";
                                }
                                item.CurrentBalance = String.valueOf(balance) + type;
//                                txtLedgerCurrentBal.setText("Current balance : " + item.CurrentBalance);
//                                txtLedgerCurrentBal.setVisibility(View.VISIBLE);

                            } else {
                                item.CurrentBalance = "";
//                                txtLedgerCurrentBal.setVisibility(View.GONE);
                            }

                        } else {
                            ledgerId[0] = 0;
                            ledgerName[0] = "";
                        }

                        item.LedgerId = ledgerId[0];
                        item.LedgerName = ledgerName[0];

                    }
                });*/

        final int[] ledgerId = new int[1];
        final String[] ledgerName = new String[1];
        ledgerId[0] = 0;
        ledgerName[0] = "";
        final CurrentBalanceModel[] ledgerDetail = {new CurrentBalanceModel()};

        final View view = LayoutInflater.from(context).inflate(R.layout.row_payment_receipt_item_input, parent, false);
        final TextView txtLedgerCurrentBal = view.findViewById(R.id.currentBal_dialog);
        final Spinner ledgersSpn = view.findViewById(R.id.ledgersSpn_dialog);
        //   final AutoCompleteTextView ledgersSpn = view.findViewById(R.id.ledgersSpn_dialog);
        final EditText desc = view.findViewById(R.id.desc_dialog);
        final EditText itemAmount = view.findViewById(R.id.itemAmount_dialog);

        final ImageView addCreditLedgerIc = view.findViewById(R.id.addCreditLedgerIc);

        addCreditLedgerIc.setOnClickListener(this);

        Utility.ShowTapTargetView(context, addCreditLedgerIc, "Add Item Button", "Click it to add new item.", 0, R.color.white);

        ledgersSpn.setAdapter(ledgerAdapter);

        ledgerAdapter.notifyDataSetChanged();
        ledgersSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DropDownModel selectedLedger = ledgerAdapter.getItem(position);

                if (selectedLedger.getId() != 0) {

                    ledgerId[0] = selectedLedger.getId();
                    ledgerName[0] = selectedLedger.getText();

                    ledgerDetail[0] = GetLedgerDetail(ledgerId[0]);

                    float balance = ledgerDetail[0].getDebit() - ledgerDetail[0].getCredit();
                    if (balance != 0) {
                        String type = "";

                        if (ledgerDetail[0].Debit > ledgerDetail[0].Credit) {
                            type = " Dr.";
                        } else if (ledgerDetail[0].Credit > ledgerDetail[0].Debit) {
                            type = " Cr.";
                        }
                        item.CurrentBalance = String.valueOf(balance) + type;
                        txtLedgerCurrentBal.setText("Current balance : " + item.CurrentBalance);
                        txtLedgerCurrentBal.setVisibility(View.VISIBLE);

                    } else {
                        item.CurrentBalance = "";
                        txtLedgerCurrentBal.setVisibility(View.GONE);
                    }

                } else {
                    ledgerId[0] = 0;
                    ledgerName[0] = "";
                }

                item.LedgerId = ledgerId[0];
                item.LedgerName = ledgerName[0];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

/*            @Override
            public void onItemSelect(AdapterView<?> parent, View view, int position, long id) {

//                int index = ledgers.indexOf(ledgersSpn.getText().toString());
//                Log.i("AutocompleteTv itemid-", String.valueOf(index));
                //     Log.i("ACTV :", String.valueOf(id));

                DropDownModel selectedLedger = ledgerAdapter.getItem(position);

                //  if (temp != null) {
                // DropDownModel selectedLedger = (DropDownModel) temp;
                if (selectedLedger.getId() != 0) {

                    ledgerId[0] = selectedLedger.getId();
                    ledgerName[0] = selectedLedger.getText();

                    ledgerDetail[0] = GetLedgerDetail(ledgerId[0]);

                    float balance = ledgerDetail[0].getDebit() - ledgerDetail[0].getCredit();
                    if (balance != 0) {
                        String type = "";

                        if (ledgerDetail[0].Debit > ledgerDetail[0].Credit) {
                            type = " Dr.";
                        } else if (ledgerDetail[0].Credit > ledgerDetail[0].Debit) {
                            type = " Cr.";
                        }
                        item.CurrentBalance = String.valueOf(balance) + type;
                        txtLedgerCurrentBal.setText("Current balance : " + item.CurrentBalance);
                        txtLedgerCurrentBal.setVisibility(View.VISIBLE);

                    } else {
                        item.CurrentBalance = "";
                        txtLedgerCurrentBal.setVisibility(View.GONE);
                    }

                } else {
                    ledgerId[0] = 0;
                    ledgerName[0] = "";
                }

                item.LedgerId = ledgerId[0];
                item.LedgerName = ledgerName[0];
                // }
            }*/
        });

        String dialogTitle = "";
        switch (action) {
            case ADD:
                dialogTitle = "Enter item detail";
                break;

            case EDIT:
                dialogTitle = "Edit item detail";
                ledgersSpn.setSelection(GetIndex(item.getLedgerId()));
                desc.setText(item.getDescription());
                itemAmount.setText(String.valueOf(item.getAmount()));
                break;
        }

        final AlertDialog dialog = new AlertDialog.Builder(context)

                .setTitle(dialogTitle)
                /* .setPositiveButton("OK", null)

                 .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         dialog.dismiss();
                     }
                 })*/

                .setView(view)
                .show();

      /*  Button positiveBtn = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                item.Description = desc.getText().toString();
                String amount = itemAmount.getText().toString();
                item.Amount = amount.equals("") ? 0 : Float.valueOf(amount);

                if (CheckValidation(item)) {

                    switch (action) {
                        case ADD:
                            items.add(item);
                            //    item.RowIndex = items.size();
                            adapter.notifyItemInserted(items.size() - 1);
                            //      AddNewRow(item);
                            break;

                        case EDIT:
                            adapter.notifyItemChanged(adapterPosition, item);
                            break;
                    }

                    dialog.dismiss();
                    UpdateSubheader();

                } else {
                    Log.i(TAG, "Invalid form");
                }
            }
        });*/
        Button positiveBtn = view.findViewById(R.id.positiveBtn);
        Button negativeBtn = view.findViewById(R.id.negativeBtn);

        positiveBtn.setText(R.string.dialogPositiveButtonOk);
        negativeBtn.setText(R.string.dialogNegativeButtonCancel);

        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.Description = desc.getText().toString();
                String amount = itemAmount.getText().toString();
                item.Amount = amount.equals("") ? 0 : Float.valueOf(amount);

                if (CheckValidation(item)) {

                    switch (action) {
                        case ADD:
                            items.add(item);
                            //    item.RowIndex = items.size();
                            adapter.notifyItemInserted(items.size() - 1);
                            //      AddNewRow(item);
                            break;

                        case EDIT:
                            adapter.notifyItemChanged(adapterPosition, item);
                            break;
                    }

                    dialog.dismiss();
                    UpdateSubheader();

                } else {
                    Log.i(TAG, "Invalid form");
                }
            }
        });
        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    private void UpdateSubheader() {
        totalItems = items.size();
        totalAmount = 0;

        for (PaymentReceiptItems item : items) {
            totalAmount += item.getAmount();
        }

        //  txtTotalItems.setText(String.valueOf(totalItems));
        txtTotalAmount.setText(String.valueOf(totalAmount));
    }

    private void AddNewRow(final PaymentReceiptItems item) {
        final ConstraintLayout view = (ConstraintLayout) LayoutInflater.from(context).inflate(R.layout.row_payment_receipt_item, rowContainer, false);

        final ConstraintLayout row = view.findViewById(R.id.itemRow);
        final TextView ledgerName = view.findViewById(R.id.ledgerName);
        final TextView currentBal = view.findViewById(R.id.currentBal);
        final TextView desc = view.findViewById(R.id.desc);
        final TextView amount = view.findViewById(R.id.amount);
        final TextView txtIndex = view.findViewById(R.id.index);

        ledgerName.setText(item.getLedgerName());
        if (!item.getCurrentBalance().equals("")) {
            currentBal.setText("Current balance : " + item.getCurrentBalance());
            currentBal.setVisibility(View.VISIBLE);
        } else {
            currentBal.setVisibility(View.GONE);
        }
        desc.setText(item.getDescription());
        amount.setText("Rs. " + String.valueOf(item.getAmount()));
        //  item.RowIndex = index;
        txtIndex.setText(String.valueOf(item.getRowIndex()));

        rowContainer.addView(view);

        registerForContextMenu(row);
    }

    private boolean CheckValidation(PaymentReceiptItems item) {
        if (item.getLedgerId() == 0) {
            Toast.makeText(context, "Please select ledger", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (item.getAmount() == 0) {
            Toast.makeText(context, "Please enter amount", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public boolean onContextItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case Constants.InventoryItemsContextualMenu.EDIT:
                PaymentReceiptItems item = items.get(menuItem.getGroupId());
                GetInputFromUser(menuItem.getGroupId(), EDIT, item);
                break;

            case Constants.InventoryItemsContextualMenu.DELETE:
                boolean isRemoved = adapter.RemoveItem(menuItem.getGroupId());
                String msg = isRemoved ? "Successfully deleted." : "Failed to delete";
                DisplayMsg(msg);
                UpdateSubheader();
                break;
        }
        return super.onContextItemSelected(menuItem);
    }

    private void DisplayMsg(String msg) {
        Snackbar.make(parent, msg, Snackbar.LENGTH_SHORT).show();
    }

    private int GetIndex(int ledgerId) {
        int index = 1;
        boolean match = false;
        for (AllLedgerModel ledger : ledgers) {
            if (ledger.Id == ledgerId) {
                match = true;
                break;
            }
            index++;
        }
        return match ? index : 0;
    }

    private void GetInputLedgerFromUser(final int action) {

        ArrayList<DropDownModel> groupLedger = new ArrayList<>();
        final DropDownModel model = new DropDownModel();
        model.setId(0);
        model.setText("Select Group Type");
        groupLedger.add(model);

        for (AccountGroupModel ledger : accountGroupList) {
            DropDownModel dropDownModel = new DropDownModel();
            dropDownModel.setId(ledger.Id);
            dropDownModel.setText(ledger.Title);
            groupLedger.add(dropDownModel);
        }

        final DropDownAdapter creditLedgerAdapter = new DropDownAdapter(context, R.layout.row_spinner, groupLedger);

        final View view = LayoutInflater.from(context).inflate(R.layout.row_group_ledger_item_input, parent1, false);
        final Spinner ledgersSpn = view.findViewById(R.id.ledgersSpn_dialog);
        final EditText name_dec = view.findViewById(R.id.ledgerName_dialog);
        final EditText email_dec = view.findViewById(R.id.email_dialog);
        final EditText mobile_dec = view.findViewById(R.id.mobile_dialog);

        ledgersSpn.setAdapter(creditLedgerAdapter);
        ledgersSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int potision, long id) {
                DropDownModel selectGroup = creditLedgerAdapter.getItem(potision);

                if (selectGroup != null) {
                    if (selectGroup.getId() != 0) {
                        accountGroupId = selectGroup.getId();
                        accountGroupName = selectGroup.getText();
                    } else {
                        accountGroupId = 0;
                        accountGroupName = "";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                accountGroupId = 0;
                accountGroupName = "";
            }
        });


        String dialogTitle = "";
        switch (action) {
            case ADD:
                dialogTitle = "Add New Ledger";
                break;

        }

        final android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(context)
                .setTitle(dialogTitle)
                .setPositiveButton("Add", null)

                .setView(view)
                .show();

        Button positiveBtn = dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE);
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ledgerName = name_dec.getText().toString();
                mobile = mobile_dec.getText().toString();
                email = email_dec.getText().toString();
                boolean isValid = Validation();
                if (isValid) {
                    GetSaveLedger();
                }
                dialog.dismiss();
            }
        });
    }

    private void GetAccountGroupLedgerList() {

        service.GetAccountGroupDetail(userData.getSchoolId()).enqueue(new Callback<AccountGroupBean>() {
            @Override
            public void onResponse(Call<AccountGroupBean> call, Response<AccountGroupBean> response) {

                AccountGroupBean resp = response.body();
                if (resp != null) {

                    if (resp.rcode == Constants.Rcode.OK) {
                        accountGroupList = resp.data;

                    } else {
                        Toast.makeText(context, "Account Group could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<AccountGroupBean> call, Throwable t) {
                Toast.makeText(context, "Something went wrong.Please try again.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void GetSaveLedger() {
        pb.setVisibility(View.VISIBLE);

        AddLedgerInputModel input = new AddLedgerInputModel();
        input.setAcademicYearId(userData.getAcademicyearId());
        input.setSchoolId(userData.getSchoolId());
        input.setCreatedBy(userData.getUserId());
        input.setName(ledgerName);
        input.setGroupId(accountGroupId);
        input.setMobile(mobile);
        input.setEmail(email);

        service.SaveLedger(input).enqueue(new Callback<AddLedgerBean>() {
            @Override
            public void onResponse(Call<AddLedgerBean> call, Response<AddLedgerBean> response) {
                AddLedgerBean bean = response.body();
                if (bean != null) {
                    if (bean.rcode == Constants.Rcode.OK) {
                        Toast.makeText(context, "Successfully saved", Toast.LENGTH_SHORT).show();
                        GetAllLedgers();

                    } else {
                        Toast.makeText(context, "Failed to add ledger", Toast.LENGTH_SHORT).show();

                    }

                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<AddLedgerBean> call, Throwable t) {
                Toast.makeText(context, "Something went wrong.Please try again", Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.GONE);
            }
        });
    }

    private boolean Validation() {
        if (accountGroupId == 0) {
            Toast.makeText(context, "Please select ledger", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (ledgerName.equals("")) {
            Toast.makeText(context, "Please enter ledger name", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}