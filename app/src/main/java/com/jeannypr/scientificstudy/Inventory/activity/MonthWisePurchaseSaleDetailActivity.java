package com.jeannypr.scientificstudy.Inventory.activity;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Inventory.adapter.MonthWisePurchaseSaleDetailAdapter;
import com.jeannypr.scientificstudy.Inventory.api.InventoryService;
import com.jeannypr.scientificstudy.Inventory.model.PurchaseSaleItemBean;
import com.jeannypr.scientificstudy.Inventory.model.PurchaseSaleItemModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.ActivityPurcahseSaleSummaryBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MonthWisePurchaseSaleDetailActivity extends AppCompatActivity {
    private Context context;
    InventoryService service;
    UserModel userData;
    String reportType, date;
    float totalBalance;
    private MonthWisePurchaseSaleDetailAdapter adapter;
    ArrayList<PurchaseSaleItemModel> purchaseItemList;
    private RecyclerView puechaseList;
    private ProgressBar pb;
    private LinearLayout noRecord;
    private TextView noRecordMsg, txtRecordVal;
    ActivityPurcahseSaleSummaryBinding binding;
    RelativeLayout subHeader;

    @Override
    public void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_purcahse_sale_summary);
        context = this;

        userData = UserPreference.getInstance(context).getUserData();
        service = new DataRepo<>(InventoryService.class, context).getService();

        reportType = getIntent().getStringExtra("transactionType");
        date = getIntent().getStringExtra("date");
        totalBalance = getIntent().getFloatExtra("amount", totalBalance);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, reportType + " " + "Summary", date);


        pb = findViewById(R.id.progressBar);
        noRecordMsg = findViewById(R.id.noRecordMsg);
        noRecord = findViewById(R.id.noRecord);

        subHeader = findViewById(R.id.subHeader);
        subHeader.setVisibility(View.VISIBLE);

        txtRecordVal = findViewById(R.id.txtRecordVal);
        txtRecordVal.setText("Rs." + " " + (String.valueOf(totalBalance)));


        purchaseItemList = new ArrayList<>();
        puechaseList = findViewById(R.id.puechaseList);
        adapter = new MonthWisePurchaseSaleDetailAdapter(context, purchaseItemList);
        puechaseList.setAdapter(adapter);
        ShowPurchaseSumaryItem();
    }

    private void ShowPurchaseSumaryItem() {
        pb.setVisibility(View.VISIBLE);
        service.GetPurchaseSaleItemSummary(reportType, date, userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<PurchaseSaleItemBean>() {
            @Override
            public void onResponse(Call<PurchaseSaleItemBean> call, Response<PurchaseSaleItemBean> response) {
                PurchaseSaleItemBean resp = response.body();

                if (resp != null) {
                    purchaseItemList.clear();
                    if (resp.rcode == Constants.Rcode.OK) {

                        //float totalBalance=0;

                        if (resp.data != null) {

                            for (PurchaseSaleItemModel model : resp.data) {
                                purchaseItemList.add(model);
                                // totalBalance = totalBalance + model.Amount;
                            }
                            // txtRecordVal.setText("Rs. " + (String.valueOf(totalBalance)));
                            adapter.notifyDataSetChanged();
                        }

                    } else if (resp.rcode == Constants.Rcode.NORECORDS) {
                        noRecord.setVisibility(View.VISIBLE);
                        noRecordMsg.setText("No record found");

                    } else {
                        Toast.makeText(context, "No Collection found.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "Something went wrong.Please try again.", Toast.LENGTH_LONG).show();
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<PurchaseSaleItemBean> call, Throwable t) {
                pb.setVisibility(View.VISIBLE);
                Toast.makeText(context, "Item wise collections could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
