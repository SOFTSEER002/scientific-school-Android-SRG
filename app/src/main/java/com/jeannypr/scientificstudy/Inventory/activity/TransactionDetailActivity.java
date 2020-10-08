package com.jeannypr.scientificstudy.Inventory.activity;

import android.content.Context;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Inventory.api.InventoryService;
import com.jeannypr.scientificstudy.Inventory.model.PurchaseSummaryItemsModel;
import com.jeannypr.scientificstudy.Inventory.model.PurchaseSummaryModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;

import java.util.List;

public class TransactionDetailActivity extends AppCompatActivity {

    private InventoryService service;
    private Context context;
    UserModel userdata;
    String partyAccount;
    List<PurchaseSummaryItemsModel> items;
    PurchaseSummaryModel transactionDetail;
    Bundle bundle;
    LayoutInflater inflater;
    LinearLayout itemsList;
    TextView grandTotal, discount, tax, graceTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        setContentView(R.layout.activity_transaction_detail);

        bundle = getIntent().getExtras();
        try {
            transactionDetail = bundle.getParcelable("transactionDetail");
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        userdata = UserPreference.getInstance(context).getUserData();
        service = new DataRepo<>(InventoryService.class, context).getService();
        inflater = LayoutInflater.from(context);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        itemsList = findViewById(R.id.itemsList);
        graceTotal = findViewById(R.id.graceTotal);
        grandTotal = findViewById(R.id.grandTotal);
        tax = findViewById(R.id.tax);
        discount = findViewById(R.id.discount);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(transactionDetail.PartyAccount != null ? transactionDetail.PartyAccount : "Transaction detail");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        SetData();
        InflateItems();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void SetData() {
        graceTotal.setText(String.valueOf(transactionDetail.GraceTotal));
        grandTotal.setText(String.valueOf(transactionDetail.GrandTotal));
        tax.setText(String.valueOf(transactionDetail.Tax));
        discount.setText(String.valueOf(transactionDetail.Discount));

    }

    private void InflateItems() {
        int totalItems = transactionDetail.Items.size();
        if (totalItems > 0) {
            for (PurchaseSummaryItemsModel item : transactionDetail.Items) {

                ConstraintLayout view = (ConstraintLayout) inflater.inflate(R.layout.row_transaction_detail, itemsList, false);
                final TextView txtItemName = view.findViewById(R.id.item);
                final TextView txtQty = view.findViewById(R.id.qty);
                final TextView txtRate = view.findViewById(R.id.rate);
                final TextView txtAmount = view.findViewById(R.id.amount);

                String qty = String.valueOf(item.Quantity != -1 ? item.Quantity : 0);
                String rate = String.valueOf(item.Rate != -1 ? item.Rate : 0);
                String amount = String.valueOf(item.Amount != -1 ? item.Amount : 0);

                txtItemName.setText(item.Title != null ? item.Title : "");
                txtQty.setText(qty);
                txtRate.setText(rate);
                txtAmount.setText(amount);

                itemsList.addView(view);
            }
        }
    }
}
