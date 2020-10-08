package com.jeannypr.scientificstudy.Inventory.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.DropDownModel;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Base.adapter.DropDownAdapter;
import com.jeannypr.scientificstudy.Inventory.adapter.PurchaseSaletItemAdapter;
import com.jeannypr.scientificstudy.Inventory.api.InventoryService;
import com.jeannypr.scientificstudy.Inventory.model.ItemDetailsBean;
import com.jeannypr.scientificstudy.Inventory.model.ItemDetailsModel;
import com.jeannypr.scientificstudy.Inventory.model.ItemsBean;
import com.jeannypr.scientificstudy.Inventory.model.ItemsModel;
import com.jeannypr.scientificstudy.Inventory.model.DiscountModel;
import com.jeannypr.scientificstudy.Inventory.model.PaymentReceiptItems;
import com.jeannypr.scientificstudy.Inventory.model.PurchaseSaleItems;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.FloatingActionButton.MovableFloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchaseSaleItemsActivity extends AppCompatActivity implements View.OnClickListener {
    InventoryService service;
    UserModel userData;
    String transactionType, discontName;
    Context context;
    ArrayList<PurchaseSaleItems> items;
    RecyclerView rowContainer;
    ArrayList<ItemsModel> ledgers;
    MovableFloatingActionButton addItemBtn;
    TextView txtTotalItems, continueBtn, txtTotalAmount, txtDiscountVal, txtGrandtval;
    EditText txtDiscounttype, txtAmountval;
    int totalItems, discontId;
    float totalAmount, totalDiscount;
    ProgressBar pb;
    PurchaseSaletItemAdapter adapter;
    ConstraintLayout parent;
    private static String TAG = PurchaseSaleItemsActivity.class.getSimpleName();
    Spinner discountList;
    DropDownModel selectedDiscount;
    ArrayList<DropDownModel> dropDownModels;
    DropDownAdapter dropDownAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_sale_items);
        context = this;

        userData = UserPreference.getInstance(context).getUserData();
        service = new DataRepo<>(InventoryService.class, context).getService();
        ledgers = new ArrayList<>();
        items = new ArrayList<>();
        adapter = new PurchaseSaletItemAdapter(context, items);

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
        discountList = findViewById(R.id.discountList);
        txtGrandtval = findViewById(R.id.txtGrandtval);
        txtDiscountVal = findViewById(R.id.txtDiscountVal);
        txtDiscounttype = findViewById(R.id.txtDiscounttype);
        txtAmountval = findViewById(R.id.txtAmountval);

        dropDownModels = new ArrayList<>();
        dropDownAdapter = new DropDownAdapter(context, R.layout.row_spinner, dropDownModels);

        discountList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selectedDiscount = dropDownAdapter.getItem(position);
                if (selectedDiscount != null) {

                    if (selectedDiscount.getId() != 0) {
                        discontId = selectedDiscount.getId();
                        discontName = selectedDiscount.getText();
                        Discount();
                    } else {
                        discontId = 0;
                        discontName = "";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        txtDiscountVal.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                return true;
            }
        });
        txtDiscounttype.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int befor, int count) {
                // Discount();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        continueBtn.setOnClickListener(this);
        addItemBtn.setOnClickListener(this);

        pb.setVisibility(View.VISIBLE);
        GetAllItems();
        GetDiscount();
    }

    private void GetDiscount() {
        DropDownModel model = new DropDownModel();
        model.setId(0);
        model.setText("Select Ledger");
        dropDownModels.add(model);

        discountList.setAdapter(dropDownAdapter);


        ArrayList<DiscountModel> arrayList = new ArrayList<>();
        arrayList.add(new DiscountModel(1, "Parcentage"));
        arrayList.add(new DiscountModel(2, "Flat"));

        for (DiscountModel discountModel : arrayList) {
            DropDownModel model1 = new DropDownModel();
            model1.setId(discountModel.Id);
            model1.setText(discountModel.Name);
            dropDownModels.add(model1);
        }
        dropDownAdapter.notifyDataSetChanged();

    }

    private void Discount() {

        String discountPar = txtDiscounttype.getText().toString();
        float discountParcentage = Float.valueOf(discountPar);
        float grandTotalDis;

        switch (discontName) {
            case Constants.InventoryDiscountTypes.PERCENTAGE:
                totalDiscount = totalAmount * discountParcentage / 100;
                grandTotalDis = totalAmount - totalDiscount;
                txtGrandtval.setText(String.valueOf(grandTotalDis));

                break;

            case Constants.InventoryDiscountTypes.FLAT:
                totalDiscount = totalAmount - discountParcentage;
                grandTotalDis = totalAmount - totalDiscount;
                txtGrandtval.setText(String.valueOf(grandTotalDis));
                break;
        }
    }


    private void GetAllItems() {
        service.GetItems(userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<ItemsBean>() {
            @Override
            public void onResponse(Call<ItemsBean> call, Response<ItemsBean> response) {
                ItemsBean resp = response.body();

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
            public void onFailure(Call<ItemsBean> call, Throwable t) {
                Toast.makeText(context, "Ledger could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                pb.setVisibility(View.GONE);
            }
        });
    }

    private ItemDetailsModel GetItemDetail(int ledgerId) {
        final ItemDetailsModel[] itemDetail = {new ItemDetailsModel()};

        service.GetItemDetails(userData.getSchoolId(), userData.getAcademicyearId(), ledgerId).enqueue(new Callback<ItemDetailsBean>() {
            @Override
            public void onResponse(Call<ItemDetailsBean> call, Response<ItemDetailsBean> response) {

                ItemDetailsBean resp = response.body();
                if (resp != null) {
                    if (resp.rcode == Constants.Rcode.OK) {

                        if (resp.data != null) {
                            itemDetail[0] = resp.data;

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
            public void onFailure(Call<ItemDetailsBean> call, Throwable t) {
                Toast.makeText(context, "Current balance no could not be loaded. Please try again", Toast.LENGTH_SHORT).show();
            }
        });

        return itemDetail[0];
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
                    return;
                }

                GetInputFromUser();
                break;

            case R.id.continueBtn:
                Intent transIntent = new Intent(context, PurchaseSaleTransactionActivity.class);
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

        }
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
        ArrayList<PurchaseSaleItems> temp = getIntent().getParcelableArrayListExtra("items");
        for (PurchaseSaleItems transactionItems : temp) {
            items.add(transactionItems);
        }
        adapter.notifyDataSetChanged();
        //     }
        UpdateSubheader();
    }

    private void GetInputFromUser() {
        final PurchaseSaleItems item = new PurchaseSaleItems();
        ArrayList<DropDownModel> ledgersList = new ArrayList<>();
        final DropDownAdapter ledgerAdapter = new DropDownAdapter(context, R.layout.row_spinner, ledgersList);
        final int[] ledgerId = new int[1];
        final String[] ledgerName = new String[1];
        final ItemDetailsModel[] ledgerDetail = {new ItemDetailsModel()};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder.create();
        builder.setTitle("Enter item detail");

        View view = LayoutInflater.from(context).inflate(R.layout.row_purchase_sale_item_input, parent, false);

        final TextView txtLedgerCurrentBal = view.findViewById(R.id.currentBal_dialog);
        final Spinner ledgersSpn = view.findViewById(R.id.ledgersSpn_dialog);
        final EditText desc = view.findViewById(R.id.desc_dialog);
        final EditText itemAmount = view.findViewById(R.id.itemAmount_dialog);
        final EditText qtyEdt = view.findViewById(R.id.qty_dialog);
        final EditText rateEdt = view.findViewById(R.id.rate_dialog);

        final DropDownModel[] model = {new DropDownModel()};
        model[0].setId(0);
        model[0].setText("Select Item");
        ledgersList.add(model[0]);
        ledgersSpn.setAdapter(ledgerAdapter);

        for (ItemsModel ledger : ledgers) {
            DropDownModel dropDownModel = new DropDownModel();
            dropDownModel.setId(ledger.ItemId);
            dropDownModel.setText(ledger.ItemName);
            ledgersList.add(dropDownModel);
        }
        ledgerAdapter.notifyDataSetChanged();

        ledgersSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                DropDownModel selectedLedger = ledgerAdapter.getItem(position);

                if (selectedLedger != null) {
                    if (selectedLedger.getId() != 0) {

                        ledgerId[0] = selectedLedger.getId();
                        ledgerName[0] = selectedLedger.getText();

                        ledgerDetail[0] = GetItemDetail(ledgerId[0]);
                        rateEdt.setText(String.valueOf(ledgerDetail[0].Rate));

                    } else {
                        ledgerId[0] = 0;
                        ledgerName[0] = "";
                    }

                    item.ItemId = ledgerId[0];
                    item.Title = ledgerName[0];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                ledgerId[0] = 0;
                ledgerName[0] = "";
            }
        });

     /*   builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                item.Description = desc.getText().toString();
                String amount = itemAmount.getText().toString();
                item.Amount = amount.equals("") ? 0 : Float.valueOf(amount);

                String qty = qtyEdt.getText().toString();
                item.Quantity = qty.equals("") ? 0 : Float.valueOf(qty);

                String rate = rateEdt.getText().toString();
                item.Rate = rate.equals("") ? 0 : Float.valueOf(rate);

                if (CheckValidation(item)) {
                    items.add(item);
                    //    item.RowIndex = items.size();

                    adapter.notifyItemInserted(items.size() - 1);
                    //      AddNewRow(item);
                    dialog.dismiss();
                    UpdateSubheader();

                } else {
                    Log.i(TAG, "Invalid form");
                }
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });*/

        builder.setView(view);
        builder.show();

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

                String qty = qtyEdt.getText().toString();
                item.Quantity = qty.equals("") ? 0 : Float.valueOf(qty);

                String rate = rateEdt.getText().toString();
                item.Rate = rate.equals("") ? 0 : Float.valueOf(rate);

                if (CheckValidation(item)) {
                    items.add(item);
                    //    item.RowIndex = items.size();

                    adapter.notifyItemInserted(items.size() - 1);
                    //      AddNewRow(item);
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
        for (PurchaseSaleItems item : items) {
            totalAmount += item.getAmount();
        }

        txtTotalItems.setText(String.valueOf(totalItems));
        txtTotalAmount.setText(String.valueOf(totalAmount));
        txtGrandtval.setText(String.valueOf(totalAmount));
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

    private boolean CheckValidation(PurchaseSaleItems item) {
        if (item.getLedgerId() == 0) {
            Toast.makeText(context, "Please select ledger", Toast.LENGTH_SHORT).show();
            return false;
        }

       /* if (item.getAmount() == 0) {
            Toast.makeText(context, "Please enter amount", Toast.LENGTH_SHORT).show();
            return false;
        }*/

        if (item.getQuantity() == 0) {
            Toast.makeText(context, "Please enter quantity", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (item.getRate() == 0) {
            Toast.makeText(context, "Please enter rate", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.inventory_item_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onContextItemSelected(item);
    }
}
