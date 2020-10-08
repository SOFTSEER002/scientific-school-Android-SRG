package com.jeannypr.scientificstudy.Inventory.activity;

import android.content.Context;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Inventory.api.InventoryService;
import com.jeannypr.scientificstudy.Inventory.model.PaymentReceiptSummaryModel;
import com.jeannypr.scientificstudy.Inventory.model.PurchaseSummaryItemsModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;

import java.text.DecimalFormat;
import java.util.List;

public class PaymentReceiptDetailActivity extends AppCompatActivity {

    private InventoryService service;
    private Context context;
    UserModel userdata;
    String transactionType;
    List<PurchaseSummaryItemsModel> items;
    PaymentReceiptSummaryModel transactionDetail;
    Bundle bundle;
    LayoutInflater inflater;
    LinearLayout itemsList;
    TextView grandTotal, rateLbl, quntityLbl, itemLbl;
    ConstraintLayout discountRow, taxRow, graceTotalRow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        setContentView(R.layout.activity_transaction_detail);

        bundle = getIntent().getExtras();
        transactionType = getIntent().getStringExtra("transactionType");

        try {
            transactionDetail = bundle.getParcelable("transactionDetail");
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        userdata = UserPreference.getInstance(context).getUserData();
        service = new DataRepo<>(InventoryService.class, context).getService();
        inflater = LayoutInflater.from(context);

        Toolbar toolbar = findViewById(R.id.toolbar);
        itemsList = findViewById(R.id.itemsList);
        graceTotalRow = findViewById(R.id.graceTotalRow);
        grandTotal = findViewById(R.id.grandTotal);
        taxRow = findViewById(R.id.taxRow);
        discountRow = findViewById(R.id.discountRow);

        itemLbl = findViewById(R.id.itemLbl);
        itemLbl.setText("LEDGER");

        rateLbl = findViewById(R.id.rateLbl);
        rateLbl.setVisibility(View.GONE);
        quntityLbl = findViewById(R.id.qtyLbl);
        quntityLbl.setVisibility(View.GONE);


        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(transactionDetail.LedgerName != null ? transactionDetail.LedgerName : "Transaction detail");
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
        grandTotal.setText(String.valueOf(transactionDetail.Amount));
        taxRow.setVisibility(View.GONE);
        graceTotalRow.setVisibility(View.GONE);
        discountRow.setVisibility(View.GONE);

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
                //  String amount = String.valueOf(item.getAmount() );
                DecimalFormat df = new DecimalFormat("#.00");


                txtItemName.setText(item.Title != null ? item.Title : "");
             /*   txtQty.setText(qty);
                txtRate.setText(rate);*/
                txtAmount.setText(df.format(item.getAmount()));

                itemsList.addView(view);
            }
        }
    }
}
