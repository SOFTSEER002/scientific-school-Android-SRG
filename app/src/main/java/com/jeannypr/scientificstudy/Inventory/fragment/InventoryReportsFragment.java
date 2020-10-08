package com.jeannypr.scientificstudy.Inventory.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Inventory.activity.DayBookActivity;

import com.jeannypr.scientificstudy.Inventory.activity.LedgerReportActivity;
import com.jeannypr.scientificstudy.Inventory.activity.PurchaseSaleSummaryActivity;
import com.jeannypr.scientificstudy.R;

public class InventoryReportsFragment extends Fragment implements View.OnClickListener {
    private Context context;
    private View view;
    String reportType, moduleType;
   // ConstraintLayout dayBookBtn, purchaseSaleBtn, ledgerReportBtn, saleSummaryBtn;
    ImageView dayBookIc, purchaseSaleIc, saleSummaryIc, ledgerIc;
    TextView dayBook, purchaseSale, saleSummary, saleLeger;
    View purchaseDiv, saleDiv, ledgerDiv;

    public InventoryReportsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        Bundle bundle = getArguments();
        if (bundle != null) {
            reportType = bundle.getString("transactionType");
            moduleType = bundle.getString("moduleType");
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_inventory_report,
                container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /*dayBookBtn = view.findViewById(R.id.dayBookBtn);
        dayBookBtn.setOnClickListener(this);*/

        dayBookIc = view.findViewById(R.id.dayBookIc);
        dayBook = view.findViewById(R.id.dayBook);
        dayBookIc.setOnClickListener(this);
        dayBook.setOnClickListener(this);

        switch (moduleType) {
            case Constants.Module.ACCOUNTS:
                saleLeger = view.findViewById(R.id.saleLeger);
                saleLeger.setOnClickListener(this);

                ledgerIc = view.findViewById(R.id.ledgerIc);
                ledgerIc.setOnClickListener(this);

                ledgerDiv = view.findViewById(R.id.ledgerDiv);
                ledgerDiv.setOnClickListener(this);

                saleLeger.setVisibility(View.VISIBLE);
                ledgerIc.setVisibility(View.VISIBLE);
                ledgerDiv.setVisibility(View.VISIBLE);
                break;

            case Constants.Module.INVENTORY:
                purchaseSale = view.findViewById(R.id.purchaseSale);
                purchaseSale.setOnClickListener(this);

                purchaseSaleIc = view.findViewById(R.id.purchaseSaleIc);
                purchaseSaleIc.setOnClickListener(this);

                purchaseDiv = view.findViewById(R.id.purchaseDiv);
                purchaseDiv.setOnClickListener(this);

                saleSummary = view.findViewById(R.id.saleSummary);
                saleSummary.setOnClickListener(this);

                saleSummaryIc = view.findViewById(R.id.saleSummaryIc);
                saleSummaryIc.setOnClickListener(this);

                saleDiv = view.findViewById(R.id.saleDiv);
                saleDiv.setOnClickListener(this);

                purchaseSale.setVisibility(View.VISIBLE);
                purchaseSaleIc.setVisibility(View.VISIBLE);
                saleSummary.setVisibility(View.VISIBLE);
                saleSummaryIc.setVisibility(View.VISIBLE);
                saleDiv.setVisibility(View.VISIBLE);
                purchaseDiv.setVisibility(View.VISIBLE);
                break;
         /*   case Constants.Module.ACCOUNTS:
                ledgerReportBtn = view.findViewById(R.id.ledgerReportBtn);
                ledgerReportBtn.setOnClickListener(this);
                ledgerReportBtn.setVisibility(View.VISIBLE);
                break;

            case Constants.Module.INVENTORY:
                purchaseSaleBtn = view.findViewById(R.id.purchaseSaleBtn);
                purchaseSaleBtn.setOnClickListener(this);

                saleSummaryBtn = view.findViewById(R.id.saleSummaryBtn);
                saleSummaryBtn.setOnClickListener(this);

                purchaseSaleBtn.setVisibility(View.VISIBLE);
                saleSummaryBtn.setVisibility(View.VISIBLE);
                break;*/
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.dayBookIc:
            case R.id.dayBook:
                Intent dayBookIntent = new Intent(context, DayBookActivity.class);
                startActivity(dayBookIntent);
                break;

            case R.id.purchaseSaleIc:
            case R.id.purchaseSale:
                Intent purchaseSaleIntent = new Intent(context, PurchaseSaleSummaryActivity.class);
                purchaseSaleIntent.putExtra("transactionType", Constants.InventoryReportType.PURCHASE);
                startActivity(purchaseSaleIntent);
                break;

            case R.id.saleSummaryIc:
            case R.id.saleSummary:
                Intent saleIntent = new Intent(context, PurchaseSaleSummaryActivity.class);
                saleIntent.putExtra("transactionType", Constants.InventoryReportType.SALE);
                startActivity(saleIntent);
                break;

            case R.id.ledgerIc:
            case R.id.saleLeger:
                Intent ledgerIntent = new Intent(context, LedgerReportActivity.class);
                startActivity(ledgerIntent);
                break;

          /*  case R.id.dayBookBtn:
                Intent dayBookIntent = new Intent(context, DayBookActivity.class);
                startActivity(dayBookIntent);
                break;

            case R.id.purchaseSaleBtn:
                Intent purchaseSaleIntent = new Intent(context, PurchaseSaleSummaryActivity.class);
                purchaseSaleIntent.putExtra("transactionType", Constants.InventoryReportType.PURCHASE);
                startActivity(purchaseSaleIntent);
                break;

            case R.id.saleSummaryBtn:
                Intent saleIntent = new Intent(context, PurchaseSaleSummaryActivity.class);
                saleIntent.putExtra("transactionType", Constants.InventoryReportType.SALE);
                startActivity(saleIntent);
                break;

            case R.id.ledgerReportBtn:
                Intent ledgerIntent = new Intent(context, LedgerReportActivity.class);
                startActivity(ledgerIntent);
                break;*/
        }
    }
}